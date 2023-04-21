package edu.byu.ece.edif.jedif;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import edu.byu.ece.edif.core.EdifCell;
import edu.byu.ece.edif.core.EdifCellInstance;
import edu.byu.ece.edif.core.EdifDesign;
import edu.byu.ece.edif.core.EdifEnvironment;
import edu.byu.ece.edif.core.EdifNameConflictException;
import edu.byu.ece.edif.core.EdifNameable;
import edu.byu.ece.edif.core.EdifNet;
import edu.byu.ece.edif.core.EdifPort;
import edu.byu.ece.edif.core.EdifPortRef;
import edu.byu.ece.edif.core.EdifUtils;
import edu.byu.ece.edif.core.InvalidEdifNameException;
import edu.byu.ece.edif.core.NamedObject;
import edu.byu.ece.edif.core.Property;
import edu.byu.ece.edif.tools.LogFile;
import edu.byu.ece.edif.tools.flatten.FlattenedEdifCell;
import edu.byu.ece.edif.tools.replicate.PartialReplicationDescription;
import edu.byu.ece.edif.tools.replicate.PartialReplicationStringDescription;
import edu.byu.ece.edif.tools.replicate.ReplicationException;
import edu.byu.ece.edif.tools.replicate.ReplicationType;
import edu.byu.ece.edif.tools.replicate.ijmr.TMRDWCEdifCell;
import edu.byu.ece.edif.tools.replicate.nmr.NMRArchitecture;
import edu.byu.ece.edif.tools.replicate.nmr.dwc.XilinxDWCArchitecture;
import edu.byu.ece.edif.tools.replicate.nmr.tmr.XilinxTMRArchitecture;
import edu.byu.ece.edif.util.clockdomain.ClockDomainParser;
import edu.byu.ece.edif.util.graph.EdifCellInstanceGraph;
import edu.byu.ece.edif.util.jsap.EdifCommandParser;
import edu.byu.ece.edif.util.jsap.InputFileCommandGroup;
import edu.byu.ece.edif.util.jsap.LogFileCommandGroup;
import edu.byu.ece.edif.util.jsap.OutputFileCommandGroup;
import edu.byu.ece.edif.util.jsap.PTMRFileCommandGroup;

/**
 * Final stage of DWC process. This executable will perform the actual
 * duplication and generate a netlist.
 */
public class JEdifDWC extends EDIFMain {

    public static String NO_OBUFS = "no_obufs";

    public static void main(String args[]) {
        PrintStream out = System.out;
        PrintStream err = System.out;
        printProgramExecutableString(out);
        EXECUTABLE_NAME = "JEdifDWC";
        TOOL_SUMMARY_STRING = "Duplicates .jedif netlist according to previous duplication policy.";
        EdifCommandParser parser = new EdifCommandParser();
        parser.addCommands(new InputFileCommandGroup());
        parser.addCommands(new OutputFileCommandGroup());
        parser.addCommands(new JEdifDWCParserCommandGroup());
        parser.addCommands(new PTMRFileCommandGroup());
        parser.addCommands(new LogFileCommandGroup("JEdifDWC.log"));
        try {
            parser.registerParameter(new Switch(NO_OBUFS).setLongFlag(NO_OBUFS).setDefault("false").setHelp("Disable insertion of output buffers on error detection signals."));
        } catch (JSAPException e1) {
            e1.printStackTrace();
        }
        JSAPResult result = parser.parse(args, err);
        if (!result.success()) System.exit(1);
        LogFileCommandGroup.CreateLog(result);
        out = LogFile.out();
        err = LogFile.err();
        printProgramExecutableString(LogFile.log());
        EdifEnvironment top = JEdifParserCommandGroup.getEdifEnvironment(result, out);
        PartialReplicationStringDescription sdesc = PTMRFileCommandGroup.getPartialReplicationDescription(result, out);
        PartialReplicationDescription desc = null;
        try {
            desc = sdesc.getDescription(top);
        } catch (ReplicationException e) {
            e.toRuntime();
        }
        if (desc == null) System.exit(1);
        dwc(result, top, desc);
        String outputFileName = OutputFileCommandGroup.getOutputFileName(result);
        if (!result.userSpecified(OutputFileCommandGroup.OUTPUT_OPTION)) {
            outputFileName = InputFileCommandGroup.getInputFileName(result);
            outputFileName = outputFileName.substring(0, outputFileName.lastIndexOf('.'));
            outputFileName += "_dwc";
            if (result.getBoolean(JEdifDWCParserCommandGroup.GENERATE_EDIF_FLAG)) outputFileName += ".edf"; else outputFileName += ".jedif";
        }
        if (result.getBoolean(JEdifDWCParserCommandGroup.GENERATE_EDIF_FLAG)) {
            LogFile.out().println("Generating .edf file " + outputFileName);
            try {
                JEdifNetlist.generateEdifNetlist(top, result, EXECUTABLE_NAME, VERSION_STRING);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            LogFile.out().println("Generating .jedif file " + outputFileName);
            OutputFileCommandGroup.serializeObject(LogFile.out(), outputFileName, top);
        }
    }

    public static void dwc(JSAPResult result, EdifEnvironment top, PartialReplicationDescription desc) {
        JEdifDWCParserCommandGroup.getUserTMRPorts(result);
        NMRArchitecture dwcArch = new XilinxDWCArchitecture();
        EdifCell topCell = top.getTopCell();
        EdifNameable newCellName = null;
        try {
            newCellName = JEdifDWCParserCommandGroup.getCellName(result);
            if (newCellName == null) {
                newCellName = new NamedObject(topCell.getName() + "_DWC");
            }
        } catch (InvalidEdifNameException edif) {
            LogFile.err().println("Error: " + newCellName + " is an invalid EDIF name");
            System.exit(1);
        }
        LogFile.out().print("Duplicating design . . .");
        TMRDWCEdifCell dwcCell = null;
        EdifCellInstanceGraph graph = new EdifCellInstanceGraph(topCell);
        ClockDomainParser cdp = null;
        try {
            cdp = new ClockDomainParser((FlattenedEdifCell) topCell, graph);
        } catch (InvalidEdifNameException e4) {
            e4.toRuntime();
        }
        EdifNet clockNet = cdp.getECIMap().keySet().iterator().next();
        Map<Integer, List<EdifPort>> portsToDuplicateMap = new LinkedHashMap<Integer, List<EdifPort>>();
        Map<Integer, List<EdifCellInstance>> instancesToDuplicateMap = new LinkedHashMap<Integer, List<EdifCellInstance>>();
        List<EdifPort> portsToDuplicateList = new ArrayList<EdifPort>();
        portsToDuplicateList.addAll(desc.getPorts(ReplicationType.DUPLICATE));
        List<EdifCellInstance> instancesToDuplicateList = new ArrayList<EdifCellInstance>();
        instancesToDuplicateList.addAll(desc.getInstances(ReplicationType.DUPLICATE));
        portsToDuplicateMap.put(new Integer(2), portsToDuplicateList);
        portsToDuplicateMap.put(new Integer(3), new ArrayList<EdifPort>());
        instancesToDuplicateMap.put(new Integer(2), instancesToDuplicateList);
        instancesToDuplicateMap.put(new Integer(3), new ArrayList<EdifCellInstance>());
        Collection<EdifPortRef> persistentPortRefsToCompare = desc.portRefsToCut;
        Collection<EdifCellInstance> feedbackPlusInput = desc.getInstances(ReplicationType.FEEDBACK);
        try {
            if (result.contains(JEdifDWCParserCommandGroup.DWC_SUFFIX)) {
                Map<Integer, List<String>> replicationSuffixMap = new LinkedHashMap<Integer, List<String>>();
                List<String> tmrSuffixes = new ArrayList<String>();
                tmrSuffixes.add("_TMR_0");
                tmrSuffixes.add("_TMR_1");
                tmrSuffixes.add("_TMR_2");
                replicationSuffixMap.put(new Integer(3), tmrSuffixes);
                replicationSuffixMap.put(new Integer(2), Arrays.asList(result.getStringArray(JEdifDWCParserCommandGroup.DWC_SUFFIX)));
                dwcCell = new TMRDWCEdifCell(topCell.getLibrary(), newCellName.toString(), topCell, feedbackPlusInput, new XilinxTMRArchitecture(), dwcArch, portsToDuplicateMap, instancesToDuplicateMap, persistentPortRefsToCompare, result.getBoolean(JEdifDWCParserCommandGroup.USE_DRC), true, result.getBoolean(JEdifDWCParserCommandGroup.REGISTER_DETECTION), clockNet, replicationSuffixMap, result.getBoolean(NO_OBUFS));
            } else {
                dwcCell = new TMRDWCEdifCell(topCell.getLibrary(), newCellName.toString(), topCell, feedbackPlusInput, new XilinxTMRArchitecture(), dwcArch, portsToDuplicateMap, instancesToDuplicateMap, persistentPortRefsToCompare, result.getBoolean(JEdifDWCParserCommandGroup.USE_DRC), result.getBoolean(JEdifDWCParserCommandGroup.PACK_DETECTION_REGS), result.getBoolean(JEdifDWCParserCommandGroup.REGISTER_DETECTION), clockNet, result.getBoolean(NO_OBUFS));
            }
        } catch (EdifNameConflictException e2) {
            e2.toRuntime();
        } catch (InvalidEdifNameException e2) {
            e2.toRuntime();
        }
        LogFile.out().println("Done");
        String domain_report_filename = JEdifTMRParserCommandGroup.getDomainReportFilename(result);
        try {
            dwcCell.printDomainReport(domain_report_filename);
        } catch (FileNotFoundException e) {
            LogFile.err().println(e.toString());
            System.exit(1);
        }
        printStats(LogFile.out(), topCell, dwcCell);
        EdifCellInstance dwcInstance = null;
        EdifDesign newDesign = null;
        try {
            dwcInstance = new EdifCellInstance(dwcCell.getName(), null, dwcCell);
            newDesign = new EdifDesign(dwcCell.getEdifNameable());
        } catch (InvalidEdifNameException e1) {
            e1.toRuntime();
        }
        newDesign.setTopCellInstance(dwcInstance);
        EdifDesign oldDesign = topCell.getLibrary().getLibraryManager().getEdifEnvironment().getTopDesign();
        if (oldDesign.getPropertyList() != null) {
            for (Object o : oldDesign.getPropertyList().values()) {
                Property p = (Property) o;
                newDesign.addProperty((Property) p.clone());
            }
        }
        top.setTopDesign(newDesign);
        topCell.getLibrary().deleteCell(topCell, true);
    }

    public static void printStats(PrintStream out, EdifCell origCell, TMRDWCEdifCell dwcCell) {
        int numberOfCompares = 0;
        int numberOfOutputCompares = 0;
        int numberOfPersistentCompares = 0;
        int numberOfDuplicatedInstances = 0;
        int numberOfDuplicatedNets = 0;
        int numberOfDuplicatedPorts = 0;
        numberOfCompares = dwcCell.getComparators().size();
        numberOfOutputCompares = dwcCell.getNonPersistentErrorNets().size();
        numberOfPersistentCompares = dwcCell.getPersistentErrorNets().size();
        numberOfDuplicatedInstances = dwcCell.getReplicatedInstances().size();
        numberOfDuplicatedNets = dwcCell.getReplicatedNets().size();
        numberOfDuplicatedPorts = dwcCell.getReplicatedPorts().size();
        int numberOfOriginalInstances = origCell.getSubCellList().size();
        out.print("\tAdded " + numberOfCompares + " compares ");
        out.println("(" + numberOfPersistentCompares + " persistent, " + numberOfOutputCompares + " non-persistent)");
        out.println("\t" + numberOfDuplicatedInstances + " instances out of " + numberOfOriginalInstances + " cells duplicated (" + (numberOfDuplicatedInstances * 100 / numberOfOriginalInstances) + "% coverage)");
        out.println("\t" + 2 * numberOfDuplicatedInstances + " new instances added to design. ");
        out.println("\t" + numberOfDuplicatedNets + " nets duplicated (" + 2 * numberOfDuplicatedNets + " new nets added).");
        out.println("\t" + numberOfDuplicatedPorts + " ports duplicated.");
        int dwcPrimitives = EdifUtils.countRecursivePrimitives(dwcCell);
        int flatPrimitives = EdifUtils.countRecursivePrimitives(origCell);
        int dwcNets = EdifUtils.countRecursiveNets(dwcCell);
        int flatNets = EdifUtils.countRecursiveNets(origCell);
        int dwcPortRefs = EdifUtils.countPortRefs(dwcCell, true);
        int flatPortRefs = EdifUtils.countPortRefs(origCell, true);
        out.println("");
        out.println("DWC circuit contains:");
        out.println("\t" + dwcPrimitives + " primitives (" + (100 * (dwcPrimitives - flatPrimitives)) / flatPrimitives + "% increase)");
        out.println("\t" + dwcNets + " nets (" + (100 * (dwcNets - flatNets)) / flatNets + "% increase)");
        out.println("\t" + dwcPortRefs + " net connections (" + (100 * (dwcPortRefs - flatPortRefs)) / flatPortRefs + "% increase)");
    }
}

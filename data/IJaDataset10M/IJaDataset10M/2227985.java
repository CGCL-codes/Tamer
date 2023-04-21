package net.sf.orc2hdl.backend;

import static net.sf.orc2hdl.preference.Constants.P_MODELSIM;
import static net.sf.orcc.OrccLaunchConstants.DEBUG_MODE;
import static net.sf.orcc.OrccLaunchConstants.MAPPING;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.sf.openforge.app.Engine;
import net.sf.openforge.app.Forge;
import net.sf.openforge.app.ForgeFatalException;
import net.sf.openforge.app.GenericJob;
import net.sf.openforge.app.NewJob;
import net.sf.openforge.app.OptionRegistry;
import net.sf.orc2hdl.Activator;
import net.sf.orc2hdl.analysis.ExecutionChart;
import net.sf.orc2hdl.analysis.SimParser;
import net.sf.orc2hdl.analysis.TimeGoDone;
import net.sf.orc2hdl.analysis.WeightWriter;
import net.sf.orc2hdl.design.DesignEngine;
import net.sf.orc2hdl.printer.Orc2HDLPrinter;
import net.sf.orcc.OrccException;
import net.sf.orcc.backends.AbstractBackend;
import net.sf.orcc.backends.CustomPrinter;
import net.sf.orcc.backends.StandardPrinter;
import net.sf.orcc.backends.transformations.DivisionSubstitution;
import net.sf.orcc.backends.transformations.EmptyNodeRemover;
import net.sf.orcc.backends.transformations.Inliner;
import net.sf.orcc.backends.transformations.InstPhiTransformation;
import net.sf.orcc.backends.transformations.Multi2MonoToken;
import net.sf.orcc.backends.transformations.StoreOnceTransformation;
import net.sf.orcc.backends.transformations.UnitImporter;
import net.sf.orcc.backends.xlim.XlimActorTemplateData;
import net.sf.orcc.backends.xlim.XlimExprPrinter;
import net.sf.orcc.backends.xlim.XlimTypePrinter;
import net.sf.orcc.backends.xlim.transformations.CustomPeekAdder;
import net.sf.orcc.backends.xlim.transformations.GlobalArrayInitializer;
import net.sf.orcc.backends.xlim.transformations.InstTernaryAdder;
import net.sf.orcc.backends.xlim.transformations.ListFlattener;
import net.sf.orcc.backends.xlim.transformations.LiteralIntegersAdder;
import net.sf.orcc.backends.xlim.transformations.LocalArrayRemoval;
import net.sf.orcc.backends.xlim.transformations.UnaryListRemoval;
import net.sf.orcc.backends.xlim.transformations.XlimDeadVariableRemoval;
import net.sf.orcc.backends.xlim.transformations.XlimVariableRenamer;
import net.sf.orcc.df.Action;
import net.sf.orcc.df.Actor;
import net.sf.orcc.df.DfFactory;
import net.sf.orcc.df.Instance;
import net.sf.orcc.df.Network;
import net.sf.orcc.df.transformations.Instantiator;
import net.sf.orcc.df.transformations.NetworkFlattener;
import net.sf.orcc.df.util.DfSwitch;
import net.sf.orcc.ir.transformations.BlockCombine;
import net.sf.orcc.ir.transformations.CfgBuilder;
import net.sf.orcc.ir.transformations.DeadCodeElimination;
import net.sf.orcc.ir.transformations.DeadGlobalElimination;
import net.sf.orcc.ir.transformations.SSATransformation;
import net.sf.orcc.ir.transformations.TacTransformation;
import net.sf.orcc.ir.util.IrUtil;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This class defines an XLIM and OpenForge based back-end
 * 
 * @author Endri Bezati
 * @author Herve Yviquel
 * 
 */
public class Orc2HDL extends AbstractBackend {

    private boolean debugMode;

    private boolean instanceToDesign;

    private boolean goDoneSignal;

    private boolean modelsimAnalysis;

    private String simTime;

    private String fpgaName;

    private List<String> forgeFlags;

    private List<String> entities;

    private HashSet<String> entitySet;

    private Map<String, String> clkDomains;

    private String simPath;

    private String srcPath;

    private String srcGoDonePath;

    private void computeEntityList(Instance instance) {
        if (instance.isActor()) {
            String name = instance.getName();
            if (!entitySet.contains(name)) {
                entitySet.add(name);
                entities.add(name);
            }
        } else if (instance.isNetwork()) {
            String name = instance.getName();
            Network network = instance.getNetwork();
            if (!entitySet.contains(name)) {
                for (Instance subInstance : network.getInstances()) {
                    computeEntityList(subInstance);
                }
                entitySet.add(name);
            }
        }
    }

    private void copySystemActorsLib() {
        String systemBuilderPath = path + File.separator + "lib" + File.separator + "systemActors";
        new File(systemBuilderPath).mkdir();
        new File(systemBuilderPath + File.separator + "io").mkdir();
        new File(systemBuilderPath + File.separator + "types").mkdir();
        URL hdlLibrariesURL = Platform.getBundle("net.sf.orc2hdl").getEntry("/HdlLibraries/systemActors");
        try {
            List<String> systemActorsFileList = Arrays.asList("types/sa_types.vhd", "io/Source.vhd");
            String hdlLibrariesPath = new File(FileLocator.resolve(hdlLibrariesURL).getFile()).getAbsolutePath();
            IFileSystem fileSystem = EFS.getLocalFileSystem();
            for (String files : systemActorsFileList) {
                String path = hdlLibrariesPath + File.separator + files;
                URI uri = new File(path).toURI();
                IFileStore pluginDir = fileSystem.getStore(uri);
                path = systemBuilderPath + File.separator + files;
                uri = new File(path).toURI();
                IFileStore copyDir = fileSystem.getStore(uri);
                pluginDir.copy(copyDir, EFS.OVERWRITE, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void copySystemBuilderLib() {
        String systemBuilderPath = path + File.separator + "lib" + File.separator + "systemBuilder";
        new File(systemBuilderPath).mkdir();
        URL hdlLibrariesURL = Platform.getBundle("net.sf.orc2hdl").getEntry("/HdlLibraries/systemBuilder/vhdl");
        try {
            List<String> systemBuilderFifo = Arrays.asList("sbtypes.vhdl", "sbfifo_behavioral.vhdl", "sbfifo.vhdl");
            String hdlLibrariesPath = new File(FileLocator.resolve(hdlLibrariesURL).getFile()).getAbsolutePath();
            IFileSystem fileSystem = EFS.getLocalFileSystem();
            for (String files : systemBuilderFifo) {
                String path = hdlLibrariesPath + File.separator + files;
                URI uri = new File(path).toURI();
                IFileStore pluginDir = fileSystem.getStore(uri);
                path = systemBuilderPath + File.separator + files;
                uri = new File(path).toURI();
                IFileStore copyDir = fileSystem.getStore(uri);
                pluginDir.copy(copyDir, EFS.OVERWRITE, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doInitializeOptions() {
        clkDomains = getAttribute(MAPPING, new HashMap<String, String>());
        instanceToDesign = getAttribute("net.sf.orc2hdl.instanceDesign", false);
        goDoneSignal = getAttribute("net.sf.orc2hdl.goDoneSignal", false);
        modelsimAnalysis = getAttribute("net.sf.orc2hdl.modelSimAnalysis", false);
        simTime = getAttribute("net.sf.orc2hdl.simTime", "5000");
        srcPath = path + File.separator + "src";
        new File(srcPath).mkdir();
        srcGoDonePath = path + File.separator + "srcGoDone";
        if (goDoneSignal) {
            new File(srcGoDonePath).mkdir();
        }
        simPath = path + File.separator + "sim";
        new File(simPath).mkdir();
        if (modelsimAnalysis) {
            goDoneSignal = true;
        }
        String fpgaType = getAttribute("net.sf.orc2hdl.FpgaType", "Virtex 2");
        if (fpgaType.equals("Spartan 3")) {
            fpgaName = "xc3s200-4-tq144C";
        } else if (fpgaType.equals("Virtex 2")) {
            fpgaName = "xc2vp30-7-ff1152";
        } else if (fpgaType.equals("Virtex 4")) {
            fpgaName = "xc4vlx100-10-ff1513";
        }
        debugMode = getAttribute(DEBUG_MODE, true);
        forgeFlags = new ArrayList<String>();
        if (getAttribute("net.sf.orc2hdl.Verbose", false)) {
            forgeFlags.add("-vv");
        }
        if (getAttribute("net.sf.orc2hdl.Pipelining", true)) {
            forgeFlags.add("-pipeline");
        }
        if (getAttribute("net.sf.orc2hdl.NoBlockIO", true)) {
            forgeFlags.add("-noblockio");
        }
        if (getAttribute("net.sf.orc2hdl.NoBlockBasedScheduling", true)) {
            forgeFlags.add("-no_block_sched");
        }
        if (getAttribute("net.sf.orc2hdl.SimpleSharedMemoryArbitration", true)) {
            forgeFlags.add("-simple_arbitration");
        }
        if (getAttribute("net.sf.orc2hdl.NoEDKGeneration", true)) {
            forgeFlags.add("-noedk");
        }
        if (getAttribute("net.sf.orc2hdl.BalanceLoopLatency", true)) {
            forgeFlags.add("-loopbal");
        }
        if (getAttribute("net.sf.orc2hdl.MultiplierDecomposition", true)) {
            forgeFlags.add("-multdecomplimit");
            forgeFlags.add("2");
        }
        if (getAttribute("net.sf.orc2hdl.CombinationallyLUTReads", true)) {
            forgeFlags.add("-comb_lut_mem_read");
        }
        if (getAttribute("net.sf.orc2hdl.AllowDualPortLUT", true)) {
            forgeFlags.add("-dplut");
        }
        if (getAttribute("net.sf.orc2hdl.NoLog", true)) {
            forgeFlags.add("-nolog");
        }
        if (getAttribute("net.sf.orc2hdl.NoInclude", true)) {
            forgeFlags.add("-noinclude");
        }
        if (getAttribute("net.sf.orc2hdl.NoInclude", true)) {
            forgeFlags.add("-report");
            forgeFlags.add("-Xdetailed_report");
        }
    }

    @Override
    protected void doTransformActor(Actor actor) throws OrccException {
        XlimActorTemplateData data = new XlimActorTemplateData();
        actor.setTemplateData(data);
        DfSwitch<?>[] transformations = { new StoreOnceTransformation(), new LocalArrayRemoval(), new Multi2MonoToken(), new DivisionSubstitution(), new UnitImporter(), new SSATransformation(), new GlobalArrayInitializer(true), new Inliner(true, true), new InstTernaryAdder(), new UnaryListRemoval(), new CustomPeekAdder(), new DeadGlobalElimination(), new DeadCodeElimination(), new XlimDeadVariableRemoval(), new ListFlattener(), new TacTransformation(), new CfgBuilder(), new InstPhiTransformation(), new LiteralIntegersAdder(), new XlimVariableRenamer(), new EmptyNodeRemover(), new BlockCombine() };
        for (DfSwitch<?> transformation : transformations) {
            transformation.doSwitch(actor);
            ResourceSet set = new ResourceSetImpl();
            if (debugMode && !IrUtil.serializeActor(set, path, actor)) {
                System.out.println("oops " + transformation + " " + actor.getName());
            }
        }
        data.computeTemplateMaps(actor);
    }

    @Override
    protected void doVtlCodeGeneration(List<IFile> files) throws OrccException {
    }

    @Override
    protected void doXdfCodeGeneration(Network network) throws OrccException {
        network = new Instantiator().doSwitch(network);
        new NetworkFlattener().doSwitch(network);
        transformActors(network.getAllActors());
        network.computeTemplateMaps();
        TopNetworkTemplateData data = new TopNetworkTemplateData();
        data.computeTemplateMaps(network, clkDomains);
        network.setTemplateData(data);
        printNetwork(network);
        printSimDoFile(network);
        new File(path + File.separator + "lib").mkdir();
        copySystemBuilderLib();
        copySystemActorsLib();
        printInstances(network);
        if (modelsimAnalysis) {
            String exe = Activator.getDefault().getPreference(P_MODELSIM, "");
            if (exe == null || exe.isEmpty()) {
                write("Warning: The path to ModelSim executable is not set!\n" + "Go to Window > Preferences > Orc2HDL to edit them.\n");
            } else {
                runModelSim(exe, network);
                SimParser simParser = new SimParser(network, path + File.separator + "analysis");
                simParser.createMaps();
                Map<Instance, Map<Action, TimeGoDone>> execution = simParser.getExecutionMap();
                WeightWriter weightWriter = new WeightWriter(execution, network, path + File.separator + "analysis" + File.separator + network.getName() + ".ew");
                weightWriter.writeProtobuf();
                ExecutionChart chart = new ExecutionChart(execution, network, path);
                chart.saveChart();
            }
        }
    }

    public void runModelSim(String modelSim, Network network) {
        try {
            String line;
            File fPath = new File(simPath);
            String arg = " -c -do sim_tb_" + network.getSimpleName() + ".do";
            String cmd = modelSim + arg;
            write("Launching Modelsim\n");
            Process p = Runtime.getRuntime().exec(cmd, null, fPath);
            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = bri.readLine()) != null) {
                write("Orc2HDL: " + line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean printInstance(Instance instance) {
        StandardPrinter printer = new StandardPrinter("net/sf/orcc/backends/xlim/hw/Actor.stg", !debugMode);
        printer.getOptions().put("fpgaType", fpgaName);
        printer.setExpressionPrinter(new XlimExprPrinter());
        printer.setTypePrinter(new XlimTypePrinter());
        String xlimPath = path + File.separator + "xlim";
        new File(xlimPath).mkdir();
        Boolean printOK = true;
        if (!instance.getActor().isNative()) {
            printOK = printer.print(instance.getName() + ".xlim", xlimPath, instance);
            if (!printOK) {
                try {
                    String xlim = null;
                    String id = instance.getName();
                    File file = new File(xlimPath + File.separator + id + ".xlim");
                    if (file.exists()) {
                        xlim = file.getCanonicalPath();
                    }
                    List<String> flags = new ArrayList<String>(forgeFlags);
                    flags.addAll(Arrays.asList("-d", srcPath, "-o", id, xlim));
                    long t0 = System.currentTimeMillis();
                    Boolean okForge = false;
                    if (instanceToDesign) {
                        okForge = runForge(flags.toArray(new String[0]), instance);
                    } else {
                        okForge = Forge.runForge(flags.toArray(new String[0]));
                    }
                    long t1 = System.currentTimeMillis();
                    if (okForge) {
                        if (goDoneSignal) {
                            VerilogAddGoDone instanceWithGoDone = new VerilogAddGoDone(instance, srcPath, srcGoDonePath);
                            instanceWithGoDone.addGoDone();
                        }
                        write("Compiling instance: " + id + ": Compiled in: " + ((float) (t1 - t0) / (float) 1000) + "s\n");
                    } else {
                        write("Compiling instance: " + id + ": OpenForge failed to compile" + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return printOK;
    }

    private void printNetwork(Network network) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String currentTime = dateFormat.format(date);
        File folder = new File(path + File.separator + "Testbench");
        if (!folder.exists()) {
            folder.mkdir();
        }
        StandardPrinter instancePrinter = new StandardPrinter("net/sf/orcc/backends/xlim/hw/ModelSim_Testbench.stg");
        Instance instance = DfFactory.eINSTANCE.createInstance(network.getName(), network);
        printTestbench(instancePrinter, instance);
        printTCL(instance);
        Orc2HDLPrinter printer;
        String file = network.getSimpleName();
        file += ".vhd";
        printer = new Orc2HDLPrinter("net/sf/orc2hdl/templates/Network.stg");
        printer.setExpressionPrinter(new XlimExprPrinter());
        printer.setTypePrinter(new XlimTypePrinter());
        printer.getOptions().put("fifoSize", fifoSize);
        printer.getOptions().put("currentTime", currentTime);
        printer.print(file, srcPath, network);
        if (goDoneSignal || modelsimAnalysis) {
            new File(srcGoDonePath).mkdir();
            printer.getOptions().put("goDoneSignal", goDoneSignal);
            printer.print(file, srcGoDonePath, network);
            if (modelsimAnalysis) {
                String analysis = path + File.separator + "analysis";
                new File(analysis).mkdir();
                printer = new Orc2HDLPrinter("net/sf/orc2hdl/templates/GoDoneTestBench.stg");
                printer.setExpressionPrinter(new XlimExprPrinter());
                printer.setTypePrinter(new XlimTypePrinter());
                printer.getOptions().put("currentTime", currentTime);
                file = "tb_" + network.getSimpleName() + ".vhd";
                printer.print(file, srcGoDonePath, network);
            }
        }
    }

    private void printSimDoFile(Network network) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String currentTime = dateFormat.format(date);
        Orc2HDLPrinter printer;
        String file = network.getName();
        printer = new Orc2HDLPrinter("net/sf/orc2hdl/templates/Top_Sim_do.stg");
        printer.setExpressionPrinter(new XlimExprPrinter());
        printer.setTypePrinter(new XlimTypePrinter());
        printer.getOptions().put("currentTime", currentTime);
        file = "sim_" + network.getSimpleName() + ".do";
        printer.print(file, simPath, network);
        if (goDoneSignal) {
            printer.getOptions().put("goDoneSignal", goDoneSignal);
            file = "sim_" + network.getSimpleName() + "_goDone" + ".do";
            new File(simPath).mkdir();
            printer.print(file, simPath, network);
        }
        if (modelsimAnalysis) {
            printer.getOptions().put("goDoneSignal", goDoneSignal);
            printer.getOptions().put("modelsimAnalysis", modelsimAnalysis);
            printer.getOptions().put("simTime", simTime);
            file = "sim_tb_" + network.getSimpleName() + ".do";
            new File(simPath).mkdir();
            printer.print(file, simPath, network);
        }
        URL glblFileURL = Platform.getBundle("net.sf.orc2hdl").getEntry("/HdlLibraries/glbl");
        try {
            String glblFilePath = new File(FileLocator.resolve(glblFileURL).getFile()).getAbsolutePath();
            IFileSystem fileSystem = EFS.getLocalFileSystem();
            String path = glblFilePath + "/glbl.v";
            URI uri = new File(path).toURI();
            IFileStore pluginDir = fileSystem.getStore(uri);
            path = simPath + "/glbl.v";
            uri = new File(path).toURI();
            IFileStore copyDir = fileSystem.getStore(uri);
            pluginDir.copy(copyDir, EFS.OVERWRITE, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void printTCL(Instance instance) {
        CustomPrinter printer = new CustomPrinter("net/sf/orcc/backends/xlim/hw/ModelSim_Script.stg");
        entities = new ArrayList<String>();
        entitySet = new HashSet<String>();
        computeEntityList(instance);
        printer.print("TCLLists.tcl", path, "TCLLists", "name", instance.getNetwork().getName(), "entities", entities);
    }

    private void printTestbench(StandardPrinter printer, Instance instance) {
        printer.print(instance.getName() + "_tb.vhd", path + File.separator + "Testbench", instance);
        if (instance.isNetwork()) {
            Network network = instance.getNetwork();
            for (Instance subInstance : network.getInstances()) {
                printTestbench(printer, subInstance);
            }
        }
    }

    private boolean runForge(String[] args, Instance instance) {
        Forge f = new Forge();
        GenericJob forgeMainJob = new GenericJob();
        boolean error = false;
        try {
            forgeMainJob.setOptionValues(args);
            f.preprocess(forgeMainJob);
            Engine engine = new DesignEngine(forgeMainJob, instance);
            engine.begin();
        } catch (NewJob.ForgeOptionException foe) {
            write("Command line option error: " + foe.getMessage());
            write("");
            write(OptionRegistry.usage(false));
            error = true;
        } catch (ForgeFatalException ffe) {
            write("Forge compilation ended with fatal error:");
            write(ffe.getMessage());
            error = true;
        }
        return !error;
    }
}

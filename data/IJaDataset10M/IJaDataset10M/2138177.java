package net.sf.RecordEditor.utils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import net.sf.JRecord.Common.RecordException;
import net.sf.JRecord.Details.AbstractLayoutDetails;
import net.sf.JRecord.Details.AbstractLine;
import net.sf.JRecord.Details.DefaultLineProvider;
import net.sf.JRecord.Details.LineProvider;
import net.sf.JRecord.External.ExternalRecord;
import net.sf.JRecord.IO.AbstractLineReader;
import net.sf.JRecord.IO.AbstractLineIOProvider;
import net.sf.JRecord.IO.LineIOProvider;
import net.sf.RecordEditor.utils.common.Common;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * Run velocity on a Record Based File
 *
 * @author Bruce Martin
 *
 */
public class RunVelocity {

    private static RunVelocity instance = null;

    /**
     * process a Record Oriented File using a Velocity Template
     *
     * @param layout Record layout of the file
     * @param inputFile input file to process
     * @param template Velocity template to use
     * @param outputFile output filename
     *
     * @throws Exception any error that occurs in running velocity
     */
    public void processFile(AbstractLayoutDetails layout, String inputFile, String template, String outputFile) throws Exception {
        int[] records = {};
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        processFile(layout, new DefaultLineProvider(), records, inputFile, outputFile, template, writer);
        writer.close();
    }

    /**
     * process a Record Oriented File using a Velocity Template
     *
     * @param layout Record layout of the file
     * @param lineProvider line provider to use to create Lines
     *        note use null for the standard Line
     * @param records Records to select
     * @param inputFile input file to process
     * @param template Velocity template to use
     * @param writer output writer where the generated template is to
     *        be written
     *
     * @throws Exception any error that occurs in running velocity
     */
    public void processFile(AbstractLayoutDetails layout, LineProvider lineProvider, int[] records, String inputFile, String outputFile, String template, Writer writer) throws Exception {
        int preferedLayout, i, recordsToCheck;
        AbstractLine line;
        ArrayList<AbstractLine> recordList = new ArrayList<AbstractLine>();
        boolean saveRecord;
        VelocityContext context = new VelocityContext();
        AbstractLineIOProvider ioProvider = LineIOProvider.getInstance();
        AbstractLineReader reader = ioProvider.getLineReader(layout.getFileStructure(), lineProvider);
        ioProvider = new LineIOProvider(lineProvider);
        reader = ioProvider.getLineReader(layout.getFileStructure());
        recordsToCheck = 0;
        if (records != null) {
            recordsToCheck = records.length;
        }
        if (inputFile.endsWith(".gz")) {
            reader.open(new GZIPInputStream(new FileInputStream(inputFile)), layout);
        } else {
            reader.open(inputFile, layout);
        }
        while ((line = reader.read()) != null) {
            preferedLayout = line.getPreferredLayoutIdx();
            saveRecord = recordsToCheck == 0;
            for (i = 0; i < recordsToCheck; i++) {
                if (preferedLayout == records[i]) {
                    saveRecord = true;
                    break;
                }
            }
            if (saveRecord) {
                recordList.add(line);
            }
        }
        reader.close();
        if (recordList.size() > 0) {
            context.put("records", recordList);
            context.put("fileName", inputFile);
            context.put("layout", layout);
            context.put("recordLayout", layout);
            context.put("outputFile", outputFile);
            context.put("recordLayout", layout);
            context.put("typeNames", new TypeNameArray());
            context.put("onlyData", Boolean.TRUE);
            context.put("showBorder", Boolean.TRUE);
            context.put("recordIdx", Integer.valueOf(recordList.get(0).getPreferredLayoutIdx()));
            genSkel(template, writer, context);
        }
    }

    /**
     * Generate a Velocity template with the supplied records
     * @param template template file
     * @param recordList list of lines (or records to use)
     * @param inputFile input file the records came from
     * @param writer writer used to write the generated skelto
     *
     * @throws Exception any error that occurs
     */
    public final void genSkel(String template, ExternalRecord layout, TypeNameArray typeNames, String outputFile, Writer writer) throws Exception {
        VelocityContext context = new VelocityContext();
        context.put("outputFile", outputFile);
        context.put("typeNames", typeNames);
        context.put("recordLayout", layout);
        genSkel(template, writer, context);
    }

    /**
     * Generate a Velocity template with the supplied records
     * @param template template file
     * @param recordList list of lines (or records to use)
     * @param inputFile input file the records came from
     * @param writer writer used to write the generated skelto
     *
     * @throws Exception any error that occurs
     */
    public final void genSkel(String template, List<List<AbstractLine>> recordList, Object root, Object nodes, int treeDepth, boolean onlyData, boolean showBorder, int recordIdx, String inputFile, String outputFile, Writer writer) throws Exception {
        if (recordList.size() > 0) {
            VelocityContext context = new VelocityContext();
            AbstractLine l = recordList.get(0).get(0);
            context.put("records", recordList.get(0));
            context.put("file", recordList.get(1));
            context.put("view", recordList.get(2));
            context.put("treeRoot", root);
            context.put("treeNodes", nodes);
            context.put("treeDepth", treeDepth);
            context.put("fileName", inputFile);
            context.put("outputFile", outputFile);
            context.put("layout", l.getLayout());
            context.put("onlyData", Boolean.valueOf(onlyData));
            context.put("showBorder", Boolean.valueOf(showBorder));
            context.put("recordIdx", Integer.valueOf(recordIdx));
            genSkel(template, writer, context);
        }
    }

    /**
     * Generate a velocity skelton
     *
     * @param templateFile file to be generated
     * @param writer output writer
     * @param context variable definitions
     *
     * @throws Exception any error that occurs
     */
    public final void genSkel(String templateFile, Writer writer, VelocityContext context) throws Exception {
        Template template = null;
        try {
            VelocityEngine e = new VelocityEngine();
            int idx = templateFile.lastIndexOf(Common.FILE_SEPERATOR);
            if (idx > 0) {
                String s1 = templateFile.substring(0, idx);
                e.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, s1);
                templateFile = templateFile.substring(idx + 1);
            }
            e.init();
            template = e.getTemplate(templateFile);
        } catch (ResourceNotFoundException rnfe) {
            String msg = "Error : cannot find template " + templateFile;
            Common.logMsg(msg, rnfe);
            throw new RecordException(msg);
        } catch (ParseErrorException pee) {
            String msg = "Syntax error in template " + templateFile + ":" + pee;
            Common.logMsg(msg, pee);
            throw new RecordException(msg);
        }
        if (template != null) {
            template.merge(context, writer);
        }
        writer.flush();
    }

    /**
     * @return Returns the instance.
     */
    public static RunVelocity getInstance() {
        if (instance == null) {
            instance = new RunVelocity();
        }
        return instance;
    }
}

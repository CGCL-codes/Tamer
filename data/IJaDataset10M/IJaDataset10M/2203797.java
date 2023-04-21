package org.swfparser.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jswiff.SWFDocument;
import org.swfparser.ActionBlockContext;
import org.swfparser.CodeUtil;
import org.swfparser.ExecutionContext;
import org.swfparser.Operation;
import org.swfparser.StatementBlock;
import org.swfparser.TagContext;
import org.swfparser.exception.StatementBlockException;
import org.swfparser.operation.ByteCodeOperation;
import com.jswiff.swfrecords.actions.ActionBlockReader;

public class SWFDumpActionScript extends ASTagsProcessor {

    private static Logger logger = Logger.getLogger(SWFDumpActionScript.class);

    private String outputFileName;

    protected OutputStreamWriter writer;

    public SWFDumpActionScript(SWFDocument doc, String outputFileName) {
        super(doc);
        this.outputFileName = outputFileName;
    }

    public SWFDumpActionScript(String swfFileName, String outputFileName) {
        super(swfFileName);
        this.outputFileName = outputFileName;
    }

    @Override
    protected void beforeProcess() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFileName);
            writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void afterProcess() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeData(String data) throws IOException {
        writer.write(data);
    }

    @Override
    protected void processActions(ActionBlockContext context, ActionBlockReader actionBlock) {
        StatementBlock statementBlock = CodeUtil.getStatementBlockReader();
        ExecutionContext executionContext = CodeUtil.getExecutionContext();
        executionContext.setTag(context.getTag());
        executionContext.setFrameNumber(context.getFrameNum());
        statementBlock.setExecutionContext(executionContext);
        List<Operation> operations;
        try {
            statementBlock.read(actionBlock.getActions());
            operations = statementBlock.getOperations();
        } catch (StatementBlockException e) {
            logger.error("Error reading action block.", e);
            operations = new ArrayList<Operation>();
            operations.add(new ByteCodeOperation(actionBlock.getData()));
        }
        processOperations(context, operations);
    }

    protected void processOperations(ActionBlockContext context, List<Operation> operations) {
        try {
            writeData(getActionBlockHeader(context));
            writeData("\n\n");
            for (Operation op : operations) {
                String endOfStatement = CodeUtil.endOfStatement(op);
                String writeOp = op.getStringValue(0) + endOfStatement + "\n";
                writeData(writeOp);
            }
            writeData("\n");
        } catch (IOException e) {
            logger.error("Error writing data ", e);
        }
    }

    protected String getActionBlockHeader(ActionBlockContext context) {
        return context.getDumpString();
    }

    @Override
    protected void processNonActionTag(TagContext context) {
    }
}

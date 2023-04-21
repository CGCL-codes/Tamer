package com.google.code.jtracert.traceBuilder.impl.sequence;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.impl.BaseMethodCallProcessor;
import com.google.code.jtracert.util.FileUtils;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class SequenceOutClient extends BaseMethodCallProcessor {

    /**
     * @param methodCall
     */
    public void processMethodCall(MethodCall methodCall) {
        try {
            Writer diagramWriter = new OutputStreamWriter(System.out);
            writeSequence(methodCall, diagramWriter, 0);
            diagramWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param methodCall
     * @param diagramWriter
     * @param level
     * @throws IOException
     */
    private void writeSequence(MethodCall methodCall, Writer diagramWriter, int level) throws IOException {
        String className = methodCall.getRealClassName().replaceAll("\\.", "/");
        String methodName = methodCall.getMethodName().replaceAll("\\<", "").replaceAll("\\>", "");
        StringBuffer tabStringBuffer = new StringBuffer();
        for (int i = 0; i < level; i++) {
            tabStringBuffer.append("    ");
        }
        String tabString = tabStringBuffer.toString();
        diagramWriter.write(tabString);
        diagramWriter.write(className);
        diagramWriter.write('.');
        diagramWriter.write(methodName);
        diagramWriter.write(" {");
        diagramWriter.write(FileUtils.LINE_SEPARATOR);
        for (MethodCall callee : methodCall.getCallees()) {
            writeSequence(callee, diagramWriter, level + 1);
        }
        diagramWriter.write(tabString);
        diagramWriter.write("}");
        diagramWriter.write(FileUtils.LINE_SEPARATOR);
    }
}

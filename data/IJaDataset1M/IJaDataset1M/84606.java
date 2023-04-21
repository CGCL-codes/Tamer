package com.google.code.jtracert.traceBuilder.impl.sdedit;

import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.util.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class SDEditFileClient extends BaseSDEditClient {

    /**
     * @param methodCall
     */
    @Override
    public void processMethodCall(MethodCall methodCall) {
        Writer diagramWriter = null;
        try {
            File diagramFile = getDiagramFile(methodCall);
            diagramWriter = new FileWriter(diagramFile);
            diagramWriter.append("user:Actor").append(FileUtils.LINE_SEPARATOR);
            writeObjectNames(methodCall, diagramWriter);
            diagramWriter.append(FileUtils.LINE_SEPARATOR);
            diagramWriter.append("user:").append(methodCall.getClassName().replaceAll("\\.", "\\\\.")).append(".").append(methodCall.getMethodName()).append(FileUtils.LINE_SEPARATOR);
            writeMethodNames(methodCall, diagramWriter);
            diagramWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != diagramWriter) {
                try {
                    diagramWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param methodCall
     * @return
     */
    public File saveMethodCall(MethodCall methodCall) {
        Writer diagramWriter = null;
        try {
            File diagramFile = getDiagramFile(methodCall);
            diagramWriter = new FileWriter(diagramFile);
            diagramWriter.append("user:Actor").append(FileUtils.LINE_SEPARATOR);
            writeObjectNames(methodCall, diagramWriter);
            diagramWriter.append(FileUtils.LINE_SEPARATOR);
            String className = methodCall.getClassName();
            if (null == className) className = methodCall.getRealClassName();
            diagramWriter.append("user:").append(className.replaceAll("\\.", "\\\\.")).append(".").append(methodCall.getMethodName()).append(FileUtils.LINE_SEPARATOR);
            writeMethodNames(methodCall, diagramWriter);
            diagramWriter.flush();
            return diagramFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != diagramWriter) {
                try {
                    diagramWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param methodCall
     * @return
     * @throws IOException
     */
    private File getDiagramFile(MethodCall methodCall) throws IOException {
        String baseDiagramsFolderName = getAnalyzeProperties().getOutputFolder();
        String fullClassName = methodCall.getRealClassName();
        StringBuffer diagramFolderNameStringBuffer = new StringBuffer(baseDiagramsFolderName);
        String[] classNameParts = fullClassName.split("\\.");
        for (int i = 0; i < classNameParts.length - 1; i++) {
            diagramFolderNameStringBuffer.append(FileUtils.FILE_SEPARATOR).append(classNameParts[i]);
        }
        File diagramFolder = new File(diagramFolderNameStringBuffer.toString());
        FileUtils.forceMkdir(diagramFolder);
        diagramFolderNameStringBuffer.append(FileUtils.FILE_SEPARATOR).append(classNameParts[classNameParts.length - 1]).append('.').append(methodCall.getMethodName().replaceAll("\\<", "").replaceAll("\\>", ""));
        File diagramFile = new File(diagramFolderNameStringBuffer.toString() + ".sdx");
        int i = 0;
        while (diagramFile.exists()) {
            i++;
            diagramFile = new File(diagramFolderNameStringBuffer.toString() + '.' + i + ".sdx");
        }
        return diagramFile;
    }
}

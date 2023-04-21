package com.ivis.xprocess.artifact_substituter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Logger;

public class TextSubstituter {

    private static final Logger logger = Logger.getLogger(TextSubstituter.class.getName());

    protected static void substitute(String path, Map<String, String> parameterMap) {
        try {
            File inputFile = new File(path);
            if (inputFile.exists() && inputFile.isFile() && inputFile.canRead() && inputFile.canWrite()) {
                InputStream in = new FileInputStream(inputFile);
                boolean eof = false;
                StringBuffer strbuf = new StringBuffer();
                while (!eof) {
                    int i = 0;
                    i = in.read();
                    if (i == -1) {
                        eof = true;
                    } else {
                        strbuf.append((char) i);
                    }
                }
                in.close();
                String contentToSubstitute = strbuf.toString();
                for (String key : parameterMap.keySet()) {
                    contentToSubstitute = contentToSubstitute.replaceAll("\\$" + key + "\\$", parameterMap.get(key));
                }
                OutputStream out = new FileOutputStream(inputFile);
                char[] charArray = contentToSubstitute.toCharArray();
                for (char c : charArray) {
                    out.write(c);
                }
                out.close();
                logger.finest("Document at :" + path + " substituted");
            }
        } catch (FileNotFoundException e) {
            logger.warning("Document at : " + path + "not substituted : " + e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.warning("Document at : " + path + "not substituted : " + e);
            throw new RuntimeException(e);
        }
    }
}

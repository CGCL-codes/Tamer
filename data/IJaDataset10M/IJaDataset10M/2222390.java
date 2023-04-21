package com.metanology.mde.core.codeFactory;

import java.io.FileWriter;

/**
 * CodeWriter provide the functionality to write the source
 *  code to a user specified file.
 */
public class CodeWriter {

    private FileWriter outWriter = null;

    private String fileName = "";

    public CodeWriter(String outFileName) {
        this.initialize(outFileName);
    }

    public void initialize(String outFileName) {
        try {
            if (this.outWriter != null) {
                this.outWriter.close();
                this.outWriter = null;
            }
            this.outWriter = new java.io.FileWriter(outFileName);
            this.fileName = outFileName;
        } catch (java.io.IOException e) {
            ;
        }
    }

    public void write(String inStr) {
        try {
            if (this.outWriter != null) {
                this.outWriter.write(inStr);
                this.outWriter.flush();
            }
        } catch (java.io.IOException e) {
            ;
        }
    }

    public void close() {
        try {
            if (this.outWriter != null) {
                this.outWriter.close();
                this.outWriter = null;
            }
        } catch (java.io.IOException e) {
            ;
        }
    }

    public String getFileName() {
        return this.fileName;
    }
}

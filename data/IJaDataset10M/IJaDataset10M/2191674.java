package com.knitml.tools.runner.support;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;

public abstract class AbstractOptions {

    private boolean validate = false;

    private String applicationContextFile;

    private Reader reader;

    private Writer writer;

    public AbstractOptions() {
    }

    public AbstractOptions(CommandLine line) throws MissingArgumentException, IOException {
        this.validate = line.hasOption("checksyntax");
        String optionValue = line.getOptionValue("applicationContextFile");
        if (optionValue != null) {
            this.applicationContextFile = optionValue;
        }
        optionValue = line.getOptionValue("output");
        if (optionValue != null) {
            this.writer = new BufferedWriter(new FileWriter(optionValue));
        } else {
            this.writer = new BufferedWriter(new OutputStreamWriter(System.out));
        }
        String[] leftoverArgs = line.getArgs();
        if (leftoverArgs.length != 1) {
            throw new MissingArgumentException("filename");
        }
        this.reader = new FileReader(leftoverArgs[0]);
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getApplicationContextFile() {
        return applicationContextFile;
    }

    public void setApplicationContextFile(String applicationContextFile) {
        this.applicationContextFile = applicationContextFile;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }
}

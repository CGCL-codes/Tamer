package org.expasy.jpl.commons.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.expasy.jpl.commons.base.builder.BuilderException;
import org.expasy.jpl.commons.base.builder.InstanceBuilder;

public final class InteractiveInputScannerImpl implements InteractiveInputScanner {

    public static final Pattern YES_NO_PATTERN = Pattern.compile("^[yn]$", Pattern.CASE_INSENSITIVE);

    private BufferedReader in;

    private BufferedWriter out;

    private String prompt;

    private Pattern inputPattern;

    private Pattern exitPattern;

    private String defaultInput;

    private Matcher matcher;

    public InteractiveInputScannerImpl(Builder builder) {
        setInputStream(builder.is);
        setOutputStream(builder.os);
        this.prompt = builder.prompt;
        this.inputPattern = builder.inputPattern;
        this.exitPattern = builder.exitPattern;
        this.defaultInput = builder.defaultInput;
    }

    public static class Builder implements InstanceBuilder<InteractiveInputScannerImpl> {

        private InputStream is;

        private OutputStream os;

        private String prompt;

        private Pattern inputPattern;

        private Pattern exitPattern;

        private String defaultInput;

        public Builder() {
            is = System.in;
            os = System.out;
            prompt = "";
            inputPattern = Pattern.compile("^.+$");
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder inputStream(InputStream is) {
            this.is = is;
            return this;
        }

        public Builder outputStream(OutputStream os) {
            this.os = os;
            return this;
        }

        public Builder inputPattern(Pattern pat) {
            this.inputPattern = pat;
            return this;
        }

        public Builder defaultInput(String defaultInput) {
            this.defaultInput = defaultInput;
            return this;
        }

        public Builder exitPattern(Pattern pat) {
            this.exitPattern = pat;
            return this;
        }

        public InteractiveInputScannerImpl build() throws BuilderException {
            return new InteractiveInputScannerImpl(this);
        }
    }

    public void setInputStream(InputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("undefined input stream!");
        }
        in = new BufferedReader(new InputStreamReader(stream));
    }

    public void setOutputStream(OutputStream stream) {
        out = new BufferedWriter(new OutputStreamWriter(stream));
    }

    public String waitInput() throws IOException {
        String input = null;
        while (true) {
            if (prompt != null && prompt.length() > 0) {
                out.append(prompt);
                out.append(": ");
                out.flush();
            }
            input = in.readLine();
            matcher = inputPattern.matcher(input);
            if (matcher.find()) {
                break;
            }
            if (defaultInput != null) {
                return defaultInput;
            }
            if (exitPattern != null) {
                matcher = exitPattern.matcher(input);
                if (matcher.find()) {
                    break;
                }
            }
        }
        return input;
    }

    @Override
    public Pattern getInputPattern() {
        return inputPattern;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public Pattern getExitPattern() {
        return exitPattern;
    }

    @Override
    public String getDefaultInput() {
        return defaultInput;
    }
}

package sanger.argml.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import sanger.argml.environment.Environment;
import sanger.argml.environment.Environmental;

public class TextOutput extends Environmental {

    protected File file;

    protected FileWriter fileWriter;

    protected PrintWriter printWriter;

    protected TextOutput(Environment env) {
        super(env);
    }

    public TextOutput(Environment env, OutputStream out) {
        super(env);
        this.printWriter = new PrintWriter(out);
    }

    public TextOutput(Environment env, String pathname) throws IOException {
        super(env);
        file = new File(env.outbase(), pathname);
        if (file.getParentFile().exists() ? true : file.getParentFile().mkdirs()) {
            fileWriter = new FileWriter(file, false);
            printWriter = new PrintWriter(fileWriter, false);
        }
    }

    public void close() throws IOException {
        printWriter.flush();
        printWriter.close();
        if (file != null) fileWriter.close();
    }

    public PrintWriter writer() {
        return printWriter;
    }

    public String toString() {
        return file != null ? file.toString() : "stdout";
    }
}

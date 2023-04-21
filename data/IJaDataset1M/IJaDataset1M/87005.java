package de.denkselbst.sentrick.eval;

import java.io.File;
import java.io.IOException;

public abstract class Eval {

    protected File rootDir;

    protected MisclassificationRecorder outputRecorder;

    protected String encoding;

    public Eval(String[] args) throws IOException {
        if (args.length < 3 || args.length > 4) {
            printUsage();
            System.exit(1);
        }
        rootDir = new File(args[0]);
        encoding = args[1];
        if (args.length == 4) outputRecorder = new FileMisclassificationRecorder(new File(args[3])); else outputRecorder = new NullMisclassificationRecorder();
    }

    public abstract void execute() throws IOException;

    protected abstract void printUsage() throws IOException;

    protected void traverse(File f, ContingencyTable akk) throws IOException {
        if (f.isDirectory()) for (File c : f.listFiles()) traverse(c, akk); else if (f.getName().endsWith(".txt")) {
            ContingencyTable ct = handle(f);
            akk.add(ct);
        }
    }

    protected abstract ContingencyTable handle(File txt) throws IOException;
}

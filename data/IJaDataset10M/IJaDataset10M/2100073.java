package com.peterhi.servlet.cmd;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.StringTokenizer;
import com.peterhi.servlet.Kernel;

public class BatchCommand extends AbstractKernelCommand implements CommandConstants {

    private Console console;

    private Kernel kernel;

    private String R1;

    @Override
    public boolean interact(Kernel kernel, Console console, Properties props, StringTokenizer tok) throws InterruptedException {
        super.interact(kernel, console, props, tok);
        this.kernel = kernel;
        this.console = console;
        if (!tok.hasMoreTokens()) {
            return false;
        }
        R1 = tok.nextToken();
        return true;
    }

    @Override
    public void clean() {
        R1 = null;
    }

    @Override
    public String getName() {
        return batchCommand;
    }

    @Override
    public void usage() {
        console.printf("use %s [file name]\n", getName());
    }

    @Override
    public void run() {
        try {
            File file = new File(R1);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            console.printf("execute batch from %s\n", file.getAbsolutePath());
            String line;
            while ((line = reader.readLine()) != null) {
                console.printf("executing: %s\n", line);
                kernel.executeCommand(line);
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            console.printf("aborted\n");
        }
    }
}

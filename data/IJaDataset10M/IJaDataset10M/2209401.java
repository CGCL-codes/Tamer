package engine.cmd.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import engine.Command;
import engine.expr.AbstractBlock.BlockExecution;

public class _open extends Command {

    @Override
    public void run(final BlockExecution exec) {
        final String mode = exec.popString();
        final String file = exec.popString();
        try {
            if (mode.equals("r")) exec.push(new BufferedReader(new FileReader(file)));
            if (mode.equals("w")) exec.push(new PrintStream(file));
            if (mode.equals("wa")) exec.push(new PrintStream(new FileOutputStream(file, true)));
        } catch (final FileNotFoundException e) {
            exec.push("fnf");
        }
    }
}

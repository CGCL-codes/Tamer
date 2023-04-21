package engine.cmd.io;

import java.io.Reader;
import java.util.Scanner;
import engine.Command;
import engine.error.VException;
import engine.expr.AbstractBlock.BlockExecution;

public class _scanner extends Command {

    @Override
    public void run(final BlockExecution exec) {
        final String delimiter = exec.popString();
        final Object on = exec.pop();
        Scanner s;
        if (on instanceof String) s = new Scanner((String) on); else if (on instanceof Reader) s = new Scanner((Reader) on); else throw new VException("Cannot initialise scanner with " + on.getClass());
        exec.push(s.useDelimiter(delimiter));
    }
}

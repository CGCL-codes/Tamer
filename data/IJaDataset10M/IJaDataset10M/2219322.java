package engine.cmd.var;

import engine.Command;
import engine.error.VException;
import engine.expr.AbstractBlock.BlockExecution;

public class _put extends Command {

    public _put() {
        super(":=");
    }

    @Override
    public void run(final BlockExecution exec) {
        if (!(exec.peek() instanceof Changable)) throw new VException("Cannot assign to " + exec.peek() + ". Did you quote?");
        Changable var = (Changable) exec.pop();
        Object val = exec.pop();
        var.set(exec, val);
    }
}

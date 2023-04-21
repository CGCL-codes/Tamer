package engine;

import engine.error.CompileVExcexption;
import engine.expr.AbstractBlock;
import engine.expr.AbstractBlock.BlockExecution;

/**
 * A command which loads libraries. The library is loaded before execution time.
 * This command does not have any effect during run-time. It may appear anywhere
 * in the file with the same effect.
 * 
 * @author Robert J. C. Himmelmann (robert-h@gmx.de)
 */
public abstract class RequireCommand extends Command implements FlowCommand {

    public RequireCommand() {
        super();
    }

    public RequireCommand(final String name) {
        super(name);
    }

    @Override
    public final void runCompile(final AbstractBlock parent) throws CompileVExcexption {
        final Object before = ((Symbol) parent.peekInst()).get();
        parent.insts.remove(parent.insts.size() - 1);
        runCompile(parent, (String) before);
    }

    public abstract void runCompile(AbstractBlock parent, String name) throws CompileVExcexption;

    @Override
    public final void run(final BlockExecution exec) {
    }
}

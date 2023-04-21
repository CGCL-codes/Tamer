package engine.cmd.flow;

import engine.Command;
import engine.FlowCommand;
import engine.cmd.flow._if.IfJump;
import engine.expr.AbstractBlock;
import engine.expr.AbstractBlock.BlockExecution;

public class _else extends Command implements FlowCommand {

    public static class ElseJump extends IfJump {

        public ElseJump() {
            super(null);
        }

        @Override
        public void run(final BlockExecution parent) {
            parent.instPointer = destination;
        }
    }

    @Override
    public void runCompile(final AbstractBlock parent) {
        final IfJump j = parent.ifs.pop();
        j.destination = parent.insts.size();
        final ElseJump jump = new ElseJump();
        parent.ifs.push(jump);
        parent.pushInst(jump);
    }

    @Override
    public void run(final BlockExecution exec) {
    }
}

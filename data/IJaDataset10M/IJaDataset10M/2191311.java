package net.sf.brightside.eterminals.core.command;

public class SimpleEvaluator implements Evaluator {

    @Override
    public <Evaluation> Evaluation evaluate(Command<Evaluation> command) {
        return command.execute();
    }
}

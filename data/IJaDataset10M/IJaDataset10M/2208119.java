package upperbound.learningtools.funes;

import java.util.Set;

public class MultipleChoiceExamProcessFinished extends MultipleChoiceExamProcessState {

    public MultipleChoiceExamProcessFinished(MultipleChoiceExamProcess process) {
        super(process);
    }

    @Override
    public void finish() {
        throw new IllegalStateException();
    }

    @Override
    public void start() {
        throw new IllegalStateException();
    }

    @Override
    public void answer(MultipleChoiceQuestion question, Set selectedAnswers) {
        throw new IllegalStateException();
    }

    @Override
    public MultipleChoiceExamResult getResult() {
        return process.getResultWhenFinished();
    }
}

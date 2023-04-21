package upperbound.learningtools.funes;

import java.util.Set;

public class MultipleChoiceExamResult {

    private Object student;

    private MultipleChoiceExam exam;

    private AbstractMultipleChoiceExamQualification qualification;

    private Set<MultipleChoiceAnswer> correctAnswers;

    private Set<MultipleChoiceAnswer> wrongAnswers;

    public MultipleChoiceExamResult(Object student, MultipleChoiceExam exam, AbstractMultipleChoiceExamQualification qualification, Set<MultipleChoiceAnswer> correctAnswers, Set<MultipleChoiceAnswer> wrongAnswers) {
        this.student = student;
        this.exam = exam;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.qualification = qualification;
    }

    public boolean isApproved() {
        return qualification.isApproved();
    }

    public Object getStudent() {
        return student;
    }

    public MultipleChoiceExam getExam() {
        return exam;
    }

    public Set<MultipleChoiceAnswer> getCorrectAnswers() {
        return correctAnswers;
    }

    public Set<MultipleChoiceAnswer> getWrongAnswers() {
        return wrongAnswers;
    }
}

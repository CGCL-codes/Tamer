package ru.spbspu.staub.entity;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import ru.spbspu.staub.model.answer.AnswerType;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The <code>QuestionTrace</code> class represents QuestionTrace entity.
 *
 * @author Alexander V. Elagin
 */
@Entity
@Table(schema = "staub", name = "question_trace")
@org.hibernate.annotations.TypeDef(name = "answer_type", typeClass = ru.spbspu.staub.entity.XmlType.class, parameters = { @Parameter(name = "pojoClass", value = "ru.spbspu.staub.model.answer.AnswerType") })
public class QuestionTrace implements Serializable {

    private static final long serialVersionUID = -2332696164283250715L;

    private Integer id;

    private Date started;

    private Date finished;

    private Integer totalTime;

    private AnswerType answer;

    private Question question;

    private TestTrace testTrace;

    private Boolean correct;

    private Integer part;

    @Id
    @SequenceGenerator(name = "QuestionTraceIdGenerator", sequenceName = "seq_question_trace", allocationSize = 1)
    @GeneratedValue(generator = "QuestionTraceIdGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, length = 10)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "started")
    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finished")
    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    @Basic
    @Column(name = "total_time", length = 10)
    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    @Column(name = "answer")
    @Type(type = "answer_type")
    public AnswerType getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerType answer) {
        this.answer = answer;
    }

    @OneToOne
    @JoinColumn(name = "fk_question", referencedColumnName = "id", nullable = false)
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @ManyToOne
    @JoinColumn(name = "fk_test_trace", referencedColumnName = "id", nullable = false)
    public TestTrace getTestTrace() {
        return testTrace;
    }

    public void setTestTrace(TestTrace testTrace) {
        this.testTrace = testTrace;
    }

    @Basic
    @Column(name = "correct")
    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    @Basic
    @Column(name = "part")
    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof QuestionTrace)) {
            return false;
        }
        QuestionTrace other = (QuestionTrace) otherObject;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QuestionTrace");
        sb.append("{id=").append(id);
        sb.append(", started=").append(started);
        sb.append(", finished=").append(finished);
        sb.append(", totalTime=").append(totalTime);
        sb.append(", correct=").append(correct);
        sb.append(", part=").append(part);
        sb.append('}');
        return sb.toString();
    }
}

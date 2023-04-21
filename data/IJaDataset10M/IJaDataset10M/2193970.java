package cat.jm.languages.model;

import javax.persistence.*;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "PAST_EXAMPLES")
@XStreamAlias("pastExamples")
public class PastExamples implements java.io.Serializable {

    @Id
    @Column(name = "ID", unique = true, nullable = false, precision = 4, scale = 4)
    @XStreamAsAttribute
    @javax.validation.constraints.NotNull
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "POSITIVE", nullable = false, length = 100)
    @javax.validation.constraints.NotNull
    @javax.validation.constraints.Size(max = 100)
    private String positive;

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    @Column(name = "NEGATIVE", nullable = false, length = 100)
    @javax.validation.constraints.NotNull
    @javax.validation.constraints.Size(max = 100)
    private String negative;

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    @Column(name = "QUESTION", nullable = false, length = 100)
    @javax.validation.constraints.NotNull
    @javax.validation.constraints.Size(max = 100)
    private String question;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @ManyToOne
    @JoinColumn(name = "ID_PAST")
    private Past past;

    public Past getPast() {
        return past;
    }

    public void setPast(Past past) {
        this.past = past;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!this.getId().equals(((PastExamples) o).getId())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (this.getId() != null) result = 31 * result + this.getId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append(" [");
        sb.append("ID").append("=").append(getId()).append(", ");
        sb.append("POSITIVE").append("=").append(getPositive()).append(", ");
        sb.append("NEGATIVE").append("=").append(getNegative()).append(", ");
        sb.append("QUESTION").append("=").append(getQuestion());
        sb.append("]");
        return sb.toString();
    }
}

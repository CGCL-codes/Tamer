package gr.academic.city.msc.industrial.mobileclickers.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@Entity
public class SubmitedAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Answer answer;

    private String uniqueSubmissionCode;

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getUniqueSubmissionCode() {
        return uniqueSubmissionCode;
    }

    public void setUniqueSubmissionCode(String uniqueSubmissionCode) {
        this.uniqueSubmissionCode = uniqueSubmissionCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SubmitedAnswer)) {
            return false;
        }
        SubmitedAnswer other = (SubmitedAnswer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.academic.city.msc.industrial.mobileclickers.entity.SubmitedAnswer[id=" + id + "]";
    }
}

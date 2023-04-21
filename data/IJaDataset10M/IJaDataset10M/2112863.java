package at.itsecuritycheckpoint.hibernate.domain;

import java.util.Date;
import java.util.Set;

public class History {

    private Long id;

    private Date date;

    private String email;

    private Branche branche;

    private Set<Answer> answers;

    public History() {
        this.date = null;
        this.email = "";
    }

    public History(Date date) {
        this.date = date;
        this.email = "";
    }

    public History(Date date, Set<Answer> answers) {
        this.date = date;
        this.answers = answers;
        this.email = "";
    }

    public History(Set<Answer> answers) {
        this.answers = answers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Branche getBranche() {
        return this.branche;
    }

    public void setBranche(Branche branche) {
        this.branche = branche;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public void assignBranche(Branche branche) {
        if (branche != null) {
            branche.addHistory(this);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Id : ");
        sb.append(id);
        sb.append("\nDate: ");
        sb.append(date);
        return sb.toString();
    }
}

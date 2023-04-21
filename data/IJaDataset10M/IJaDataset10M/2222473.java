package org.shopformat.domain.marketing.competition;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.shopformat.domain.customer.Customer;

public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Customer entrant;

    private Date submissionTime;

    private String answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getEntrant() {
        return entrant;
    }

    public void setEntrant(Customer entrant) {
        this.entrant = entrant;
    }

    public Date getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Date submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

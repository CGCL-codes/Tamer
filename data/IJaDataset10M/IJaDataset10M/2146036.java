package org.tolven.passwordrecovery.model;

import java.io.Serializable;

/**
 * 
 * @author Joseph Isaac
 * 
 * This entity contains the security questions with their purpose.
 *
 */
public class SecurityQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String purpose;

    private String question;

    public SecurityQuestion() {
    }

    public SecurityQuestion(String question, String purpose) {
        this.question = question;
        this.purpose = purpose;
    }

    public Long getId() {
        return id;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getQuestion() {
        return question;
    }

    public String getPurposeAndQuestion() {
        return getPurpose() + ":" + getQuestion();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

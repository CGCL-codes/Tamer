package org.vardb.polls.dao;

import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.vardb.util.*;

@Entity
@Table(name = "choices")
public class CChoice {

    protected Integer m_id;

    protected Integer m_question_id;

    protected String m_text = "";

    protected CQuestion m_question;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return m_id;
    }

    public void setId(Integer id) {
        m_id = id;
    }

    @Column(insertable = false, updatable = false)
    public Integer getQuestion_id() {
        return m_question_id;
    }

    public void setQuestion_id(final Integer question_id) {
        m_question_id = question_id;
    }

    public String getText() {
        return m_text;
    }

    public void setText(String text) {
        m_text = text;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    public CQuestion getQuestion() {
        return m_question;
    }

    public void setQuestion(final CQuestion question) {
        m_question = question;
    }

    public CChoice() {
    }

    public CChoice(String text) {
        m_text = text;
    }

    public void initialize() {
    }
}

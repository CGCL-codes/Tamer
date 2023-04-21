package com.epam.mvc3.model;

import java.sql.Date;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_COMMENT")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "TOPIC_ID")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User author;

    private String text;

    private Date creationDate;

    public Comment() {
        Calendar calendarInstance = Calendar.getInstance();
        creationDate = new java.sql.Date(calendarInstance.getTimeInMillis());
    }

    /**
	 * @return the iD
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param iD the iD to set
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the topic
	 */
    public Topic getTopic() {
        return topic;
    }

    /**
	 * @param topic the topic to set
	 */
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    /**
	 * @return the author
	 */
    public User getAuthor() {
        return author;
    }

    /**
	 * @param author the author to set
	 */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
	 * @return the text
	 */
    public String getText() {
        return text;
    }

    /**
	 * @param text the text to set
	 */
    public void setText(String text) {
        this.text = text;
    }

    /**
	 * @return the creationDate
	 */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
	 * @param creationDate the creationDate to set
	 */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

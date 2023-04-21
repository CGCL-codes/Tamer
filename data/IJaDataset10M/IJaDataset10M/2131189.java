package fr.umlv.jee.hibou.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import fr.umlv.jee.hibou.bdd.table.Meeting;

/**
 * Bean class for Meeting object.
 * @author micka, alex, nak, matt
 *
 */
@XmlRootElement(name = "meetingBean", namespace = "http://javax.hibou/jaxws")
@XmlAccessorType(XmlAccessType.FIELD)
public class MeetingBean {

    @XmlElement(name = "id", namespace = "")
    private long id;

    @XmlElement(name = "postDate", namespace = "")
    private long postDate;

    @XmlElement(name = "author", namespace = "")
    private String author;

    @XmlElement(name = "recipients", namespace = "")
    private String recipients;

    @XmlElement(name = "subject", namespace = "")
    private String subject;

    @XmlElement(name = "meetingDate", namespace = "")
    private long meetingDate;

    @XmlElement(name = "place", namespace = "")
    private String place;

    @XmlElement(name = "time", namespace = "")
    private String time;

    @XmlElement(name = "message", namespace = "")
    private String message;

    /**
	 * Constructor.
	 * @param meeting the meeting object.
	 */
    public MeetingBean(Meeting meeting) {
        this.id = meeting.getId();
        this.postDate = meeting.getPostDate().getTime();
        this.author = meeting.getAuthor();
        this.recipients = meeting.getRecipients();
        this.subject = meeting.getSubject();
        this.meetingDate = meeting.getMeetingDate().getTime();
        this.place = meeting.getPlace();
        this.time = meeting.getTime();
        this.message = meeting.getMessage();
    }

    /**
	 * @return the author
	 */
    public String getAuthor() {
        return author;
    }

    /**
	 * @param author the author to set
	 */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
	 * @return the id
	 */
    public long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(long id) {
        this.id = id;
    }

    /**
	 * @return the meetingDate
	 */
    public long getMeetingDate() {
        return meetingDate;
    }

    /**
	 * @param meetingDate the meetingDate to set
	 */
    public void setMeetingDate(long meetingDate) {
        this.meetingDate = meetingDate;
    }

    /**
	 * @return the message
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * @param message the message to set
	 */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
	 * @return the place
	 */
    public String getPlace() {
        return place;
    }

    /**
	 * @param place the place to set
	 */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
	 * @return the postDate
	 */
    public long getPostDate() {
        return postDate;
    }

    /**
	 * @param postDate the postDate to set
	 */
    public void setPostDate(long postDate) {
        this.postDate = postDate;
    }

    /**
	 * @return the recipients
	 */
    public String getRecipients() {
        return recipients;
    }

    /**
	 * @param recipients the recipients to set
	 */
    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    /**
	 * @return the subject
	 */
    public String getSubject() {
        return subject;
    }

    /**
	 * @param subject the subject to set
	 */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
	 * @return the time
	 */
    public String getTime() {
        return time;
    }

    /**
	 * @param time the time to set
	 */
    public void setTime(String time) {
        this.time = time;
    }
}

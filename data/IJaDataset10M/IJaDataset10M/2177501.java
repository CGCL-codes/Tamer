package net.sf.gilead.test.domain.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.gilead.pojo.java5.LightEntity;
import net.sf.gilead.test.domain.interfaces.IMessage;
import net.sf.gilead.test.domain.interfaces.IUser;

/**
 * DTO Message class for Java5 support
 * This class just has to inherit from LazyGwtPojo
 * It is also used as DTO for the Java5 Message POJO
 * @author bruno.marchesson
 *
 */
public class MessageDTO extends LightEntity implements IMessage {

    /**
	 * Serialisation ID
	 */
    private static final long serialVersionUID = 3445339493203407152L;

    private int id;

    private Integer version;

    private String message;

    private Date date;

    private UserDTO author;

    private Map<String, Integer> keywords;

    /**
	 * @return the id
	 */
    public final int getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public final void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the version
	 */
    public Integer getVersion() {
        return version;
    }

    /**
	 * @param version the version to set
	 */
    public void setVersion(Integer version) {
        this.version = version;
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
	 * @return the timeStamp
	 */
    public Date getDate() {
        return date;
    }

    /**
	 * @param timeStamp the timeStamp to set
	 */
    public void setDate(Date timeStamp) {
        this.date = timeStamp;
    }

    /**
	 * @return the author
	 */
    public IUser getAuthor() {
        return author;
    }

    /**
	 * @param author the author to set
	 */
    public void setAuthor(UserDTO author) {
        this.author = (UserDTO) author;
    }

    /**
	 * @see net.sf.gilead.testApplication.domain.IMessage#getKeywords(
	 */
    private Map<String, Integer> getKeywords() {
        return keywords;
    }

    /**
	 * @see net.sf.gilead.test.domain.interfaces.IMessage#setKeywords(java.util.Map)
	 */
    private void setKeywords(Map<String, Integer> keywords) {
        this.keywords = keywords;
    }

    /**
	 * Add a keyword in the associated map
	 * @param key
	 * @param value
	 */
    public void addKeyword(String key, Integer value) {
        if (keywords == null) {
            keywords = new HashMap<String, Integer>();
        }
        keywords.put(key, value);
    }

    public void clearKeywords() {
        if (keywords != null) {
            keywords.clear();
        }
    }

    public int countKeywords() {
        if (keywords != null) {
            return keywords.size();
        } else {
            return 0;
        }
    }

    /**
	 * Equality function
	 */
    public boolean equals(Object obj) {
        if ((obj == null) || (obj instanceof MessageDTO == false)) {
            return false;
        } else if (this == obj) {
            return true;
        }
        MessageDTO other = (MessageDTO) obj;
        return (id == other.getId());
    }
}

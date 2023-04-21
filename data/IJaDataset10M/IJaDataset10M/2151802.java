package net.sf.hibernate4gwt.testApplication.domain;

import java.util.Date;
import java.util.Map;

/**
 * Interface of the Java 1.4 domain message classes (stateful and stateless)
 * @author bruno.marchesson
 *
 */
public interface IMessage {

    /**
	 * @return the id
	 */
    public int getId();

    /**
	 * @param id the id to set
	 */
    public void setId(int id);

    /**
	 * @return the version
	 */
    public Integer getVersion();

    /**
	 * @param version the version to set
	 */
    public void setVersion(Integer version);

    /**
	 * @return the message
	 */
    public String getMessage();

    /**
	 * @param message the message to set
	 */
    public void setMessage(String message);

    /**
	 * @return the timeStamp
	 */
    public Date getDate();

    /**
	 * @param timeStamp the timeStamp to set
	 */
    public void setDate(Date timeStamp);

    /**
	 * @return the author
	 */
    public IUser getAuthor();

    /**
	 * @return the associated keywords and votes
	 */
    public Map getKeywords();

    /**
	 * @param keywords the keywords to set.
	 */
    public void setKeywords(Map keywords);
}

package org.telscenter.sail.webapp.domain.brainstorm.answer;

import java.util.Date;
import net.sf.sail.webapp.domain.Persistable;

/**
 * A revision contains a timestamp and the actual body of the post.
 * 
 * @author Hiroki Terashima
 * @author Patrick Lawler
 * @version $Id: Revision.java 2318 2008-12-10 00:00:29Z honchikun $
 */
public interface Revision extends Persistable, Comparable<Revision> {

    /**
	 * Returns when this revision was authored.
	 * 
	 * @return <code>Date</code> timestamp indicating
	 *     when this revision was written.
	 */
    public Date getTimestamp();

    /**
	 * Sets when this revision was authored.
	 * 
	 * @param timestamp <code>Date</code> timestamp indicating
	 *     when this revision was written.
	 */
    public void setTimestamp(Date timestamp);

    /**
	 * Gets the actual body of this revision in string format.
	 * @return body of this revision
	 */
    public String getBody();

    /**
	 * Sets the body of this revision in string format.
	 * @param body
	 */
    public void setBody(String body);

    /**
	 * Returns the displayname of this revision. This is only
	 * applicable if this revision is for a <code>PreparedAnswer</code>
	 * 
	 * @return String displayname. Name to display this revision under.
	 */
    public String getDisplayname();

    /**
	 * Sets the displayname of this revision. This is only
	 * applicable if this revision is for a <code>PreparedAnswer</code>
	 * 
	 * @param String displayname. Name to display this revision under.
	 */
    public void setDisplayname(String displayname);
}

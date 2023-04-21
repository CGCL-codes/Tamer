package org.telscenter.sail.webapp.domain.project;

import java.io.Serializable;
import net.sf.sail.webapp.domain.Persistable;

/**
 * @author patrick lawler
 * @version $Id:$
 */
public interface Tag extends Persistable {

    /**
	 * @return String - the name to get
	 */
    public String getName();

    /**
	 * @param String - the name to set
	 */
    public void setName(String name);

    /**
	 * @return Serializable - the id to get
	 */
    public Serializable getId();

    /**
	 * @param Long - the id to set
	 */
    public void setId(Long id);
}

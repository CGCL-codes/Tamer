package org.yafra.model;

import java.io.Serializable;
import org.yafra.interfaces.IYafraMYafraRole;

/**
 * Object</br>
 * @see org.yafra.interfaces.IYafraMYafraRole
 * @version $Id: MYafraRole.java,v 1.2 2009-12-09 22:40:15 mwn Exp $
 * @author <a href="mailto:martin.weber@yafra.org">Martin Weber</a>
 * @since 1.0
 */
public class MYafraRole implements IYafraMYafraRole, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String description;

    private String name;

    private String rights;

    public MYafraRole() {
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getRights() {
        return this.rights;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

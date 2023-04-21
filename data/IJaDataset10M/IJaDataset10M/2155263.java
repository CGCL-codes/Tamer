package org.yafra.model;

import java.io.Serializable;
import org.yafra.interfaces.IYafraMYafraBusinessRole;

/**
 * Object</br>
 * @see org.yafra.interfaces.IYafraMYafraBusinessRole
 * @version $Id: MYafraBusinessRole.java,v 1.3 2009-12-09 22:40:15 mwn Exp $
 * @author <a href="mailto:martin.weber@yafra.org">Martin Weber</a>
 * @since 1.0
 */
public class MYafraBusinessRole implements IYafraMYafraBusinessRole, Serializable {

    private static final long serialVersionUID = 1L;

    private String description;

    private Boolean flag;

    private String name;

    public MYafraBusinessRole() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}

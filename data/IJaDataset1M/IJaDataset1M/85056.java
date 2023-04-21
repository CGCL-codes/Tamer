package com.asoft.common.logs.model;

import java.io.Serializable;
import com.asoft.common.base.model.BaseObject;

/**
 * <p>Title: 操作日志 - 模块</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: asoft</p>
 * @ $Author: author $
 * @ $Date: 2005/02/15 09:14:59 $
 * @ $Revision: 1.7 $
 * @ created in 2005-11-14
 *
 */
public class Module extends BaseObject implements Serializable {

    private String id;

    private String name;

    public Module(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Module)) {
            return false;
        }
        Module module = (Module) object;
        return (this.id.equals(module.getId()));
    }
}

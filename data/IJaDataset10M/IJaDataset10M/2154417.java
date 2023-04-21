package com.companyname.common.logs.model;

import java.sql.Timestamp;
import java.io.Serializable;
import com.companyname.common.base.model.BaseObject;

/**
 * <p>Title: 操作日志 - 实体</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 公司名</p>
 * @ $Author: author $
 * @ $Date: 2005/02/15 09:14:59 $
 * @ $Revision: 1.7 $
 * @ created in 2005-11-14
 *
 */
public class Model extends BaseObject implements Serializable {

    private String id;

    private String name;

    private String moduleId;

    private String module;

    public Model(String id, String name, String moduleId, String module) {
        this.moduleId = moduleId;
        this.module = module;
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

    public String getModuleId() {
        return this.moduleId;
    }

    public String getModule() {
        return this.module;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Module)) {
            return false;
        }
        Module module = (Module) object;
        return (this.id.equals(module.getId()));
    }
}

package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 绯荤粺瀛楀吀瑙嗗浘
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class VZdSystemdicDto implements Serializable {

    public VZdSystemdicDto() {
    }

    private String systemdicid;

    private String name;

    private String fatherid;

    private String typeid;

    private String typename;

    private String viewid;

    /**
     * getters and setters
     */
    public void setSystemdicid(String systemdicid) {
        this.systemdicid = systemdicid;
    }

    public String getSystemdicid() {
        return systemdicid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFatherid(String fatherid) {
        this.fatherid = fatherid;
    }

    public String getFatherid() {
        return fatherid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTypename() {
        return typename;
    }

    public void setViewid(String viewid) {
        this.viewid = viewid;
    }

    public String getViewid() {
        return viewid;
    }
}

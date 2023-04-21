package com.narirelays.ems.persistence.orm;

import java.util.HashSet;
import java.util.Set;

/**
 * TaskInfo entity. @author MyEclipse Persistence Tools
 */
public class TaskInfo implements java.io.Serializable {

    private String id;

    private ClassRegister classRegister;

    private String name;

    private String groupname;

    private String description;

    private String version;

    private Set taskParamVs = new HashSet(0);

    /** default constructor */
    public TaskInfo() {
    }

    /** minimal constructor */
    public TaskInfo(String id) {
        this.id = id;
    }

    /** full constructor */
    public TaskInfo(String id, ClassRegister classRegister, String name, String groupname, String description, String version, Set taskParamVs) {
        this.id = id;
        this.classRegister = classRegister;
        this.name = name;
        this.groupname = groupname;
        this.description = description;
        this.version = version;
        this.taskParamVs = taskParamVs;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClassRegister getClassRegister() {
        return this.classRegister;
    }

    public void setClassRegister(ClassRegister classRegister) {
        this.classRegister = classRegister;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupname() {
        return this.groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set getTaskParamVs() {
        return this.taskParamVs;
    }

    public void setTaskParamVs(Set taskParamVs) {
        this.taskParamVs = taskParamVs;
    }
}

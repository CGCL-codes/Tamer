package org.mdextractor.dataaccess.bean;

import javax.persistence.*;

@Entity
@Table(name = "_MetaObjects")
public class MetaObjects {

    private Integer objKey;

    private Integer parKey;

    private String className;

    private Integer propCount;

    private String idInt;

    private Integer id;

    private String sname;

    private String lname;

    private String comment;

    private String objContent;

    private String strError;

    @Id
    @Column(name = "ObjKey", unique = true, insertable = true)
    public Integer getObjKey() {
        return objKey;
    }

    public void setObjKey(Integer objKey) {
        this.objKey = objKey;
    }

    @Column(name = "ParKey")
    public Integer getParKey() {
        return parKey;
    }

    public void setParKey(Integer parKey) {
        this.parKey = parKey;
    }

    @Column(name = "ClassName")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Column(name = "PropCount")
    public Integer getPropCount() {
        return propCount;
    }

    public void setPropCount(Integer propCount) {
        this.propCount = propCount;
    }

    @Column(name = "ID_Int")
    public String getIdInt() {
        return idInt;
    }

    public void setIdInt(String idInt) {
        this.idInt = idInt;
    }

    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "Sname")
    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    @Column(name = "Lname")
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @Column(name = "Comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(name = "ObjContent", length = 1024)
    public String getObjContent() {
        return objContent;
    }

    public void setObjContent(String objContent) {
        this.objContent = objContent;
    }

    @Column(name = "StrError")
    public String getStrError() {
        return strError;
    }

    public void setStrError(String strError) {
        this.strError = strError;
    }
}

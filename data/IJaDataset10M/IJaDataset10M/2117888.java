package com.performance.model;

import java.io.Serializable;
import java.util.Set;
import com.jxva.dao.annotation.OneToMany;
import com.jxva.dao.annotation.Table;

/**
 * 
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2010-02-10 10:00:22 by Automatic Generate Toolkit
 */
@Table(name = "tbl_operator", increment = "null", primaryKeys = { "code" })
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;

    private java.lang.String name;

    private java.lang.String description;

    private java.lang.String location;

    private java.lang.String code;

    @OneToMany(field = "code", foreignKey = "operatorCode")
    private Set<ColorRingUrl> colorRingUrlGroup;

    @OneToMany(field = "code", foreignKey = "operatorCode")
    private Set<SongRing> songRingGroup;

    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getDescription() {
        return this.description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getLocation() {
        return this.location;
    }

    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    public java.lang.String getCode() {
        return this.code;
    }

    public void setCode(java.lang.String code) {
        this.code = code;
    }

    public Set<ColorRingUrl> getColorRingUrlGroup() {
        return colorRingUrlGroup;
    }

    public void setColorRingUrlGroup(Set<ColorRingUrl> colorRingUrlGroup) {
        this.colorRingUrlGroup = colorRingUrlGroup;
    }

    public Set<SongRing> getSongRingGroup() {
        return songRingGroup;
    }

    public void setSongRingGroup(Set<SongRing> songRingGroup) {
        this.songRingGroup = songRingGroup;
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        sb.append("name=").append(name).append(',');
        sb.append("description=").append(description).append(',');
        sb.append("location=").append(location).append(',');
        sb.append("code=").append(code).append(" ]");
        return sb.toString();
    }
}

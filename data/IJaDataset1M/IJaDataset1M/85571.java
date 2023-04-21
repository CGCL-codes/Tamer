package com.unsins.business.domain.system;

import com.unsins.core.BaseObject;
import net.sf.json.util.JSONStringer;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: odpsoft
 * Date: 2008-11-26
 * Time: 14:37:31
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_SYSBOOK")
@org.hibernate.annotations.Entity(mutable = false)
@SequenceGenerator(name = "SEQ_SYS_PARA", sequenceName = "SEQ_SYS_PARA")
public class SysConstant extends BaseObject implements Serializable {

    @Column(name = "TYPE_CODE")
    private String typeCode;

    @Column(name = "TYPE_NAME")
    private String typeName;

    @Column(name = "LIST_CODE")
    private String listCode;

    @Column(name = "LIST_NAME")
    private String listName;

    @Column(name = "LIST_ORDER")
    private String listOrder;

    @Column(name = "REMARK")
    private String remark;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SYS_PARA")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListOrder() {
        return listOrder;
    }

    public void setListOrder(String listOrder) {
        this.listOrder = listOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getListCode() {
        return listCode;
    }

    public void setListCode(String listCode) {
        this.listCode = listCode;
    }

    public String toString() {
        JSONStringer json = new JSONStringer();
        json.object();
        json.key("name");
        json.value(typeName);
        json.key("id");
        json.value(getId());
        json.endObject();
        return json.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SysConstant)) return false;
        final SysConstant constant = (SysConstant) o;
        return this.getId() != null ? getId().equals(constant.getId()) : constant.getId() != null;
    }

    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}

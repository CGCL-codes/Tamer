package com.sporthenon.db.entity.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"~REF_ITEM\"")
public class RefItem {

    @Id
    private Integer id;

    @Column(name = "id_item", nullable = false)
    private Integer idItem;

    @Column(name = "label", length = 70, nullable = false)
    private String label;

    @Column(name = "entity", length = 2, nullable = false)
    private String entity;

    @Column(name = "count_ref")
    private String countRef;

    @Column(name = "id_rel1")
    private Integer idRel1;

    @Column(name = "id_rel2")
    private Integer idRel2;

    @Column(name = "id_rel3")
    private Integer idRel3;

    @Column(name = "id_rel4")
    private Integer idRel4;

    @Column(name = "id_rel5")
    private Integer idRel5;

    @Column(name = "label_rel1", length = 50)
    private String labelRel1;

    @Column(name = "label_rel2", length = 50)
    private String labelRel2;

    @Column(name = "label_rel3", length = 50)
    private String labelRel3;

    @Column(name = "label_rel4", length = 50)
    private String labelRel4;

    @Column(name = "label_rel5", length = 50)
    private String labelRel5;

    @Column(name = "txt1", length = 40)
    private String txt1;

    @Column(name = "txt2", length = 40)
    private String txt2;

    @Column(name = "comment", length = 20)
    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getCountRef() {
        return countRef;
    }

    public void setCountRef(String countRef) {
        this.countRef = countRef;
    }

    public Integer getIdRel1() {
        return idRel1;
    }

    public void setIdRel1(Integer idRel1) {
        this.idRel1 = idRel1;
    }

    public Integer getIdRel2() {
        return idRel2;
    }

    public void setIdRel2(Integer idRel2) {
        this.idRel2 = idRel2;
    }

    public Integer getIdRel3() {
        return idRel3;
    }

    public void setIdRel3(Integer idRel3) {
        this.idRel3 = idRel3;
    }

    public String getLabelRel1() {
        return labelRel1;
    }

    public void setLabelRel1(String labelRel1) {
        this.labelRel1 = labelRel1;
    }

    public String getLabelRel2() {
        return labelRel2;
    }

    public void setLabelRel2(String labelRel2) {
        this.labelRel2 = labelRel2;
    }

    public String getLabelRel3() {
        return labelRel3;
    }

    public void setLabelRel3(String labelRel3) {
        this.labelRel3 = labelRel3;
    }

    public Integer getIdRel4() {
        return idRel4;
    }

    public void setIdRel4(Integer idRel4) {
        this.idRel4 = idRel4;
    }

    public Integer getIdRel5() {
        return idRel5;
    }

    public void setIdRel5(Integer idRel5) {
        this.idRel5 = idRel5;
    }

    public String getLabelRel4() {
        return labelRel4;
    }

    public void setLabelRel4(String labelRel4) {
        this.labelRel4 = labelRel4;
    }

    public String getLabelRel5() {
        return labelRel5;
    }

    public void setLabelRel5(String labelRel5) {
        this.labelRel5 = labelRel5;
    }

    public String getTxt1() {
        return txt1;
    }

    public void setTxt1(String txt1) {
        this.txt1 = txt1;
    }

    public String getTxt2() {
        return txt2;
    }

    public void setTxt2(String txt2) {
        this.txt2 = txt2;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "RefItem [comment=" + comment + ", countRef=" + countRef + ", entity=" + entity + ", id=" + id + ", idItem=" + idItem + ", idRel1=" + idRel1 + ", idRel2=" + idRel2 + ", idRel3=" + idRel3 + ", idRel4=" + idRel4 + ", idRel5=" + idRel5 + ", label=" + label + ", labelRel1=" + labelRel1 + ", labelRel2=" + labelRel2 + ", labelRel3=" + labelRel3 + ", labelRel4=" + labelRel4 + ", labelRel5=" + labelRel5 + ", txt1=" + txt1 + ", txt2=" + txt2 + "]";
    }
}

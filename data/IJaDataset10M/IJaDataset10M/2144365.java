package edu.unibi.agbi.biodwh.entity.omim;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "omim_gene_disorders")
@Table(name = "omim_gene_disorders")
public class OmimGeneDisorders implements java.io.Serializable {

    private static final long serialVersionUID = -3054493313047642423L;

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "cm")
    private String cm;

    @Column(name = "MIM", nullable = false)
    private String mim;

    @Column(name = "disorder")
    private String disorder;

    @Column(name = "disorder_id")
    private String disorder_id;

    @Column(name = "disorder_c1")
    private String disorder_c1;

    @Column(name = "disorder_c1_id")
    private String disorder_c1_id;

    @Column(name = "disorder_c2", length = 1024)
    private String disorder_c2;

    @Column(name = "disorder_c2_id")
    private String disorder_c2_id;

    public OmimGeneDisorders() {
    }

    public OmimGeneDisorders(int id, String cm, String mim, String disorder, String disorder_id, String disorder_c1, String disorder_c1_id, String disorder_c2, String disorder_c2_id) {
        this.id = id;
        this.cm = cm;
        this.mim = mim;
        this.disorder = disorder;
        this.disorder_id = disorder_id;
        this.disorder_c1 = disorder_c1;
        this.disorder_c1_id = disorder_c1_id;
        this.disorder_c2 = disorder_c2;
        this.disorder_c2_id = disorder_c2_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCm() {
        return cm;
    }

    public void setCm(String cm) {
        this.cm = cm;
    }

    public String getDisorder() {
        return disorder;
    }

    public void setDisorder(String disorder) {
        this.disorder = disorder;
    }

    public String getDisorder_id() {
        return disorder_id;
    }

    public void setDisorder_id(String disorder_id) {
        this.disorder_id = disorder_id;
    }

    public String getDisorder_c1() {
        return disorder_c1;
    }

    public void setDisorder_c1(String disorder_c1) {
        this.disorder_c1 = disorder_c1;
    }

    public String getDisorder_c1_id() {
        return disorder_c1_id;
    }

    public void setDisorder_c1_id(String disorder_c1_id) {
        this.disorder_c1_id = disorder_c1_id;
    }

    public String getDisorder_c2() {
        return disorder_c2;
    }

    public void setDisorder_c2(String disorder_c2) {
        this.disorder_c2 = disorder_c2;
    }

    public String getDisorder_c2_id() {
        return disorder_c2_id;
    }

    public void setDisorder_c2_id(String disorder_c2_id) {
        this.disorder_c2_id = disorder_c2_id;
    }

    public String getMim() {
        return mim;
    }

    public void setMim(String mim) {
        this.mim = mim;
    }
}

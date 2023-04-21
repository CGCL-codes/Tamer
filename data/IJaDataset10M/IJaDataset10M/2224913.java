package com.intersys.acidminer.model;

import com.jalapeno.annotations.CacheProperty;
import com.jalapeno.annotations.PropertyParameter;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import com.jalapeno.annotations.Indices;
import com.jalapeno.annotations.Index;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
@Indices({ @Index(name = "ATreeNodeIdx", propertyNames = { "ATreeNode", "aminoAcid" }, isUnique = true), @Index(name = "AminoAcidIdx", propertyNames = { "aminoAcid" }, type = "bitmap"), @Index(name = "UnamabigousIdx", propertyNames = { "unambiguous" }, type = "bitmap") })
public class ANodeState {

    @GeneratedValue
    @Id
    private String idPlaceHolder;

    private char aminoAcid;

    private boolean unambiguous;

    private double p;

    private ATreeNode mATreeNode;

    public ANodeState() {
    }

    public ANodeState(ATreeNode node, ANodeState state) {
        this(node, state.aminoAcid, state.unambiguous, state.p);
    }

    public ANodeState(ATreeNode ATreeNode, char aminoAcid, boolean unambiguous, double p) {
        this.aminoAcid = aminoAcid;
        mATreeNode = ATreeNode;
        this.p = p;
        this.unambiguous = unambiguous;
    }

    @CacheProperty(type = "%String")
    @PropertyParameter(name = "MAXLEN", value = "1")
    public char getAminoAcid() {
        return aminoAcid;
    }

    public void setAminoAcid(char aminoAcid) {
        this.aminoAcid = aminoAcid;
    }

    @ManyToOne(targetEntity = ATreeNode.class)
    public ATreeNode getATreeNode() {
        return mATreeNode;
    }

    public void setATreeNode(ATreeNode ATreeNode) {
        mATreeNode = ATreeNode;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public boolean isUnambiguous() {
        return unambiguous;
    }

    public void setUnambiguous(boolean unambiguous) {
        this.unambiguous = unambiguous;
    }
}

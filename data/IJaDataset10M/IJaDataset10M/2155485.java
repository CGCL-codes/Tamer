package com.intersys.acidminer.model;

import com.jalapeno.annotations.CacheProperty;
import com.jalapeno.annotations.Index;
import com.jalapeno.annotations.Indices;
import com.jalapeno.annotations.Implements;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@Indices({ @Index(name = "nodeUniqueIdx", propertyNames = { "node", "dest", "source" }, isUnique = true), @Index(name = "toFromIdx", propertyNames = { "dest", "source" }, type = "bitmap"), @Index(name = "paralogIdx", propertyNames = { "paralog" }, type = "bitmap"), @Index(name = "confirmedIdx", propertyNames = { "unambiguous" }, type = "bitmap"), @Index(name = "DistIdx", propertyNames = { "age" }, type = "bitslice"), @Index(name = "DupIdx", propertyNames = { "distanceFromDuplication" }, type = "bitslice"), @Index(name = "nodeIdx", propertyNames = { "node" }) })
@Implements(classNames = { "%BI.Adaptor" })
public class Substitution implements Comparable<Substitution> {

    public static int nConfirmed = 0;

    private String mDbId;

    private char source;

    private char dest;

    private double age;

    private double distanceFromDuplication;

    private boolean unambiguous;

    private double probability;

    private boolean paralog;

    private ATreeNode node;

    private int[] options;

    @Transient
    private int cachedHashCode = -123;

    public Substitution() {
    }

    public String toString() {
        if (node == null) return String.valueOf(getSource()) + " -> " + getDest();
        return node.getPath() + ": " + String.valueOf(getSource()) + " -> " + getDest();
    }

    public void confirm() {
        setUnambiguous(true);
        setProbability(1.0);
        setOptions(null);
        nConfirmed++;
    }

    public int compareTo(Substitution substitution) {
        double d = getAge() - substitution.getAge();
        if (d > 0.001) return 1;
        if (d < -0.001) return -1;
        int ret = getSource() - substitution.getSource();
        if (ret != 0) return ret;
        ret = getDest() - substitution.getDest();
        if (ret != 0) return ret;
        ret = getNode().getPId() - substitution.getNode().getPId();
        if (ret != 0) return ret;
        return getNode().getPath().compareTo(substitution.getNode().getPath());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Substitution)) {
            return false;
        }
        Substitution that = (Substitution) o;
        if (getAge() != that.getAge()) {
            return false;
        }
        if (getSource() != that.getSource()) {
            return false;
        }
        if (getDest() != that.getDest()) {
            return false;
        }
        if (getNode() != null ? !getNode().equals(that.getNode()) : that.getNode() != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (cachedHashCode == -123) {
            int result = (int) getSource();
            result = 31 * result + (int) getDest();
            result = 31 * result + (getNode() != null ? getNode().hashCode() : 0);
            cachedHashCode = result;
        }
        return cachedHashCode;
    }

    @GeneratedValue
    @Id
    public String getDbId() {
        return mDbId;
    }

    public void setDbId(String dbId) {
        this.mDbId = dbId;
    }

    @CacheProperty(type = "%String", required = true)
    public char getSource() {
        return source;
    }

    public void setSource(char source) {
        this.source = source;
    }

    @CacheProperty(type = "%String", required = true)
    public char getDest() {
        return dest;
    }

    public void setDest(char dest) {
        this.dest = dest;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getDistanceFromDuplication() {
        return distanceFromDuplication;
    }

    public void setDistanceFromDuplication(double distanceFromDuplication) {
        this.distanceFromDuplication = distanceFromDuplication;
    }

    public boolean isUnambiguous() {
        return unambiguous;
    }

    public void setUnambiguous(boolean unambiguous) {
        this.unambiguous = unambiguous;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public boolean isParalog() {
        return paralog;
    }

    public void setParalog(boolean paralog) {
        this.paralog = paralog;
    }

    @ManyToOne(targetEntity = ATreeNode.class)
    @CacheProperty(required = true)
    public ATreeNode getNode() {
        return node;
    }

    public void setNode(ATreeNode node) {
        this.node = node;
    }

    /**
     * List of indexes of options in substitution options for unconfirmed paths
     */
    public int[] getOptions() {
        return options;
    }

    public void setOptions(int[] options) {
        this.options = options;
    }
}

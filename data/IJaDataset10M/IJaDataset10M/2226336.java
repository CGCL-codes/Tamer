package org.spantus.extract.segments.offline;

import java.io.Serializable;

/**
 * 
 * @author Mindaugas Greibus
 * 
 *
 */
public class ExtremeEntry implements Cloneable, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public enum FeatureStates {

        stable, max, increasing, min, decreasing
    }

    private Integer index;

    private Float value;

    private FeatureStates signalState;

    private ExtremeEntry next;

    private ExtremeEntry previous;

    public ExtremeEntry(Integer index, Float value, FeatureStates signalState) {
        super();
        this.index = index;
        this.value = value;
        this.signalState = signalState;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public FeatureStates getSignalState() {
        return signalState;
    }

    public void setSignalState(FeatureStates signalStates) {
        this.signalState = signalStates;
    }

    public void link(ExtremeEntry previous, ExtremeEntry next) {
        this.next = next;
        this.previous = previous;
    }

    public ExtremeEntry getNext() {
        return next;
    }

    public void setNext(ExtremeEntry next) {
        this.next = next;
    }

    public ExtremeEntry getPrevious() {
        return previous;
    }

    public void setPrevious(ExtremeEntry previous) {
        this.previous = previous;
    }

    public boolean gt(ExtremeEntry entry) {
        return this.getValue() > entry.getValue();
    }

    public boolean lt(ExtremeEntry entry) {
        return this.getValue() < entry.getValue();
    }

    @Override
    public String toString() {
        return getIndex() + "=>[" + getValue() + "; " + getSignalState() + "]";
    }

    public ExtremeEntry clone() {
        try {
            return (ExtremeEntry) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

package org.effrafax.lightsout.domain.interfaces;

/**
 * @author daan
 * 
 */
public interface State {

    public void setNumberOfStates(Integer numberOfStates) throws IllegalArgumentException;

    public Integer getNumberOfStates() throws IllegalStateException;

    public void setState(Integer state) throws IllegalArgumentException;

    public Integer getState() throws IllegalStateException;

    public void incrementState();

    public void incrementState(Integer increment) throws IllegalArgumentException;
}

package org.chernovia.lib.sims.ca;

/**
 * Write a description of interface CA_Listener here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public interface CA_Listener {

    public void startingRun();

    public void finishedRun();

    public void resize(int dim);

    public void nextTick(int tick);
}

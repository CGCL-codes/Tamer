package jaga.control;

import jaga.Genotype;
import jaga.evolve.Population;

/** An interaction model containing a nested interaction model inside.  Designed
 * to be extended so that an extra functionality can be added.
 *
 * @author  Michael Garvie
 * @version 
 */
public class ShellIM implements InteractionModel {

    protected InteractionModel yokeIM;

    public ShellIM(InteractionModel im) {
        yokeIM = im;
    }

    public double[] evaluate(Genotype[] inds) {
        return yokeIM.evaluate(inds);
    }

    public String toString() {
        String narrator = "Shell Interaction Model with:";
        narrator += "\n\n Yoke Interaction Model: " + yokeIM;
        narrator += "\n";
        return narrator;
    }

    /** @return The maximum fitness a genotype can achieve. ie. when evolution will stop
    */
    public Genotype getMaxFitness() {
        return yokeIM.getMaxFitness();
    }

    /** @return the number of evaluations per generation to be performed
     */
    public int evaluationsPerGeneration() {
        return yokeIM.evaluationsPerGeneration();
    }

    /** Picks a number of individuals to be evaluated next.
     * These individuals can be from any of the populations as long as there
     * is a standard interface between the caller and this method implementation
     * as to where each come from.
     * Example: may be first element a pac-man, and five others all ghosts..
     */
    public Genotype[] getNextIndividuals() {
        return yokeIM.getNextIndividuals();
    }

    /** Should be same as calling evaluate( pickIndividuals() );
     * @return an array of the fitnesses of the individuals.
     */
    public double[] evaluateNext() {
        return evaluate(yokeIM.getNextIndividuals());
    }

    /** Evolves to the next generation of all/some of the populations involved.
     */
    public void evolve() {
        yokeIM.evolve();
    }

    /** @return the number of evolvers (ie. populations) acting in this model.
     */
    public int getNumEvolvers() {
        return yokeIM.getNumEvolvers();
    }

    /** @return randomly picked individuals from the evolvers.
     */
    public Genotype[] pickIndividuals() {
        return yokeIM.pickIndividuals();
    }

    /** @return the current populations of the evolvers.
     */
    public Population[] getPopulations() {
        return yokeIM.getPopulations();
    }

    /** Sets the populations of the evolvers to be these
     */
    public void setPopulations(Population[] pops) {
        yokeIM.setPopulations(pops);
    }

    /** @return String displaying performance of fittest individual.
     */
    public islandev.SnapshotPainter getSnapshotPainter() {
        return yokeIM.getSnapshotPainter();
    }
}

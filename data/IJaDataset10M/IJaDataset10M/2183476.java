package org.neuroph.contrib.neat.gen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.neuroph.contrib.neat.gen.util.OrganismHelper;

/**
 * A <code>Specie</code> represents a collection of <code>Organism</code>s that
 * are deemed similar by the <code>Speciator</code>.
 * 
 * It is also responsible for storing the fitness of each <code>Organism</code>
 * during the evaulation process.
 * 
 * @author Aidan Morgan
 */
public class Specie implements Serializable {

    /**
	 * The innovation id of this <code>Specie</code>.
	 */
    private long specieId;

    /**
	 * A <code>List</code> of <code>Organism</code>s that are in this
	 * <code>Specie</code>.
	 */
    private List<Organism> organisms;

    /**
	 * The <code>NeatParameters</code> that define the environment this
	 * <code>Specie</code> is operating in.
	 */
    private NeatParameters neatParams;

    /**
	 * Whether this <code>Specie</code> is dead or not.
	 */
    private boolean isDead;

    /**
	 * The <code>Organism</code> that best represents this <code>Specie</code>.
	 */
    private Organism representativeOrganism;

    /**
	 * Constructor.
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that define the environment
	 *            this <code>Specie</code> is operating in.
	 */
    public Specie(NeatParameters params) {
        this(params, new ArrayList<Organism>());
    }

    /**
	 * Constructor
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that define the environment
	 *            this <code>Specie</code> is operating in.
	 * @param organisms
	 *            a <code>List</code> of <code>Organism</code>s that make up
	 *            this <code>Specie</code>.
	 */
    public Specie(NeatParameters params, List<Organism> organisms) {
        this.neatParams = params;
        this.specieId = neatParams.nextInnovationId();
        this.organisms = organisms;
    }

    private Specie(long specieId, List<Organism> organisms) {
        this.specieId = specieId;
        this.organisms = organisms;
    }

    /**
	 * Returns the <code>Organism</code>s that are determined to be part of this
	 * <code>Specie</code>.
	 * 
	 * @return the <code>Organism</code>s that are determined to be part of this
	 *         <code>Specie</code>.
	 */
    public List<Organism> getOrganisms() {
        return Collections.unmodifiableList(organisms);
    }

    /**
	 * Adds the provided <code>Organism</code> to this <code>Specie</code>.
	 * 
	 * @param o
	 *            the <code>Organism</code> to add to this <code>Specie</code>.
	 */
    public void addOrganism(Organism o) {
        if (o == null) {
            throw new IllegalArgumentException("Organism cannot be null.");
        }
        if (organisms.isEmpty()) {
            representativeOrganism = o;
        }
        if (!organisms.contains(o)) {
            organisms.add(o);
            o.setSpecies(this);
        }
    }

    /**
	 * Remove any <code>Organism</code>s that are in this <code>Specie</code>
	 * that are not in the provided list of survivors.
	 * 
	 * @param o
	 *            a <code>List</code> of <code>Organism</code>s to keep, all
	 *            others are killed off.
	 */
    public void cull(List<Organism> org) {
        for (Organism o : organisms) {
            if (!org.contains(o)) {
                o.setSpecies(null);
            }
        }
        organisms.retainAll(org);
    }

    /**
	 * Returns the number of <code>Organism</code>s in this <code>Specie</code>.
	 * 
	 * @return the number of <code>Organism</code>s in this <code>Specie</code>.
	 */
    public int getOrganismCount() {
        return organisms.size();
    }

    /**
	 * Sets whether this <code>Specie</code> is dead.
	 * 
	 * @param b
	 *            <code>true</code> if this <code>Specie</code> is dead,
	 *            <code>false</code> otherwise.
	 */
    public void setDead(boolean b) {
        this.isDead = b;
    }

    /**
	 * Returns whether this <code>Specie</code> is dead.
	 * 
	 * @return <code>true</code> if this <code>Specie</code> is dead,
	 *         <code>false</code> otherwise.
	 */
    public boolean isDead() {
        return isDead;
    }

    public Specie copy() {
        List<Organism> clones = OrganismHelper.copy(neatParams, organisms);
        Specie s = new Specie(specieId, clones);
        for (Organism o : clones) {
            o.setSpecies(s);
        }
        return s;
    }

    public Organism getRepresentativeOrganism() {
        return representativeOrganism;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (specieId ^ (specieId >>> 32));
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Specie other = (Specie) obj;
        if (specieId != other.specieId) return false;
        return true;
    }

    /**
	 * Callback to inform the <code>Specie</code> about which stage the
	 * evolution process is in.
	 * 
	 * @param type
	 *            the <code>EvolutionEventType</code> describing the stage.
	 */
    public void update(EvolutionEventType type) {
        for (Organism o : organisms) {
            o.update(type);
        }
    }
}

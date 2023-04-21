package com.csol.chem.net;

import com.csol.chem.core.Molecule;

/**
 * TODO change BasicResult to an interface and make the present BasicResult class into
 * BasicResult or something like that, as it would clean up StoredResult.
 * @author jiro
 *
 */
public class BasicResult extends Message implements Result {

    protected long jobId = -1;

    protected Molecule molecule;

    protected double energy = Double.NaN;

    protected Throwable evaluationException;

    /**
     * This constructor exists so that the BasicResult can be extended more
     * easily by weird stuff like CompoundJob, etc. 
     */
    protected BasicResult() {
    }

    /**
	 * Normal constructor for a successful result
	 */
    public BasicResult(long jobId, Molecule molecule, double energy) {
        super();
        this.jobId = jobId;
        this.molecule = molecule;
        this.energy = energy;
        evaluationException = null;
    }

    /**
     * Construtor for a failed result. Store the original job molecule. Energy is set to NaN
     */
    public BasicResult(long jobId, Molecule molecule, Throwable evaluationException) {
        this.evaluationException = evaluationException;
        this.jobId = jobId;
        this.molecule = molecule;
        this.energy = Double.NaN;
    }

    /** */
    public long getJobId() {
        return jobId;
    }

    /** */
    public Molecule getMolecule() {
        return molecule;
    }

    /** */
    public double getEnergy() {
        return energy;
    }

    /** */
    public boolean isOk() {
        return (evaluationException == null);
    }

    /** */
    public Throwable getException() {
        return evaluationException;
    }
}

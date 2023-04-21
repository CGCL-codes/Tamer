package gov.sns.xal.model.probe.resp;

import gov.sns.tools.beam.PhaseMatrix;
import gov.sns.tools.data.DataAdaptor;
import gov.sns.xal.model.probe.resp.traj.ParticlePerturbProbeState;
import gov.sns.xal.model.probe.resp.traj.ParticlePerturbProbeTrajectory;
import gov.sns.xal.model.probe.traj.ProbeState;
import gov.sns.xal.model.xml.ParsingException;

/**
 *
 * @author  Christopher Allen
 * @author Craig McChesney
 */
public class ParticlePerturb extends Perturbation {

    private PhaseMatrix m_matResp = PhaseMatrix.identity();

    public PhaseMatrix getTransferMatrix() {
        return m_matResp;
    }

    public void setTransferMatrix(PhaseMatrix m) {
        m_matResp = m;
    }

    /**
	 * Creates a Trajectory for saving the probe's history.
	 */
    @Override
    public ParticlePerturbProbeTrajectory createTrajectory() {
        return new ParticlePerturbProbeTrajectory();
    }

    /**
	 * Captures the probe's state.
	 */
    @Override
    public ParticlePerturbProbeState createProbeState() {
        return new ParticlePerturbProbeState(this);
    }

    @Override
    protected ProbeState readStateFrom(DataAdaptor container) throws ParsingException {
        ParticlePerturbProbeState state = new ParticlePerturbProbeState();
        state.load(container);
        return state;
    }
}

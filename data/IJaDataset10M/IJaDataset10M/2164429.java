package gov.sns.xal.model.probe.traj;

import gov.sns.tools.beam.PhaseMap;
import gov.sns.tools.beam.PhaseMatrix;
import gov.sns.tools.math.r3.R3;
import java.util.Iterator;

/**
 * Specializes the <code>Trajectory</code> class to the 
 * <code>TransferMapProbe<code> behavior.
 * 
 * @author Christopher K. Allen
 *
 */
public class TransferMapTrajectory extends Trajectory {

    private static final int NUM_MODES = 3;

    /** full turn map at the lattice origin */
    private final PhaseMap _originFullTurnMap;

    /** tunes for x, y and z */
    private final double[] _tunes;

    /** tunes for x, y and z */
    private final double[] _fullTunes;

    /** indicates if the tune needs to be calculated */
    private boolean _needsTuneCalculation;

    /** indicates if the tune needs to be calculated */
    private boolean _needsFullTuneCalculation;

    /**
	 * Constructor
	 */
    public TransferMapTrajectory() {
        _originFullTurnMap = new PhaseMap();
        _tunes = new double[3];
        _fullTunes = new double[3];
        _needsTuneCalculation = true;
        _needsFullTuneCalculation = true;
    }

    /**
     * Create and return an uninitialized <code>ProbeState</code> object
     * of the appropriate type.
     * 
     * @return  an empty <code>TransferMapState</code> object
     * 
     * @see gov.sns.xal.model.probe.traj.Trajectory#newProbeState()
     * @see gov.sns.xal.model.probe.traj.TransferMapState
     */
    @Override
    protected ProbeState newProbeState() {
        return new TransferMapState(this, null, null);
    }

    /**
	 * Set the full turn map to the one specified.
	 *
	 * @param fullTurnMap the full turn map to use for the trajectory
	 */
    public void setFullTurnMap(final PhaseMap fullTurnMap) {
        _originFullTurnMap.setFrom(fullTurnMap);
    }

    /**
	 * Get the full turn map at the origin.
	 */
    public PhaseMap getFullTurnMapAtOrigin() {
        return _originFullTurnMap;
    }

    /**
	 * Get the x, y and z tunes.
	 * @return the array of tunes of the three modes (x, y and z).
	 */
    public double[] getTunes() {
        calculateTunesIfNeeded();
        return _tunes;
    }

    public double[] getFullTunes() {
        calculateFullTunesIfNeeded();
        return _fullTunes;
    }

    private void calculateFullTunesIfNeeded() {
        if (!_needsFullTuneCalculation) {
            return;
        } else {
            _needsFullTuneCalculation = false;
        }
        _needsTuneCalculation = true;
        calculateTunesIfNeeded();
        Iterator<ProbeState> iter = this.stateIterator();
        int nx = 0;
        int ny = 0;
        int nz = 0;
        double betaxMax = 0;
        double betayMax = 0;
        double betazMax = 0;
        double epsilon = Math.PI / 4;
        int counter = 0;
        while (iter.hasNext()) {
            TransferMapState state = (TransferMapState) iter.next();
            R3 beta = state.getBetatronPhase();
            if ((betaxMax > 2 * Math.PI - epsilon) && (beta.getx() < epsilon)) {
                betaxMax = 0;
                nx++;
            } else if (beta.getx() > betaxMax) {
                betaxMax = beta.getx();
            }
            if ((betayMax > 2 * Math.PI - epsilon) && (beta.gety() < epsilon)) {
                betayMax = 0;
                ny++;
            } else if (beta.gety() > betayMax) {
                betayMax = beta.gety();
            }
            if ((betazMax > 2 * Math.PI - epsilon) && (beta.getz() < epsilon)) {
                betayMax = 0;
                ny++;
            } else if (beta.getz() > betazMax) {
                betazMax = beta.gety();
            }
            counter++;
        }
        if (_tunes[0] < 0) {
            _fullTunes[0] = _tunes[0] + (nx + 1);
        } else {
            _fullTunes[0] = _tunes[0] + nx;
        }
        if (_tunes[1] < 0) {
            _fullTunes[1] = _tunes[1] + (ny + 1);
        } else {
            _fullTunes[1] = _tunes[1] + ny;
        }
        if (_tunes[2] < 0) {
            _fullTunes[2] = _tunes[2] + (nz + 1);
        } else {
            _fullTunes[2] = _tunes[2] + nz;
        }
    }

    private void calculateTunesIfNeeded() {
        if (_needsTuneCalculation) {
            final double PI2 = 2 * Math.PI;
            final PhaseMatrix matrix = _originFullTurnMap.getFirstOrder();
            for (int mode = 0; mode < NUM_MODES; mode++) {
                final int index = 2 * mode;
                double trace = matrix.getElem(index, index) + matrix.getElem(index + 1, index + 1);
                double m12 = matrix.getElem(index, index + 1);
                double mu = Math.acos(trace / 2);
                if (m12 < 0) {
                    mu *= (-1);
                }
                _tunes[mode] = mu / PI2;
            }
            _needsTuneCalculation = false;
        }
    }
}

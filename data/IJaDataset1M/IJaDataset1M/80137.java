package de.ibis.permoto.gui.preddesigner.model.mediator;

import java.math.BigInteger;
import java.util.List;
import java.util.ListIterator;
import de.ibis.permoto.model.basic.predictionbusinesscase.MeasureTypeType;
import de.ibis.permoto.model.basic.predictionbusinesscase.StationPerformanceIndex;
import de.ibis.permoto.model.definitions.IClassSection;
import de.ibis.permoto.model.definitions.IPredictionBusinessCase;
import de.ibis.permoto.model.definitions.IPredictionSolverSection;
import de.ibis.permoto.model.definitions.IStationSection;

/**
 * @author Slavko Segota
 *
 */
public class SimulationSolverMediator implements SolverMediator {

    private IPredictionSolverSection pss;

    private boolean isChangedDuringSession;

    private boolean isSet;

    private static BigInteger randomSeed = BigInteger.valueOf(23000);

    private static boolean randomUsage = true;

    private static Double maxDuration = 600.0;

    private static boolean isInfiniteTime = true;

    private static boolean isNoAutomaticStop = false;

    private static BigInteger maxNrSamples = BigInteger.valueOf(500000);

    private IPredictionBusinessCase pbc;

    public SimulationSolverMediator(IPredictionBusinessCase pbc) {
        this.pbc = pbc;
        this.pss = this.pbc.getPredictionSolverSection();
        this.isChangedDuringSession = false;
        this.isSet = this.pss.areSimulationSolverParametersSet();
        this.initParameters();
    }

    /**
	 * 
	 */
    private void initParameters() {
        if (!this.pss.areSimulationSolverParametersSet()) {
            this.pss.setSimulationSolverParameters(true);
            this.pss.setRandomSeed(randomSeed);
            this.pss.setRandomUsage(randomUsage);
            this.pss.setMaxDuration(maxDuration);
            this.pss.setInfiniteTime(isInfiniteTime);
            this.pss.setMaxNrSamples(maxNrSamples);
            this.pss.setIsNoAutomaticStop(isNoAutomaticStop);
        }
    }

    public boolean isDifferentFromOriginal() {
        return false;
    }

    public IPredictionSolverSection getOriginal() {
        return this.pss;
    }

    public IPredictionSolverSection getToReadFrom() {
        return this.pss;
    }

    public boolean hasNotAppliedChanges() {
        return false;
    }

    public boolean reloadValuesFromModel(boolean overWriteNotSavedChanges) {
        return true;
    }

    public void applyToModel() {
        this.pss.setSimulationSolverParameters(this.isSet);
    }

    private void updateConfiguration(Integer rs, Boolean ru, Double md, Boolean it, Integer mns, Boolean nas) {
        this.pss.setRandomSeed(BigInteger.valueOf(rs.longValue()));
        this.pss.setRandomUsage(ru);
        this.pss.setMaxDuration(md);
        this.pss.setInfiniteTime(it);
        this.pss.setMaxNrSamples(BigInteger.valueOf(mns.longValue()));
        this.pss.setIsNoAutomaticStop(nas);
        this.isChangedDuringSession = true;
    }

    private void updateStationPerformanceIndices(List<StationPerformanceIndexMock> pi) {
        List<StationPerformanceIndex> list = this.pss.getPerfomanceIndices();
        list.clear();
        ListIterator<StationPerformanceIndexMock> li = pi.listIterator();
        IClassSection cs = this.pbc.getBasicBusinessCase().getClassSection();
        IStationSection iss = this.pbc.getBasicBusinessCase().getStationSection();
        while (li.hasNext()) {
            StationPerformanceIndexMock mock = li.next();
            this.pss.addStationPerformanceIndex(iss.getStationByID(mock.getStationID()), cs.getClassByID(mock.getClassID()), MeasureTypeType.fromValue(mock.getMeasureType()), mock.getConfInterval(), mock.getRelMaxError());
            this.isChangedDuringSession = true;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean updateActualValues(Object[] p) {
        if (p.length == 1 && p[0] instanceof List) {
            this.updateStationPerformanceIndices((List<StationPerformanceIndexMock>) p[0]);
            return true;
        } else if (p.length == 6) {
            if (p[0] instanceof Integer && p[1] instanceof Boolean && p[2] instanceof Double && p[3] instanceof Boolean && p[4] instanceof Integer && p[5] instanceof Boolean) {
                this.updateConfiguration((Integer) p[0], (Boolean) p[1], (Double) p[2], (Boolean) p[3], (Integer) p[4], (Boolean) p[5]);
                return true;
            }
        } else if (p.length == 7) {
            if (p[0] instanceof Integer && p[1] instanceof Boolean && p[2] instanceof Double && p[3] instanceof Boolean && p[4] instanceof Integer && p[5] instanceof Boolean && p[6] instanceof List) {
                this.updateConfiguration((Integer) p[0], (Boolean) p[1], (Double) p[2], (Boolean) p[3], (Integer) p[4], (Boolean) p[5]);
                this.updateStationPerformanceIndices((List<StationPerformanceIndexMock>) p[6]);
                return true;
            }
        }
        return false;
    }

    public boolean isChangedDuringSession() {
        return this.isChangedDuringSession;
    }

    public boolean isSet() {
        return this.isSet;
    }

    public void setIsSet(boolean value) {
        this.isSet = value;
    }

    public void setHasNotAppliedChanges(boolean value) {
    }

    public void setIsChangedDuringSession(boolean value) {
        this.isChangedDuringSession = value;
    }
}

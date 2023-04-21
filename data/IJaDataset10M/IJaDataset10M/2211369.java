package de.ibis.permoto.gui.solver.parametric;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import de.ibis.permoto.exception.IncorrectDistributionParameterException;
import de.ibis.permoto.model.basic.scenario.ArrivalRate;
import de.ibis.permoto.model.basic.scenario.DistributionParameter;
import de.ibis.permoto.model.basic.scenario.PerMoToObjectFactory;
import de.ibis.permoto.model.definitions.IClassSection;
import de.ibis.permoto.model.definitions.IStationSection;
import de.ibis.permoto.model.distributions.Distribution;
import de.ibis.permoto.solver.sim.mgt.definitions.ISimulationDefinition;
import de.ibis.permoto.solver.sim.mgt.definitions.impl.PerMoToParametricAnalysisDefinition;

/**
 * <p>
 * Title: ArrivalRateParametricAnalysis
 * </p>
 * <p>
 * Description: this class is used to describe a parametric analysis where the
 * varied parameter is the mean value of the arrival rate of an/all open
 * classes. It adds the <code >classKey</code> field used to keep the key of
 * the Job-Class whose service time will be varied, and a boolean value
 * <code>singleClass</code> used to choose the type of service time growth
 * (single or all class).
 * </p>
 * 
 * @author Francesco D'Aquino Date: 14-dic-2005 Time: 12.22.28
 * @author Oliver Huehn
 */
public class ArrivalRateParametricAnalysis extends PerMoToParametricAnalysisDefinition {

    private static final double FROM_ALL = 100;

    private static final double TO_ALL = 150;

    private static final double INCREMENT_SINGLE = 2;

    private static final int STEPS = 10;

    @SuppressWarnings("unused")
    private static final boolean SINGLE_CLASS = false;

    /** tells whether the used Analysis depends on a single class */
    private boolean singleClass;

    private String classKey;

    private List<String> availableClasses;

    private Object values;

    /** Factory for creating PerMoTo Objects */
    private PerMoToObjectFactory pof = new PerMoToObjectFactory();

    public ArrivalRateParametricAnalysis(final IClassSection cd, final IStationSection sd, final ISimulationDefinition simd) {
        super(IParametricAnalysis.PA_TYPE_ARRIVAL_RATE, cd, sd, simd);
        setNumberOfSteps(STEPS);
        final List<String> avaible = new ParametricAnalysisChecker(cd, sd).checkForArrivalRatesParametricSimulationAvailableClasses();
        try {
            if ((cd.getClassIDsOfOpenClasses().size() == 1) || (avaible.size() < cd.getClassIDsOfOpenClasses().size())) {
                singleClass = true;
                classKey = avaible.get(0);
                ArrivalRate ar = cd.getClassByID(classKey).getArrivalRate();
                Distribution arrivalRateDist = getCachedArrivalRateDistribution(classKey, ar);
                final double arrivalRate = arrivalRateDist.getMean();
                setInitialValue(arrivalRate);
                setFinalValue(arrivalRate * INCREMENT_SINGLE);
            } else {
                singleClass = false;
                for (String classID : avaible) {
                    ArrivalRate arrivalRate = cd.getClassByID(classID).getArrivalRate();
                    getCachedArrivalRateDistribution(classID, arrivalRate);
                }
                setInitialValue(FROM_ALL);
                setFinalValue(TO_ALL);
            }
        } catch (IncorrectDistributionParameterException idpe) {
            idpe.printStackTrace();
        }
    }

    /**
	 * Changes the model preparing it for the next step. The ArrivalRates of the
	 * Classes of the business case must be changed according the specification
	 * from the UI.
	 * @throws IncorrectDistributionParameterException
	 */
    public void changeModel(int step) throws IncorrectDistributionParameterException {
        if (step >= getNumberOfSteps()) {
            return;
        }
        if (values != null) {
            if (singleClass) {
                final Double refAR = (Double) ((List<?>) values).get(step);
                ArrivalRate arrivalRate = classDef.getClassByID(classKey).getArrivalRate();
                Double newMean = refAR;
                Distribution arrivalDistribution = getCachedArrivalRateDistributionWithNewMean(classKey, arrivalRate, newMean);
                List<DistributionParameter> ldp = arrivalDistribution.getParameters().getDistributionParameter();
                ArrivalRate newArrivalRate = pof.createArrivalRate(arrivalRate.getDistributionName(), ldp.get(0));
                classDef.getClassByID(classKey).setArrivalRate(newArrivalRate);
            } else {
                for (String classID : availableClasses) {
                    final double refAR = ((ParametricAnalysisValuesTable) values).getValue(classID, step);
                    ArrivalRate arrivalRate = classDef.getClassByID(classID).getArrivalRate();
                    Double newMean = refAR;
                    Distribution arrivalDistribution = getCachedArrivalRateDistributionWithNewMean(classID, arrivalRate, newMean);
                    List<DistributionParameter> ldp = arrivalDistribution.getParameters().getDistributionParameter();
                    ArrivalRate newArrivalRate = pof.createArrivalRate(arrivalRate.getDistributionName(), ldp.get(0));
                    classDef.getClassByID(classID).setArrivalRate(newArrivalRate);
                }
            }
        }
    }

    public int checkCorrectness(boolean autocorrect) {
        int code = 0;
        final ParametricAnalysisChecker checker = new ParametricAnalysisChecker(classDef, stationDef);
        final List<String> openClasses = classDef.getClassIDsOfOpenClasses();
        final List<String> aClasses = checker.checkForArrivalRatesParametricSimulationAvailableClasses();
        if (aClasses.isEmpty()) {
            code = 2;
        } else {
            if (isSingleClass()) {
                if (!aClasses.contains(classKey)) {
                    code = 1;
                    if (autocorrect) {
                        classKey = openClasses.get(0);
                        setDefaultInitialValue();
                        setDefaultFinalValue();
                    }
                } else {
                    ArrivalRate arrivalRate = classDef.getClassByID(classKey).getArrivalRate();
                    Distribution arrivalRateDistribution = null;
                    try {
                        arrivalRateDistribution = getCachedArrivalRateDistribution(classKey, arrivalRate);
                    } catch (IncorrectDistributionParameterException e) {
                        e.printStackTrace();
                    }
                    final double refRate = arrivalRateDistribution.getMean();
                    if (getInitialValue() != refRate) {
                        code = 1;
                        if (autocorrect) {
                            setInitialValue(refRate);
                            setFinalValue(refRate * INCREMENT_SINGLE);
                        }
                    }
                }
            } else {
                if ((aClasses.size() < openClasses.size()) || (openClasses.size() == 1)) {
                    code = 1;
                    if (autocorrect) {
                        classKey = aClasses.get(0);
                        singleClass = true;
                        setDefaultInitialValue();
                        setDefaultFinalValue();
                    }
                }
            }
        }
        return code;
    }

    /**
	 * Finds the set of possible values of the population on which the
	 * simulation may be iterated on.
	 * 
	 */
    @SuppressWarnings("unchecked")
    public void createValuesSet() {
        if (singleClass) {
            double sum = 0;
            final double increment = (getFinalValue() - getInitialValue()) / ((getNumberOfSteps() - 1));
            values = new LinkedList<Object>();
            for (int i = 0; i < getNumberOfSteps(); i++) {
                final double value = getInitialValue() + sum;
                LinkedList<Double> list = (LinkedList<Double>) values;
                list.add(new Double(value));
                sum += increment;
            }
            originalValues = new Double(getInitialValue());
        } else {
            double sum = 1;
            final double increment = (getFinalValue() - getInitialValue()) / (100 * (double) (getNumberOfSteps() - 1));
            final List<String> allClasses = classDef.getClassIDsOfOpenClasses();
            availableClasses = new Vector<String>(0, 1);
            for (String classID : allClasses) {
                ArrivalRate arrivalRate = classDef.getArrivalRate(classID);
                Distribution distr = null;
                try {
                    distr = getCachedArrivalRateDistribution(classID, arrivalRate);
                } catch (IncorrectDistributionParameterException e) {
                    e.printStackTrace();
                }
                if (distr.isHasMean()) {
                    availableClasses.add(classID);
                }
            }
            values = new ParametricAnalysisValuesTable(classDef, availableClasses, getNumberOfSteps());
            for (int i = 0; i < getNumberOfSteps(); i++) {
                for (String classID : availableClasses) {
                    ArrivalRate arrivalRate = classDef.getArrivalRate(classID);
                    Distribution arrivalRateDistribution = null;
                    try {
                        arrivalRateDistribution = getCachedArrivalRateDistribution(classID, arrivalRate);
                    } catch (IncorrectDistributionParameterException e) {
                        e.printStackTrace();
                    }
                    final double thisInitialArrivalRate = arrivalRateDistribution.getMean();
                    final double value = thisInitialArrivalRate * sum;
                    ((ParametricAnalysisValuesTable) values).setValue(classID, value);
                }
                sum += increment;
            }
            originalValues = new Vector<Object>(availableClasses.size());
            for (String classID : availableClasses) {
                ArrivalRate arrivalRate = classDef.getArrivalRate(classID);
                Distribution arrivalRateDistribution = null;
                try {
                    arrivalRateDistribution = getCachedArrivalRateDistribution(classID, arrivalRate);
                } catch (IncorrectDistributionParameterException e) {
                    e.printStackTrace();
                }
                final double thisRate = arrivalRateDistribution.getMean();
                ((List) originalValues).add(new Double(thisRate));
            }
        }
    }

    /**
	 * Returns the values assumed by the varying parameter.
	 * 
	 * @return a Vector containing the values assumed by the varying parameter
	 */
    @SuppressWarnings("unchecked")
    public LinkedList<Double> getParameterValues() {
        final LinkedList<Double> assumedValues = new LinkedList<Double>();
        if (singleClass) {
            LinkedList<Double> list = (LinkedList<Double>) values;
            Double first = list.removeFirst();
            Double rel = 100.0;
            list.addFirst(rel);
            for (Double a : list) {
                int index = list.indexOf(a);
                if (!a.equals(rel)) {
                    a = (a / first) * 100;
                    list.set(index, a);
                }
            }
            return list;
        } else {
            final ParametricAnalysisValuesTable temp = (ParametricAnalysisValuesTable) values;
            final double originalValue = ((Double) ((List<?>) originalValues).get(0)).doubleValue();
            for (int i = 0; i < getNumberOfSteps(); i++) {
                final double thisValue = temp.getValue(availableClasses.get(0), i);
                final double ratio = thisValue / originalValue * 100;
                assumedValues.add(new Double(ratio));
            }
        }
        return assumedValues;
    }

    /**
	 * Gets a TreeMap containing for each property its value. The supported
	 * properties are defined as constants inside this class.
	 * @return a TreeMap containing the value for each property
	 */
    public Map<String, String> getProperties() {
        final TreeMap<String, String> properties = new TreeMap<String, String>();
        properties.put(TYPE_PROPERTY, getType());
        properties.put(TO_PROPERTY, Double.toString(getFinalValue()));
        properties.put(STEPS_PROPERTY, Integer.toString(getNumberOfSteps()));
        properties.put(IS_SINGLE_CLASS_PROPERTY, Boolean.toString(singleClass));
        if (singleClass) {
            properties.put(REFERENCE_CLASS_PROPERTY, classKey);
        }
        return properties;
    }

    /**
	 * Gets the class key of the class whose arrival rate will be increased. If
	 * the simulation is not single class, the <code> null </code> value will be
	 * returned
	 * @return the key of the class whose arrival rate will be increased if the
	 *         parametric analysis is single class, <code> null </code>
	 *         otherwise.
	 */
    public String getReferenceClass() {
        if (singleClass) {
            return classKey;
        }
        return null;
    }

    /**
	 * Get the reference class name.
	 * 
	 * @return the name of the class
	 */
    public String getReferenceClassName() {
        return classDef.getClassName("" + classKey);
    }

    /**
	 * Returns true if only the arrival rate of one class will be increased.
	 * @return true if only the arrrival rate of one class will be increased
	 */
    public boolean isSingleClass() {
        return singleClass;
    }

    /**
	 * Restore the original values of the parameter.
	 */
    public void restoreOriginalValues() {
        if (originalValues != null) {
            if (singleClass) {
                final Double rate = (Double) originalValues;
                ArrivalRate arrivalRate = classDef.getArrivalRate(classKey);
                Double newMean = rate;
                Distribution arrivalDistribution = null;
                List<DistributionParameter> ldp = null;
                try {
                    arrivalDistribution = getCachedArrivalRateDistributionWithNewMean(classKey, arrivalRate, newMean);
                    ldp = arrivalDistribution.getParameters().getDistributionParameter();
                } catch (IncorrectDistributionParameterException e1) {
                    e1.printStackTrace();
                }
                ArrivalRate newArrivalRate = pof.createArrivalRate(arrivalRate.getDistributionName(), ldp.get(0));
                classDef.getClassByID(classKey).setArrivalRate(newArrivalRate);
            } else {
                final List<?> vals = (List<?>) originalValues;
                final List<String> classIDs = classDef.getClassIDsOfOpenClasses();
                int i = 0;
                for (String classID : classIDs) {
                    ArrivalRate ar = classDef.getArrivalRate(classID);
                    final Double thisRate = (Double) vals.get(i);
                    i++;
                    Double newMean = thisRate;
                    Distribution arrivalDistribution = null;
                    List<DistributionParameter> ldp = null;
                    try {
                        arrivalDistribution = getCachedArrivalRateDistributionWithNewMean(classID, ar, newMean);
                        ldp = arrivalDistribution.getParameters().getDistributionParameter();
                    } catch (IncorrectDistributionParameterException e) {
                        e.printStackTrace();
                    }
                    ArrivalRate newArrivalRate = pof.createArrivalRate(ar.getDistributionName(), ldp.get(0));
                    classDef.getClassByID(classID).setArrivalRate(newArrivalRate);
                }
            }
        }
    }

    /**
	 * Gets the maximum number of steps compatible with the model definition and
	 * the type of parametric analysis.
	 * 
	 * @return the maximum number of steps
	 */
    public int searchForAvaibleSteps() {
        return Integer.MAX_VALUE;
    }

    /**
	 * Sets default final value.
	 */
    public void setDefaultFinalValue() {
        if (singleClass) {
            ArrivalRate arrivalRate = classDef.getArrivalRate(classKey);
            Distribution arrivalRateDistribution = null;
            try {
                arrivalRateDistribution = getCachedArrivalRateDistribution(classKey, arrivalRate);
            } catch (IncorrectDistributionParameterException e) {
                e.printStackTrace();
            }
            final double rate = arrivalRateDistribution.getMean() * INCREMENT_SINGLE;
            setFinalValue(rate);
        } else {
            setFinalValue(TO_ALL);
        }
    }

    /**
	 * Sets the default initial Value.
	 */
    public void setDefaultInitialValue() {
        if (singleClass) {
            ArrivalRate arrivalRate = classDef.getArrivalRate(classKey);
            Distribution arrivalRateDistribution = null;
            try {
                arrivalRateDistribution = getCachedArrivalRateDistribution(classKey, arrivalRate);
            } catch (IncorrectDistributionParameterException e) {
                e.printStackTrace();
            }
            final double rate = arrivalRateDistribution.getMean();
            setInitialValue(rate);
        } else {
            setInitialValue(FROM_ALL);
        }
    }

    /**
	 * Sets the value for the specified property. The supported properties are:
	 * <br> - TO_PROPERTY <br> - STEPS_PROPERTY <br> - IS_SINGLE_CLASS_PROPERTY
	 * <br> - REFERENCE_CLASS_PROPERTY
	 * @param propertyName the name of the property to be set
	 * @param value the value to be set
	 */
    public void setProperty(String propertyName, String value) {
        if (propertyName.equals(TO_PROPERTY)) {
            setFinalValue(Double.parseDouble(value));
        } else if (propertyName.equals(STEPS_PROPERTY)) {
            setNumberOfSteps(Integer.parseInt(value));
        } else if (propertyName.equals(IS_SINGLE_CLASS_PROPERTY)) {
            singleClass = Boolean.valueOf(value).booleanValue();
            setDefaultInitialValue();
            setDefaultFinalValue();
        } else if (propertyName.equals(REFERENCE_CLASS_PROPERTY)) {
            classKey = value;
        }
        simDef.setSaveChanged();
    }

    /**
	 * Sets the class whose arrival rate will be increased. If
	 * <code> singleClass </code> value is not true nothing will be done
	 * @param cKey the key of the class whose number of job will be increased
	 */
    public void setReferenceClass(String cKey) {
        if (singleClass) {
            if (!classKey.equals(cKey)) {
                simDef.setSaveChanged();
                classKey = cKey;
            }
        }
    }

    /**
	 * Sets the type of arrival rate increase. If <code> isSingleClass</code>
	 * param is true only the arrival rate of one class will be increased
	 * @param isSingleClass
	 */
    public void setSingleClass(boolean isSingleClass) {
        if (singleClass != isSingleClass) {
            simDef.setSaveChanged();
            singleClass = isSingleClass;
        }
    }
}

package com.pl.itsense.ftsm.common.model.continuous.regression;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import com.pl.itsense.ftsm.common.IAdaptable;
import com.pl.itsense.ftsm.common.IDecisionMaker;
import com.pl.itsense.ftsm.common.IJMXModel;
import com.pl.itsense.ftsm.common.IMarketInfo;
import com.pl.itsense.ftsm.common.IOptimizable;
import com.pl.itsense.ftsm.common.IOrder;
import com.pl.itsense.ftsm.common.IOuputProducer;
import com.pl.itsense.ftsm.common.IParameter;
import com.pl.itsense.ftsm.common.ISignalSelector.Attribute;
import com.pl.itsense.ftsm.common.ISignalSelector.Symbol;
import com.pl.itsense.ftsm.common.ISymbolInfo;
import com.pl.itsense.ftsm.common.IOrder.ID;
import com.pl.itsense.ftsm.common.impl.BaseModel;
import com.pl.itsense.ftsm.common.impl.BasicParameter;
import com.pl.itsense.ftsm.common.impl.Order;
import com.pl.itsense.ftsm.common.impl.Signal;
import com.pl.itsense.ftsm.common.impl.SignalSelector;
import com.pl.itsense.ftsm.common.log.FTSMLogger;
import com.pl.itsense.ftsm.common.log.FTSMLogger.Level;
import com.pl.itsense.ftsm.common.stats.DiscreteDistribution;
import com.pl.itsense.ftsm.common.utils.IOUtils;

public class GeneralRegression extends BaseModel {

    private static final FTSMLogger logger = FTSMLogger.getLogger(GeneralRegression.class);

    private static final long serialVersionUID = 1L;

    public static enum Parameter {

        DISTRIBUTION_N_SAMPLES, TARGET, PREDICTORS, UPDATECOUNTER;

        public static enum OrderGenerator {

            STOP_LOST, TAKE_PROFIT, LOTS
        }
    }

    private IParameter TARGET;

    private IParameter PREDICTORS;

    private IParameter UPDATECOUNTER;

    private IParameter DISTRIBUTION_N_SAMPLES;

    RegressionVariable[] targets;

    RegressionVariable[] predictors;

    int distributonNSamples;

    int update;

    int updateCnt;

    double[] sample;

    List<ISymbolInfo> series;

    List<double[]> samples;

    public GeneralRegression() {
        logger.debug("GeneralRegression()");
        UPDATECOUNTER = new BasicParameter(Parameter.UPDATECOUNTER.name(), null, this);
        TARGET = new BasicParameter(Parameter.TARGET.name(), null, this);
        PREDICTORS = new BasicParameter(Parameter.PREDICTORS.name(), null, this);
        DISTRIBUTION_N_SAMPLES = new BasicParameter(Parameter.DISTRIBUTION_N_SAMPLES.name(), null, this);
        parameters.put(TARGET.getName(), TARGET);
        parameters.put(PREDICTORS.getName(), PREDICTORS);
        parameters.put(UPDATECOUNTER.getName(), UPDATECOUNTER);
        parameters.put(DISTRIBUTION_N_SAMPLES.getName(), DISTRIBUTION_N_SAMPLES);
        samples = new ArrayList<double[]>();
        series = new ArrayList<ISymbolInfo>();
    }

    @Override
    public <T> T getOutput(Class<T> outputType) {
        if (outputType == double[].class) {
            return (T) sample;
        }
        return null;
    }

    private int fillTargets() {
        for (RegressionVariable target : targets) target.clear();
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < series.size(); i++) {
            final ISymbolInfo symbolInfo = series.get(i);
            boolean isComplete = true;
            for (RegressionVariable target : targets) {
                if (!target.isFull()) {
                    if (target.getSignalSelector().getSymbol() == symbolInfo.getID()) {
                        target.append(new Signal(symbolInfo.getTimeStamp(), symbolInfo.get(target.getSignalSelector().getAttribut())));
                    }
                }
                isComplete &= target.isFull();
            }
            if (isComplete) {
                result = i + 1;
                break;
            }
        }
        return result;
    }

    int fillPredictors(int beginIndex) {
        logger.debug("fillPredictors(" + beginIndex + ")");
        int result = Integer.MAX_VALUE;
        for (RegressionVariable predictor : predictors) predictor.clear();
        for (int i = beginIndex; i < series.size(); i++) {
            final ISymbolInfo symbolInfo = series.get(i);
            boolean isComplete = true;
            for (RegressionVariable predictor : predictors) {
                if (!predictor.isFull()) {
                    if (predictor.getSignalSelector().getSymbol() == symbolInfo.getID()) {
                        predictor.append(new Signal(symbolInfo.getTimeStamp(), symbolInfo.get(predictor.getSignalSelector().getAttribut())));
                    }
                }
                isComplete &= predictor.isFull();
            }
            if (isComplete) {
                result = i + 1;
                break;
            }
        }
        return result;
    }

    boolean fillCumulators() {
        final int beginIndexPredictors = fillTargets();
        if (beginIndexPredictors < series.size()) {
            final int lastSample = fillPredictors(beginIndexPredictors);
            if (lastSample <= series.size()) {
                if (lastSample < series.size()) {
                    series.removeAll(new ArrayList(series.subList(lastSample, series.size())));
                }
                return true;
            }
        }
        return false;
    }

    /**
	 * 
	 * @return true if sample is ready
	 */
    private void collectSample() {
        int count = 0;
        for (RegressionVariable target : targets) {
            sample[count] = target.getValue();
            count++;
        }
        for (RegressionVariable predictor : predictors) {
            sample[count] = predictor.getValue();
            count++;
        }
    }

    private boolean isTargetSample(ISymbolInfo symbolInfo) {
        for (RegressionVariable target : targets) {
            if (target.getSignalSelector().getSymbol() == symbolInfo.getID()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void processInput(IOuputProducer source) {
        logger.debug("processInput()");
        final ISymbolInfo symbolInfo = source.getOutput(ISymbolInfo.class);
        if (symbolInfo != null) {
            if (isTargetSample(symbolInfo)) {
                series.add(0, symbolInfo);
                update++;
                if (updateCnt == update) {
                    update = 0;
                    if (fillCumulators()) {
                        collectSample();
                        sendToOutputListners();
                    }
                }
            }
        }
    }

    private boolean createDistribution() {
        logger.debug("createDistribution()");
        if ((targets != null) && (targets.length > 0) && (predictors != null) && (predictors.length > 0)) {
            sample = new double[targets.length + predictors.length];
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStructure() {
        boolean result = createDistribution();
        update = 0;
        samples.clear();
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (RegressionVariable target : targets) {
            builder.append("Target = " + target + "\n");
        }
        for (RegressionVariable predictor : predictors) {
            builder.append("Predictor = " + predictor + "\n");
        }
        return builder.toString();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        for (RegressionVariable target : targets) target.clear();
        for (RegressionVariable predictor : predictors) predictor.clear();
        update = 0;
        Arrays.fill(sample, -1);
        series.clear();
        ;
    }

    @Override
    public void valueChanged(IParameter parameter, Object oldValue, Object newValue) {
        logger.debug("valueChanged: " + parameter);
        isValid = false;
        if (parameter.equals(TARGET)) {
            targets = (RegressionVariable[]) newValue;
            for (RegressionVariable var : targets) {
                assert (var instanceof IDecisionMaker);
            }
            return;
        }
        if (parameter.equals(PREDICTORS)) {
            predictors = (RegressionVariable[]) newValue;
            return;
        }
        if (parameter.equals(UPDATECOUNTER)) {
            updateCnt = (Integer) newValue;
            return;
        }
        if (parameter.equals(DISTRIBUTION_N_SAMPLES)) {
            distributonNSamples = (Integer) newValue;
            return;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getExtension(Class<T> extensionClass) {
        if (extensionClass == OrderGenerator.class) {
            return (T) new OrderGenerator(this);
        }
        if (extensionClass == IJMXModel.class) {
            return (T) new IJMXModel() {

                @Override
                public ObjectName getObjectName() {
                    ObjectName on = null;
                    try {
                        on = new ObjectName("com.pl.itsense.ftsm.common.model", "type", "GeneralDiscreteRegressionJMX");
                    } catch (MalformedObjectNameException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return on;
                }

                @Override
                public Object getMBeanImpl() {
                    return new GeneralRegressionJMX(GeneralRegression.this);
                }
            };
        }
        return null;
    }
}

package com.pl.itsense.ftsm.model.continuous.indicatorbased;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.pl.itsense.ftsm.common.IAdaptable;
import com.pl.itsense.ftsm.common.IDimension;
import com.pl.itsense.ftsm.common.IOptimizable;
import com.pl.itsense.ftsm.common.IOptimizationManager;
import com.pl.itsense.ftsm.common.IOuputProducer;
import com.pl.itsense.ftsm.common.IParameter;
import com.pl.itsense.ftsm.common.IPlatformLifecycle;
import com.pl.itsense.ftsm.common.IPlatformState;
import com.pl.itsense.ftsm.common.IPlatformVariable;
import com.pl.itsense.ftsm.common.IPlatformVariableChangeListener;
import com.pl.itsense.ftsm.common.ISymbolInfo;
import com.pl.itsense.ftsm.common.IPlatformLifecycle.Lifecycle;
import com.pl.itsense.ftsm.common.impl.BaseModel;
import com.pl.itsense.ftsm.common.impl.BasicParameter;
import com.pl.itsense.ftsm.common.impl.Cumulator;
import com.pl.itsense.ftsm.common.impl.Signal;
import com.pl.itsense.ftsm.common.log.FTSMLogger;
import com.pl.itsense.ftsm.common.model.continuous.regression.RegressionVariable;
import com.pl.itsense.ftsm.common.utils.IOUtils;
import com.pl.itsense.ftsm.common.utils.ParameterDiscreteDimension;

public class IndicatorBased extends BaseModel {

    private static final FTSMLogger logger = FTSMLogger.getLogger(IndicatorBased.class);

    private static final long serialVersionUID = 1L;

    public static enum Parameter {

        INDICATORS, UPDATECOUNTER, ACTIVEVARIABLE
    }

    private IParameter INDICATORS;

    private IParameter UPDATECOUNTER;

    private IParameter ACTIVEVARIABLE;

    RegressionVariable[] indicators;

    int update;

    int updateCnt;

    List<double[]> samples;

    Long activeVariable;

    public IndicatorBased() {
        logger.debug(this.getClass().getName());
        UPDATECOUNTER = new BasicParameter(Parameter.UPDATECOUNTER.name(), null, this);
        INDICATORS = new BasicParameter(Parameter.INDICATORS.name(), null, this);
        ACTIVEVARIABLE = new BasicParameter(Parameter.ACTIVEVARIABLE.name(), null, this);
        parameters.put(ACTIVEVARIABLE.getName(), ACTIVEVARIABLE);
        parameters.put(INDICATORS.getName(), INDICATORS);
        parameters.put(UPDATECOUNTER.getName(), UPDATECOUNTER);
        samples = new ArrayList<double[]>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOutput(Class<T> outputType) {
        if (outputType == RegressionVariable[].class) {
            final ArrayList<RegressionVariable> list = new ArrayList<RegressionVariable>();
            StringBuilder platformStateContributor = new StringBuilder();
            for (RegressionVariable regVar : indicators) {
                if (regVar.isActive()) {
                    platformStateContributor.append(regVar.getValue());
                    platformStateContributor.append(",");
                    list.add(regVar);
                }
            }
            if (platformStateContributor.length() > 0) {
                platformStateContributor.deleteCharAt(platformStateContributor.length() - 1);
            }
            final IPlatformState platformState = (IPlatformState) getPlatform().getPlatformVariable(IPlatformState.class.getName());
            platformState.setState(this, platformStateContributor.toString());
            return (T) list.toArray(new RegressionVariable[0]);
        }
        return null;
    }

    @Override
    protected void processInput(IOuputProducer source) {
        logger.debug("processInput(): source = " + source);
        final ISymbolInfo symbolInfo = source.getOutput(ISymbolInfo.class);
        if (symbolInfo != null) {
            boolean isComplete = true;
            boolean isFirst = false;
            int i = 0;
            for (RegressionVariable indicator : indicators) {
                if (!indicator.isActive()) continue;
                if (indicator.getSignalSelector().getSymbol() == symbolInfo.getID()) {
                    indicator.add(new Signal(symbolInfo.getTimeStamp(), symbolInfo.get(indicator.getSignalSelector().getAttribut())));
                    if (i == 0) {
                        isFirst = true;
                    }
                }
                i++;
                isComplete &= indicator.isFull();
            }
            if ((isComplete) && (isFirst)) {
                update++;
                if (updateCnt == update) {
                    update = 0;
                    sendToOutputListners();
                }
            }
        }
    }

    @Override
    public boolean updateStructure() {
        logger.info("updateStructure()");
        update = 0;
        samples.clear();
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].clear();
            indicators[i].setActive((activeVariable & (1 << i)) != 0);
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IndicatorBased: [");
        for (RegressionVariable rVariable : indicators) {
            if (rVariable.isActive()) {
                builder.append(rVariable.getIndicator() + "[" + rVariable.getHistoryLength() + "," + rVariable.getSignalSelector().getSymbol() + "],");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        for (RegressionVariable indicator : indicators) indicator.clear();
        update = 0;
    }

    @Override
    public void valueChanged(IParameter parameter, Object oldValue, Object newValue) {
        logger.debug("valueChanged: " + parameter);
        isValid = false;
        if (parameter.equals(INDICATORS)) {
            indicators = (RegressionVariable[]) newValue;
            return;
        }
        if (parameter.equals(UPDATECOUNTER)) {
            updateCnt = (int) Math.floor((Double) newValue);
            return;
        }
        if (parameter.equals(ACTIVEVARIABLE)) {
            activeVariable = new Long(Math.round((Double) newValue));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getExtension(Class<T> extensionClass) {
        if (extensionClass == IOptimizable.class) {
            return (T) new IOptimizable() {

                @Override
                public void register(final List<IOptimizationManager> managers) {
                    if (!isValid) updateStructure();
                    for (RegressionVariable indicator : indicators) {
                        if (!indicator.isActive()) continue;
                        if (indicator instanceof IAdaptable) {
                            final IOptimizable optimizable = indicator.getExtension(IOptimizable.class);
                            if (optimizable != null) {
                                optimizable.register(managers);
                            }
                        }
                    }
                }
            };
        }
        return null;
    }

    private class OptimizationManager implements IOptimizationManager {

        private ArrayList<IDimension> dimensions = new ArrayList<IDimension>();

        @Override
        public List<IDimension> getDimensions() {
            dimensions.add(new ParameterDiscreteDimension(ACTIVEVARIABLE.getName(), 1, 1, 1 << indicators.length, ACTIVEVARIABLE));
            return dimensions;
        }
    }
}

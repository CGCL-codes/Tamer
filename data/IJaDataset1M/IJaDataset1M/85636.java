package com.pl.itsense.ftsm.model.continuous.indicatorbased.ordergenerator;

import java.util.ArrayList;
import java.util.List;
import com.pl.itsense.ftsm.common.IDimension;
import com.pl.itsense.ftsm.common.IMarketInfo;
import com.pl.itsense.ftsm.common.IOptimizable;
import com.pl.itsense.ftsm.common.IOptimizationManager;
import com.pl.itsense.ftsm.common.IOrder;
import com.pl.itsense.ftsm.common.IOuputProducer;
import com.pl.itsense.ftsm.common.IParameter;
import com.pl.itsense.ftsm.common.ISignalSelector.Symbol;
import com.pl.itsense.ftsm.common.ISymbolInfo;
import com.pl.itsense.ftsm.common.IOrder.ID;
import com.pl.itsense.ftsm.common.ISignalSelector.Attribute;
import com.pl.itsense.ftsm.common.impl.BaseModel;
import com.pl.itsense.ftsm.common.impl.BasicParameter;
import com.pl.itsense.ftsm.common.impl.Order;
import com.pl.itsense.ftsm.common.log.FTSMLogger;
import com.pl.itsense.ftsm.common.model.continuous.regression.RegressionVariable;
import com.pl.itsense.ftsm.common.utils.ParameterDiscreteDimension;
import com.pl.itsense.ftsm.model.continuous.indicatorbased.IDecisionMaker;
import com.pl.itsense.ftsm.model.continuous.indicatorbased.IndicatorBased;

public class OrderGenerator extends BaseModel {

    private static final FTSMLogger logger = FTSMLogger.getLogger(OrderGenerator.class);

    private double takeProfit;

    private double stopLost;

    private double lots;

    private IMarketInfo marketInfo;

    private IOrder output;

    private IDecisionMaker decisionMaker;

    public static enum Parameter {

        STOP_LOST, TAKE_PROFIT, LOTS, DECISION_MAKER
    }

    private IParameter STOP_LOST;

    private IParameter TAKE_PROFIT;

    private IParameter LOTS;

    private IParameter DECISION_MAKER;

    public OrderGenerator() {
        STOP_LOST = new BasicParameter(Parameter.STOP_LOST.name(), null, this);
        TAKE_PROFIT = new BasicParameter(Parameter.TAKE_PROFIT.name(), null, this);
        LOTS = new BasicParameter(Parameter.LOTS.name(), null, this);
        DECISION_MAKER = new BasicParameter(Parameter.DECISION_MAKER.name(), null, this);
        parameters.put(STOP_LOST.getName(), STOP_LOST);
        parameters.put(TAKE_PROFIT.getName(), TAKE_PROFIT);
        parameters.put(LOTS.getName(), LOTS);
        parameters.put(DECISION_MAKER.getName(), DECISION_MAKER);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOutput(Class<T> outputType) {
        if (outputType == IOrder.class) {
            return (T) output;
        }
        return null;
    }

    @Override
    public void valueChanged(IParameter parameter, Object oldValue, Object newValue) {
        isValid = false;
        if (STOP_LOST.equals(parameter)) {
            stopLost = (Double) newValue;
        }
        if (TAKE_PROFIT.equals(parameter)) {
            takeProfit = (Double) newValue;
        }
        if (LOTS.equals(parameter)) {
            lots = (Double) newValue;
        }
        if (DECISION_MAKER.equals(parameter)) {
            decisionMaker = (IDecisionMaker) newValue;
        }
    }

    @Override
    protected void processInput(IOuputProducer source) {
        logger.debug("processInput(): source = " + source);
        RegressionVariable[] decisionVar = source.getOutput(RegressionVariable[].class);
        if (decisionVar == null) {
            return;
        }
        ID id = decisionMaker.getDecision(decisionVar);
        Symbol symbol = decisionVar[0].getSignalSelector().getSymbol();
        output = null;
        if (ID.SELL.equals(id)) {
            final ISymbolInfo symbolInfo = marketInfo.getSymbolInfo(symbol);
            final Order order = new Order();
            order.setAsk(symbolInfo.get(Attribute.ASK));
            order.setBid(symbolInfo.get(Attribute.BID));
            order.setSymbol(symbol.name());
            order.setTimeStamp(symbolInfo.getTimeStamp());
            order.setLots(lots);
            order.setStopLost(order.getBid() + stopLost);
            order.setTakeProfit(order.getBid() - takeProfit);
            order.setId(id);
            output = order;
            sendToOutputListners();
        } else {
            if (ID.BUY.equals(id)) {
                final ISymbolInfo symbolInfo = marketInfo.getSymbolInfo(symbol);
                final Order order = new Order();
                order.setAsk(symbolInfo.get(Attribute.ASK));
                order.setBid(symbolInfo.get(Attribute.BID));
                order.setSymbol(symbol.name());
                order.setTimeStamp(symbolInfo.getTimeStamp());
                order.setLots(lots);
                order.setStopLost(order.getAsk() - stopLost);
                order.setTakeProfit(order.getAsk() + takeProfit);
                order.setId(id);
                output = order;
                sendToOutputListners();
            }
        }
    }

    @Override
    protected boolean updateStructure() {
        marketInfo = (IMarketInfo) getPlatform().getPlatformVariable(IMarketInfo.class.getName());
        return marketInfo == null || decisionMaker == null ? false : true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getExtension(Class<T> extensionClass) {
        if (extensionClass == IOptimizable.class) {
            return (T) new IOptimizable() {

                @Override
                public void register(List<IOptimizationManager> managers) {
                    managers.add(new OptimizationManager());
                }
            };
        }
        return null;
    }

    private class OptimizationManager implements IOptimizationManager {

        private ArrayList<IDimension> dimensions = new ArrayList<IDimension>();

        @Override
        public List<IDimension> getDimensions() {
            dimensions.add(new ParameterDiscreteDimension(STOP_LOST.getName(), 0.0005, 0.0002, 5, STOP_LOST));
            dimensions.add(new ParameterDiscreteDimension(TAKE_PROFIT.getName(), 0.0005, 0.0002, 5, TAKE_PROFIT));
            return dimensions;
        }
    }
}

package tcg.scada.da;

import java.util.ArrayList;
import tcg.common.LoggerManager;
import tcg.common.util.ExpressionParser;
import tcg.scada.cos.CosDpValueTypeEnum;

/**
 * Implementation of output datapoint.
 * 
 * <p>
 * Output datapoint is a dummy datapoint structure. No operation is supported
 * other than set and get.
 * </p>
 * 
 * <p>
 * Expected behaviour for output datapoint (behave like dummy datapoint):
 * <ul>
 * <li>Source value: not applicable. always default value</li>
 * <li>Source quality: not applicable. (default = GoodQuality)</li>
 * <li>Source timestamp: not applicable. (default = 0)</li>
 * <li>Output value: not applicable.</li>
 * <li>Output quality: not applicable.</li>
 * <li>Output timestamp: not applicable.</li>
 * </ul>
 * </p>
 * 
 * @author Yoga
 * 
 */
public class OutputDataPoint extends DataPoint {

    protected ExpressionParser lccParser = null;

    protected ArrayList<IDataPoint> lccParams = null;

    protected ExpressionParser rccParser = null;

    protected ArrayList<IDataPoint> rccParams = null;

    /**
	 * Default ctor.
	 * 
	 * @param inDataType
	 *            - the internal data type
	 */
    public OutputDataPoint(CosDpValueTypeEnum inDataType) {
        if (inDataType.value() != CosDpValueTypeEnum._TypeNumber && inDataType.value() != CosDpValueTypeEnum._TypeBoolean) {
            inDataType = CosDpValueTypeEnum.TypeNumber;
        }
        super._setDataType(inDataType);
        logger = LoggerManager.getLogger(this.getClass().getName());
    }

    @Override
    public EDataPointType getType() {
        return EDataPointType.TYPE_OUTPUT;
    }

    @Override
    public synchronized boolean setReturnCondition(String inExpression, DataPointList inDpList) {
        if (inExpression == null || inExpression.isEmpty()) {
            return false;
        }
        Class<?> type = boolean.class;
        ArrayList<IDataPoint> params = new ArrayList<IDataPoint>();
        ArrayList<String> varnames = new ArrayList<String>();
        ArrayList<Class<?>> vartypes = new ArrayList<Class<?>>();
        ArrayList<Object> varvalues = new ArrayList<Object>();
        String parsedExpression = _parseExpression(inDpList, inExpression, params, varnames, vartypes, varvalues);
        rccParser = new ExpressionParser(parsedExpression, type);
        for (int i = 0; i < varnames.size(); i++) {
            rccParser.createVariable(varnames.get(i), vartypes.get(i), varvalues.get(i));
        }
        rccParams = params;
        try {
            Object result = rccParser.evaluate();
            if (result == null) {
                return false;
            }
        } catch (Exception ex) {
            logger.error("Can not set RCC expression: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public synchronized boolean checkReturnCondition() {
        if (rccParser == null) {
            return false;
        }
        if (!rccParser.isValid()) {
            logger.error("Invalid RCC expression. " + "Check the first time this happens for error.");
            return false;
        }
        IDataPoint datapoint = null;
        Object value = null;
        for (int i = 0; i < rccParams.size(); i++) {
            datapoint = rccParams.get(i);
            if (datapoint == null) {
                logger.trace("RCC parameter : UNKNOWN");
                value = Integer.valueOf(0);
            } else {
                logger.trace("RCC parameter : " + datapoint.getName());
                value = convCosDpValueUnion2Object(datapoint.getValue());
            }
            rccParser.set(i, value);
            logger.trace("RCC parameter value : " + value.toString());
        }
        Boolean status = null;
        try {
            status = (Boolean) rccParser.evaluate();
        } catch (Exception ex) {
            logger.warn("Can not evaluate RCC expression. Exception: " + ex.toString());
        }
        if (status == null) {
            return false;
        } else {
            return status.booleanValue();
        }
    }

    @Override
    public synchronized boolean setLaunchCondition(String inExpression, DataPointList inDpList) {
        if (inExpression == null || inExpression.isEmpty()) {
            return false;
        }
        Class<?> type = boolean.class;
        ArrayList<IDataPoint> params = new ArrayList<IDataPoint>();
        ArrayList<String> varnames = new ArrayList<String>();
        ArrayList<Class<?>> vartypes = new ArrayList<Class<?>>();
        ArrayList<Object> varvalues = new ArrayList<Object>();
        String parsedExpression = _parseExpression(inDpList, inExpression, params, varnames, vartypes, varvalues);
        lccParser = new ExpressionParser(parsedExpression, type);
        for (int i = 0; i < varnames.size(); i++) {
            lccParser.createVariable(varnames.get(i), vartypes.get(i), varvalues.get(i));
        }
        lccParams = params;
        try {
            Object result = lccParser.evaluate();
            if (result == null) {
                return false;
            }
        } catch (Exception ex) {
            logger.error("Can not set LCC expression: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public synchronized boolean checkLaunchCondition() {
        if (lccParser == null) {
            return false;
        }
        if (!lccParser.isValid()) {
            logger.error("Invalid LCC expression. " + "Check the first time this happens for error.");
            return false;
        }
        IDataPoint datapoint = null;
        Object value = null;
        for (int i = 0; i < lccParams.size(); i++) {
            datapoint = lccParams.get(i);
            if (datapoint == null) {
                logger.trace("LCC parameter : UNKNOWN");
                value = Integer.valueOf(0);
            } else {
                logger.trace("LCC parameter : " + datapoint.getName());
                value = convCosDpValueUnion2Object(datapoint.getValue());
            }
            lccParser.set(i, value);
            logger.trace("LCC parameter value : " + value.toString());
        }
        Boolean status = null;
        try {
            status = (Boolean) lccParser.evaluate();
        } catch (Exception ex) {
            logger.warn("Can not evaluate LCC expression. Exception: " + ex.toString());
        }
        if (status == null) {
            return false;
        } else {
            return status.booleanValue();
        }
    }
}

package ca.nanometrics.gflot.client;

import ca.nanometrics.gflot.client.util.JSONObjectWrapper;
import com.google.gwt.json.client.JSONObject;

/**
 * Model of a plot axis.
 *
 * @author David Easton
 */
public class Axis extends JSONObjectWrapper {

    private static final String MIN = "min";

    private static final String MAX = "max";

    private static final String TICK_DECIMALS = "tickDecimals";

    private static final String TICK_SIZE = "tickSize";

    protected Axis() {
        super();
    }

    protected Axis(JSONObject object) {
        super(object);
    }

    public Double getMinimumValue() {
        return super.getDouble(MIN);
    }

    public Double getMaximumValue() {
        return super.getDouble(MAX);
    }

    public Integer getTickDecimals() {
        return super.getInteger(TICK_DECIMALS);
    }

    public Integer getTickSize() {
        return super.getInteger(TICK_SIZE);
    }
}

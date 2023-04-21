package org.sensorweb.core.scs.parser;

import java.util.Collection;
import org.sensorweb.GlobalConstant;
import org.sensorweb.model.gml.basic.*;
import org.sensorweb.model.gml.vo.*;
import org.sensorweb.util.SensorTypeHandler;
import net.opengis.om.x10.ObservationCollectionDocument;

/**
 * @author Xingchen Chu
 * @version 0.1
 *
 * <code> DefaultObservationCollectionFormatter </code>
 */
public final class DefaultObservationCollectionFormatter extends AbstractObservationCollectionFormatter {

    protected String provideId() {
        return "default-observation-id";
    }

    protected ValueObject parseValue(Object key, Object value) {
        ValueObject result = new ValueObject();
        if (value == null || "".equals(value.toString())) {
            result.setNull(new NullType(NullEnumeration.MISSING));
        } else if (value instanceof Integer) {
            result.setValue(new Count((Integer) value));
        } else if (value instanceof String) {
            result.setValue(new Category(value.toString()));
        } else if (value instanceof Double) {
            String unit = (String) SensorTypeHandler.getSensorType().getUnits().get(GlobalConstant.SensorTypeConstants.DEGREE);
            result.setValue(new Quantity(((Double) value).doubleValue(), unit));
        } else {
            result.setNull(new NullType(NullEnumeration.UNKNOWN));
        }
        return result;
    }

    protected Collection provideRelatedFeature() {
        return null;
    }

    public ObservationCollectionDocument composeOM(Collection dataset) {
        return null;
    }

    @Override
    protected ObservationCollectionDocument provideObservationMeasurement() {
        return null;
    }
}

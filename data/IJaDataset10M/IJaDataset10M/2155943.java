package net.sf.doolin.sdo;

import net.sf.doolin.sdo.constraint.support.CustomConstraint;
import net.sf.doolin.sdo.factory.json.JSONUtils;
import org.codehaus.jackson.JsonNode;

public class KeyCustomConstraint implements CustomConstraint {

    private long min;

    @Override
    public boolean apply(Object input) {
        Long value = (Long) input;
        return (value == null || value >= this.min);
    }

    @Override
    public void setDefinition(JsonNode jConstraint) {
        this.min = JSONUtils.getLong(jConstraint, "min", true, 0);
    }
}

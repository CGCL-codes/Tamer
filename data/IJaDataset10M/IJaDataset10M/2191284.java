package mou.core.civilization;

import java.util.HashMap;
import java.util.Map;
import mou.Main;
import mou.core.MapWrapper;

/**
 * Klasse dient der Speicherung verschiedenen Einfl�ssfaktoren mit begrenztem Zeitdauer
 * 
 * @author pb
 */
public class InfluenceFactor extends MapWrapper {

    private static final String ATTR_DESCRIPTION = "ATTR_DESCRIPTION";

    private static final String ATTR_VALUE = "ATTR_VALUE";

    private static final String ATTR_DURATION = "ATTR_DURATION";

    public InfluenceFactor(Map data) {
        super(data);
    }

    public InfluenceFactor(long duration, Number value, String description) {
        this(new HashMap<String, Object>());
        setDescription(description);
        setDuration(duration);
        setValue(value);
    }

    public String getDescription() {
        return (String) getAttribute(ATTR_DESCRIPTION, "");
    }

    public void setDescription(String val) {
        setAttribute(ATTR_DESCRIPTION, val);
    }

    public Number getValue() {
        return (Number) getAttribute(ATTR_VALUE, Main.ZERO_NUMBER);
    }

    public void setValue(Number val) {
        setAttribute(ATTR_VALUE, val);
    }

    public Long getDuration() {
        return (Long) getAttribute(ATTR_DURATION, Main.ZERO_NUMBER);
    }

    public void setDuration(long val) {
        setAttribute(ATTR_DURATION, val);
    }
}

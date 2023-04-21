package ca.ucalgary.cpsc.ebe.fitClipse.core.data.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import ca.ucalgary.cpsc.ebe.fitClipse.core.data.ITestCell;

public class TestCell implements ITestCell {

    private final Map<String, String> attribute = new HashMap<String, String>();

    private String value;

    public TestCell(final String value) {
        setValue(value);
    }

    public Object clone() {
        final TestCell clone = new TestCell(value);
        final Set<String> keys = attribute.keySet();
        for (final String key : keys) {
            clone.setAttribute(key, attribute.get(key));
        }
        return clone;
    }

    public String getAttribute(final String key) {
        return attribute.get(key);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attribute);
    }

    public String getValue() {
        return value;
    }

    public void setAttribute(final String key, final String value) {
        attribute.put(key, value);
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String toString() {
        return getValue();
    }
}

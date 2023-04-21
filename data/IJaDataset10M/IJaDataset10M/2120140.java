package com.newisys.joveutils.properties;

import java.util.LinkedList;
import java.util.List;

/**
 * A container class for Integer overrides
 * @author scott.diesing
 *
 */
public class MetaPropertyInt {

    private final List<PropertyOverride<Integer>> overrides = new LinkedList<PropertyOverride<Integer>>();

    public void addOverride(PropertyOverride<Integer> override) {
        overrides.add(override);
    }

    public Integer getDefault(Integer def) {
        Integer rval = def;
        for (PropertyOverride over : overrides) {
            final String metaProperty = PropertyGlobals.getPropStr(over.name + "=", null);
            if (metaProperty != null) {
                if (over.isPresent(metaProperty)) {
                    rval = (Integer) over.get(metaProperty);
                }
            }
        }
        return rval;
    }
}

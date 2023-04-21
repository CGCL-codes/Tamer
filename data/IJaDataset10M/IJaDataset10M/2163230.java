package com.googlecode.gchartjava.parameters;

import java.util.List;
import com.googlecode.gchartjava.collect.Lists;

/**
 * Class for building axis types parameter string for the Google Chart API.
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
final class AxisTypesParameter implements Parameter {

    /** The Google Chart API axis types parameter. */
    private static final String URL_PARAMETER_KEY = "chxt";

    /** The axis types list. */
    private final List<AxisTypes> axisTypesList = Lists.newLinkedList();

    /**
     * Add an axis type.
     *
     * @param axisTypes
     *            the axis types
     */
    void addAxisTypes(final AxisTypes axisTypes) {
        axisTypesList.add(axisTypes);
    }

    /**
     * {@inheritDoc}
     */
    public String toURLParameterString() {
        final StringBuilder sb = new StringBuilder(URL_PARAMETER_KEY + "=");
        int cnt = 0;
        for (AxisTypes axisType : axisTypesList) {
            sb.append(cnt++ > 0 ? "," + axisType : axisType);
        }
        return !axisTypesList.isEmpty() ? sb.toString() : "";
    }
}

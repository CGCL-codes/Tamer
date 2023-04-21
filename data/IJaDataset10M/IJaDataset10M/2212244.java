package org.dozer.classmap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal class used to hold any "allowed exceptions" that may have been specified in the mapping xml. Only intended
 * for internal use.
 * 
 * @author garsombke.franz
 */
public class AllowedExceptionContainer {

    private final List<Class<RuntimeException>> exceptions = new ArrayList<Class<RuntimeException>>();

    public List<Class<RuntimeException>> getExceptions() {
        return exceptions;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

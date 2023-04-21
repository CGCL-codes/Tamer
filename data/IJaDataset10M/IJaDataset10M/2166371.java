package com.volantis.mcs.xml.schema.compiler;

import com.volantis.mcs.xml.schema.model.ElementType;

/**
 */
public interface CompiledSchema {

    boolean containsElementType(ElementType type);
}

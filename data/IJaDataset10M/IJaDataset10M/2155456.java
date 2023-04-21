package com.newisys.dv.ifgen.schema;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.newisys.langschema.Annotation;
import com.newisys.langschema.Schema;
import com.newisys.langschema.Type;
import com.newisys.langschema.TypeModifier;

/**
 * Base class for all types in an ifgen schema.
 * 
 * @author Jon Nall
 */
public abstract class IfgenType implements Type {

    private final IfgenSchema schema;

    protected IfgenType(IfgenSchema schema) {
        this.schema = schema;
    }

    public Set<? extends TypeModifier> getModifiers() {
        return Collections.emptySet();
    }

    public boolean isAssignableFrom(Type other) {
        return other.getClass() == this.getClass();
    }

    public boolean isStrictIntegral() {
        return false;
    }

    public boolean isIntegralConvertible() {
        return isStrictIntegral();
    }

    public Schema getSchema() {
        return schema;
    }

    public List<? extends Annotation> getAnnotations() {
        return Collections.emptyList();
    }

    @Override
    public final String toString() {
        return schema.isUseSourceString() ? toSourceString() : toDebugString();
    }
}

package org.apache.openjpa.sdo.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Lob;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.sdo.ImplHelper;
import org.apache.openjpa.sdo.SDOInstanceMapping;
import org.apache.openjpa.sdo.SDOMappingRepository;
import serp.bytecode.Annotation;
import serp.bytecode.Annotations;
import serp.bytecode.BCMember;
import commonj.sdo.ChangeSummary;
import commonj.sdo.DataObject;
import commonj.sdo.Property;

/**
 * Maps Primitive, single-valued Property.
 * 
 * @author Pinaki Poddar
 * @since 0.2.0
 */
public class PrimitiveMapping extends AbstractPropertyMapping {

    PrimitiveMapping(TypeMapping tm, Property p, FieldMetaData fm) {
        super(tm, p, fm);
    }

    public String getName() {
        return "PrimitiveMapping";
    }

    public String getFieldTypeName() {
        return property.getType().getInstanceClass().getName();
    }

    public Class getFieldType(SDOMappingRepository repos) {
        return property.getType().getInstanceClass();
    }

    public String[] getAnnotationStrings() {
        if (ImplHelper.isLOB(property)) return new String[] { "@Lob", "@Basic" };
        return new String[] {};
    }

    public void annotate(BCMember field) {
        Annotations annos = field.getDeclaredRuntimeAnnotations(true);
        annos.addAnnotation(Basic.class);
        if (ImplHelper.isLOB(property)) annos.addAnnotation(Lob.class);
    }

    public DataObject populateDataObject(DataObject target, Object source, SDOInstanceMapping ctx) throws Exception {
        Object value = getter.invoke(source, NULL_ARGS);
        Object oldvalue = null;
        if (target.isSet(property)) oldvalue = target.get(property);
        if (oldvalue == null && value == null) return target;
        if (oldvalue == null) {
            target.set(property, value);
            return target;
        }
        if (!oldvalue.equals(value)) target.set(property, value);
        return target;
    }

    public Object populatePC(Object pc, DataObject data, SDOInstanceMapping ctx) throws Exception {
        Object oldvalue = getter.invoke(pc, NULL_ARGS);
        Object value = null;
        if (data.isSet(property) || property.getType().getInstanceClass().isPrimitive()) value = data.get(property);
        if (oldvalue == null && value == null) return pc;
        if (oldvalue == null) {
            setter.invoke(pc, value);
            return pc;
        }
        if (!oldvalue.equals(value)) setter.invoke(pc, value);
        return pc;
    }
}

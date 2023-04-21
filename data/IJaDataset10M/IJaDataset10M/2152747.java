package com.abra.j2xb.beans.propertyEditors;

import com.abra.j2xb.beans.model.MOValueValidationResult;
import com.abra.j2xb.beans.model.MOPropertySimpleTypeDescriptor;
import com.abra.j2xb.beans.exceptions.MOBeansException;
import com.abra.j2xb.beans.xmlBinding.XmlValue;
import com.abra.j2xb.annotations.xmlAnnotations.MoXmlBaseSimpleType;
import java.util.EnumSet;

/**
 * @author Yoav Abrahami
 * @version 1.0, May 1, 2008
 * @since   JDK1.5
 */
public class MOBytesPropertyEditor implements MOPropertyEditor {

    private static EnumSet<MoXmlBaseSimpleType> compatibleTypes = EnumSet.of(MoXmlBaseSimpleType.xmlHexBinary, MoXmlBaseSimpleType.xmlBase64binary);

    public Object fromString(Class<?> propertyType, String value) {
        return null;
    }

    public String toString(Class<?> propertyType, Object value) {
        return null;
    }

    public Object fromXmlValue(MOPropertySimpleTypeDescriptor simpleTypeDescriptor, XmlValue value) throws MOBeansException {
        return value.getValue();
    }

    public XmlValue toXmlValue(MOPropertySimpleTypeDescriptor simpleTypeDescriptor, Object value) {
        return new XmlValue(value, getXmlDefaultSimpleType(simpleTypeDescriptor.getDerivedTypeDefinition()));
    }

    public MOValueValidationResult validateStringEncoding(Class propertyValueType, String value) {
        return new MOValueValidationResult();
    }

    public boolean isCompatibleSimpleType(MoXmlBaseSimpleType xmlSimpleType) {
        return compatibleTypes.contains(xmlSimpleType);
    }

    public MoXmlBaseSimpleType getXmlDefaultSimpleType(MoXmlBaseSimpleType xmlSimpleType) {
        if (xmlSimpleType != MoXmlBaseSimpleType.xmlNotSpecified) return xmlSimpleType; else return MoXmlBaseSimpleType.xmlHexBinary;
    }

    public String[] getEnumerations(Class<?> propertyValueType) {
        return null;
    }

    public boolean supportsStringValidations() {
        return false;
    }

    public boolean supportsNumberValidations() {
        return false;
    }

    public int compareValues(Object obj_a, Object obj_b) {
        return 0;
    }
}

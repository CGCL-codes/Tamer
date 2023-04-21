package com.jemframework.domain.identifier;

import java.util.ArrayList;
import java.util.List;
import com.jemframework.Context;
import com.jemframework.domain.EntityTypeNameMapping;
import com.jemframework.domain.exception.PropertyConverterException;
import com.jemframework.domain.propertyconverter.PropertyConverter;

public abstract class AbstractIdentifierConverter implements IdentifierConverter {

    public abstract int getEntityType();

    private PropertyConverter[] propertyConverters;

    public AbstractIdentifierConverter(PropertyConverter[] propertyConverters) {
        this.propertyConverters = propertyConverters;
    }

    public static int getEntityTypeFromString(String identifierAsString) {
        int index = identifierAsString.indexOf(IdentifierConverter.ENTITY_TYPE_DELIMITER);
        String theEntityTypeName = identifierAsString.substring(0, index);
        EntityTypeNameMapping theMapping = Context.getInstance().getEntityTypeNameMapping();
        int theEntityType = theMapping.getEntityType(theEntityTypeName);
        return theEntityType;
    }

    public PropertyConverter getPropertyConverter(int i) {
        return propertyConverters[i];
    }

    protected String[] getParametersFromString(String string) {
        List<String> parameters = new ArrayList<String>();
        Integer[] positions = getParameterStartPositions(string);
        for (int i = 0; i < positions.length; i++) {
            int start = positions[i];
            int end = (i < positions.length - 1) ? positions[i + 1] : string.length();
            String parameter = string.substring(start, end);
            if (parameter.endsWith(IdentifierConverter.PARAMETER_DELIMITER)) {
                parameter = parameter.substring(0, parameter.length() - IdentifierConverter.PARAMETER_DELIMITER.length());
            }
            parameters.add(parameter);
        }
        return parameters.toArray(new String[parameters.size()]);
    }

    protected Integer[] getParameterStartPositions(String string) {
        List<Integer> positions = new ArrayList<Integer>();
        int position = string.indexOf(IdentifierConverter.ENTITY_TYPE_DELIMITER);
        position += IdentifierConverter.ENTITY_TYPE_DELIMITER.length();
        while (position != -1) {
            positions.add(position);
            position = string.indexOf(IdentifierConverter.PARAMETER_DELIMITER, position + 1);
            if (position != -1) {
                position += IdentifierConverter.PARAMETER_DELIMITER.length();
            }
        }
        return positions.toArray(new Integer[positions.size()]);
    }

    public Identifier getAsObject(String string) {
        try {
            if (string == null || string.trim().length() == 0) {
                return null;
            }
            String[] parameters = getParametersFromString(string);
            if (parameters.length != propertyConverters.length) {
                throw new IllegalArgumentException("Number of parameters doesn't not match: " + string);
            }
            Object[] properties = new Object[parameters.length];
            for (int i = 0; i < properties.length; i++) {
                properties[i] = getPropertyConverter(i).getAsObject(parameters[i]);
            }
            return createIdentifier(properties);
        } catch (PropertyConverterException e) {
            throw new RuntimeException();
        }
    }

    protected abstract Identifier createIdentifier(Object[] properties);

    public String getAsString(Identifier entityIdentifier) {
        if (entityIdentifier == null) {
            return "";
        }
        return computeString(entityIdentifier);
    }

    protected String convertAndEscape(Object aString, int aPropertyId) {
        PropertyConverter thePropertyConverter = getPropertyConverter(aPropertyId);
        thePropertyConverter.getAsString(aString);
        return aString.toString();
    }

    protected String getEntityTypeName() {
        return Context.getInstance().getEntityTypeNameMapping().getEntityTypeName(getEntityType());
    }

    protected abstract String computeString(Identifier entityId);
}

package com.jemframework.domain.identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import com.jemframework.Initializable;
import com.jemframework.domain.exception.IdentifierConverterException;

public class DefaultIdentifierFactory implements IdentifierFactory, Initializable {

    private Map<Integer, IdentifierConverter> mapping = new HashMap<Integer, IdentifierConverter>();

    public DefaultIdentifierFactory() {
        initialize();
    }

    public void initialize() {
        try {
            Properties properties = new Properties();
            InputStream in = getClass().getClassLoader().getResourceAsStream("identifier-factory.properties");
            properties.load(in);
            for (Entry<Object, Object> eachEntry : properties.entrySet()) {
                String theClassName = (String) eachEntry.getValue();
                registerIdentifierConverter(theClassName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void registerIdentifierConverter(String className) {
        try {
            IdentifierConverter entityIdConverter = (IdentifierConverter) Class.forName(className).newInstance();
            mapping.put(entityIdConverter.getEntityType(), entityIdConverter);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Identifier createIdentifier(String identifierAsString) throws IdentifierConverterException {
        int entityType = AbstractIdentifierConverter.getEntityTypeFromString(identifierAsString);
        return createIdentifier(entityType, identifierAsString);
    }

    protected Identifier createIdentifier(int entityType, String identifierAsString) throws IdentifierConverterException {
        IdentifierConverter converter = mapping.get(entityType);
        Identifier identifier = converter.getAsObject(identifierAsString);
        return identifier;
    }

    public String getIdentifierAsString(Identifier entityId) {
        IdentifierConverter converter = mapping.get(entityId.getEntityType());
        String string = converter.getAsString(entityId);
        return string;
    }
}

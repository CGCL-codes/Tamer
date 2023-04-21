package net.sourceforge.comeback.prolog.abstractParser.ast.impl;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.comeback.prolog.abstractParser.ast.ASTFactory;
import net.sourceforge.comeback.prolog.abstractParser.ast.ASTNode;

public class ASTGenericFactoryImpl implements ASTFactory {

    private Map<Class<? extends ASTNode>, Class<? extends ASTNode>> registeredTypes;

    public ASTGenericFactoryImpl() {
        registeredTypes = new HashMap<Class<? extends ASTNode>, Class<? extends ASTNode>>();
    }

    @SuppressWarnings("unchecked")
    public <T extends ASTNode> T create(Class<T> type) throws InstantiationException, IllegalAccessException {
        Class<T> newType = (Class<T>) registeredTypes.get(type);
        if (newType != null) {
            return newType.newInstance();
        }
        return null;
    }

    public <T extends ASTNode> void registerType(Class<T> interfaceType, Class<? extends T> implementingClassType) {
        if (interfaceType == null) {
            throw new IllegalArgumentException("Parameter interfaceType is null.");
        } else if (implementingClassType == null) {
            throw new IllegalArgumentException("Parameter implementingClassType is null.");
        } else if (implementingClassType.isInterface() || implementingClassType.isPrimitive() || implementingClassType.isArray() || Modifier.isAbstract(implementingClassType.getModifiers())) {
            throw new IllegalArgumentException("Invalid implementing class.");
        } else if (!interfaceType.isAssignableFrom(implementingClassType)) {
            throw new IllegalArgumentException("Given class does not implement interface.");
        }
        try {
            implementingClassType.getConstructor();
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("Implementing class does not have public no-argument constructor.");
        }
        this.registeredTypes.put(interfaceType, implementingClassType);
    }

    public Map<Class<? extends ASTNode>, Class<? extends ASTNode>> getRegisteredTypes() {
        return registeredTypes;
    }
}

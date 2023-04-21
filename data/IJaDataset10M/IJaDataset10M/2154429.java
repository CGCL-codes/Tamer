package org.databene.model.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps abstract types to concrete types and vice versa.
 * @author Volker Bergmann
 * @since 0.3.04
 */
public class TypeMapper {

    private Map<String, Class<?>> abstractToConcrete;

    private Map<Class<?>, String> concreteToAbstract;

    /**
     * @param typeMappings name-class pairs that list the mappings to be defined
     */
    public TypeMapper(Object... typeMappings) {
        this.abstractToConcrete = new HashMap<String, Class<?>>();
        this.concreteToAbstract = new HashMap<Class<?>, String>();
        for (int i = 0; i < typeMappings.length; i += 2) {
            String abstractType = (String) typeMappings[i];
            Object concreteType = typeMappings[i + 1];
            map(abstractType, (Class<?>) concreteType);
        }
    }

    public void map(String abstractType, Class<?> concreteType) {
        abstractToConcrete.put(abstractType, concreteType);
        concreteToAbstract.put(concreteType, abstractType);
    }

    public Class<?> concreteType(String abstractType) {
        return abstractToConcrete.get(abstractType);
    }

    public String abstractType(Class<?> concreteType) {
        return concreteToAbstract.get(concreteType);
    }
}

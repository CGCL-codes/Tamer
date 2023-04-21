package com.prefabware.jmodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * a type with actual type paramaters
 * used to combine the raw type and the type parameters for
 * a JMember
 * @see java.lang.reflect.ParameterizedType
 * @author sisele_job
 *
 */
public class JParameterizedType {

    private JType type;

    private List<JType> typeArguments;

    public JParameterizedType(JType type, JType... types) {
        super();
        this.type = type;
        if (types == null || types.length == 0) {
            this.typeArguments = Collections.emptyList();
        } else {
            this.typeArguments = Arrays.asList(types);
        }
    }

    /**
	 * the generic type e.g. java.util.List
	 * @return
	 */
    public JType getRawType() {
        return type;
    }

    /**
	 * @return the type parameters e.g. String for List<String>
	 */
    public List<JType> getTypeArguments() {
        return typeArguments;
    }
}

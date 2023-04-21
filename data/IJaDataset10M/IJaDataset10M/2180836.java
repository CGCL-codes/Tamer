package com.exjali.namedparameters;

/**
 * <p>Allow usage of some type of named parameters</p>
 * 
 * 
 * @author Raphael Lemaire
 *
 */
public final class NamedParametersFactory {

    private NamedParametersFactory() {
    }

    public static NamedParameter param(String name, Object actualParameter) {
        return new NamedParameter(name, actualParameter);
    }

    public static NamedParameters params(NamedParameter... params) {
        return new NamedParameters(params);
    }

    public static <T> ParameterVerification paramVerify(String name, Class<T> clazz) {
        return new ParameterVerification(name, clazz);
    }
}

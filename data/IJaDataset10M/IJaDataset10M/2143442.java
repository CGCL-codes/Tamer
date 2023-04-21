package org.brandao.brutos;

import java.util.HashMap;
import java.util.Map;

/**
 * Descreve os tipos de mapeamento para enum.
 *
 * @author Afonso Brandao
 */
public class EnumerationType {

    /**
     * O enum � mapeado como um inteiro.
     */
    public static final EnumerationType ORDINAL = new EnumerationType("ordinal");

    /**
     * O enum � mapeado como uma string.
     */
    public static final EnumerationType STRING = new EnumerationType("string");

    private static final Map defaultTypes = new HashMap();

    static {
        defaultTypes.put(ORDINAL.toString(), ORDINAL);
        defaultTypes.put(STRING.toString(), STRING);
    }

    private String name;

    public EnumerationType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static EnumerationType valueOf(String value) {
        return (EnumerationType) defaultTypes.get(value);
    }
}

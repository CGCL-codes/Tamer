package org.opennms.mavenize;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesUtils {

    public static interface SymbolTable {

        public String getSymbolValue(String symbol);
    }

    private static class PropertyBasedSymbolTable implements SymbolTable {

        Properties m_properties;

        PropertyBasedSymbolTable(Properties properties) {
            m_properties = properties;
        }

        public String getSymbolValue(String symbol) {
            return m_properties.getProperty(symbol);
        }
    }

    /**
     * This recursively substitutes occurrences ${property.name} in initialString with the value of 
     * the property property.name taken from the supplied properties object. If 
     * property.name is not defined in properties that the substitution is not done. 
     * @param initialString the string to preform the substitions in
     * @param properties the properties to take the values from
     * @return The string with appropriate substitutions made.
     */
    public static String substitute(String initialString, Properties properties) {
        return substitute(initialString, new PropertyBasedSymbolTable(properties));
    }

    public static String substitute(String initialString, SymbolTable symbols) {
        return substitute(initialString, symbols, new ArrayList());
    }

    private static String substitute(String initialString, SymbolTable symTable, List list) {
        if (initialString == null) return null;
        StringBuffer result = new StringBuffer(initialString);
        int startIndex = 0;
        while (startIndex >= 0) {
            int beginIndex = result.indexOf("${", startIndex);
            int endIndex = (beginIndex < 0 ? -1 : result.indexOf("}", beginIndex + "${".length()));
            if (endIndex >= 0) {
                String symbol = result.substring(beginIndex + "${".length(), endIndex);
                if (list.contains(symbol)) throw new IllegalStateException("recursive loop involving symbol ${" + symbol + "}");
                String symbolVal = symTable.getSymbolValue(symbol);
                if (symbolVal != null) {
                    list.add(symbol);
                    String substVal = substitute(symbolVal, symTable, list);
                    list.remove(list.size() - 1);
                    result.replace(beginIndex, endIndex + 1, substVal);
                    startIndex = beginIndex + substVal.length();
                } else {
                    startIndex = endIndex + 1;
                }
            } else {
                startIndex = -1;
            }
        }
        return result.toString();
    }
}

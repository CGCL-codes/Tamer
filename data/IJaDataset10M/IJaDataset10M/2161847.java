package com.v1r3n.bol.codegen;

import java.util.Hashtable;

/**
 * @author Viren
 *
 */
public abstract class CompilerContext {

    private Hashtable<String, String[]> fnNameCache;

    private Hashtable<String, String[]> typeNameCache;

    protected CompilerContext() {
        this.fnNameCache = new Hashtable<String, String[]>();
        this.typeNameCache = new Hashtable<String, String[]>();
    }

    /**
	 * @param statement
	 * @return Returns the function name(s) for the given statement
	 * note that a given statement can result into multiple functions belonging to different packages.
	 */
    public String[] getFunctionName(String statement) {
        String[] name = fnNameCache.get(statement);
        if (name == null) {
            name = resolveFunctionName(statement);
            fnNameCache.put(statement, name);
        }
        return name;
    }

    /**
	 * @param shortName
	 * @param statement
	 */
    public String defineFunction(String shortName, String statement) {
        if (shortName == null || "".equals(shortName.trim())) {
            shortName = generateFnName(statement);
        }
        fnNameCache.put(statement, new String[] { shortName });
        fnNameCache.put(shortName, new String[] { shortName });
        return shortName;
    }

    /**
	 * @param typename
	 * @return
	 */
    public String[] getTypeName(String typename) {
        String[] name = typeNameCache.get(typename);
        if (name == null) {
            name = resolveTypeName(typename);
            fnNameCache.put(typename, name);
        }
        return name;
    }

    /**
	 * @param type
	 * @return
	 */
    public String defineType(String type) {
        String typeName = generateTypeName(type);
        typeNameCache.put(type, new String[] { typeName });
        return typeName;
    }

    protected abstract String[] resolveFunctionName(String statement);

    protected abstract String[] resolveTypeName(String type);

    protected abstract String generateFnName(String typename);

    protected abstract String generateTypeName(String typename);
}

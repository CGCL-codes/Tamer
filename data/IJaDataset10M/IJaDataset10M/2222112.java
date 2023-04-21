package com.monad.homerun.filter;

import com.monad.homerun.object.Instance;
import com.monad.homerun.object.Type;

/**
 * TypeScreen extends {@link com.monad.homerun.filter.Screen} to describe
 * inheritance of instance type. That is, the screen
 * passes the object if it's type is or is not the named type.
 */
public class TypeScreen extends Screen {

    private static final long serialVersionUID = -2863813937929882184L;

    public static final int OP_IS_A = 0;

    public static final int OP_IS_NOT_A = 1;

    private String domainName = null;

    private String typeName = null;

    private int operator = -1;

    public TypeScreen() {
    }

    public TypeScreen(boolean alternate, String domainName, String typeName, int operator) {
        super(alternate, Screen.TYPE);
        this.domainName = domainName;
        this.typeName = typeName;
        this.operator = operator;
    }

    public TypeScreen(TypeScreen screen) {
        super(screen);
        this.domainName = screen.domainName;
        this.typeName = screen.typeName;
        this.operator = screen.operator;
    }

    /**
     * Returns the domain name
     * 
     * @return domain
     *         the domain name
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Returns the type name
     * 
     * @return type
     *         the type name
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Returns the screen operator
     * 
     * @return oper
     *         the screen operator
     */
    public int getOperator() {
        return operator;
    }

    public String toString() {
        return super.toString() + domainName + ":" + typeName + ":" + operator;
    }

    public boolean isRuntime(Screener screener) {
        return false;
    }

    /**
     * Validates the terms of the screen with optional tracing
     * 
     * @param screener the Screening agent
     * @param domain the domain of the object being screened
     * @param trace the optional trace object
     * @return true if screen valid
     */
    public boolean validate(Screener screener, String domain, FilterTrace trace) {
        if (!validateBase(screener, domain, trace)) {
            return false;
        }
        if (!(operator == OP_IS_A || operator == OP_IS_NOT_A)) {
            if (trace != null) {
                trace.setDesc("invalid operator: '" + operator + "'");
            }
            return false;
        }
        boolean match = false;
        for (Type type : screener.getTypes(domain)) {
            if (type.getName().equals(typeName)) {
                match = true;
                break;
            }
        }
        if (trace != null) {
            if (match) {
                trace.setDesc("ok");
            } else {
                trace.setDesc("no type matching name: '" + typeName + "'");
            }
        }
        return match;
    }

    /**
     * Tests the object against the screen (with optional tracing)
     * 
     * @param screener the Screening agent
     * @param domain the domain of the object being screened
     * @param objectName the object being screened
     * @param trace the optional trace object
     * @return true if object passes through screen
     */
    public boolean pass(Screener screener, String domain, String objectName, FilterTrace trace) {
        boolean ret = false;
        if (screener != null) {
            Instance instance = screener.getObject(domain, objectName);
            if (instance != null) {
                ret = compare(domain, instance.getTypeName());
            }
            if (trace != null) {
                String verb = ret ? "is a" : "is not a";
                trace.setDesc(ret + ": " + objectName + " " + verb + " " + domain + " '" + typeName + "'");
            }
        }
        return ret;
    }

    protected boolean compare(String domain, String typeName) {
        boolean matched = domain.equals(domainName) && typeName.equals(this.typeName);
        return (operator == OP_IS_A) ? matched : !matched;
    }
}

package de.fzi.herakles.util.configuration.impl;

import java.util.HashSet;
import org.apache.log4j.Logger;

/**
 * the conditions of the OWL-Reasoner Function
 * @author Xu
 *
 */
public class FunctionCondition {

    private String functionName = "";

    private HashSet<HashSet<ReasonerProperty>> conditions;

    private Logger logger;

    /**
	 * Constructor of the class
	 * @param name the function name
	 */
    public FunctionCondition(String name) {
        this.logger = Logger.getLogger(FunctionCondition.class);
        if (Parameter.isFunction(name) || Parameter.isGroupFunction(name)) {
            functionName = name;
        } else {
            functionName = "";
        }
        conditions = new HashSet<HashSet<ReasonerProperty>>();
    }

    /**
	 * add a property condition
	 * @param newProperty reasoner property
	 */
    public void addCondition(ReasonerProperty newProperty) {
        HashSet<ReasonerProperty> currentCondition = null;
        for (HashSet<ReasonerProperty> cons : conditions) {
            ReasonerProperty pro = cons.iterator().next();
            String propertyName = pro.getKey();
            if (newProperty.getKey().equals(propertyName)) {
                currentCondition = cons;
                break;
            }
            System.out.println("Added property: " + newProperty.getKey() + " - " + newProperty.getValue());
        }
        if (currentCondition == null) {
            currentCondition = new HashSet<ReasonerProperty>();
            conditions.add(currentCondition);
        }
        for (ReasonerProperty pro : currentCondition) {
            if (pro.equals(newProperty)) {
                return;
            }
        }
        currentCondition.add(newProperty);
    }

    /**
	 * remove a property condition
	 * @param prop reasoner property
	 */
    public void removeCondition(ReasonerProperty prop) {
        HashSet<ReasonerProperty> set = null;
        ReasonerProperty property = null;
        for (HashSet<ReasonerProperty> cons : conditions) {
            for (ReasonerProperty pro : cons) {
                if (pro.equals(prop)) {
                    set = cons;
                    property = pro;
                }
            }
        }
        if ((set != null) && (property != null)) {
            set.remove(property);
            if (set.isEmpty()) {
                conditions.remove(set);
            }
        }
    }

    /**
	 * clear all conditions
	 */
    public void removeAllCondition() {
        conditions.clear();
    }

    /**
	 * get the name of the function
	 * @return the string of function name
	 */
    public String getFunctionName() {
        return functionName;
    }

    /**
	 * get the conditions
	 * @return all the reasoner properties of the function
	 */
    public HashSet<HashSet<ReasonerProperty>> getConditions() {
        return conditions;
    }

    /**
	 * change the name of function
	 * @param name thename of the function
	 */
    public void setNewFunctionName(String name) {
        if (Parameter.isFunction(name) || Parameter.isGroupFunction(name)) {
            functionName = name;
        }
    }

    /**
	 * test if the condition is empty
	 * @return <code>true</code> if there is no saved condition, else <code>false</code>
	 */
    public boolean isEmpty() {
        if (conditions.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * test if the input function is a group function
	 * @return <code>true</code>  if the input string is a function name of group function returns true,
	 * else return <code>false</code>
	 */
    public boolean isGroupFunction() {
        if (functionName.equals(Parameter.allFunctionsString)) return true;
        if (functionName.equals(Parameter.basicFunctionsString)) return true;
        if (functionName.equals(Parameter.classFunctionsString)) return true;
        if (functionName.equals(Parameter.individualFunctionsString)) return true;
        if (functionName.equals(Parameter.propertyFunctionsString)) return true;
        return false;
    }

    @Override
    public FunctionCondition clone() {
        FunctionCondition clone = new FunctionCondition(functionName);
        for (HashSet<ReasonerProperty> cons : conditions) {
            for (ReasonerProperty prop : cons) {
                clone.addCondition(prop);
            }
        }
        return clone;
    }
}

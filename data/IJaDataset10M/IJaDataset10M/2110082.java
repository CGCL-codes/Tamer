package de.ibis.permoto.model.netStrategies.routingStrategies;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: orsotronIII
 * Date: 6-lug-2005
 * Time: 16.29.02
 * To change this template use Options | File Templates.
 * Modified by Bertoli Marco
 * Modified by Francesco D'Aquino
 * Modified by Thomas Jansson, 21.03.2007 - Added javadoc comments.
 */
public abstract class RoutingStrategy {

    /** Used to store all strategies. */
    protected static RoutingStrategy[] all;

    /** Description of this strategy. */
    protected String description;

    /** 
     * Returns the name of this strategy. 
     * @return String
     */
    public abstract String getName();

    /**
     * Returns the description of this strategy.
     * @return String
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns a HashMap with all values of this strategy.
     * @return HashMap
     */
    @SuppressWarnings("unchecked")
    public abstract HashMap getValues();

    /** 
     * Returns the string representation of this strategy.
     * @return String
     */
    public String toString() {
        return this.getName();
    }

    /**
     * Returns the clone of this strategy.
     * @return Object
     */
    public abstract Object clone();

    /**
     * Compares this strategy to the specified object.
     * @param o - Object
     * @return true if the strategies are equal; false otherwise
     */
    public final boolean equals(final Object o) {
        if (o instanceof RoutingStrategy) {
            return this.getName().equals(((RoutingStrategy) o).getName());
        } else {
            return false;
        }
    }

    /**
     * Returns the classpath of this strategy.
     * @return String
     */
    public abstract String getClassPath();

    /**
     * Return an array with an istance of every allowed Routing Strategy. 
     * Uses Reflection on <code>JMODELConstants</code> field to find all 
     * strategies and uses internal caching to search for strategiess only 
     * the first time that this method is called.
     *
     * @return an array with an istance of every allowed RoutingStrategy
     */
    public static RoutingStrategy[] findAll() {
        if (RoutingStrategy.all != null) {
            return RoutingStrategy.all;
        }
        final List<RoutingStrategy> strategies = new Vector<RoutingStrategy>();
        final Field[] fields = de.ibis.permoto.util.Constants.class.getFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().startsWith("ROUTING_")) {
                    strategies.add((RoutingStrategy) fields[i].get(null));
                }
            }
        } catch (IllegalAccessException ex) {
            System.err.println("A security manager has blocked reflection");
            ex.printStackTrace();
        }
        final RoutingStrategy[] ret = new RoutingStrategy[strategies.size()];
        for (int i = 0; i < strategies.size(); i++) {
            ret[i] = strategies.get(i);
        }
        RoutingStrategy.all = ret;
        return ret;
    }

    /**
     * Returns true if the routing strategy is dependent from the state of
     * the model.
     * @return  true if the routing strategy is dependent from the state of
     * the model
     */
    public abstract boolean isModelStateDependent();
}

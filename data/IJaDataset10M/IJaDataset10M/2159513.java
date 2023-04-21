package de.ufinke.cubaja.config;

import java.util.Map;

/**
 * Provider of <tt>ElementFactory</tt>s.
 * A configuration node which implements this interface is able to choose
 * the source of subordinate node instances dynamically.
 * This feature is needed when the classes which represent XML sub-elements
 * are not known at compile time, e.g. when they depend on other configuration parameters.
 * @author Uwe Finke
 */
public interface ElementFactoryProvider {

    /**
   * Returns an <tt>ElementFactory</tt> to the parser.
   * If the given tag or attributes are not supported by the implementing class,
   * the result may be <tt>null</tt>, so that the parser tries to create a node instance
   * in the standard way. 
   * @param tagName
   * @param attributes
   * @return element factory
   * @throws Exception
   */
    public ElementFactory getFactory(String tagName, Map<String, String> attributes) throws Exception;
}

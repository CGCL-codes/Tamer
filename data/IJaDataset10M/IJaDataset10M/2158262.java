package net.java.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the ip flows list grouped AVP type.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V1.4.0) specification:
 * 
 * <pre>
 * 7.3.18 Flow-Grouping AVP
 * The Binding-input-list AVP (AVP Code 508) is of type Grouped AVP and holds a list of IP flows 
 * 
 * It has the following ABNF grammar: 
 *  Flow-Grouping ::= AVP Header: 508 13019
 *      [ Flows ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface FlowGrouping extends GroupedAvp {

    /**
   * Returns the value of the V4-Transport-Address AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
    abstract Flows[] getFlows();

    /**
   * Sets the value of the Flows AVP, of type Grouped.
   */
    abstract void setFlow(Flows flow);

    /**
   * Sets the value of the Flows AVP, of type Grouped.
   */
    abstract void setFlows(Flows[] flows);
}

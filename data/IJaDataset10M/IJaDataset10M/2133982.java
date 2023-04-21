package com.idna.dm.domain;

import javax.xml.bind.annotation.XmlRegistry;
import com.idna.dm.domain.crud.DecisionHierarchy;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the rewards.oxm package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: rewards.oxm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Rule }
     * 
     */
    public DecisionHierarchy createDecisionHierarchy() {
        return new DecisionHierarchy();
    }
}

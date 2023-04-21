package com.sun.xacml.attr.proxy;

import com.sun.xacml.attr.AttributeProxy;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.RFC822NameAttribute;
import org.w3c.dom.Node;

/**
 * A proxy class that is provided mainly for the run-time configuration
 * code to use.
 *
 * @since 1.2
 * @author Seth Proctor
 */
public class RFC822NameAttributeProxy implements AttributeProxy {

    public AttributeValue getInstance(Node root) throws Exception {
        return RFC822NameAttribute.getInstance(root);
    }

    public AttributeValue getInstance(String value) throws Exception {
        return RFC822NameAttribute.getInstance(value);
    }
}

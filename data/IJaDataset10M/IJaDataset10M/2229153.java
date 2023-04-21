package org.j2eebuilder;

import org.jdom.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)InterfaceRuleDefinition.java	1.350 01/12/03
 * Specifies an adapter definition to read the adapter specific file and convert
 * it into an XML document
 * 		<interface>
 * 			<name>input</name>
 * 			<description>input interface</description>
 * 			<interface-rule>
 * 				<name>input1</name>
 * 				<description>input interface level 1</description>
 * 				<interface-attribute>
 * 					<attribute-name>inputID</attribute-name>
 *					<source-attribute>
 *						<component-name>Buyer</component-name>
 *						<attribute-name>buyerID</attribute-name>
 *						<attribute-value />
 *					</source-attribute>
 * 				</interface-attribute>
 * 				.
 * 				.
 * 				.
 * 			</interface-rule>
 * 		</interface>
 */
public class InterfaceRuleDefinition implements Definition {

    private static transient LogManager log = new LogManager(InterfaceRuleDefinition.class);

    private String name;

    private String description;

    private Collection interfaceAttributeDefinitions = new ArrayList();

    public InterfaceRuleDefinition() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InterfaceAttributeDefinition[] getInterfaceAttributeDefinitions() {
        return (InterfaceAttributeDefinition[]) this.interfaceAttributeDefinitions.toArray(new InterfaceAttributeDefinition[0]);
    }

    public void addInterfaceAttributeDefinition(InterfaceAttributeDefinition interfaceAttributeDefinition) {
        this.interfaceAttributeDefinitions.add(interfaceAttributeDefinition);
    }

    public InterfaceAttributeDefinition findInterfaceAttributeDefinitionByAttributeName(String name) {
        for (Iterator itr = this.interfaceAttributeDefinitions.iterator(); itr.hasNext(); ) {
            InterfaceAttributeDefinition interfaceAttributeDefinition = (InterfaceAttributeDefinition) itr.next();
            if (interfaceAttributeDefinition.getAttributeName() != null && interfaceAttributeDefinition.getAttributeName().equals(name)) return interfaceAttributeDefinition;
        }
        return null;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("InterfaceRuleDefinition[");
        buf.append(this.getName());
        buf.append(", ");
        buf.append(this.getDescription());
        buf.append(", ");
        if (interfaceAttributeDefinitions != null && interfaceAttributeDefinitions.size() > 0) buf.append(interfaceAttributeDefinitions.toString());
        buf.append("]");
        return buf.toString();
    }
}

package com.v1r3n.bol.parser;

import java.io.IOException;
import com.v1r3n.bol.Node;
import com.v1r3n.bol.NodeType;
import com.v1r3n.bol.ParserException;

/**
 * @author viren
 * <pre>
 * Examples:
 * $person
 * $person's name
 * $saras' address
 * $city = $employee's address' city
 * </pre>
 */
public class VariableInstance extends Node {

    /**
	 * @return the instanceName
	 */
    public String getInstanceName() {
        return instanceName;
    }

    private String instanceName;

    private int indx = -1;

    VariableInstance(Node parent) {
        super(parent, NodeType.VARIABLE_INSTANCE);
    }

    @Override
    public Node parse(InputReader reader) throws ParserException, IOException {
        instanceName = reader.nextToken();
        if ("$".equals(instanceName)) {
            instanceName = reader.nextToken();
        }
        String next = reader.peekToken();
        if ("'".equals(next)) {
            reader.nextToken();
            String s = reader.peekToken();
            if ("s".equalsIgnoreCase(s)) {
                reader.nextToken();
            }
            VariableInstance property = new VariableInstance(this);
            property.parse(reader);
            children.add(property);
        } else if ("[".equals(next)) {
            reader.nextToken();
            String indxStr = reader.nextToken();
            try {
                indx = Integer.parseInt(indxStr);
            } catch (NumberFormatException nfe) {
                throw new ParserException("Invalid array index: " + indxStr + ": var=" + instanceName);
            }
            String close = reader.nextToken();
            assertExpected(close, "]");
        }
        next = reader.peekToken();
        if ("'".equals(next)) {
            reader.nextToken();
            String s = reader.peekToken();
            if ("s".equalsIgnoreCase(s)) {
                reader.nextToken();
            }
            VariableInstance vi = new VariableInstance(this);
            vi.parse(reader);
            children.add(vi);
        }
        return this;
    }

    public String toString() {
        StringBuffer value = new StringBuffer();
        value.append(instanceName);
        if (indx > -1) {
            value.append("[" + indx + "]");
        }
        if (children.size() > 0) {
            value.append(".");
            value.append(children);
        }
        return value.toString();
    }
}

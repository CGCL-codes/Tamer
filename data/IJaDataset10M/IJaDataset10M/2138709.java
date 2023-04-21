package com.jawise.serviceadapter.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Defines the operations supported by the service
 * 
 * @author sathyan
 * 
 */
public class PortType {

    String name;

    Collection<Operation> operations;

    public PortType() {
        operations = new ArrayList<Operation>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Collection<Operation> operations) {
        this.operations = operations;
    }

    public Operation findOperation(String name) {
        if (operations != null) {
            for (Operation op : operations) {
                if (name.equals(op.getName())) {
                    return op;
                }
            }
        }
        return null;
    }
}

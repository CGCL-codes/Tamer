package org.opencube.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>The result of the data source call is the complex type. It consists 
 * of the record sets, and even must make the output variables values accessible. 
 * <p>This class instances are disconnected from the data source.
 * 
 * @author <a href="mailto:maciek@fingo.pl">FINGO - Maciej Mroczko</a>
 */
public class CallResult implements Serializable {

    private ArrayList<BoundVariable> boundVariables = new ArrayList<BoundVariable>();

    private ArrayList<RecordSet> recordSets = new ArrayList<RecordSet>();

    /**
	 * Returns the bound variables.
	 * 
	 * @return The bound variables.
	 */
    public BoundVariable[] getBoundVariables() {
        return this.boundVariables == null ? null : this.boundVariables.toArray(new BoundVariable[this.boundVariables.size()]);
    }

    /**
	 * Sets the bound variables.
	 * 
	 * @param values the bound variables
	 */
    public void setBoundVariables(BoundVariable[] boundVariables) {
        if (boundVariables == null) {
            this.boundVariables = null;
        } else {
            this.boundVariables = new ArrayList<BoundVariable>();
            this.boundVariables.addAll(Arrays.asList(boundVariables));
        }
    }

    /**
	 * Returns the result record sets.
	 * 
	 * @return The record set array
	 */
    public RecordSet[] getRecordSets() {
        return this.recordSets == null ? null : this.recordSets.toArray(new RecordSet[this.recordSets.size()]);
    }

    /**
	 * Sets the result resord sets.
	 * 
	 * @param values the result record sets
	 */
    public void setRecordSets(RecordSet[] recordSets) {
        if (recordSets == null) {
            this.recordSets = null;
        } else {
            this.recordSets = new ArrayList<RecordSet>();
            this.recordSets.addAll(Arrays.asList(recordSets));
        }
    }

    /**
	 * Adds the bound variable.
	 * 
	 * @param boundVariable the variable to add
	 */
    public void addBoundVariable(BoundVariable boundVariable) {
        if (this.boundVariables == null) {
            this.boundVariables = new ArrayList<BoundVariable>();
        }
        this.boundVariables.add(boundVariable);
    }

    /**
	 * Adds the record set.
	 * 
	 * @param rs the record set to add
	 */
    public void addRecordSet(RecordSet rs) {
        if (this.recordSets == null) {
            this.recordSets = new ArrayList<RecordSet>();
        }
        this.recordSets.add(rs);
    }

    /**
	 * Returns the bound variable with the given name.
	 * 
	 * @param name the name for the variable to find
	 * 
	 * @return The bound variable with the given name
	 */
    public BoundVariable getBoundVariable(String name) {
        for (int i = 0; this.boundVariables != null && i < this.boundVariables.size(); i++) {
            if (name.equals(((BoundVariable) this.boundVariables.get(i)).getName())) {
                return (BoundVariable) this.boundVariables.get(i);
            }
        }
        return null;
    }
}

package org.freeworld.prilib.column;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * <p>ColumnSchema is the most basic abstraction of a table column. In prilib,
 * this abstraction has a name and a type. The type need not be enforced by a
 * tabular object, but it must store the value and expose it here anyways.</p>
 * 
 * @author dchemko
 */
public interface ColumnSchema extends Serializable {

    /**
    * Sets the name of the column. This important column is the identifier that
    * distinguishes one column object from another. Tables that contain columns
    * cannot create columns of the same name.
    * 
    * @param name - Name of the column being created
    */
    public void setName(String name);

    /**
    * Fetches the name of the column
    * 
    * @return Unique name of the column
    */
    public String getName();

    /**
    * Assigns the class type of variables contained within the column. If
    * undefined, it is assumed that the type is 'Object' a.k.a. anything.
    * 
    * @param type - Class type of the column variables
    */
    public void setType(Class<? extends Object> type);

    /**
    * Fetches the type associated with the schema
    * 
    * @return The class type of the variables associated with this column schema
    */
    public Class<? extends Object> getType();

    /**
    * Adds a new PropertyChangeListener to the schema in order to detect and
    * react to changes in a column schema's internal representation 
    * 
    * @param listener - The property change listener to receive events
    */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
    * Removes the specified new PropertyChangeListener from receiving column
    * schema events. 
    * 
    * @param listener - The property change listener to remove from receiving
    * events
    */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
    * Creates a duplicate object instance of the column schema. Any mutable
    * fields found within the column schema are duplicated.
    * 
    * @return The newly duplicated column schema
    */
    public ColumnSchema newCopy();

    /**
    * Reads in all compatible column schema properties found within the
    * argument schema and attempts as best as it can to fill this schema with
    * the values of the parameter's column schema.
    *  
    * @param schema - The schema to copy the column schema data from
    */
    public void fill(ColumnSchema schema);
}

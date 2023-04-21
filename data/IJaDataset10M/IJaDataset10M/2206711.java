package org.jrcaf.model;

import org.jrcaf.internal.model.ModelTypeDefinition;
import org.jrcaf.model.datasources.IDatasource;

/**
 * Interface for hirarical model elements. 
 */
public interface IModelElement {

    /**
    * @return Returns the modelDefinition.
    */
    public abstract ModelTypeDefinition getModelTypeDefinition();

    /**
    * @return Returns the datasource.
    */
    public abstract IDatasource getDatasource();

    /**
    * @return Returns the model.
    */
    public abstract Object getModel();

    /**
    * @return Returns the parent.
    */
    public abstract IModelElement getParent();
}

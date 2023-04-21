package org.genos.gmf.Core.db;

/**
 * Interface to represent a order in the results of contained resources.
 * 
 * TODO: Right now, only fields from the "resource" table can be used in ordering.
 * It could be extended to allow ordering by parameters of resources, at the sql level
 * (building the results by querying the different resdefids in DbContainedResources.getResources).
 */
public interface IContainedResourcesOrder {

    /**
	 * Returns a string representing an ORDER clause in a SQL sentence 
	 */
    public String getSqlClause();
}

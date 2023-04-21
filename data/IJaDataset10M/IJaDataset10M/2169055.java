package gov.usda.gdpc;

import java.util.Map;
import java.util.Set;

/**
 */
public interface DataSource {

    /*************************************************************************
     * Returns unique identifier of data source.
     *************************************************************************/
    String getUniqueIdentifier();

    /*************************************************************************
     * Returns name of data source.
     *************************************************************************/
    String getName();

    /*************************************************************************
     * ?????????
     *************************************************************************/
    Map getMetadata();

    /*************************************************************************
     * Returns data types this data source can provide.
     *************************************************************************/
    String[] getDataTypes();

    /*************************************************************************
     * Returns true if this data source can provide specified data type.
     *************************************************************************/
    boolean canProvide(String dataType);

    /*************************************************************************
     * Returns data (type dataType) matching specified filter criteria.
     *************************************************************************/
    Set getData(String dataType, Filter filter) throws IllegalArgumentException;

    /*************************************************************************
     * Returns ids of data (type dataType) matching specified filter criteria.
     *************************************************************************/
    Set getIDs(String dataType, Filter filter) throws IllegalArgumentException;

    /*************************************************************************
     * Returns data (type dataType) with specified IDs.
     *************************************************************************/
    Set getData(String dataType, Set IDs) throws IllegalArgumentException;

    /*************************************************************************
     * Returns distinct values for specified property
     *************************************************************************/
    Set getDistinctValues(String dataType, Property property) throws IllegalArgumentException;
}

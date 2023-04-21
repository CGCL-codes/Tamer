package org.neodatis.odb.core.query;

import java.io.Serializable;
import org.neodatis.odb.OID;
import org.neodatis.odb.core.layers.layer2.instance.InstanceBuilder;
import org.neodatis.odb.core.layers.layer2.meta.AttributeValuesMap;

/**
 * Used to implement generic action on matching object. The Generic query executor is responsible for checking if an object meets the criteria conditions. 
 * Then an(some) object actions are called to execute what must be done with matching objects. A ValuesQuery can contain more than
 * one QueryFieldAction.
 * @author osmadja
 *
 */
public interface IQueryFieldAction extends Serializable {

    void start();

    void end();

    void execute(final OID oid, final AttributeValuesMap values);

    Object getValue();

    String getAttributeName();

    String getAlias();

    /** To indicate if a query will return one row (for example, sum, average, max and min, or will return more than one row*/
    boolean isMultiRow();

    void setMultiRow(boolean isMultiRow);

    /** used to create a copy!*/
    IQueryFieldAction copy();

    void setInstanceBuilder(InstanceBuilder builder);

    InstanceBuilder getInstanceBuilder();

    /**
	 * @param returnInstance
	 */
    void setReturnInstance(boolean returnInstance);

    boolean returnInstance();
}

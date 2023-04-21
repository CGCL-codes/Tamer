package org.formaria.data.pojo;

import org.formaria.aria.data.DataModel;

/**
 * <p>A model node for interacting with Hibernate POJOs.
 * </p>
 * <p>Copyright (c) Formaria Ltd., 2008<br>
 * License:      see license.txt
 */
public class HibernatePojoModel extends PersistentPojoModel {

    /**
   * Creates a new instance of HibernatePojoModel
   * @param parent the parent model node
   * @param subPath string in the format:
   * propertyName(property arguments)[ collection idx]
   * as it appears in a binding path.
   * @param ds the data source object
   */
    public HibernatePojoModel(DataModel parent, String subPath, PojoDataSource ds) {
        super(parent, subPath, ds);
    }

    /**
   * Creates a new instance of HibernatePojoModel
   * @param parent the parent model node
   * @param object the underlying pojo
   * @param ds the data source object
   */
    protected HibernatePojoModel(DataModel parent, Object pojo, PojoDataSource ds) {
        super(parent, pojo, ds);
    }

    /**   
   * Check if an active transaciton is required before invoking a getter
   * method that is used to obtain the POJO stored in the specified model node
   * @param childNode the POJO model node whose getter is to be queried
   * @return true if an active transaction is required, false otherwise
   */
    protected boolean getterRequiresTransaction(PojoModel childNode) {
        boolean isProxy = (adapter.getAdapterClassName().indexOf("$") > 0);
        return (isProxy || super.getterRequiresTransaction(childNode));
    }

    /**
   * Check if an active transaction is required before invoking a setter
   * method that is used to obtain the POJO stored in the specified model node
   * @param childNode the POJO model node whose setter is to be queried
   * @param value the value of the setter argument
   * @return true if an active transactio is required, false otherwise
   */
    protected boolean setterRequiresTransaction(PojoModel childNode, Object value) {
        boolean isProxy = (adapter.getAdapterClassName().indexOf("$") > 0);
        return (isProxy || super.setterRequiresTransaction(childNode, value));
    }
}

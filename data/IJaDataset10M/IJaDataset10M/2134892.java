package org.pojosoft.ria.gwt.client.meta;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Metadata for defining how CRUD operations for a ModelObject should be performed. This metadata must be defined
 * for each ModelObject. The metadata is maintained in the Persistence-meta.xml file. This file allows the framework
 * to know which action to take when a CRUD operation is initiated by the front-end/user.
 *
 * @author POJO Software
 * @see ModelObjectPersistenceActionMeta
 */
public class ModelObjectPersistenceMeta implements IsSerializable {

    /**
   * The short name of the model object
   */
    public String id;

    /**
   * The full classname of the model object
   */
    public String modelObjectClass;

    /**
   * The full classname of the model object's pk type
   */
    public String modelObjectIdClass;

    /**
   * Indicates whether the model object is nested. A nested model object may contain one or more children of the same
   * type and may point to a parent of the same type. In our framework, a nested model object implements AbstractTreeNode
   */
    public Boolean nested;

    /**
   * For every CRUD action, a {@link ModelObjectPersistenceActionMeta} must be defined. This attribute contains a list
   * of CRUD actions.
   *
   * gwt.typeArgs <org.pojosoft.ria.gwt.client.meta.ModelObjectPersistenceActionMeta>
   */
    public List<ModelObjectPersistenceActionMeta> persistenceActions;

    protected Object readResolve() {
        if (nested == null) nested = Boolean.FALSE;
        if (persistenceActions == null) persistenceActions = new ArrayList();
        return this;
    }
}

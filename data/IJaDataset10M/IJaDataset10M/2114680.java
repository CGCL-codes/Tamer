package org.broadleafcommerce.gwt.admin.client.datasource.promotion.offer;

import org.broadleafcommerce.gwt.admin.client.datasource.CeilingEntities;
import org.broadleafcommerce.gwt.client.datasource.dynamic.ListGridDataSource;
import org.broadleafcommerce.gwt.client.datasource.dynamic.module.BasicEntityModule;
import org.broadleafcommerce.gwt.client.datasource.dynamic.module.DataSourceModule;
import org.broadleafcommerce.gwt.client.datasource.relations.ForeignKey;
import org.broadleafcommerce.gwt.client.datasource.relations.PersistencePerspective;
import org.broadleafcommerce.gwt.client.datasource.relations.operations.OperationType;
import org.broadleafcommerce.gwt.client.datasource.relations.operations.OperationTypes;
import org.broadleafcommerce.gwt.client.service.AppServices;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;

public class FulfillmentGroupListDataSourceFactory {

    public static final String foreignKeyName = "order";

    public static ListGridDataSource dataSource = null;

    public static void createDataSource(String name, AsyncCallback<DataSource> cb) {
        if (dataSource == null) {
            OperationTypes operationTypes = new OperationTypes(OperationType.ENTITY, OperationType.ENTITY, OperationType.ENTITY, OperationType.ENTITY, OperationType.ENTITY);
            PersistencePerspective persistencePerspective = new PersistencePerspective(operationTypes, new String[] {}, new ForeignKey[] {});
            persistencePerspective.setPopulateToOneFields(true);
            persistencePerspective.setExcludeFields(new String[] { "personalMessage", "order" });
            DataSourceModule[] modules = new DataSourceModule[] { new BasicEntityModule(CeilingEntities.FULFILLMENT_GROUP, persistencePerspective, AppServices.DYNAMIC_ENTITY) };
            dataSource = new ListGridDataSource(name, persistencePerspective, AppServices.DYNAMIC_ENTITY, modules);
            dataSource.buildFields(null, true, cb);
        } else {
            if (cb != null) {
                cb.onSuccess(dataSource);
            }
        }
    }
}

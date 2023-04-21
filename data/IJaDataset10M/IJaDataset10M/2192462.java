package org.broadleafcommerce.cms.admin.client.datasource.structure;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import org.broadleafcommerce.cms.admin.client.datasource.CeilingEntities;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.module.BasicClientEntityModule;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.module.DataSourceModule;
import org.broadleafcommerce.openadmin.client.dto.ForeignKey;
import org.broadleafcommerce.openadmin.client.dto.OperationType;
import org.broadleafcommerce.openadmin.client.dto.OperationTypes;
import org.broadleafcommerce.openadmin.client.dto.PersistencePerspective;
import org.broadleafcommerce.openadmin.client.service.AppServices;

/**
 * 
 * @author jfischer
 *
 */
public class StructuredContentTypeFormListDataSourceFactory {

    public static long count = 0;

    public static StructuredContentTypeFormListDataSource createDataSource(String name, String[] customCriteria, AsyncCallback<DataSource> cb) {
        OperationTypes operationTypes = new OperationTypes(OperationType.ENTITY, OperationType.ENTITY, OperationType.ENTITY, OperationType.ENTITY, OperationType.ENTITY);
        PersistencePerspective persistencePerspective = new PersistencePerspective(operationTypes, new String[] {}, new ForeignKey[] {});
        DataSourceModule[] modules = new DataSourceModule[] { new BasicClientEntityModule(CeilingEntities.STRUCTUREDCONTENTTYPE, persistencePerspective, AppServices.DYNAMIC_ENTITY) };
        StructuredContentTypeFormListDataSource dataSource = new StructuredContentTypeFormListDataSource(name + count++, persistencePerspective, AppServices.DYNAMIC_ENTITY, modules);
        dataSource.buildFields(customCriteria, false, cb);
        return dataSource;
    }
}

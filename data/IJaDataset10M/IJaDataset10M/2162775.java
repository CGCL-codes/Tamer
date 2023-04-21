package org.broadleafcommerce.openadmin.client.datasource;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.ListGridDataSource;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.module.BasicClientEntityModule;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.module.DataSourceModule;
import org.broadleafcommerce.openadmin.client.dto.OperationTypes;
import org.broadleafcommerce.openadmin.client.dto.PersistencePerspective;
import org.broadleafcommerce.openadmin.client.service.AppServices;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author bpolster
 *
 */
public class SimpleDataSourceFactory implements DataSourceFactory {

    public static ListGridDataSource dataSource = null;

    private String entityClassName;

    public SimpleDataSourceFactory(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public PersistencePerspective setupPersistencePerspective(PersistencePerspective persistencePerspective) {
        return persistencePerspective;
    }

    public void createDataSource(String name, OperationTypes operationTypes, Object[] additionalItems, AsyncCallback<DataSource> cb) {
        if (dataSource == null) {
            PersistencePerspective persistencePerspective = createPersistencePerspective();
            DataSourceModule[] modules = createDataSourceModules().toArray(new DataSourceModule[0]);
            dataSource = new ListGridDataSource(name, persistencePerspective, AppServices.DYNAMIC_ENTITY, modules);
            dataSource.buildFields(null, false, cb);
        } else {
            if (cb != null) {
                cb.onSuccess(dataSource);
            }
        }
    }

    public PersistencePerspective createPersistencePerspective() {
        PersistencePerspective persistencePerspective = new PersistencePerspective();
        return setupPersistencePerspective(persistencePerspective);
    }

    public List<DataSourceModule> createDataSourceModules() {
        List<DataSourceModule> dsModuleList = new ArrayList<DataSourceModule>();
        dsModuleList.add(new BasicClientEntityModule(entityClassName, createPersistencePerspective(), AppServices.DYNAMIC_ENTITY));
        return dsModuleList;
    }
}

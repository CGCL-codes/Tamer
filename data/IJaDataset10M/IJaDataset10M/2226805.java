package org.broadleafcommerce.cms.admin.client.datasource.structure;

import com.anasoft.os.daofusion.cto.client.CriteriaTransferObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtincubator.security.exception.ApplicationSecurityException;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.tree.TreeNode;
import org.broadleafcommerce.openadmin.client.BLCMain;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.module.BasicClientEntityModule;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.operation.EntityOperationType;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.operation.EntityServiceAsyncCallback;
import org.broadleafcommerce.openadmin.client.dto.DynamicResultSet;
import org.broadleafcommerce.openadmin.client.dto.PersistencePackage;
import org.broadleafcommerce.openadmin.client.dto.PersistencePerspective;
import org.broadleafcommerce.openadmin.client.service.DynamicEntityServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: jfischer
 * Date: 8/24/11
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class StructuredContentListClientEntityModule extends BasicClientEntityModule {

    public StructuredContentListClientEntityModule(String ceilingEntityFullyQualifiedClassname, PersistencePerspective persistencePerspective, DynamicEntityServiceAsync service) {
        super(ceilingEntityFullyQualifiedClassname, persistencePerspective, service);
    }

    @Override
    public void executeFetch(final String requestId, DSRequest request, final DSResponse response, String[] customCriteria, final AsyncCallback<DataSource> cb) {
        BLCMain.NON_MODAL_PROGRESS.startProgress();
        Criteria criteria = request.getCriteria();
        if (((StructuredContentListDataSource) dataSource).permanentCriteria != null) {
            criteria.addCriteria(((StructuredContentListDataSource) dataSource).permanentCriteria);
        }
        CriteriaTransferObject cto = getCto(request);
        service.fetch(new PersistencePackage(ceilingEntityFullyQualifiedClassname, null, persistencePerspective, customCriteria), cto, new EntityServiceAsyncCallback<DynamicResultSet>(EntityOperationType.FETCH, requestId, request, response, dataSource) {

            public void onSuccess(DynamicResultSet result) {
                super.onSuccess(result);
                TreeNode[] recordList = buildRecords(result, null);
                response.setData(recordList);
                response.setTotalRows(result.getTotalRecords());
                if (cb != null) {
                    cb.onSuccess(dataSource);
                }
                dataSource.processResponse(requestId, response);
            }

            @Override
            protected void onSecurityException(ApplicationSecurityException exception) {
                super.onSecurityException(exception);
                if (cb != null) {
                    cb.onFailure(exception);
                }
            }

            @Override
            protected void onOtherException(Throwable exception) {
                super.onOtherException(exception);
                if (cb != null) {
                    cb.onFailure(exception);
                }
            }

            @Override
            protected void onError(EntityOperationType opType, String requestId, DSRequest request, DSResponse response, Throwable caught) {
                super.onError(opType, requestId, request, response, caught);
                if (cb != null) {
                    cb.onFailure(caught);
                }
            }
        });
    }
}

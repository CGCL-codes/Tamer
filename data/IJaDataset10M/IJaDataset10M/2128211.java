package org.broadleafcommerce.openadmin.client.presenter.user;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import org.broadleafcommerce.openadmin.client.BLCMain;
import org.broadleafcommerce.openadmin.client.datasource.EntityImplementations;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.AbstractDynamicDataSource;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.ListGridDataSource;
import org.broadleafcommerce.openadmin.client.datasource.user.AdminPermissionRelatedToUserListDataSourceFactory;
import org.broadleafcommerce.openadmin.client.datasource.user.AdminRoleListDataSourceFactory;
import org.broadleafcommerce.openadmin.client.presenter.entity.DynamicEntityPresenter;
import org.broadleafcommerce.openadmin.client.presenter.entity.SubPresentable;
import org.broadleafcommerce.openadmin.client.presenter.structure.SimpleSearchJoinStructurePresenter;
import org.broadleafcommerce.openadmin.client.reflection.Instantiable;
import org.broadleafcommerce.openadmin.client.setup.AsyncCallbackAdapter;
import org.broadleafcommerce.openadmin.client.setup.PresenterSetupItem;
import org.broadleafcommerce.openadmin.client.view.dynamic.dialog.EntitySearchDialog;
import org.broadleafcommerce.openadmin.client.view.user.RoleManagementDisplay;

/**
 * 
 * @author jfischer
 *
 */
public class RoleManagementPresenter extends DynamicEntityPresenter implements Instantiable {

    protected SubPresentable permissionsPresenter;

    @Override
    protected void changeSelection(Record selectedRecord) {
        AbstractDynamicDataSource dataSource = (AbstractDynamicDataSource) display.getListDisplay().getGrid().getDataSource();
        permissionsPresenter.load(selectedRecord, dataSource, null);
    }

    @Override
    protected void addClicked() {
        addClicked(BLCMain.getMessageManager().getString("newRoleTitle"));
    }

    @Override
    public void bind() {
        super.bind();
        permissionsPresenter.bind();
    }

    public void setup() {
        getPresenterSequenceSetupManager().addOrReplaceItem(new PresenterSetupItem("adminRoleDS", new AdminRoleListDataSourceFactory(), new AsyncCallbackAdapter() {

            public void onSetupSuccess(DataSource top) {
                setupDisplayItems(top);
                ((ListGridDataSource) top).setupGridFields(new String[] { "description" }, new Boolean[] { true });
            }
        }));
        getPresenterSequenceSetupManager().addOrReplaceItem(new PresenterSetupItem("adminPermissionDS", new AdminPermissionRelatedToUserListDataSourceFactory(), new AsyncCallbackAdapter() {

            @Override
            public void onSetupSuccess(DataSource dataSource) {
                permissionsPresenter = new SimpleSearchJoinStructurePresenter(getDisplay().getPermissionsDisplay(), new EntitySearchDialog((ListGridDataSource) dataSource), new String[] { EntityImplementations.ADMIN_ROLE }, BLCMain.getMessageManager().getString("searchForPermission"));
                permissionsPresenter.setDataSource((ListGridDataSource) dataSource, new String[] { "name", "description", "type" }, new Boolean[] { false, false, false });
            }
        }));
    }

    @Override
    public RoleManagementDisplay getDisplay() {
        return (RoleManagementDisplay) display;
    }
}

package pt.ips.estsetubal.mig.academicCloud.client.module.administrator.presenter;

import pt.ips.estsetubal.mig.academicCloud.client.AcademicCloudPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.helper.ClientApplicationHelper;
import pt.ips.estsetubal.mig.academicCloud.client.module.administrator.AdministratorEventBus;
import pt.ips.estsetubal.mig.academicCloud.client.module.administrator.view.SearchUserResultView;
import pt.ips.estsetubal.mig.academicCloud.client.service.AdministratorServiceAsync;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.user.UserDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.administrator.SearchUserCriteriaDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.administrator.SearchUserDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.InjectService;
import com.mvp4g.client.annotation.Presenter;

/**
 * This class is the SearchUserResult form presenter.
 * 
 * @see pt.ips.estsetubal.mig.academicCloud.client.module.administrator.view.SearchUserResultView
 * @author António Casqueiro
 */
@Presenter(view = SearchUserResultView.class)
public class SearchUserResultPresenter extends AcademicCloudPresenter<SearchUserResultPresenter.Display, AdministratorEventBus> {

    public static interface Display extends AcademicCloudPresenter.AcademicCloudDisplay {

        public HasClickHandlers getBackButton();

        public HasClickHandlers getCancelButton();

        public HasClickHandlers getEditPersonalDataButton();

        public HasClickHandlers getEditRolesButton();

        public int getSelectedRow();

        public void setData(SearchUserDTO user);

        public void init(SearchUserDTO data);
    }

    private AdministratorServiceAsync service;

    private SearchUserDTO model;

    @Override
    public Widget getForm() {
        return view.asWidget();
    }

    @Override
    protected void addListeners() {
        view.getBackButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                backAction();
            }
        });
        view.getCancelButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                cancelAction();
            }
        });
        view.getEditPersonalDataButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                editPersonalDataAction();
            }
        });
        view.getEditRolesButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                editRolesAction();
            }
        });
    }

    private void backAction() {
        ClientApplicationHelper.getInstance().getLog().debug("Back button clicked.");
        eventBus.backToSearchUser();
    }

    private void cancelAction() {
        ClientApplicationHelper.getInstance().getLog().debug("Cancel button clicked.");
        eventBus.cleanMainView();
    }

    private void editPersonalDataAction() {
        int selectedRow = view.getSelectedRow();
        ClientApplicationHelper.getInstance().getLog().debug("Edit personal data button clicked. Selected row: " + selectedRow);
        if (selectedRow != -1) {
            UserDTO userDTO = model.getResult().get(selectedRow);
            UserDTO data = userDTO;
            eventBus.executeGetUserAccount(data);
        }
    }

    private void editRolesAction() {
        int selectedRow = view.getSelectedRow();
        ClientApplicationHelper.getInstance().getLog().debug("Edit roles button clicked. Selected row: " + selectedRow);
        if (selectedRow != -1) {
            UserDTO userDTO = model.getResult().get(selectedRow);
            UserDTO data = userDTO;
            eventBus.executeGetManageUserRoles(data);
        }
    }

    public void onBackToSearchUserResult(boolean isToUpdateModel) {
        if (isToUpdateModel) {
            executeSearchUserResult(model.getCriteria(), true);
        } else {
            eventBus.changeMainView(view.asWidget());
        }
    }

    public void onExecuteGetSearchUserResult(SearchUserCriteriaDTO data) {
        executeSearchUserResult(data);
    }

    public void onSetDataSearchUserResult(SearchUserDTO data) {
        initViewListeners();
        model = data;
        view.init(data);
        view.setData(data);
        eventBus.changeMainView(view.asWidget());
    }

    private void executeSearchUserResult(SearchUserCriteriaDTO criteria) {
        executeSearchUserResult(criteria, false);
    }

    private void executeSearchUserResult(final SearchUserCriteriaDTO criteria, final boolean isForRefresh) {
        AsyncCallback<SearchUserDTO> callback = new AsyncCallback<SearchUserDTO>() {

            public void onFailure(Throwable caught) {
                ClientApplicationHelper.getInstance().getLog().error("Error!", caught);
                if (isForRefresh) {
                    eventBus.cleanMainView();
                }
            }

            public void onSuccess(SearchUserDTO result) {
                ClientApplicationHelper.getInstance().getLog().debug("Success: executeSearchUserResult");
                result.setCriteria(criteria);
                eventBus.setDataSearchUserResult(result);
            }
        };
        service.searchUserAccount(getSessionInfo(), criteria, callback);
    }

    @InjectService
    public void setService(AdministratorServiceAsync service) {
        this.service = service;
    }
}

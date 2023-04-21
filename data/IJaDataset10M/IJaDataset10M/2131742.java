package pt.ips.estsetubal.mig.academicCloud.client.module.administrator.view;

import pt.ips.estsetubal.mig.academicCloud.client.AcademicCloudView;
import pt.ips.estsetubal.mig.academicCloud.client.components.blocks.SchoolIdentificationDataComponent;
import pt.ips.estsetubal.mig.academicCloud.client.components.utils.WidgetUtils;
import pt.ips.estsetubal.mig.academicCloud.client.module.administrator.presenter.CreateSchoolPresenter;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.SchoolDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.administrator.CreateSchoolViewInitDataDTO;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class is the CreateSchool form view.
 * 
 * @see pt.ips.estsetubal.mig.academicCloud.client.module.administrator.presenter.CreateSchoolPresenter
 * @author António Casqueiro
 */
public class CreateSchoolView extends AcademicCloudView implements CreateSchoolPresenter.Display {

    private Button btnBack = new Button();

    private Button btnCancel = new Button();

    private Button btnConfirm = new Button();

    private SchoolIdentificationDataComponent componentSchoolIdentificationData;

    @Override
    public HasClickHandlers getBackButton() {
        return btnBack;
    }

    @Override
    public HasClickHandlers getCancelButton() {
        return btnCancel;
    }

    @Override
    public HasClickHandlers getConfirmButton() {
        return btnConfirm;
    }

    public void init(CreateSchoolViewInitDataDTO data) {
    }

    public void setData(SchoolDTO data) {
        componentSchoolIdentificationData.setData(data);
        clearErrorFlag();
    }

    public SchoolDTO getData() {
        return componentSchoolIdentificationData.getData();
    }

    public CreateSchoolView() {
        VerticalPanel contentPanel = new VerticalPanel();
        contentPanel.setWidth("100%");
        initWidget(contentPanel);
        componentSchoolIdentificationData = new SchoolIdentificationDataComponent();
        setReadOnlyFields(componentSchoolIdentificationData);
        contentPanel.add(componentSchoolIdentificationData);
        HorizontalPanel footerPanel = new HorizontalPanel();
        contentPanel.add(footerPanel);
        footerPanel.setSpacing(5);
        footerPanel.add(btnBack);
        btnBack.setText(buttonConstants.btnBack_text());
        btnBack.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnBack.addStyleName(WidgetUtils.ICON_BACK_STYLE);
        footerPanel.add(btnConfirm);
        btnConfirm.setText(buttonConstants.btnConfirm_text());
        btnConfirm.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnConfirm.addStyleName(WidgetUtils.ICON_CONFIRM_STYLE);
        footerPanel.add(btnCancel);
        btnCancel.setText(buttonConstants.btnCancel_text());
        btnCancel.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnCancel.addStyleName(WidgetUtils.ICON_CANCEL_STYLE);
    }

    private void setReadOnlyFields(SchoolIdentificationDataComponent component) {
    }
}

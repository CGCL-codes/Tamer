package pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.view;

import pt.ips.estsetubal.mig.academicCloud.client.AcademicCloudView;
import pt.ips.estsetubal.mig.academicCloud.client.components.utils.WidgetUtils;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.ManageCompetenceCoursesPresenter;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.CompetenceCourseDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.SearchCompetenceCourseResultDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class is the ManageCompetenceCourses form view.
 * 
 * @see pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.ManageCompetenceCoursesPresenter
 * @author António Casqueiro
 */
public class ManageCompetenceCoursesView extends AcademicCloudView implements ManageCompetenceCoursesPresenter.Display {

    private Button btnBack = new Button();

    private Button btnCancel = new Button();

    private Button btnCreate = new Button();

    private Button btnDelete = new Button();

    private Button btnDetail = new Button();

    private Button btnEdit = new Button();

    private Button btnManageVersions = new Button();

    private FlexTable flexTable = new FlexTable();

    private int selectedRow = -1;

    private boolean listenersInitialized = false;

    @Override
    public HasClickHandlers getBackButton() {
        return btnBack;
    }

    @Override
    public HasClickHandlers getCancelButton() {
        return btnCancel;
    }

    @Override
    public HasClickHandlers getCreateButton() {
        return btnCreate;
    }

    @Override
    public HasClickHandlers getDeleteButton() {
        return btnDelete;
    }

    @Override
    public HasClickHandlers getDetailButton() {
        return btnDetail;
    }

    @Override
    public HasClickHandlers getEditButton() {
        return btnEdit;
    }

    @Override
    public HasClickHandlers getManageVersionsButton() {
        return btnManageVersions;
    }

    public void init(SearchCompetenceCourseResultDTO data) {
        initListeners();
        removeTableContent(flexTable);
    }

    public void setData(SearchCompetenceCourseResultDTO data) {
        int row = 1;
        for (CompetenceCourseDTO elem : data.getResult()) {
            int column = 0;
            flexTable.setText(row, column++, elem.getCode());
            flexTable.setText(row, column++, elem.getName());
            row++;
        }
        clearErrorFlag();
    }

    private void initListeners() {
        synchronized (this) {
            if (!listenersInitialized) {
                flexTable.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        Cell cell = flexTable.getCellForEvent(event);
                        if (cell != null) {
                            int newIndex = cell.getRowIndex();
                            if (newIndex == 0) {
                                return;
                            }
                            styleRow(selectedRow, false);
                            if (newIndex != selectedRow) {
                                styleRow(newIndex, true);
                                selectedRow = cell.getRowIndex();
                            } else {
                                selectedRow = -1;
                            }
                        } else {
                            selectedRow = -1;
                        }
                    }
                });
                listenersInitialized = true;
            }
        }
    }

    private void styleRow(int row, boolean selected) {
        if (row != -1) {
            if (selected) {
                flexTable.getRowFormatter().addStyleName(row, WidgetUtils.SELECTED_ROW_STYLE);
            } else {
                flexTable.getRowFormatter().removeStyleName(row, WidgetUtils.SELECTED_ROW_STYLE);
            }
        }
    }

    public int getSelectedRow() {
        return selectedRow - 1;
    }

    public ManageCompetenceCoursesView() {
        VerticalPanel contentPanel = new VerticalPanel();
        contentPanel.setWidth("100%");
        initWidget(contentPanel);
        contentPanel.add(flexTable);
        flexTable.setBorderWidth(1);
        flexTable.setWidth("100%");
        addTableComponents(flexTable);
        HorizontalPanel footerPanel = new HorizontalPanel();
        contentPanel.add(footerPanel);
        footerPanel.setSpacing(5);
        footerPanel.add(btnBack);
        btnBack.setText(buttonConstants.btnBack_text());
        btnBack.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnBack.addStyleName(WidgetUtils.ICON_BACK_STYLE);
        footerPanel.add(btnCreate);
        btnCreate.setText(buttonConstants.btnCreate_text());
        btnCreate.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnCreate.addStyleName(WidgetUtils.ICON_NEW_STYLE);
        footerPanel.add(btnDelete);
        btnDelete.setText(buttonConstants.btnDelete_text());
        btnDelete.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnDelete.addStyleName(WidgetUtils.ICON_DELETE_STYLE);
        footerPanel.add(btnEdit);
        btnEdit.setText(buttonConstants.btnEdit_text());
        btnEdit.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnEdit.addStyleName(WidgetUtils.ICON_EDIT_STYLE);
        footerPanel.add(btnDetail);
        btnDetail.setText(buttonConstants.btnDetail_text());
        btnDetail.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnDetail.addStyleName(WidgetUtils.ICON_DETAIL_STYLE);
        footerPanel.add(btnManageVersions);
        btnManageVersions.setText(buttonConstants.btnManageVersions_text());
        btnManageVersions.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnManageVersions.addStyleName(WidgetUtils.ICON_DRILL_DOWN_STYLE);
        footerPanel.add(btnCancel);
        btnCancel.setText(buttonConstants.btnCancel_text());
        btnCancel.setStyleName(WidgetUtils.BUTTON_STYLE);
        btnCancel.addStyleName(WidgetUtils.ICON_CANCEL_STYLE);
    }

    private void addTableComponents(FlexTable flexTable) {
        flexTable.getRowFormatter().addStyleName(0, WidgetUtils.HEADER_ROW_STYLE);
        Label lblThCode = new Label(globalConstants.lblThCode_text());
        flexTable.setWidget(0, 0, lblThCode);
        Label lblThName = new Label(globalConstants.lblThName_text());
        flexTable.setWidget(0, 1, lblThName);
    }
}

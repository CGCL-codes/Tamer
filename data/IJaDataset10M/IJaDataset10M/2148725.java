package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.misc.View;
import honeycrm.client.mvp.presenters.DetailPresenter;
import honeycrm.client.mvp.presenters.DetailPresenter.Display;
import honeycrm.client.mvp.presenters.RelationshipsPresenter;
import honeycrm.client.services.ReadServiceAsync;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DetailView extends Composite implements Display {

    private static DetailViewUiBinder uiBinder = GWT.create(DetailViewUiBinder.class);

    @UiTemplate("DetailView.ui.xml")
    interface DetailViewUiBinder extends UiBinder<Widget, DetailView> {
    }

    @UiField
    FlexTable table;

    @UiField
    RelationshipsView relationshipsView;

    @UiField
    Button createBtn;

    @UiField
    Button editBtn;

    @UiField
    Button saveBtn;

    @UiField
    Button cancelBtn;

    @UiField
    Anchor pdfBtn;

    final String module;

    DetailPresenter presenter;

    ModuleDto moduleDto;

    Dto dto;

    private final ReadServiceAsync readService;

    private final LocalizedMessages constants;

    public DetailView(final String module, final ReadServiceAsync readService, final LocalizedMessages constants) {
        this.module = module;
        this.constants = constants;
        this.readService = readService;
        this.moduleDto = DtoModuleRegistry.instance().get(module);
        initWidget(uiBinder.createAndBindUi(this));
        createBtn.setText(constants.create());
        editBtn.setText(constants.edit());
        saveBtn.setText(constants.save());
        cancelBtn.setText(constants.cancel());
        pdfBtn.setText(constants.createPdf());
    }

    private void updatePdfCreateUrl(final long id) {
        pdfBtn.setHref("/Honey/pdf?m=" + moduleDto.getModule() + "&id=" + String.valueOf(id));
    }

    @UiFactory
    FlexTable makeTable() {
        return new FlexTable();
    }

    private void resetFields(final Dto newDto, final View view, final HashMap<String, Object> prefilledFields) {
        insertDataFromPrefilledFields(newDto, prefilledFields, view);
        updatePdfCreateUrl(newDto.getId());
        this.dto = newDto;
        final String[][] fieldIds = moduleDto.getFormFieldIds();
        for (int y = 0; y < fieldIds.length; y++) {
            for (int x = 0; x < fieldIds[y].length; x++) {
                final String id = fieldIds[y][x];
                final int labelX = 2 * x + 0, labelY = y;
                final int valueX = 2 * x + 1, valueY = y;
                final Widget widgetLabel = getWidgetLabel(id, labelX, labelY);
                final Widget widgetValue = getWidgetByType(newDto, id, view);
                widgetLabel.setStyleName("detail_view_label");
                widgetValue.setStyleName("detail_view_value");
                addEvents(view, widgetLabel, widgetValue, id);
                if (view == View.DETAIL || (view != View.DETAIL && !Dto.isInternalReadOnlyField(id))) {
                    table.setWidget(labelY, labelX, widgetLabel);
                    table.setWidget(valueY, valueX, widgetValue);
                }
            }
        }
    }

    protected void insertDataFromPrefilledFields(final Dto newDto, final HashMap<String, Object> prefilledFields, final View view) {
        if (null != prefilledFields && view != View.DETAIL) {
            for (final Entry<String, Object> entry : prefilledFields.entrySet()) {
                newDto.set(entry.getKey(), (Serializable) entry.getValue());
            }
        }
    }

    protected Widget getWidgetByType(final Dto tmpDto, final String fieldId, final View view) {
        return moduleDto.getFieldById(fieldId).getWidget(view, tmpDto, fieldId);
    }

    private void addFocus(View view, Widget widgetValue, String id, String focussedField) {
        if (View.EDIT == view && null != focussedField && id.equals(focussedField) && widgetValue instanceof FocusWidget) {
            ((FocusWidget) widgetValue).setFocus(true);
        }
    }

    private Widget getWidgetLabel(final String id, final int labelX, final int labelY) {
        final Widget widgetLabel;
        if (table.isCellPresent(labelY, labelX)) {
            if (table.getWidget(labelY, labelX) != null) {
                widgetLabel = table.getWidget(labelY, labelX);
            } else {
                widgetLabel = getLabelForField(id);
            }
        } else {
            widgetLabel = getLabelForField(id);
        }
        return widgetLabel;
    }

    protected Label getLabelForField(final String id) {
        return new Label(moduleDto.getFieldById(id).getLabel() + ":");
    }

    private void addEvents(final View view, final Widget widgetLabel, final Widget widgetValue, final String focussedField) {
        if (view == View.DETAIL) {
            if (widgetLabel instanceof Label) {
                ((Label) widgetLabel).addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        startEdit();
                    }
                });
            }
        } else {
            if (widgetValue instanceof TextBox) {
                ((TextBox) widgetValue).addKeyDownHandler(new KeyDownHandler() {

                    @Override
                    public void onKeyDown(KeyDownEvent event) {
                        if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
                            if (null != presenter) {
                                presenter.onSave();
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void setPresenter(DetailPresenter modulePresenter) {
        this.presenter = modulePresenter;
    }

    @Override
    public void setData(Dto dto) {
        createBtn.setVisible(true);
        editBtn.setVisible(true);
        cancelBtn.setVisible(false);
        saveBtn.setVisible(false);
        pdfBtn.setVisible(true);
        resetFields(dto, View.DETAIL, null);
    }

    @Override
    public void startCreate(final HashMap<String, Object> prefilledFields) {
        createBtn.setVisible(false);
        editBtn.setVisible(false);
        cancelBtn.setVisible(true);
        saveBtn.setVisible(true);
        pdfBtn.setVisible(false);
        resetFields(moduleDto.createDto(), View.CREATE, prefilledFields);
    }

    @Override
    public void startEdit() {
        createBtn.setVisible(false);
        editBtn.setVisible(false);
        cancelBtn.setVisible(true);
        saveBtn.setVisible(true);
        pdfBtn.setVisible(false);
        resetFields(dto, View.EDIT, null);
        relationshipsView.setVisible(false);
    }

    @Override
    public RelationshipsPresenter.Display getRelationshipsView() {
        return relationshipsView;
    }

    @Override
    public HasClickHandlers getCreateBtn() {
        return createBtn;
    }

    @Override
    public HasClickHandlers getSaveBtn() {
        return saveBtn;
    }

    @Override
    public HasClickHandlers getEditBtn() {
        return editBtn;
    }

    @Override
    public HasClickHandlers getCancelBtn() {
        return cancelBtn;
    }

    @Override
    public Dto getData() {
        final String[][] fields = moduleDto.getFormFieldIds();
        final Dto newDto = moduleDto.createDto();
        for (int y = 0; y < fields.length; y++) {
            for (int x = 0; x < fields[y].length; x++) {
                final String field = fields[y][x];
                if (!Dto.isInternalReadOnlyField(field)) {
                    final Widget widgetValue = table.getWidget(y, 2 * x + 1);
                    final Serializable value = moduleDto.getFieldById(field).getData(widgetValue);
                    newDto.set(field, value);
                }
            }
        }
        newDto.setId(dto.getId());
        return newDto;
    }

    @UiFactory
    RelationshipsView makeRelationshipsView() {
        final ArrayList<String> list = DtoModuleRegistry.instance().getRelatedModules(moduleDto.getModule());
        java.util.Collections.sort(list);
        return new RelationshipsView(module, list, readService, constants);
    }
}

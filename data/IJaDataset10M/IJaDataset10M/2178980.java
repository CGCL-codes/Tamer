package com.genia.toolbox.web.gwt.form.client.widget.item.impl;

import java.util.ArrayList;
import java.util.List;
import com.genia.toolbox.web.gwt.basics.client.GwtHelper;
import com.genia.toolbox.web.gwt.basics.client.i18n.GwtI18nMessage;
import com.genia.toolbox.web.gwt.basics.client.widget.HyperLinkButton;
import com.genia.toolbox.web.gwt.basics.client.widget.WidgetFactory;
import com.genia.toolbox.web.gwt.form.client.CssNames;
import com.genia.toolbox.web.gwt.form.client.form.InlineFormItem;
import com.genia.toolbox.web.gwt.form.client.manager.FormManager;
import com.genia.toolbox.web.gwt.form.client.message.Messages;
import com.genia.toolbox.web.gwt.form.client.process.ProcessSortable;
import com.genia.toolbox.web.gwt.form.client.value.FormValues;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * widget that represents a set of sub-form inlined in the current form.
 */
public class InlineFormItemWidget extends AbstractComplexItemWidget<InlineFormItem> {

    /**
   * the {@link formManager} that handle the current form.
   */
    private final transient FormManager currentFormManager;

    /**
   * the main {@link Panel} that contains this widget.
   */
    private final transient Panel mainPanel = new VerticalPanel();

    /**
   * the new button.
   */
    private final transient HyperLinkButton newButton = WidgetFactory.newHyperLinkButton(Messages.NEW_BUTTON_MESSAGE, CssNames.NEW_BUTTON_CSS_STYLE);

    /**
   * the {@link List} of remove buttons.
   */
    private final transient List<HyperLinkButton> removeButtons = new ArrayList<HyperLinkButton>();

    /**
   * the {@link Panel} that will contain the sub-forms.
   */
    private final transient VerticalPanel subFormsPanel;

    /**
   * the widgets that the setErrorMessage must modify.
   */
    private final transient Widget[] widgetsToToggle = new Widget[] { mainPanel };

    /**
   * constructor.
   * 
   * @param currentFormManager
   *          the {@link formManager} that handle the current form.
   * @param inlineFormItem
   *          the {@link InlineFormItem} to display
   */
    public InlineFormItemWidget(FormManager currentFormManager, InlineFormItem inlineFormItem) {
        super(currentFormManager, inlineFormItem);
        this.currentFormManager = currentFormManager;
        final GwtI18nMessage labelKey = inlineFormItem.getLabelKey();
        Label label = new Label();
        label.addStyleName(CssNames.INLINE_FORM_ITEM_LABEL_CSS_STYLE);
        GwtHelper.fillText(label, labelKey);
        mainPanel.addStyleName(CssNames.INLINE_FORM_ITEM_MAIN_PANEL_CSS_STYLE);
        mainPanel.addStyleName(inlineFormItem.getName());
        subFormsPanel = ProcessSortable.getVerticalPanel(this);
        subFormsPanel.addStyleName(CssNames.INLINE_FORM_ITEM_FORM_PANEL_CSS_STYLE);
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.add(label);
        newButton.addClickListener(new ClickListener() {

            public void onClick(final Widget widget) {
                addNewSubForm(null);
                updateButtons();
            }
        });
        hPanel.add(newButton);
        mainPanel.add(hPanel);
        mainPanel.add(subFormsPanel);
        for (int i = 0; i < inlineFormItem.getMinimumNumberOfSubForm(); i++) {
            addNewSubForm(null);
        }
        initWidget(mainPanel);
        updateButtons();
    }

    /**
   * add a new inline Form.
   * 
   * @param formValues
   *          the values of the new inline form.
   */
    private void addNewSubForm(final FormValues formValues) {
        final Panel subElementPanel = new HorizontalPanel();
        subFormsPanel.add(subElementPanel);
        Panel subFormPanel = new FlowPanel();
        subElementPanel.add(subFormPanel);
        final FormManager formManager = new FormManager(currentFormManager.getFormController(), subFormPanel, (getItem()).getInlineFormIdentifier(), currentFormManager.getDataIdentifier().getChildDataIdentifier(null));
        getFormManagers().add(formManager);
        formManager.addModifyListener(getItemWidgetModifyListenerCollection());
        final HyperLinkButton removeButton = WidgetFactory.newHyperLinkButton(Messages.REMOVE_BUTTON_MESSAGE, CssNames.REMOVE_BUTTON_CSS_STYLE);
        removeButtons.add(removeButton);
        removeButton.addClickListener(new ClickListener() {

            public void onClick(Widget widget) {
                getFormManagers().remove(formManager);
                subFormsPanel.remove(subElementPanel);
                getItemWidgetModifyListenerCollection().onModify(InlineFormItemWidget.this);
                removeButtons.remove(removeButton);
                updateButtons();
            }
        });
        subElementPanel.add(removeButton);
        formManager.displayForm(new Command() {

            public void execute() {
                if (formValues == null) {
                    formManager.resetForm(false);
                } else {
                    formManager.setFormValues(formValues);
                }
            }
        });
    }

    /**
   * returns an array containing all the widgets that the setErrorMessage must
   * modify.
   * 
   * @return an array containing all the widgets that the setErrorMessage must
   *         modify
   * @see com.genia.toolbox.web.gwt.form.client.widget.item.impl.AbstractItemWidget#getWidgetsToToggle()
   */
    @Override
    protected Widget[] getWidgetsToToggle() {
        return widgetsToToggle;
    }

    /**
   * set the current values of this item.
   * 
   * @param values
   *          a {@link List} of
   *          {@link com.genia.toolbox.web.gwt.form.client.value.FormValues}
   *          representing the new values of this item
   * @see com.genia.toolbox.web.gwt.form.client.widget.item.ComplexItemWidget#setValues(java.util.List)
   */
    public void setValues(final List<FormValues> values) {
        getFormManagers().clear();
        subFormsPanel.clear();
        if (values == null || values.isEmpty()) {
            return;
        }
        for (FormValues formValues : values) {
            addNewSubForm(formValues);
        }
        updateButtons();
    }

    /**
   * update the visibility of the button depending on the number of element.
   */
    private void updateButtons() {
        InlineFormItem inlineFormItem = getItem();
        int numberOfItem = getFormManagers().size();
        if (inlineFormItem.getMaximumNumberOfSubForm() != null) {
            newButton.setVisible(numberOfItem < inlineFormItem.getMaximumNumberOfSubForm().intValue());
        }
        if (inlineFormItem.getMinimumNumberOfSubForm() > 0) {
            boolean removeButtonVisibility = (numberOfItem > inlineFormItem.getMinimumNumberOfSubForm());
            for (HyperLinkButton hyperLinkButton : removeButtons) {
                hyperLinkButton.setVisible(removeButtonVisibility);
            }
        }
    }
}

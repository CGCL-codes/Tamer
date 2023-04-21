package com.citep.web.gwt.module;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import com.citep.web.gwt.validators.DataElement;
import com.citep.web.gwt.validators.ErrorListener;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class BasePresentation {

    protected Label titleLabel;

    protected HashMap errorLabels = new HashMap();

    protected HorizontalPanel titlePanel;

    public static void setListValue(ListBox list, String value) {
        for (int i = 0; i < list.getItemCount(); i++) {
            if (list.getItemText(i).toLowerCase().equals(value.toLowerCase())) {
                list.setSelectedIndex(i);
            }
        }
    }

    public static String getListSelectedValue(ListBox list) {
        if (list.getSelectedIndex() < 0) return null;
        return list.getItemText(list.getSelectedIndex());
    }

    public static TextBox createTextBox(String stylename, int length) {
        TextBox txtBox = new TextBox();
        txtBox.setStyleName(stylename);
        txtBox.setVisibleLength(length);
        return txtBox;
    }

    public static PasswordTextBox createPasswordTextBox(String stylename, int length) {
        PasswordTextBox txtBox = new PasswordTextBox();
        txtBox.setStyleName(stylename);
        txtBox.setWidth("" + length * 8 + "px");
        return txtBox;
    }

    protected void addValidationLabelToGrid(Grid g, HTML label) {
        int rows = g.getRowCount();
        int cols = g.getColumnCount();
        g.resize(rows + 1, cols);
        g.setWidget(rows, 1, label);
        g.getCellFormatter().setStyleName(rows, 1, "error_label");
        g.getRowFormatter().setStyleName(rows, "hidden");
    }

    public class ValidationErrorListener implements ErrorListener {

        public void onError(DataElement e, String msg) {
            displayError(e.getName(), msg);
        }
    }

    protected HTML createErrorLabel(String id) {
        HTML l = new HTML();
        l.setStyleName("error_label");
        errorLabels.put(id, l);
        return l;
    }

    protected HTML createErrorLabel(String[] ids) {
        HTML l = new HTML();
        l.setStyleName("error_label");
        for (int i = 0; i < ids.length; i++) errorLabels.put(ids[i], l);
        return l;
    }

    public void clearError(String id) {
        HTML l = (HTML) errorLabels.get(id);
        if (l == null) return;
        Element cell = DOM.getParent(l.getElement());
        if (cell == null) return;
        Element row = DOM.getParent(cell);
        if (row == null) return;
        DOM.setStyleAttribute(row, "display", "none");
        l.setText("");
    }

    public void clearErrors() {
        Set keys = errorLabels.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            clearError((String) iterator.next());
        }
    }

    public void displayError(String id, String msg) {
        HTML l = (HTML) errorLabels.get(id);
        if (l == null) {
            Window.alert(id + ": " + msg);
            return;
        }
        Element cell = DOM.getParent(l.getElement());
        if (cell == null) return;
        Element row = DOM.getParent(cell);
        if (row == null) return;
        try {
            DOM.setStyleAttribute(row, "display", "table-row");
        } catch (Throwable t) {
            DOM.setStyleAttribute(row, "display", "block");
        }
        if (l.getHTML().length() > 0) l.setHTML(l.getHTML() + "<br>" + msg); else l.setHTML(msg);
    }

    public HorizontalPanel getTitlePanel() {
        if (titlePanel == null) {
            titlePanel = new HorizontalPanel();
            titlePanel.setStyleName("form_title_panel");
            titlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
            titlePanel.add(getTitleLabel());
            titlePanel.setWidth("100%");
        }
        return titlePanel;
    }

    public Label getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new Label();
            titleLabel.setStyleName("form_title");
        }
        return titleLabel;
    }
}

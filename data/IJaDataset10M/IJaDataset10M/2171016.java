package openadmin.widgets.jsf.component;

import java.io.Serializable;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.event.MethodExpressionActionListener;
import org.ajax4jsf.component.html.HtmlAjaxCommandButton;
import org.ajax4jsf.component.html.HtmlAjaxCommandLink;
import org.richfaces.component.html.HtmlComponentControl;
import org.richfaces.component.html.HtmlInputText;
import org.richfaces.component.html.HtmlTab;
import org.richfaces.component.html.HtmlToolTip;

public class TplSimpleComponent implements Serializable {

    private static final long serialVersionUID = 16051001L;

    public static HtmlOutputLabel HtmlLabel01(String value) {
        HtmlOutputLabel label = new HtmlOutputLabel();
        label.setValue(value);
        label.setStyleClass("output");
        return label;
    }

    public static HtmlOutputLabel HtmlLabel02(String value) {
        HtmlOutputLabel label = new HtmlOutputLabel();
        label.setValue(value);
        label.setStyleClass("output");
        return label;
    }

    public static HtmlInputText HtmlInput01(int pLong, boolean readOnly, String value, Class<?> typeClass) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        HtmlInputText input = new HtmlInputText();
        input.setMaxlength(pLong);
        input.setSize(pLong);
        input.setReadonly(readOnly);
        if (readOnly) input.setStyleClass("inputReadOnly");
        input.setValueExpression("value", app.getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), value, typeClass));
        return input;
    }

    public static HtmlInputTextarea textArea01(int pLong, String value, Class<?> typeClass) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        HtmlInputTextarea textArea = new HtmlInputTextarea();
        textArea.setCols(80);
        textArea.setRows(2);
        textArea.setValueExpression("value", app.getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), value, typeClass));
        return textArea;
    }

    public static HtmlOutputText HtmlOutputText01(String value) {
        HtmlOutputText outputText = new HtmlOutputText();
        outputText.setValue(value);
        return outputText;
    }

    public static HtmlAjaxCommandButton commandFr1(String pValue, String pAction, String id, String icon) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        HtmlAjaxCommandButton command = new HtmlAjaxCommandButton();
        command.setId(id);
        MethodExpression methodExpr = app.getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), pAction, null, new Class[] { javax.faces.event.ActionEvent.class });
        command.addActionListener(new MethodExpressionActionListener(methodExpr));
        command.setStyleClass("button");
        command.setValue(pValue);
        command.setImage(icon);
        return command;
    }

    public static HtmlAjaxCommandButton commandFr2(String pValue, String pAction, String id, String icon) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        HtmlAjaxCommandButton command = new HtmlAjaxCommandButton();
        command.setId(id.replace(".", "_"));
        MethodExpression methodExpr = app.getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), pAction, null, new Class[] { javax.faces.event.ActionEvent.class });
        command.addActionListener(new MethodExpressionActionListener(methodExpr));
        command.setStyleClass("button");
        command.setValue(pValue);
        command.setImage(icon);
        command.setReRender("idform");
        return command;
    }

    public static UIComponent commandLink(String action) {
        HtmlAjaxCommandLink commandlink = new HtmlAjaxCommandLink();
        commandlink.setValue(action);
        commandlink.setId("idlink");
        HtmlComponentControl control = new HtmlComponentControl();
        control.setFor("idform:idmodalpanel1");
        control.setAttachTo("idlink");
        control.setOperation("show");
        control.setEvent("onclick");
        commandlink.getChildren().add(control);
        return commandlink;
    }

    public static HtmlToolTip tooltip01(String pValue) {
        HtmlToolTip tooltip = new HtmlToolTip();
        tooltip.setFollowMouse(true);
        tooltip.setValue(pValue);
        return tooltip;
    }

    public static HtmlPanelGrid panelGrid(int column) {
        HtmlPanelGrid panelData = new HtmlPanelGrid();
        panelData.setColumns(column);
        return panelData;
    }

    public static HtmlTab tab(String label) {
        HtmlTab tab = new HtmlTab();
        tab.setId(label);
        tab.setLabel(label);
        return tab;
    }

    public static HtmlSelectBooleanCheckbox checkbox(String value) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        HtmlSelectBooleanCheckbox check = new HtmlSelectBooleanCheckbox();
        check.setValueExpression("value", app.getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), value, Boolean.class));
        return check;
    }
}

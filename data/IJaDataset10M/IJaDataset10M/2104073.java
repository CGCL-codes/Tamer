package com.gwtext.client.widgets;

import com.google.gwt.core.client.JavaScriptObject;
import com.gwtext.client.util.JavaScriptObjectHelper;
import com.gwtext.client.widgets.form.*;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.PropertyGridPanel;
import com.gwtext.client.widgets.menu.Item;

/**
 * Component factory class.
 *
 * @author Sanjiv Jivan
 */
public class ComponentFactory {

    /**
     * Return a Component from the passed native component object.
     *
     * @param jsObj native object
     * @return the corresponding Component.
     * @see com.gwtext.client.widgets.Component
     */
    public static Component getComponent(JavaScriptObject jsObj) {
        Object componentJ = JavaScriptObjectHelper.getAttributeAsObject(jsObj, "__compJ");
        if (componentJ != null) {
            return (Component) componentJ;
        }
        String xtype = getXType(jsObj);
        if (xtype == null) {
            return null;
        }
        if (xtype.equalsIgnoreCase("box")) {
            return new BoxComponent(jsObj);
        } else if (xtype.equalsIgnoreCase("button")) {
            return new Button(jsObj);
        } else if (xtype.equalsIgnoreCase("colorpalette")) {
            return new ColorPalette(jsObj);
        } else if (xtype.equalsIgnoreCase("cycle")) {
            return new CycleButton(jsObj);
        } else if (xtype.equalsIgnoreCase("dataview")) {
            return new DataView(jsObj);
        } else if (xtype.equalsIgnoreCase("datepicker")) {
            return new DatePicker(jsObj);
        } else if (xtype.equalsIgnoreCase("editor")) {
            return new Editor(jsObj);
        } else if (xtype.equalsIgnoreCase("editorgrid")) {
            return new EditorGridPanel(jsObj);
        } else if (xtype.equalsIgnoreCase("propertygrid")) {
            return new PropertyGridPanel(jsObj);
        } else if (xtype.equalsIgnoreCase("grid")) {
            return new GridPanel(jsObj);
        } else if (xtype.equalsIgnoreCase("paging")) {
            return new PagingToolbar(jsObj);
        } else if (xtype.equalsIgnoreCase("button")) {
            return new Button(jsObj);
        } else if (xtype.equalsIgnoreCase("panel")) {
            return new Panel(jsObj);
        } else if (xtype.equalsIgnoreCase("progress")) {
            return new ProgressBar(jsObj);
        } else if (xtype.equalsIgnoreCase("splitbutton")) {
            return new SplitButton(jsObj);
        } else if (xtype.equalsIgnoreCase("tabpanel")) {
            return new TabPanel(jsObj);
        } else if (xtype.equalsIgnoreCase("window")) {
            return new Window(jsObj);
        } else if (xtype.equalsIgnoreCase("gwtwidget")) {
            return new WidgetComponent(jsObj);
        } else if (xtype.equalsIgnoreCase("toolbar")) {
            return new Toolbar(jsObj);
        } else if (xtype.equalsIgnoreCase("tbbutton")) {
            return new ToolbarButton(jsObj);
        } else if (xtype.equalsIgnoreCase("menu-item")) {
            return new Item(jsObj);
        } else if (xtype.equalsIgnoreCase("checkbox")) {
            return new Checkbox(jsObj);
        } else if (xtype.equalsIgnoreCase("combo")) {
            return new ComboBox(jsObj);
        } else if (xtype.equalsIgnoreCase("label")) {
            return new Label(jsObj);
        } else if (xtype.equalsIgnoreCase("datefield")) {
            return new DateField(jsObj);
        } else if (xtype.equalsIgnoreCase("fieldset")) {
            return new FieldSet(jsObj);
        } else if (xtype.equalsIgnoreCase("form")) {
            return new FormPanel(jsObj);
        } else if (xtype.equalsIgnoreCase("hidden")) {
            return new Hidden(jsObj);
        } else if (xtype.equalsIgnoreCase("htmleditor")) {
            return new HtmlEditor(jsObj);
        } else if (xtype.equalsIgnoreCase("numberfield")) {
            return new NumberField(jsObj);
        } else if (xtype.equalsIgnoreCase("radio")) {
            return new Radio(jsObj);
        } else if (xtype.equalsIgnoreCase("textarea")) {
            return new TextArea(jsObj);
        } else if (xtype.equalsIgnoreCase("textfield")) {
            return new TextField(jsObj);
        } else if (xtype.equalsIgnoreCase("timefield")) {
            return new TimeField(jsObj);
        } else {
            throw new IllegalArgumentException("Unrecognized xtype " + xtype);
        }
    }

    private static native String getXType(JavaScriptObject jsObj);
}

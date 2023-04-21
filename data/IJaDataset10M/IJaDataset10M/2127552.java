package br.ufmg.catustec.arangi.view.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * 
 * @author Cesar Correia
 *
 */
public class PanelGridTag extends UIComponentTag {

    /**
	 * Flag indicating whether or not this component should be rendered
	 * (during Render Response Phase), or processed on any subsequent
	 * form submit.
	 */
    private String rendered;

    /**
     * Name or code of the background color for this table.
     */
    private String bgcolor;

    /**
     * Width (in pixels) of the border to be drawn around this table.
     */
    private String border;

    /**
     * Definition of how much space the user agent should
     * leave between the border of each cell and its contents.
     */
    private String cellpadding;

    /**
     * Definition of how much space the user agent should
     * leave between the left side of the table and the
     * leftmost column, the top of the table and the top of
     * the top side of the topmost row, and so on for the
     * right and bottom of the table.  It also specifies
     * the amount of space to leave between cells.
     */
    private String cellspacing;

    /**
     * Comma-delimited list of CSS style classes that will be applied
     * to the columns of this table.  A space separated list of
     * classes may also be specified for any individual column.  If
     * the number of elements in this list is less than the number of
     * columns specified in the "columns" attribute, no "class"
     * attribute is output for each column greater than the number of
     * elements in the list.  If the number of elements in the list
     * is greater than the number of columns specified in the
     * "columns" attribute, the elements at the posisiton in the list
     * after the value of the "columns" attribute are ignored.
     */
    private String columnClasses;

    /**
     * The number of columns to render before
     * starting a new row.
     */
    private String columns;

    /**
     * Direction indication for text that does not inherit directionality.
     * Valid values are "LTR" (left-to-right) and "RTL" (right-to-left).
     */
    private String dir;

    /**
     * Space-separated list of CSS style class(es) that will be
     * applied to any footer generated for this table.
     */
    private String footerClass;

    /**
     * Code specifying which sides of the frame surrounding
     * this table will be visible.  Valid values are:
     * none (no sides, default value); above (top side only);
     * below (bottom side only); hsides (top and bottom sides
     * only); vsides (right and left sides only); lhs (left
     * hand side only); rhs (right hand side only); box
     * (all four sides); and border (all four sides).
     */
    private String frame;

    /**
     * Space-separated list of CSS style class(es) that will be
     * applied to any header generated for this table.
     */
    private String headerClass;

    /**
     * Code describing the language used in the generated markup
     * for this component.
     */
    private String lang;

    /**
     * Javascript code executed when a pointer button is
     * clicked over this element.
     */
    private String onclick;

    /**
     * Javascript code executed when a pointer button is
     * double clicked over this element.
     */
    private String ondblclick;

    /**
     * Javascript code executed when a key is
     * pressed down over this element.
     */
    private String onkeydown;

    /**
     * Javascript code executed when a key is
     * pressed and released over this element.
     */
    private String onkeypress;

    /**
     * Javascript code executed when a key is
     * released over this element.
     */
    private String onkeyup;

    /**
     * Javascript code executed when a pointer button is
     * pressed down over this element.
     */
    private String onmousedown;

    /**
     * Javascript code executed when a pointer button is
     * moved within this element.
     */
    private String onmousemove;

    /**
     * Javascript code executed when a pointer button is
     * moved away from this element.
     */
    private String onmouseout;

    /**
     * Javascript code executed when a pointer button is
     * moved onto this element.
     */
    private String onmouseover;

    /**
     * Javascript code executed when a pointer button is
     * released over this element.
     */
    private String onmouseup;

    /**
     * Comma-delimited list of CSS style classes that will be applied
     * to the rows of this table.  A space separated list of classes
     * may also be specified for any individual row.  Thes styles are
     * applied, in turn, to each row in the table.  For example, if
     * the list has two elements, the first style class in the list
     * is applied to the first row, the second to the second row, the
     * first to the third row, the second to the fourth row, etc.  In
     * other words, we keep iterating through the list until we reach
     * the end, and then we start at the beginning again. 
     */
    private String rowClasses;

    /**
     * Code specifying which rules will appear between cells
     * within this table.  Valid values are:  none (no rules,
     * default value); groups (between row groups); rows
     * (between rows only); cols (between columns only); and
     * all (between all rows and columns).
     */
    private String rules;

    /**
     * CSS style(s) to be applied when this component is rendered.
     */
    private String style;

    /**
     * Space-separated list of CSS style class(es) to be applied when
     * this element is rendered.  This value must be passed through
     * as the "class" attribute on generated markup.
     */
    private String styleClass;

    /**
     * Summary of this table's purpose and structure, for
     * user agents rendering to non-visual media such as
     * speech and Braille.
     */
    private String summary;

    /**
     * Advisory title information about markup elements generated
     * for this component.
     */
    private String title;

    /**
     * Width of the entire table, for visual user agents.
     */
    private String width;

    /**
     * Height of the entire table, for visual user agents.
     */
    private String height;

    /**
     * The value binding expression linking this component to a property in a backing bean
     */
    private String binding;

    public String getComponentType() {
        return "br.ufmg.catustec.arangi.view.component.SketchHtmlPanelGrid";
    }

    public String getRendererType() {
        return null;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getCellpadding() {
        return cellpadding;
    }

    public void setCellpadding(String cellpadding) {
        this.cellpadding = cellpadding;
    }

    public String getCellspacing() {
        return cellspacing;
    }

    public void setCellspacing(String cellspacing) {
        this.cellspacing = cellspacing;
    }

    public String getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFooterClass() {
        return footerClass;
    }

    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getHeaderClass() {
        return headerClass;
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getOndblclick() {
        return ondblclick;
    }

    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    public String getOnkeydown() {
        return onkeydown;
    }

    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    public String getOnkeypress() {
        return onkeypress;
    }

    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    public String getOnkeyup() {
        return onkeyup;
    }

    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    public String getOnmousedown() {
        return onmousedown;
    }

    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    public String getOnmousemove() {
        return onmousemove;
    }

    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }

    public String getOnmouseout() {
        return onmouseout;
    }

    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    public String getOnmouseover() {
        return onmouseover;
    }

    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    public String getOnmouseup() {
        return onmouseup;
    }

    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    public String getRowClasses() {
        return rowClasses;
    }

    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        setBoolean(component, "rendered", rendered);
        setString(component, "bgcolor", bgcolor);
        setInteger(component, "border", border);
        setString(component, "cellpadding", cellpadding);
        setString(component, "cellspacing", cellspacing);
        setString(component, "columnClasses", columnClasses);
        setInteger(component, "columns", columns);
        setString(component, "dir", dir);
        setString(component, "footerClass", footerClass);
        setString(component, "frame", frame);
        setString(component, "headerClass", headerClass);
        setString(component, "lang", lang);
        setString(component, "onclick", onclick);
        setString(component, "ondblclick", ondblclick);
        setString(component, "onkeydown", onkeydown);
        setString(component, "onkeypress", onkeypress);
        setString(component, "onkeyup", onkeyup);
        setString(component, "onmousemove", onmousemove);
        setString(component, "onmouseout", onmouseout);
        setString(component, "onmouseover", onmouseover);
        setString(component, "onmouseup", onmouseup);
        setString(component, "rowClasses", rowClasses);
        setString(component, "rules", rules);
        setString(component, "style", style);
        setString(component, "styleClass", styleClass);
        setString(component, "summary", summary);
        setString(component, "title", title);
        setString(component, "width", width);
        setString(component, "height", height);
        setString(component, "binding", binding);
    }

    public void setInteger(UIComponent component, String attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        if (this.isValueReference(attributeValue)) {
            this.setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName, new Integer(attributeValue));
        }
    }

    public void setString(UIComponent component, String attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        if (this.isValueReference(attributeValue)) {
            this.setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName, attributeValue);
        }
    }

    public void setBoolean(UIComponent component, String attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        if (this.isValueReference(attributeValue)) {
            this.setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName, new Boolean(attributeValue));
        }
    }

    private void setValueBinding(UIComponent component, String attributeName, String attributeValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueBinding vb = app.createValueBinding(attributeValue);
        component.setValueBinding(attributeName, vb);
    }

    public void release() {
        super.release();
        rendered = null;
        bgcolor = null;
        border = null;
        cellpadding = null;
        cellspacing = null;
        columnClasses = null;
        columns = null;
        dir = null;
        footerClass = null;
        frame = null;
        headerClass = null;
        lang = null;
        onclick = null;
        ondblclick = null;
        onkeydown = null;
        onkeypress = null;
        onkeyup = null;
        onmousemove = null;
        onmouseout = null;
        onmouseover = null;
        onmouseup = null;
        rowClasses = null;
        rules = null;
        style = null;
        styleClass = null;
        summary = null;
        title = null;
        width = null;
        height = null;
        binding = null;
    }
}

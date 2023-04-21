package org.sss.presentation.faces.custom.datatable;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import org.sss.presentation.faces.common.AbstractTableTag;

/**
 * DataTable的标签类
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 55 $ $Date: 2007-12-23 05:05:56 -0500 (Sun, 23 Dec 2007) $
 */
public class DataTableTag extends AbstractTableTag {

    public static final String COMPONENT_TYPE = "org.sss.faces.UIDataTable";

    public static final String RENDERER_TYPE = "org.sss.faces.DataTableRenderer";

    public static final String TAG_ROWINDEXVAR = "rowIndexVar";

    public static final String TAG_ROWCOUNTVAR = "rowCountVar";

    public static final String TAG_INSERTENABLED = "insertEnabled";

    public static final String TAG_DELETEENABLED = "deleteEnabled";

    public static final String TAG_APPENDENABLED = "appendEnabled";

    public static final String TAG_BUTTONSTYLE = "buttonStyle";

    public static final String TAG_BUTTONSTYLECLASS = "buttonStyleClass";

    public static final String TAG_BUTTONSTYLECLASSES = "buttonStyleClasses";

    public static final String TAG_ITEMCLASSNAME = "itemClassName";

    private ValueExpression _rowIndexVar;

    private ValueExpression _rowCountVar;

    private ValueExpression _insertEnabled;

    private ValueExpression _deleteEnabled;

    private ValueExpression _appendEnabled;

    private ValueExpression _buttonStyle;

    private ValueExpression _buttonStyleClass;

    private ValueExpression _buttonStyleClasses;

    private ValueExpression _itemClassName;

    public void setRowCountVar(ValueExpression countVar) {
        _rowCountVar = countVar;
    }

    public void setRowIndexVar(ValueExpression indexVar) {
        _rowIndexVar = indexVar;
    }

    public void setAppendEnabled(ValueExpression appendEnabled) {
        this._appendEnabled = appendEnabled;
    }

    public void setDeleteEnabled(ValueExpression deleteEnabled) {
        this._deleteEnabled = deleteEnabled;
    }

    public void setInsertEnabled(ValueExpression insertEnabled) {
        this._insertEnabled = insertEnabled;
    }

    public void setButtonStyle(ValueExpression buttonStyle) {
        this._buttonStyle = buttonStyle;
    }

    public void setButtonStyleClass(ValueExpression buttonStyleClass) {
        this._buttonStyleClass = buttonStyleClass;
    }

    public void setButtonStyleClasses(ValueExpression buttonStyleClasses) {
        this._buttonStyleClasses = buttonStyleClasses;
    }

    public void setItemClassName(ValueExpression itemClassName) {
        this._itemClassName = itemClassName;
    }

    public String getComponentType() {
        return DataTableTag.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return DataTableTag.RENDERER_TYPE;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        component.setValueExpression(DataTableTag.TAG_ROWINDEXVAR, _rowIndexVar);
        component.setValueExpression(DataTableTag.TAG_ROWCOUNTVAR, _rowCountVar);
        component.setValueExpression(DataTableTag.TAG_INSERTENABLED, _insertEnabled);
        component.setValueExpression(DataTableTag.TAG_DELETEENABLED, _deleteEnabled);
        component.setValueExpression(DataTableTag.TAG_APPENDENABLED, _appendEnabled);
        component.setValueExpression(DataTableTag.TAG_BUTTONSTYLE, _buttonStyle);
        component.setValueExpression(DataTableTag.TAG_BUTTONSTYLECLASS, _buttonStyleClass);
        component.setValueExpression(DataTableTag.TAG_BUTTONSTYLECLASSES, _buttonStyleClasses);
        component.setValueExpression(DataTableTag.TAG_ITEMCLASSNAME, _itemClassName);
    }

    @Override
    public void release() {
        super.release();
        _rowIndexVar = null;
        _rowCountVar = null;
        _insertEnabled = null;
        _deleteEnabled = null;
        _appendEnabled = null;
        _buttonStyle = null;
        _buttonStyleClass = null;
        _buttonStyleClasses = null;
        _itemClassName = null;
    }
}

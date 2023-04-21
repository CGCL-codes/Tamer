package com.gorillalogic.dex.taglib;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import com.gorillalogic.dal.ColumnHdr;
import com.gorillalogic.dal.Type;
import com.gorillalogic.dal.utils.TypeEnum;
import com.gorillalogic.dex.*;
import com.gorillalogic.util.ExceptionLogger;
import java.util.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author  Stu
 */
public class ColumnTag extends GorillaTag {

    Logger logger = Logger.getLogger(ColumnTag.class);

    private String _fieldName = null;

    private String _title = null;

    private TableBean _table = null;

    private int _rowcount = 0;

    private String _titleStyle = "dexHeading";

    private TableTag _parent = null;

    private boolean _hasIcon = false;

    private Style _style = null;

    public int doStartTag() throws JspTagException {
        _parent = (TableTag) findAncestorWithClass(this, TableTag.class);
        if (_parent == null) {
            throw new JspTagException("gorilla:column tag without enclosing gorilla:table tag");
        }
        _table = _parent.getTable();
        _rowcount = _table.getRowCount();
        String s;
        if (_parent.isHeading()) {
            if (_fieldName == null) {
                s = getAllHeadings(_table);
            } else {
                s = getHeading(_fieldName);
            }
        } else {
            RowBean row = _parent.getRow();
            try {
                if (_fieldName == null) {
                    s = getAllFormattedCells(row);
                } else {
                    s = getFormattedCell(row, _fieldName);
                }
            } catch (DexException e) {
                throw new JspTagException("Error getting formatted cells");
            }
        }
        out(s);
        return SKIP_BODY;
    }

    protected String getAllHeadings(TableBean table) throws JspTagException {
        Iterator iter = table.getColArray().iterator();
        StringBuffer s = new StringBuffer();
        while (iter.hasNext()) {
            String fieldName = ((TableBean._Column) iter.next()).getName();
            if (!_parent.isShowIds()) {
                if (fieldName.equals(com.gorillalogic.dal.Type.RIDNAME) || fieldName.equals(com.gorillalogic.dal.Type.OIDNAME)) {
                    continue;
                }
            }
            s.append(getHeading(fieldName));
        }
        return s.toString();
    }

    protected String getHeading(String fieldName) throws JspTagException {
        _parent.countColumn();
        String heading;
        if (_title == null) {
            heading = fieldName;
        } else {
            heading = _title;
        }
        String[] attrs = { attr("class", _titleStyle) };
        ColumnHdr col = _table.getTable().column(fieldName);
        if (col == null) {
            String msg = "column " + fieldName + " not found in table " + _table.getTable().path();
            logger.error(msg);
            throw new JspTagException(msg);
        }
        if (!TypeEnum.checker.isTable(col) && _rowcount > 1) {
            try {
                heading = this.a("getTable.dex?" + "setNext=dexTable=" + StringEscapeUtils.escapeHtml(_table.getTableID()) + " sortBy " + fieldName, "Sort by " + heading, null, heading);
            } catch (DexException e) {
                throw new JspTagException("Error getting table id");
            }
        }
        return tag("td", attrs, heading);
    }

    protected String getAllFormattedCells(RowBean row) throws DexException {
        Iterator iter = row.getAsTable().getColArray().iterator();
        StringBuffer s = new StringBuffer();
        while (iter.hasNext()) {
            String fieldName = ((TableBean._Column) iter.next()).getName();
            if (!_parent.isShowIds()) {
                if (fieldName.equals(com.gorillalogic.dal.Type.RIDNAME) || fieldName.equals(com.gorillalogic.dal.Type.OIDNAME)) {
                    continue;
                }
            }
            s.append(getFormattedCell(row, fieldName));
        }
        return s.toString();
    }

    protected String getFormattedCell(RowBean row, String fieldName) throws DexException {
        _parent.countColumn();
        Style style = getStyle(row.getAsTable(), fieldName);
        String s;
        int count;
        Object cell;
        try {
            cell = row.getCellMap().get(fieldName);
        } catch (Exception e) {
            String[] attrs = { attr("title", StringEscapeUtils.escapeHtml(ExceptionLogger.getMessage(e))), attr("style", "cursor:help") };
            return tag("td", attrs, img(Icons.Small.ERROR, null));
        }
        boolean isComposite = false;
        TableBean table;
        switch(style.getDexType()) {
            case CARD_ONE_COMPOSITE:
                isComposite = true;
            case CARD_ONE_TABLE:
                table = (TableBean) cell;
                count = table.getRowCount();
                if (count == 1) {
                    s = table.getAsRow().getDisplayKey();
                    if (s == null | s.equals("")) s = "_";
                    s = a(editRowRequest(table.getAsRow()), "View/edit " + table.getName(), Icons.Small.FORM, s);
                    break;
                }
                if (count == 0) {
                    if (isComposite) {
                        s = a(addRowRequest(table), "Add a new " + table.getName(), Icons.Small.LINK_NEW, "");
                        break;
                    }
                    if (table.getTable().isQuery()) {
                        s = "(0)";
                        break;
                    }
                    s = a(this.browseRequest(table.getBaseTable(), table), "Select an existing " + table.getName(), Icons.Small.LINK_ADD, "");
                    break;
                }
                s = a(displayTableRequest(table), "View " + table.getName() + " list", Icons.Small.TABLE_VIEW, "(" + table.getRowCount() + ")");
                ;
                break;
            case CARD_N_COMPOSITE:
                isComposite = true;
            case TABLE:
                table = (TableBean) cell;
                s = a(displayTableRequest(table), "View " + table.getName() + " list", Icons.Small.TABLE_VIEW, "(" + table.getRowCount() + ")");
                break;
            case BOOLEAN:
                s = (String) cell;
                if (s.equals("true")) {
                    s = "Yes";
                } else {
                    s = "No";
                }
                break;
            default:
                s = (String) cell + nbsp(1);
                if (s == null) {
                    s = "N/A";
                }
        }
        String sty;
        if (_class == null) {
            sty = style.getStyle();
        } else {
            sty = _class;
        }
        String[] attrs = { attr("class", sty) };
        return tag("td", attrs, s);
    }

    public void setFieldName(String value) {
        if (value.equals("*")) {
            _fieldName = null;
            return;
        }
        _fieldName = value;
    }

    public String getFieldName() {
        return _fieldName;
    }

    public void setTitle(String value) {
        _title = value;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitleStyle(String value) {
        _titleStyle = value;
    }

    public String getTitleStyle() {
        return _titleStyle;
    }
}

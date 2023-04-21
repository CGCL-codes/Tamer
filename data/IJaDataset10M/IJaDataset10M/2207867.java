package com.gorillalogic.dex.taglib;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import com.gorillalogic.dex.*;
import com.gorillalogic.dex.explorer.Node;
import com.gorillalogic.dex.explorer.NodeException;
import com.gorillalogic.util.ExceptionLogger;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author  Stu
 */
public class TableView extends GorillaTag {

    Logger logger = Logger.getLogger(TableView.class);

    private String _nodeId = null;

    private String _messageStyle = null;

    private String _titleStyle = null;

    private static final String TITLE_STYLE = "dexTableTitle";

    private static final String STYLE = "dexTable";

    private static final String MESSAGE_STYLE = "dexBooleanType";

    private Iterator _iter;

    private RowBean _row;

    private boolean _isHeading;

    private int _columns;

    private boolean _showIds = false;

    private static TypeTable _typeTable = null;

    public int doStartTag() throws JspTagException {
        if (_rootNode == null) {
            out("Node " + _nodeId + " not found");
            return SKIP_BODY;
        }
        _columns = 0;
        _isHeading = true;
        try {
            _iter = _rootNode.getChildren().iterator();
        } catch (NodeException e) {
            throw (JspTagException) ExceptionLogger.log(logger, new JspTagException("Error accessing children for root node " + _nodeId));
        }
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() {
        logger.debug("doAfterBody");
        if (_isHeading) {
            String view = (String) pageContext.getRequest().getAttribute("dexTableView");
            String id = _nodeId;
            String[] attrs = { attr("class", getStyle(STYLE)), attr("cellpadding", "0"), attr("cellspacing", "0"), attr("width", "100%"), attr("id", id) };
            out(startTag("table", attrs), true);
            String[] lintel = { attr("class", "dexHeading"), attr("colspan", Integer.toString(_columns)) };
            out(tr(tag("td", lintel, nbsp(1))), true);
            String[] titleattrs = { attr("class", getTitleStyle(TITLE_STYLE)), attr("colspan", Integer.toString(_columns)) };
            out(tr(tag("td", titleattrs, getName(_table.getName()))), true);
            _isHeading = false;
            if (!_iter.hasNext()) {
                String[] messageAttrs = { attr("class", getMessageStyle(MESSAGE_STYLE)), attr("colspan", Integer.toString(_columns)) };
                BodyContent body = getBodyContent();
                out(tr(body.getString()), true);
                body.clearBody();
                out(tr(tag("td", messageAttrs, "This table is empty")), true);
                out(endTag("table"), true);
                return SKIP_BODY;
            }
        }
        out(startTag("tr"), true);
        BodyContent body = getBodyContent();
        out(body.getString(), true);
        body.clearBody();
        out(endTag("tr"), true);
        if (_iter.hasNext()) {
            _row = (RowBean) _iter.next();
            return EVAL_BODY_AGAIN;
        } else {
            out(endTag("table"), true);
            return SKIP_BODY;
        }
    }

    public void setTitleStyle(String value) {
        _path = value;
    }

    public String getTitleStyle(String defaultVal) {
        return (_titleStyle == null) ? defaultVal : _titleStyle;
    }

    /**
	 * 
	 * @param The style for table messages (e.g., "This table has no rows")
	 */
    public void setMessageStyle(String value) {
        _messageStyle = value;
    }

    public String getMessageStyle(String defaultVal) {
        return (_messageStyle == null) ? defaultVal : _messageStyle;
    }

    void countColumn() {
        _columns++;
    }

    boolean isHeading() {
        return _isHeading;
    }

    /**
	 * @return the root of the node tree that's being displayed as a table
	 */
    Node getRootNode() {
        return _rootNode;
    }

    /**
	 * @return the next node to be displayed as a row of this table
	 */
    Node getNextChild() {
        if (_iter.hasNext()) {
            return (Node) _iter.next();
        } else {
            return null;
        }
    }
}

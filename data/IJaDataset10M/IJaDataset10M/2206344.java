package com.aragost.araspect.handlers;

import java.util.Map;
import com.aragost.araspect.ExceptionInfo;
import com.aragost.araspect.Path;
import com.aragost.araspect.evaluators.GenericMapEvaluator;
import com.aragost.html.CellHeader;
import com.aragost.html.HtmlWriter;
import com.aragost.html.Row;
import com.aragost.html.Table;
import com.aragost.servlet.HttpServletRequestInfo;

/**
 *
 * @author  aragost
 */
public class ExceptionInfoHandler extends Handler {

    void writeHeaderHtml(HtmlWriter out) {
        ExceptionInfo ei = (ExceptionInfo) getPath().getSafeValue();
        HttpServletRequestInfo req = ei.getRequest();
        Table table = getHtmlUtils().createTable("Request Parameters", new String[] { "name", "value" }, req.getParameterMap(), getPath(), getRequestParameterEvaluator());
        table.writeTo(out);
        table = getHtmlUtils().createTable("Request Attributes", new String[] { "name", "value", "type" }, req.getAttributeMap(), getPath(), getRequestAttributeEvaluator());
        table.writeTo(out);
        table = new Table();
        CellHeader header = new CellHeader("Stack Trace");
        header.setBgcolor(getInspector().getHeaderColor());
        table.add(new Row(header));
        Path p = getPath().append(ObjectHandler.getMethodEvaluator().createPathElement("getStackTrace"));
        table.add(new Row(getInspector().displayElement(p, true)));
        table.writeTo(out);
        super.writeHeaderHtml(out);
    }

    static GenericMapEvaluator requestParameterEvaluator = new GenericMapEvaluator("exinforeqparam", "getParameter") {

        public Map getMap(Object ei) {
            return ((ExceptionInfo) ei).getRequest().getParameterMap();
        }
    };

    /**
   * Gets the requestParameterEvaluator.
   * @return Returns a GenericMapEvaluator
   */
    public static GenericMapEvaluator getRequestParameterEvaluator() {
        return requestParameterEvaluator;
    }

    static GenericMapEvaluator requestAttributeEvaluator = new GenericMapEvaluator("exinforeqattr", "getAttribute") {

        public Map getMap(Object ei) {
            return ((ExceptionInfo) ei).getRequest().getAttributeMap();
        }
    };

    /**
   * Gets the requestAttributeEvaluator.
   * @return Returns a GenericMapEvaluator
   */
    public static GenericMapEvaluator getRequestAttributeEvaluator() {
        return requestAttributeEvaluator;
    }

    static {
        getRequestAttributeEvaluator().register();
        getRequestParameterEvaluator().register();
    }
}

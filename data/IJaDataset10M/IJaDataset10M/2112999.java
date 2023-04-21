package org.osmius.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeImpl;
import org.jmesa.limit.*;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.editor.HeaderEditor;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.web.HttpServletRequestWebContext;
import org.jmesa.web.WebContext;
import org.osmius.Constants;
import org.osmius.service.OsmNGuardManager;
import org.osmius.webapp.util.OsmiusToolbar;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class OsmNGuardListController implements Controller {

    private final Log log = LogFactory.getLog(OsmNScheduleListController.class);

    private MessageSource messageSource = null;

    private OsmNGuardManager osmNGuardManager;

    private String id = "osmNGuardList";

    /**
    * Sets the messge source
    *
    * @param messageSource The message source
    */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setOsmNGuardManager(OsmNGuardManager osmNGuardManager) {
        this.osmNGuardManager = osmNGuardManager;
    }

    /**
    * From Spring documentation:
    * <p/>
    * Process the request and return a ModelAndView object which the DispatcherServlet
    * will render. A <code>null</code> return value is not an error: It indicates that
    * this object completed request processing itself, thus there is no ModelAndView
    * to render.
    *
    * @param request  current HTTP request
    * @param response current HTTP response
    * @return a ModelAndView to render, or <code>null</code> if handled directly
    * @throws Exception in case of errors
    */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("--> 'handleRequest' method...");
        }
        ModelAndView model = new ModelAndView("notifications/osmNGuardListAJAX");
        String checks = request.getParameter("checks");
        if (checks != null) {
            String[] splittedChecks = checks.split(";");
            osmNGuardManager.deleteGuards(splittedChecks);
        }
        String html = render(request, response);
        if (html == null) {
            return null;
        } else {
            String ajax = request.getParameter("ajax");
            if (ajax != null && ajax.equals("true")) {
                byte[] contents = html.getBytes();
                response.getOutputStream().write(contents);
                return null;
            } else {
                StringBuffer tmp = new StringBuffer(html);
                request.setAttribute(Constants.OSMNGUARD_LIST, tmp);
            }
        }
        return model;
    }

    protected String render(final HttpServletRequest request, HttpServletResponse response) {
        Locale locale = request.getLocale();
        MessageSourceAccessor text = new MessageSourceAccessor(messageSource, request.getLocale());
        TableFacade tableFacade = new TableFacadeImpl(id, request);
        tableFacade.setColumnProperties("select", "idnGuard", "desGuard");
        setDataAndLimitVariables(tableFacade, request);
        tableFacade.autoFilterAndSort(false);
        Table table = tableFacade.getTable();
        Column select = table.getRow().getColumn("select");
        HtmlColumn htmlColumn = (HtmlColumn) select;
        htmlColumn.getHeaderRenderer().setHeaderEditor(new HeaderEditor() {

            public Object getValue() {
                return "<input style=\"align:center\" type=\"checkbox\" name=\"allbox\" id=\"allbox\" onclick=\"checkAll(this.form)\"/>";
            }
        });
        ((HtmlColumn) select).setFilterable(false);
        ((HtmlColumn) select).setWidth("10%");
        ((HtmlColumn) select).getCellRenderer().setStyle("height:20px;align:center;");
        Column idnGuard = table.getRow().getColumn("idnGuard");
        ((HtmlColumn) idnGuard).getHeaderRenderer().setStyle("display:none");
        ((HtmlColumn) idnGuard).setSortable(false);
        ((HtmlColumn) idnGuard).setFilterable(false);
        ((HtmlColumn) idnGuard).setWidth("1px");
        ((HtmlColumn) idnGuard).getCellRenderer().setStyle("display:none");
        Column desGuard = table.getRow().getColumn("desGuard");
        desGuard.setTitle(text.getMessage("osmNGuard.desGuard"));
        ((HtmlColumn) desGuard).setFilterable(false);
        ((HtmlColumn) desGuard).setWidth("90%");
        Limit limit = tableFacade.getLimit();
        if (limit.isExported()) {
            tableFacade.render();
            return null;
        } else {
            HtmlTable htmlTable = (HtmlTable) table;
            htmlTable.getTableRenderer().setWidth("100%");
            select.getCellRenderer().setCellEditor(new CellEditor() {

                public Object getValue(Object item, String property, int rowcount) {
                    Object value = new BasicCellEditor().getValue(item, "idnGuard", rowcount);
                    StringBuffer strBuff = new StringBuffer();
                    strBuff.append("<p align=\"center\"><input style=\"align:center;\" type=\"checkbox\" id=\"check_").append(rowcount).append("\" value=\"").append(value.toString()).append("\"/></p>");
                    return strBuff.toString();
                }
            });
            idnGuard.getCellRenderer().setCellEditor(new CellEditor() {

                public Object getValue(Object item, String property, int rowcount) {
                    Object value = new BasicCellEditor().getValue(item, property, rowcount);
                    StringBuffer strBuff = new StringBuffer();
                    strBuff.append("<a href=javascript:testGuard('").append(value);
                    strBuff.append("')>").append(value).append("</a>");
                    return strBuff.toString();
                }
            });
            desGuard.getCellRenderer().setCellEditor(new CellEditor() {

                public Object getValue(Object item, String property, int rowcount) {
                    Object value = new BasicCellEditor().getValue(item, property, rowcount);
                    Object id = new BasicCellEditor().getValue(item, "idnGuard", rowcount);
                    StringBuffer strBuff = new StringBuffer();
                    strBuff.append("<a href=javascript:testGuard('").append(id);
                    strBuff.append("')>").append(value).append("</a>");
                    return strBuff.toString();
                }
            });
            tableFacade.setToolbar(new OsmiusToolbar());
            return tableFacade.render();
        }
    }

    protected void setDataAndLimitVariables(TableFacade tableFacade, HttpServletRequest request) {
        Limit limit = tableFacade.getLimit();
        SortSet sortSet = limit.getSortSet();
        Collection<Sort> sorts = sortSet.getSorts();
        StringBuffer orderByBuff = new StringBuffer();
        for (Sort sort : sorts) {
            String property = sort.getProperty();
            String order = sort.getOrder().toParam();
            if (property.equals("idnGuard")) {
                orderByBuff.append(" ong.idnGuard ").append(order).append(",");
            } else if (property.equals("desGuard")) {
                orderByBuff.append(" ong.desGuard ").append(order).append(",");
            }
        }
        String orderBy;
        if (orderByBuff.length() == 0) {
            orderBy = " ong.desGuard ";
        } else {
            orderBy = orderByBuff.toString().substring(0, orderByBuff.toString().length() - 1);
        }
        WebContext webContext = new HttpServletRequestWebContext(request);
        LimitFactory limitFactory = new LimitFactoryImpl(id, webContext);
        Long size = null;
        int pageSize = 15;
        size = osmNGuardManager.getsizeOsmNGuards();
        limit.setRowSelect(limitFactory.createRowSelect(pageSize, size.intValue()));
        int rowStart = limit.getRowSelect().getRowStart();
        List data = osmNGuardManager.getOsmNGuards(rowStart, pageSize, orderBy);
        tableFacade.setItems(data);
    }
}

package org.osmius.webapp.action;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeImpl;
import org.jmesa.limit.*;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.editor.HeaderEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.web.HttpServletRequestWebContext;
import org.jmesa.web.WebContext;
import org.joda.time.DateTime;
import org.osmius.Constants;
import org.osmius.model.OsmInterruption;
import org.osmius.service.OsmInterruptionManager;
import org.osmius.service.UtilsManager;
import org.osmius.webapp.util.OsmiusToolbar;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

public class InterruptionsController implements Controller {

    private MessageSource messageSource = null;

    private OsmInterruptionManager osmInterruptionManager;

    private String id = "osmInterruptionsList";

    private UtilsManager utilsManager;

    public void setUtilsManager(UtilsManager utilsManager) {
        this.utilsManager = utilsManager;
    }

    /**
    * Sets the messge source
    *
    * @param messageSource The message source
    */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setOsmInterruptionManager(OsmInterruptionManager osmInterruptionManager) {
        this.osmInterruptionManager = osmInterruptionManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView model = new ModelAndView("/services/osmInterruptionsListAJAX");
        String checks = request.getParameter("checks");
        if (checks != null) {
            String[] splittedChecks = checks.split(";");
            osmInterruptionManager.deleteInterruptions(splittedChecks);
        }
        String interruption = request.getParameter("close");
        if (interruption != null && !interruption.equals("")) {
            osmInterruptionManager.closeInterruption(new Long(interruption));
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
                request.setAttribute(Constants.OSMINTERRUPTIONS_LIST, tmp);
            }
        }
        return model;
    }

    protected String render(final HttpServletRequest request, HttpServletResponse response) {
        Locale locale = request.getLocale();
        final MessageSourceAccessor text = new MessageSourceAccessor(messageSource, request.getLocale());
        TableFacade tableFacade = new TableFacadeImpl(id, request);
        Limit limit = tableFacade.getLimit();
        tableFacade.setColumnProperties("select", "idnInterruption", "desInterruption", "dtiIni", "dtiFini");
        setDataAndLimitVariables(tableFacade, request);
        tableFacade.autoFilterAndSort(false);
        Table table = tableFacade.getTable();
        Column select = table.getRow().getColumn("select");
        HtmlColumn htmlColumn = (HtmlColumn) select;
        htmlColumn.getHeaderRenderer().setHeaderEditor(new HeaderEditor() {

            public Object getValue() {
                return "<p align=\"center\"><input style=\"align:center\" type=\"checkbox\" name=\"allbox\" id=\"allbox\" onclick=\"checkAll(this.form)\"/></p>";
            }
        });
        ((HtmlColumn) select).setFilterable(false);
        ((HtmlColumn) select).setSortable(false);
        ((HtmlColumn) select).setWidth("15px");
        ((HtmlColumn) select).getCellRenderer().setStyle("height:20px;align:center;");
        Column idnInterruption = table.getRow().getColumn("idnInterruption");
        ((HtmlColumn) idnInterruption).setSortable(false);
        ((HtmlColumn) idnInterruption).setFilterable(false);
        ((HtmlColumn) idnInterruption).setWidth("15px");
        idnInterruption.setTitle(text.getMessage("close"));
        Column desInterruption = table.getRow().getColumn("desInterruption");
        desInterruption.setTitle(text.getMessage("osmInterruption.desInterruption"));
        ((HtmlColumn) desInterruption).setFilterable(false);
        ((HtmlColumn) desInterruption).setWidth("400px");
        Column dtiIni = table.getRow().getColumn("dtiIni");
        dtiIni.setTitle(text.getMessage("osmInterruption.dtiIni"));
        ((HtmlColumn) dtiIni).setFilterable(true);
        ((HtmlColumn) dtiIni).setWidth("160px");
        Column dtiFini = table.getRow().getColumn("dtiFini");
        dtiFini.setTitle(text.getMessage("osmInterruption.dtiFini"));
        ((HtmlColumn) dtiFini).setFilterable(true);
        ((HtmlColumn) dtiFini).setWidth("160px");
        if (limit.isExported()) {
            tableFacade.render();
            return null;
        } else {
            HtmlTable htmlTable = (HtmlTable) table;
            htmlTable.getTableRenderer().setWidth("100%");
            select.getCellRenderer().setCellEditor(new CellEditor() {

                public Object getValue(Object item, String property, int rowcount) {
                    Object value = new BasicCellEditor().getValue(item, "idnInterruption", rowcount);
                    Date now = new Date(utilsManager.getActualTimestamp().getTime());
                    Date fini = (Date) new BasicCellEditor().getValue(item, "dtiIni", rowcount);
                    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    StringBuffer strBuff = new StringBuffer();
                    if (now.getTime() < fini.getTime()) {
                        strBuff.append("<p align=\"center\"><input style=\"align:center;\" type=\"checkbox\" id=\"check_").append(rowcount).append("\" value=\"").append(value.toString()).append("_").append(formatter.format(fini)).append("\"/></p>");
                    } else {
                        strBuff.append("<p></p>");
                    }
                    return strBuff.toString();
                }
            });
            idnInterruption.getCellRenderer().setCellEditor(new CellEditor() {

                public Object getValue(Object item, String property, int rowcount) {
                    Object value = new BasicCellEditor().getValue(item, property, rowcount);
                    StringBuffer strBuff = new StringBuffer();
                    Map config = (Map) request.getSession().getServletContext().getAttribute(Constants.CONFIG);
                    String theme = (String) config.get(Constants.CSS_THEME);
                    Date ffini = (Date) new BasicCellEditor().getValue(item, "dtiFini", rowcount);
                    Date now = new Date(utilsManager.getActualTimestamp().getTime());
                    Date fini = (Date) new BasicCellEditor().getValue(item, "dtiIni", rowcount);
                    if (ffini == null && (now.getTime() < fini.getTime())) {
                        SecurityContext ctx = SecurityContextHolder.getContext();
                        Authentication auth = ctx.getAuthentication();
                        String role = ((UserDetails) auth.getPrincipal()).getAuthorities()[0].getAuthority();
                        if (role.equals("DEMO")) {
                            strBuff.append("<img title=\"").append(text.getMessage("close", request.getLocale())).append("\" onmouseover=\"this.style.cursor='pointer'\" src=\"").append(request.getContextPath()).append("/styles/themes/").append(theme).append("/images/minimize.gif\" onclick=\"javascript:alert(msgDemo)\"/>");
                        } else {
                            strBuff.append("<img title=\"").append(text.getMessage("close", request.getLocale())).append("\" onmouseover=\"this.style.cursor='pointer'\" src=\"").append(request.getContextPath()).append("/styles/themes/").append(theme).append("/images/minimize.gif\" onclick=\"javascript:closeInterruption(").append(value.toString()).append(")\"/>");
                        }
                    } else {
                        strBuff.append("<p></p>");
                    }
                    return strBuff.toString();
                }
            });
            desInterruption.getCellRenderer().setCellEditor(new CellEditor() {

                public Object getValue(Object item, String property, int rowcount) {
                    Object value = new BasicCellEditor().getValue(item, property, rowcount);
                    Object idnInterruption = new BasicCellEditor().getValue(item, "idnInterruption", rowcount);
                    StringBuffer strBuff = new StringBuffer();
                    if (value.toString().length() > (400 / 10)) {
                        strBuff.append("<a href=javascript:testInterruption('").append(idnInterruption);
                        strBuff.append("')>").append(value.toString().substring(0, 400 / 10)).append("...").append("</a>");
                    } else {
                        strBuff.append("<a href=javascript:testInterruption('").append(idnInterruption);
                        strBuff.append("')>").append(value).append("</a>");
                    }
                    return strBuff.toString();
                }
            });
            dtiIni.getCellRenderer().setCellEditor(new CellEditor() {

                public Object getValue(Object item, String property, int rowcount) {
                    Object value = new BasicCellEditor().getValue(item, property, rowcount);
                    HtmlBuilder html = new HtmlBuilder();
                    html.p().align("left").close().append(value.toString().substring(0, 10)).append("<span style=\"color: #CC0000\">|</span>").append(value.toString().subSequence(11, 19));
                    html.pEnd();
                    return html.toString();
                }
            });
            dtiFini.getCellRenderer().setCellEditor(new CellEditor() {

                public Object getValue(Object item, String property, int rowcount) {
                    Object value = new BasicCellEditor().getValue(item, property, rowcount);
                    HtmlBuilder html = new HtmlBuilder();
                    if (value != null) {
                        html.p().align("left").close().append(value.toString().substring(0, 10)).append("<span onclick style=\"color: #CC0000\">|</span>").append(value.toString().subSequence(11, 19));
                    } else {
                        html.p();
                    }
                    html.pEnd();
                    return html.toString();
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
            if (property.equals("desInterruption")) {
                orderByBuff.append(" desInterruption ").append(order).append(",");
            } else if (property.equals("dtiIni")) {
                orderByBuff.append(" dtiIni ").append(order).append(",");
            }
            if (property.equals("dtiFini")) {
                orderByBuff.append(" dtiFini ").append(order).append(",");
            }
        }
        String orderBy;
        if (orderByBuff.length() == 0) {
            orderBy = " dtiFini desc, dtiIni desc ";
        } else {
            orderBy = orderByBuff.toString().substring(0, orderByBuff.toString().length() - 1);
        }
        FilterSet filterSet = limit.getFilterSet();
        OsmInterruption object = new OsmInterruption();
        Collection<Filter> filters = filterSet.getFilters();
        for (Filter filter : filters) {
            String property = filter.getProperty();
            String value = filter.getValue();
            if (property.equals("dtiIni")) {
                DateTime dt = new DateTime(Integer.parseInt(value.substring(0, 4)), Integer.parseInt(value.substring(5, 7)), Integer.parseInt(value.substring(8, 10)), Integer.parseInt(value.substring(11, 13)), Integer.parseInt(value.substring(14, 16)), Integer.parseInt(value.substring(17, 19)), 0);
                object.setDtiIni(new java.sql.Date(dt.getMillis()));
            } else if (property.equals("dtiFini")) {
                DateTime dt = new DateTime(Integer.parseInt(value.substring(0, 4)), Integer.parseInt(value.substring(5, 7)), Integer.parseInt(value.substring(8, 10)), Integer.parseInt(value.substring(11, 13)), Integer.parseInt(value.substring(14, 16)), Integer.parseInt(value.substring(17, 19)), 0);
                object.setDtiIni(new java.sql.Date(dt.getMillis()));
            }
        }
        WebContext webContext = new HttpServletRequestWebContext(request);
        LimitFactory limitFactory = new LimitFactoryImpl(id, webContext);
        Long size = null;
        int pageSize = 15;
        size = osmInterruptionManager.getSizeInterruptions(object);
        limit.setRowSelect(limitFactory.createRowSelect(pageSize, size.intValue()));
        int rowStart = limit.getRowSelect().getRowStart();
        List data = osmInterruptionManager.getOsmInterruptions(object, rowStart, pageSize, orderBy);
        tableFacade.setItems(data);
    }
}

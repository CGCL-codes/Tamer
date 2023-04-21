package com.norteksoft.struts.web.ec;

import org.extremecomponents.table.core.PreferencesConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.DefaultToolbar;
import org.extremecomponents.table.view.html.BuilderUtils;
import org.extremecomponents.table.view.html.TableActions;
import org.extremecomponents.util.HtmlBuilder;

/**
 * @author Jeff Johnston
 */
public class MessagesToolbar extends DefaultToolbar {

    public MessagesToolbar(HtmlBuilder html, TableModel model) {
        super(html, model);
    }

    protected void columnRight(HtmlBuilder html, TableModel model) {
        html.td(2).align("right").close();
        html.table(2).border("0").close();
        html.tr(3).close();
        int page = model.getLimit().getPage();
        int totalPages = BuilderUtils.getTotalPages(model);
        int totalRows = model.getLimit().getTotalRows();
        String rowsDisplayed = model.getPreferences().getPreference(PreferencesConstants.TABLE_ROWS_DISPLAYED);
        String statusMessegeHtml = "<font class='fontClass'>记录总数</font><font class = 'numberClass'>" + totalRows + "</font><font class='fontClass'>条 共</font><font class = 'numberClass'>" + totalPages + "</font><font class='fontClass'>页 当前第</font><font class = 'numberClass'>" + page + "</font><font class='fontClass'>页 每页</font><font class = 'numberClass'>" + rowsDisplayed + "</font><font class='fontClass'>条</font>";
        String action_firstPage = (new TableActions(model)).getPageAction(1);
        String action_prevPage = (new TableActions(model)).getPageAction(page - 1);
        String action_nextPage = (new TableActions(model)).getPageAction(page + 1);
        String action_lastPage = (new TableActions(model)).getPageAction(totalPages);
        String enable_firstPage, enable_prevPage, enable_nextPage, enable_lastPage;
        if (BuilderUtils.isFirstPageEnabled(page)) enable_firstPage = "yes"; else enable_firstPage = "no";
        if (BuilderUtils.isPrevPageEnabled(page)) enable_prevPage = "yes"; else enable_prevPage = "no";
        if (BuilderUtils.isNextPageEnabled(page, totalPages)) enable_nextPage = "yes"; else enable_nextPage = "no";
        if (BuilderUtils.isLastPageEnabled(page, totalPages)) enable_lastPage = "yes"; else enable_lastPage = "no";
        BuilderUtils.isPrevPageEnabled(page);
        BuilderUtils.isNextPageEnabled(page, totalPages);
        BuilderUtils.isLastPageEnabled(page, totalPages);
        html.td(4).close();
        html.input("hidden").name("action_firstPage").value(action_firstPage).close();
        html.input("hidden").name("action_prevPage").value(action_prevPage).close();
        html.input("hidden").name("action_nextPage").value(action_nextPage).close();
        html.input("hidden").name("action_lastPage").value(action_lastPage).close();
        html.input("hidden").name("enable_firstPage").value(enable_firstPage).close();
        html.input("hidden").name("enable_prevPage").value(enable_prevPage).close();
        html.input("hidden").name("enable_nextPage").value(enable_nextPage).close();
        html.input("hidden").name("enable_lastPage").value(enable_lastPage).close();
        html.input("hidden").name("statusMessegeHtml").value(statusMessegeHtml).close();
        html.input("hidden").name("totalPages").value(totalPages + "").close();
        html.input("hidden").name("selectAll").value("0").close();
        html.tdEnd();
        html.trEnd(3);
        html.tableEnd(2);
        html.newline();
        html.tabs(2);
        html.tdEnd();
    }
}

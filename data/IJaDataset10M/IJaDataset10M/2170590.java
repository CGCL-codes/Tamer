package cn.sduo.app.pagination.jmesa;

import java.util.Collection;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.limit.Limit;
import org.jmesa.view.html.component.HtmlTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.sduo.app.pagination.TableFacadeUtils;
import cn.sduo.app.pagination.dto.PaginationCriteria;

/**
 * This is a common table rendering kit, which encapsulates the complexity of jmesa.
 * 
 * @author 80070535
 *
 */
public class TableFacadeRendererImpl implements TableFacadeRenderer {

    private static final Logger log = LoggerFactory.getLogger(TableFacadeRendererImpl.class);

    private Class<?> typeOfItem;

    private String tableId;

    private HttpServletRequest request;

    public TableFacadeRendererImpl(Class<?> typeOfItem, String tableId, HttpServletRequest request) {
        super();
        this.typeOfItem = typeOfItem;
        this.tableId = tableId;
        this.request = request;
    }

    public String executeCallback(TableRendererCallback<?> callback) {
        TableFacade tableFacade = TableFacadeFactory.createSpringTableFacade(tableId, request);
        tableFacade.setStateAttr("restore");
        Limit limit = tableFacade.getLimit();
        PaginationCriteria paginationCriteria = new PaginationCriteria();
        paginationCriteria.setPageFilter(TableFacadeUtils.getPageFilter(limit, typeOfItem));
        paginationCriteria.setPageSorter(TableFacadeUtils.getPageSorter(limit));
        callback.customPaginationCriteria(paginationCriteria);
        if (!limit.isComplete()) {
            int totalRows = callback.countItems(paginationCriteria);
            tableFacade.setTotalRows(totalRows);
            log.info("total " + totalRows + " records found!");
        }
        paginationCriteria.setRowEnd(limit.getRowSelect().getRowEnd());
        paginationCriteria.setRowStart(limit.getRowSelect().getRowStart());
        Collection<?> items = callback.searchItems(paginationCriteria);
        if (items == null || items.isEmpty()) {
            log.info("empty result found!");
            items = Collections.EMPTY_LIST;
        }
        tableFacade.setItems(items);
        return render(tableFacade, callback);
    }

    private String render(TableFacade tableFacade, TableRendererCallback<?> callback) {
        try {
            return renderAsHTML(tableFacade, callback);
        } catch (Exception e) {
            log.error("Unexpected Exception in rendering Jmesa Table.", e);
            throw new RuntimeException(e);
        }
    }

    private String renderAsHTML(TableFacade tableFacade, TableRendererCallback<?> callback) throws Exception {
        String[] props = callback.getDisplayColumns();
        tableFacade.setColumnProperties(props);
        callback.customTableFacade(tableFacade);
        HtmlTable table = (HtmlTable) tableFacade.getTable();
        table.getTableRenderer().setWidth("100%");
        return tableFacade.render();
    }
}

package nuts.ext.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.ext.struts2.components.ListView;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see ListView
 */
public class ListViewTag extends AbstractUITag {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 7398742694175919413L;

    protected Object list;

    protected Object columns;

    protected String start;

    protected String limit;

    protected String total;

    protected String sort;

    protected String dir;

    protected String filters;

    protected String query;

    protected String fields;

    protected String action;

    protected String method;

    protected String onsubmit;

    protected String onreset;

    protected String options;

    protected String pager;

    protected String extra;

    protected Object link;

    protected Object toolbar;

    protected String singleSelect;

    protected String toggleSelect;

    protected String onrowclick;

    protected String enableValues;

    /**
	 * @param list the list to set
	 */
    public void setList(Object list) {
        this.list = list;
    }

    /**
	 * @param columns the columns to set
	 */
    public void setColumns(Object columns) {
        this.columns = columns;
    }

    /**
	 * @param start the start to set
	 */
    public void setStart(String start) {
        this.start = start;
    }

    /**
	 * @param limit the limit to set
	 */
    public void setLimit(String limit) {
        this.limit = limit;
    }

    /**
	 * @param total the total to set
	 */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
	 * @param sort the sort to set
	 */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
	 * @param dir the dir to set
	 */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
	 * @param filters the filters to set
	 */
    public void setFilters(String filters) {
        this.filters = filters;
    }

    /**
	 * @param query the query to set
	 */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
	 * @param fields the fields to set
	 */
    public void setFields(String fields) {
        this.fields = fields;
    }

    /**
	 * @param action the action to set
	 */
    public void setAction(String action) {
        this.action = action;
    }

    /**
	 * @param method the method to set
	 */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
	 * @param onsubmit the onsubmit to set
	 */
    public void setOnsubmit(String onsubmit) {
        this.onsubmit = onsubmit;
    }

    /**
	 * @param onreset the onreset to set
	 */
    public void setOnreset(String onreset) {
        this.onreset = onreset;
    }

    /**
	 * @param toolbar the toolbar to set
	 */
    public void setToolbar(Object toolbar) {
        this.toolbar = toolbar;
    }

    /**
	 * @param extra the extra to set
	 */
    public void setExtra(String extra) {
        this.toolbar = extra;
    }

    /**
	 * @param link the link to set
	 */
    public void setLink(Object link) {
        this.link = link;
    }

    /**
	 * @param singleSelect the singleSelect to set
	 */
    public void setSingleSelect(String singleSelect) {
        this.singleSelect = singleSelect;
    }

    /**
	 * @param toggleSelect the toggleSelect to set
	 */
    public void setToggleSelect(String toggleSelect) {
        this.toggleSelect = toggleSelect;
    }

    /**
	 * @param onrowclick the onrowclick to set
	 */
    public void setOnrowclick(String onrowclick) {
        this.onrowclick = onrowclick;
    }

    /**
	 * @param options the options to set
	 */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
	 * @param pager the pager to set
	 */
    public void setPager(String pager) {
        this.pager = pager;
    }

    /**
	 * @param enableValues the enableValues to set
	 */
    public void setEnableValues(String enableValues) {
        this.enableValues = enableValues;
    }

    /**
     * @see org.apache.struts2.views.jsp.ComponentTagSupport#getBean(com.opensymphony.xwork2.util.ValueStack,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new ListView(stack, req, res);
    }

    /**
     * @see org.apache.struts2.views.jsp.ui.AbstractUITag#populateParams()
     */
    protected void populateParams() {
        super.populateParams();
        ListView lv = ((ListView) component);
        lv.setList(list);
        lv.setColumns(columns);
        lv.setStart(start);
        lv.setLimit(limit);
        lv.setTotal(total);
        lv.setSort(sort);
        lv.setDir(dir);
        lv.setFilters(filters);
        lv.setQuery(query);
        lv.setFields(fields);
        lv.setAction(action);
        lv.setMethod(method);
        lv.setOnsubmit(onsubmit);
        lv.setOnreset(onreset);
        lv.setOptions(options);
        lv.setPager(pager);
        lv.setExtra(extra);
        lv.setLink(link);
        lv.setToolbar(toolbar);
        lv.setSingleSelect(singleSelect);
        lv.setToggleSelect(toggleSelect);
        lv.setOnrowclick(onrowclick);
        lv.setEnableValues(enableValues);
    }
}

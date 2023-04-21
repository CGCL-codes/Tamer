package org.opencms.workplace.tools.searchindex;

import org.opencms.configuration.CmsSearchConfiguration;
import org.opencms.i18n.CmsMessageContainer;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsIllegalStateException;
import org.opencms.main.OpenCms;
import org.opencms.search.CmsSearchIndexSource;
import org.opencms.search.CmsSearchManager;
import org.opencms.workplace.list.CmsListColumnAlignEnum;
import org.opencms.workplace.list.CmsListColumnDefinition;
import org.opencms.workplace.list.CmsListDirectAction;
import org.opencms.workplace.list.CmsListItem;
import org.opencms.workplace.list.CmsListMetadata;
import org.opencms.workplace.list.CmsListOrderEnum;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * A list that displays the search resources of a request parameter given 
 * <code>{@link org.opencms.search.CmsSearchIndexSource}</code> ("indexsource"). 
 * 
 * This list is no stand-alone page but has to be embedded in another dialog 
 * (see <code> {@link org.opencms.workplace.tools.searchindex.A_CmsEmbeddedListDialog}</code>. <p>
 * 
 * @author Achim Westermann 
 * 
 * @version $Revision: 1.4 $
 * 
 * @since 6.0.0
 */
public class CmsSearchResourcesList extends A_CmsEmbeddedListDialog {

    /** list action dummy id constant. */
    public static final String LIST_ACTION_NONE = "an";

    /** list column id constant. */
    public static final String LIST_COLUMN_ICON = "ci";

    /** list column id constant. */
    public static final String LIST_COLUMN_PATH = "cp";

    /** list id constant. */
    public static final String LIST_ID = "lssr";

    /** Stores the value of the request parameter for the search index source name. */
    private String m_paramIndexsource;

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsSearchResourcesList(CmsJspActionElement jsp) {
        this(jsp, LIST_ID, Messages.get().container(Messages.GUI_LIST_SEARCHRESOURCES_NAME_0));
    }

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     * @param listId the id of the list
     * @param listName the list name
     */
    public CmsSearchResourcesList(CmsJspActionElement jsp, String listId, CmsMessageContainer listName) {
        this(jsp, listId, listName, LIST_COLUMN_PATH, CmsListOrderEnum.ORDER_ASCENDING, null);
    }

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     * @param listId the id of the displayed list
     * @param listName the name of the list
     * @param sortedColId the a priory sorted column
     * @param sortOrder the order of the sorted column
     * @param searchableColId the column to search into
     */
    public CmsSearchResourcesList(CmsJspActionElement jsp, String listId, CmsMessageContainer listName, String sortedColId, CmsListOrderEnum sortOrder, String searchableColId) {
        super(jsp, listId, listName, sortedColId, sortOrder, searchableColId);
    }

    /**
     * Public constructor.<p>
     * 
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsSearchResourcesList(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListMultiActions()
     */
    public void executeListMultiActions() {
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListSingleActions()
     */
    public void executeListSingleActions() {
    }

    /**
     * Returns the request parameter "indexsource".<p>
     *  
     * @return the request parameter "indexsource"
     */
    public String getParamIndexsource() {
        return m_paramIndexsource;
    }

    /**
     * Sets the request parameter "indexsource". <p>
     * 
     * Method intended for workplace-properietary automatic filling of 
     * request parameter values to dialogs, not for manual invocation. <p>
     *  
     * @param indexsource the request parameter "indexsource" to set 
     */
    public void setParamIndexsource(String indexsource) {
        m_paramIndexsource = indexsource;
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#fillDetails(java.lang.String)
     */
    protected void fillDetails(String detailId) {
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#getListItems()
     */
    protected List getListItems() {
        List result = new ArrayList();
        List resources = resources();
        Iterator itResources = resources.iterator();
        String path;
        while (itResources.hasNext()) {
            path = (String) itResources.next();
            CmsListItem item = getList().newItem(path);
            item.set(LIST_COLUMN_PATH, path);
            result.add(item);
        }
        return result;
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initMessages()
     */
    protected void initMessages() {
        addMessages(Messages.get().getBundleName());
        super.initMessages();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setColumns(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setColumns(CmsListMetadata metadata) {
        CmsListColumnDefinition dummyCol = new CmsListColumnDefinition(LIST_COLUMN_ICON);
        dummyCol.setName(Messages.get().container(Messages.GUI_LIST_RESOURCES_NAME_0));
        dummyCol.setHelpText(Messages.get().container(Messages.GUI_LIST_RESOURCES_NAME_HELP_0));
        dummyCol.setWidth("20");
        dummyCol.setAlign(CmsListColumnAlignEnum.ALIGN_LEFT);
        dummyCol.setSorteable(false);
        CmsListDirectAction dummyAction = new CmsListDirectAction(LIST_ACTION_NONE);
        dummyAction.setName(Messages.get().container(Messages.GUI_LIST_INDEXSOURCE_COL_ICON_NAME_0));
        dummyAction.setHelpText(Messages.get().container(Messages.GUI_LIST_INDEXSOURCE_COL_ICON_NAME_HELP_0));
        dummyAction.setIconPath(CmsSearchIndexSourceControlList.ICON_FOLDER);
        dummyCol.addDirectAction(dummyAction);
        metadata.addColumn(dummyCol);
        CmsListColumnDefinition pathCol = new CmsListColumnDefinition(LIST_COLUMN_PATH);
        pathCol.setAlign(CmsListColumnAlignEnum.ALIGN_LEFT);
        pathCol.setName(Messages.get().container(Messages.GUI_LIST_SEARCHINDEX_COL_NAME_0));
        pathCol.setWidth("100%");
        metadata.addColumn(pathCol);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setIndependentActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setIndependentActions(CmsListMetadata metadata) {
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setMultiActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setMultiActions(CmsListMetadata metadata) {
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#validateParamaters()
     */
    protected void validateParamaters() throws Exception {
        if (getParamIndexsource() == null) {
            throw new CmsIllegalStateException(Messages.get().container(Messages.ERR_SEARCHINDEX_EDIT_MISSING_PARAM_1, A_CmsEditIndexSourceDialog.PARAM_INDEXSOURCE));
        }
    }

    /**
     * Writes the updated search configuration back to the XML 
     * configuration file and refreshes the complete list.<p>
     * 
     * @param refresh if true, the list items are refreshed
     */
    protected void writeConfiguration(boolean refresh) {
        OpenCms.writeConfiguration(CmsSearchConfiguration.class);
        if (refresh) {
            refreshList();
        }
    }

    /**
     * Returns the configured resources of the current indexsource. 
     * 
     * @return the configured resources of the current indexsource
     */
    private List resources() {
        CmsSearchManager manager = OpenCms.getSearchManager();
        CmsSearchIndexSource indexsource = manager.getIndexSource(getParamIndexsource());
        List result = indexsource.getResourcesNames();
        return result;
    }
}

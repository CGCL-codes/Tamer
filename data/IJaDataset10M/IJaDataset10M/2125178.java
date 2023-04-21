package org.opencms.workplace.list;

import org.opencms.file.CmsResource;
import org.opencms.i18n.CmsMessageContainer;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.loader.CmsImageScaler;
import org.opencms.main.CmsException;
import org.opencms.main.CmsRuntimeException;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsWorkplace;
import org.opencms.workplace.explorer.CmsExplorerTypeSettings;
import org.opencms.workplace.explorer.Messages;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Super class for all dialogs needed to display a list of resource types.<p>
 * 
 * @author Peter Bonrad
 * 
 * @version $Revision: 1.6 $ 
 * 
 * @since 6.7.2
 */
public abstract class A_CmsListResourceTypeDialog extends A_CmsListDialog {

    /** List independent action id constant. */
    public static final String LIST_ACTION_SEL = "rs";

    /** List column id constant. */
    public static final String LIST_COLUMN_ICON = "nrci";

    /** List column id constant. */
    public static final String LIST_COLUMN_NAME = "nrcn";

    /** List column id constant. */
    public static final String LIST_COLUMN_SELECT = "nrcs";

    /** List detail description info. */
    public static final String LIST_DETAIL_DESCRIPTION = "dd";

    /** List id constant. */
    public static final String LIST_ID = "nrt";

    /** Request parameter name for the index page resource type. */
    public static final String PARAM_SELECTED_TYPE = "selectedtype";

    /** Item comparator to ensure that special types go first. */
    private static final I_CmsListItemComparator LIST_ITEM_COMPARATOR = new I_CmsListItemComparator() {

        /**
         * @see org.opencms.workplace.list.I_CmsListItemComparator#getComparator(java.lang.String, java.util.Locale)
         */
        public Comparator getComparator(final String columnId, final Locale locale) {
            return new Comparator() {

                /**
                 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
                 */
                public int compare(Object o1, Object o2) {
                    if ((o1 == o2) || !(o1 instanceof CmsListItem) || !(o2 instanceof CmsListItem)) {
                        return 0;
                    }
                    CmsListItem li1 = (CmsListItem) o1;
                    CmsListItem li2 = (CmsListItem) o2;
                    String id1 = li1.getId();
                    String id2 = li2.getId();
                    CmsExplorerTypeSettings set1 = OpenCms.getWorkplaceManager().getExplorerTypeSetting(id1);
                    CmsExplorerTypeSettings set2 = OpenCms.getWorkplaceManager().getExplorerTypeSetting(id2);
                    if (set1 == null) {
                        return -1;
                    } else if (set2 == null) {
                        return 1;
                    }
                    return set1.compareTo(set2);
                }
            };
        }
    };

    /** Parameter which contains the selected resource type. */
    private String m_paramSelectedType;

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public A_CmsListResourceTypeDialog(CmsJspActionElement jsp) {
        this(jsp, LIST_ID, Messages.get().container(Messages.GUI_NEWRESOURCE_LIST_SELECT_NAME_0), null, CmsListOrderEnum.ORDER_ASCENDING, null);
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
    public A_CmsListResourceTypeDialog(CmsJspActionElement jsp, String listId, CmsMessageContainer listName, String sortedColId, CmsListOrderEnum sortOrder, String searchableColId) {
        super(jsp, listId, listName, sortedColId, sortOrder, searchableColId);
        setParamStyle("");
        getList().setMaxItemsPerPage(Integer.MAX_VALUE);
        getList().getMetadata().getIndependentAction(CmsListPrintIAction.LIST_ACTION_ID).setVisible(false);
        getList().setBoxed(false);
        getList().setShowTitle(false);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public A_CmsListResourceTypeDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#actionDialog()
     */
    public void actionDialog() throws JspException, ServletException, IOException {
        I_CmsListDirectAction action = getList().getMetadata().getColumnDefinition(LIST_COLUMN_SELECT).getDirectAction(LIST_ACTION_SEL);
        if (action != null) {
            String selected = getParamSelectedType();
            if (selected != null) {
                ((CmsListItemSelectionAction) action).setSelectedItemId(selected);
            }
        }
        super.actionDialog();
    }

    /**
     * Builds a default button row with a continue and cancel button.<p>
     * 
     * Override this to have special buttons for your dialog.<p>
     * 
     * @return the button row 
     */
    public String dialogButtons() {
        return dialogButtons(new int[] { BUTTON_CONTINUE, BUTTON_CANCEL }, new String[] { " onclick=\"submitAction('" + DIALOG_CONTINUE + "', form, '" + getListId() + "-form');\" id=\"nextButton\"", " onclick=\"submitAction('" + DIALOG_CANCEL + "', form, '" + getListId() + "-form');\"" });
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListMultiActions()
     */
    public void executeListMultiActions() throws CmsRuntimeException {
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListSingleActions()
     */
    public void executeListSingleActions() throws CmsRuntimeException {
    }

    /**
     * Returns the paramSelectedType.<p>
     *
     * @return the paramSelectedType
     */
    public String getParamSelectedType() {
        return m_paramSelectedType;
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#paramsAsHidden()
     */
    public String paramsAsHidden() {
        List excludes = new ArrayList();
        return paramsAsHidden(excludes);
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#paramsAsHidden(java.util.Collection)
     */
    public String paramsAsHidden(Collection excludes) {
        excludes.add(PARAM_SELECTED_TYPE);
        excludes.add(PARAM_SORT_COL);
        excludes.add(LIST_INDEPENDENT_ACTION);
        return super.paramsAsHidden(excludes);
    }

    /**
     * Sets the paramSelectedType.<p>
     *
     * @param paramSelectedType the paramSelectedType to set
     */
    public void setParamSelectedType(String paramSelectedType) {
        m_paramSelectedType = paramSelectedType;
    }

    /**
     * Returns the html code to add directly before the list inside the form element.<p>
     * 
     * @return the html code to add directly before the list inside the form element
     */
    protected String customHtmlBeforeList() {
        return "";
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#customHtmlEnd()
     */
    protected String customHtmlEnd() {
        StringBuffer result = new StringBuffer(256);
        result.append(dialogSpacer());
        result.append(dialogButtons());
        result.append("</form>\n");
        return result.toString();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#customHtmlStart()
     */
    protected String customHtmlStart() {
        StringBuffer result = new StringBuffer(256);
        result.append("<script type='text/javascript' src='");
        result.append(CmsWorkplace.getSkinUri());
        result.append("admin/javascript/general.js'></script>\n");
        result.append("<script type='text/javascript' src='");
        result.append(CmsWorkplace.getSkinUri());
        result.append("editors/xmlcontent/help.js'></script>\n");
        result.append("<form name='");
        result.append(getList().getId());
        result.append("-form' action='");
        result.append(getDialogRealUri());
        result.append("' method='post' class='nomargin'");
        if (getList().getMetadata().isSearchable()) {
            result.append(" onsubmit=\"listSearchAction('");
            result.append(getList().getId());
            result.append("', '");
            result.append(getList().getMetadata().getSearchAction().getId());
            result.append("', '");
            result.append(getList().getMetadata().getSearchAction().getConfirmationMessage().key(getLocale()));
            result.append("');\"");
        }
        result.append(">\n");
        result.append(paramsAsHidden());
        result.append("\n");
        result.append("<input type=\"hidden\" name=\"" + PARAM_FRAMENAME + "\" value=\"\">\n");
        result.append("<input type=\"hidden\" name=\"" + PARAM_LIST_ACTION + "\" value=\"\">\n");
        result.append("<input type=\"hidden\" name=\"" + PARAM_SORT_COL + "\" value=\"\">\n");
        return result.toString();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#defaultActionHtmlContent()
     */
    protected String defaultActionHtmlContent() {
        StringBuffer result = new StringBuffer(2048);
        result.append("<!-- start before list -->\n");
        result.append(customHtmlBeforeList());
        result.append("<!-- end before list -->\n");
        result.append(dialogBlockStart(key(getList().getName().getKey())));
        result.append(dialogWhiteBoxStart());
        getList().setWp(this);
        result.append(getList().listHtml());
        result.append(dialogWhiteBoxEnd());
        result.append(dialogBlockEnd());
        return result.toString();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#fillDetails(java.lang.String)
     */
    protected void fillDetails(String detailId) {
        List types = getList().getAllContent();
        Iterator iter = types.iterator();
        while (iter.hasNext()) {
            CmsListItem item = (CmsListItem) iter.next();
            String resType = item.getId();
            StringBuffer description = new StringBuffer();
            if (detailId.equals(LIST_DETAIL_DESCRIPTION)) {
                try {
                    try {
                        int resTypeId = Integer.parseInt(resType);
                        resType = OpenCms.getResourceManager().getResourceType(resTypeId).getTypeName();
                    } catch (NumberFormatException e) {
                    }
                    CmsExplorerTypeSettings set = OpenCms.getWorkplaceManager().getExplorerTypeSetting(resType);
                    if (set != null) {
                        String imgSrc = set.getDescriptionImage();
                        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(imgSrc)) {
                            try {
                                CmsResource res = getCms().readResource(imgSrc);
                                CmsImageScaler origImage = new CmsImageScaler(getCms(), res);
                                CmsImageScaler scaler = new CmsImageScaler();
                                scaler.setWidth(60);
                                scaler.setHeight(60);
                                CmsImageScaler resultScaler = origImage.getDownScaler(scaler);
                                if (resultScaler == null) {
                                    resultScaler = origImage;
                                }
                                Map attrs = new HashMap();
                                attrs.put("align", "left");
                                attrs.put("vspace", "5");
                                attrs.put("hspace", "5");
                                description.append(getJsp().img(imgSrc, resultScaler, attrs));
                            } catch (CmsException ex) {
                            }
                        }
                        description.append(key(set.getInfo()));
                    }
                } catch (Exception e) {
                }
            } else {
                continue;
            }
            item.set(detailId, description);
        }
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setColumns(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setColumns(CmsListMetadata metadata) {
        CmsListColumnDefinition radioSelCol = new CmsListColumnDefinition(LIST_COLUMN_SELECT);
        radioSelCol.setName(Messages.get().container(Messages.GUI_NEWRESOURCE_LIST_COLS_SELECT_0));
        radioSelCol.setWidth("20");
        radioSelCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        radioSelCol.setSorteable(false);
        CmsListItemSelectionCustomAction selAction = new CmsListItemSelectionCustomAction(LIST_ACTION_SEL, PARAM_SELECTED_TYPE);
        selAction.setName(Messages.get().container(Messages.GUI_NEWRESOURCE_LIST_SELECT_NAME_0));
        selAction.setEnabled(true);
        radioSelCol.addDirectAction(selAction);
        metadata.addColumn(radioSelCol);
        CmsListColumnDefinition iconCol = new CmsListColumnDefinition(LIST_COLUMN_ICON);
        iconCol.setName(Messages.get().container(Messages.GUI_NEWRESOURCE_LIST_COLS_ICON_0));
        iconCol.setWidth("20");
        iconCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        iconCol.setSorteable(true);
        iconCol.setListItemComparator(LIST_ITEM_COMPARATOR);
        metadata.addColumn(iconCol);
        CmsListColumnDefinition nameCol = new CmsListColumnDefinition(LIST_COLUMN_NAME);
        nameCol.setName(Messages.get().container(Messages.GUI_NEWRESOURCE_LIST_COLS_NAME_0));
        metadata.addColumn(nameCol);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setIndependentActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setIndependentActions(CmsListMetadata metadata) {
        CmsListItemDetails resourceTypeDescription = new CmsListItemDetails(LIST_DETAIL_DESCRIPTION);
        resourceTypeDescription.setAtColumn(LIST_COLUMN_NAME);
        resourceTypeDescription.setVisible(false);
        resourceTypeDescription.setShowActionName(Messages.get().container(Messages.GUI_NEWRESOURCE_DETAIL_SHOW_DESCRIPTION_NAME_0));
        resourceTypeDescription.setShowActionHelpText(Messages.get().container(Messages.GUI_NEWRESOURCE_DETAIL_SHOW_DESCRIPTION_HELP_0));
        resourceTypeDescription.setHideActionName(Messages.get().container(Messages.GUI_NEWRESOURCE_DETAIL_HIDE_DESCRIPTION_NAME_0));
        resourceTypeDescription.setHideActionHelpText(Messages.get().container(Messages.GUI_NEWRESOURCE_DETAIL_HIDE_DESCRIPTION_HELP_0));
        metadata.addItemDetails(resourceTypeDescription);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setMultiActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setMultiActions(CmsListMetadata metadata) {
    }
}

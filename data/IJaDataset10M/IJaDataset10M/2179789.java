package org.opencms.workplace.tools.searchindex;

import org.opencms.i18n.CmsMessageContainer;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.workplace.list.A_CmsListDialog;
import org.opencms.workplace.list.CmsListOrderEnum;

/**
 * A list dialog that may be embedded in 
 * the output of other <code>{@link org.opencms.workplace.CmsDialog}</code> instances.<p>
 * 
 * With std. <code>{@link org.opencms.workplace.list.A_CmsListDialog}</code> this attempt will 
 * result in double gray headers in the workplace. <p>
 * 
 * <h4>Howto</h4>
 * 
 * <h5>1. Include content in JSP</h5> 
 * <pre>
 <% 
 CmsJspActionElement actionElement = new CmsJspActionElement(pageContext, request, response);
 CmsWidgetDialog wpWidget = new &lt;TYPE&gt;(actionElement);

 // perform the widget actions (write later)
 wpWidget.displayDialog(true);
 A_CmsEmbeddedListDialog wpList = new &lt;TYPE&gt;(actionElement);
 
 // perform the list actions (write later)
 wpList.displayDialog(true);
 // write the content of widget dialog
 wpWidget.writeDialog();
 // write the content of list dialog
 wpList.writeDialog();
 %>
 * </pre>
 * 
 * <h5>2. Include in code of other CmsDialog</h5>
 <pre>
 protected String createDialogHtml(String dialog) {

 StringBuffer result = new StringBuffer(1024);

 result.append(createWidgetTableStart());
 // do your dialog output here....
 ...
 result.append(createWidgetTableEnd());
 // create the list : 
 A_CmsEmbeddedListDialog wpList = new &lt;TYPE&gt;(getJsp());
 wpList.writeDialog();
 </pre>
 * 
 * 
 * @author Achim Westermann
 * 
 * @version $Revision: 1.5 $
 * 
 * @since 6.0.0
 */
public abstract class A_CmsEmbeddedListDialog extends A_CmsListDialog {

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
    public A_CmsEmbeddedListDialog(CmsJspActionElement jsp, String listId, CmsMessageContainer listName, String sortedColId, CmsListOrderEnum sortOrder, String searchableColId) {
        super(jsp, listId, listName, sortedColId, sortOrder, searchableColId);
    }

    /**
     * Overrides the implementation to skip generation of gray header. <p>
     * 
     * @see org.opencms.workplace.list.A_CmsListDialog#defaultActionHtmlStart()
     */
    public String defaultActionHtmlStart() {
        return new StringBuffer(getList().listJs()).append(dialogContentStart(getParamTitle())).toString();
    }
}

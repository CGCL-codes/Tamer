package org.sourceforge.vlibrary.user.actions;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.sourceforge.vlibrary.user.domain.Book;
import org.sourceforge.vlibrary.user.domain.Reader;
import org.sourceforge.vlibrary.user.forms.BookForm;

/**
 * Action used to view (read only) boooks
 * @version $Revision$ $Date$
 */
public class ViewBookAction extends LibraryAction {

    /** log4j Logger */
    private static Logger logger = Logger.getLogger(ViewBookAction.class.getName());

    /**
     * Sets up Book view page
     *
     * Loads all book data
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @param messages message resources
     *
     * @exception Exception
     */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, MessageResources messages) throws Exception {
        BookForm frm = (BookForm) form;
        try {
            Book bk = new Book();
            bk.setId(frm.getId());
            bk = libraryManager.retrieveBook(bk.getId());
            PropertyUtils.copyProperties(frm, bk);
            frm.setAuthors(libraryManager.getAuthors(bk));
            frm.setSubjects(libraryManager.getSubjects(bk));
            Reader rd = libraryManager.retrieveReader(bk.getOwner());
            frm.setOwnerPhone(rd.getDeskPhone());
            rd = null;
        } catch (Throwable t) {
            String errString = messages.getMessage("error.book.retrieve");
            logger.error(messages.getMessage(errString, t));
        }
        return standardForward(mapping, request, new ArrayList());
    }
}

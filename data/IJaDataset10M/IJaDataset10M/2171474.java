package maldade.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import maldade.persistence.dao.ItemDAO;
import maldade.persistence.exception.TransactionException;
import maldade.persistence.model.Item;
import maldade.persistence.service.PersistenceService;
import maldade.persistence.service.PersistenceServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditAction extends AbstractMappingDispatchAction {

    private static final String EDITED_ITEM = "item";

    /**
	 * Edit page action.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws TransactionException
	 * @throws NumberFormatException
	 */
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, TransactionException {
        String id = request.getParameter("idItem");
        ItemDAO itemDAO = new ItemDAO();
        Item item = new Item();
        PersistenceService persistenceService = PersistenceServiceFactory.getPersistenceService();
        persistenceService.beginTransaction();
        if (!id.isEmpty()) item = itemDAO.load(Integer.valueOf(id));
        item.setName(request.getParameter("name"));
        item.setShortDesc(request.getParameter("shortDesc"));
        item.setDescr(request.getParameter("descr"));
        item.setHttp(request.getParameter("http"));
        item.setDescr(request.getParameter("descr"));
        itemDAO.saveOrUpdate(item);
        persistenceService.commit();
        request.setAttribute(EDITED_ITEM, item);
        return mapping.findForward("edit");
    }
}

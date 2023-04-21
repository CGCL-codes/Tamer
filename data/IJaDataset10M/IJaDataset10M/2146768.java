package ro.gateway.aida.fnd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ro.gateway.aida.admin.PropertyList;
import ro.gateway.aida.admin.PropertyListDB;
import ro.gateway.aida.db.PersistenceManager;
import ro.gateway.aida.db.PersistenceToken;
import ro.gateway.aida.obj.Activity;
import ro.gateway.aida.obj.OrganizationInv;
import ro.gateway.aida.obj.OrganizationRole;
import ro.gateway.aida.obj.Organization;
import ro.gateway.aida.obj.db.CurrencyDB;
import ro.gateway.aida.obj.db.PersonDB;
import ro.gateway.aida.servlet.EditActivityServlet;
import ro.gateway.aida.utils.HttpUtils;

/**
 * <p>Title: Romanian AIDA</p> <p>Description: </p> <p>Copyright: Copyright (comparator) 2003</p> <p>Company:
 * ro-gateway</p>
 *
 * @author Mihai Popoaei, mihai_popoaei@yahoo.com, smike@intellisource.ro
 * @version 1.0-* @version $Id: FundingEditorAction.java,v 1.1 2004/10/24 23:37:14 mihaipostelnicu Exp $
 */
public class FundingEditorAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm _form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PersistenceToken token = PersistenceManager.tokenLookup(this.getServlet().getServletContext());
        FndPersMan persMan = FndPersMan.getManager(token);
        HttpSession session = request.getSession();
        FundingForm form = (FundingForm) _form;
        form.event = HttpUtils.getValidTrimedString(request, "event", "start");
        if ("start".equals(form.event)) {
            form.action = HttpUtils.getValidTrimedString(request, "action", null);
            try {
                PropertyList ltypeofassistance = PropertyListDB.getManager(token).getList("assistTypes");
                PropertyList ltermsofassistance = PropertyListDB.getManager(token).getList("assistterms");
                PropertyList lstatus = PropertyListDB.getManager(token).getList("fndstatus");
                if (ltypeofassistance != null) {
                    form.typesofassistance = ltypeofassistance.getItems();
                }
                if (ltermsofassistance != null) {
                    form.termsofassistance = ltermsofassistance.getItems();
                }
                if (lstatus != null) {
                    form.statuses = lstatus.getItems();
                }
                form.currencies = CurrencyDB.getManager(token).getAll();
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
            if ("edit".equals(form.action)) {
                form.reset_data();
                form.key = HttpUtils.getValidTrimedString(request, "key", null);
                form.item_idx = HttpUtils.getInt(request, "item_idx", -1);
                long org_id = HttpUtils.getLong(request, "org_id", -1);
                if (form.key == null || org_id == -1 || form.item_idx == -1) {
                    System.err.println("\t\tkey:" + form.key);
                    System.err.println("\t\torg_id:" + org_id);
                    System.err.println("\t\titem_idx:" + form.item_idx);
                    System.err.println("\t\t something's wrong");
                    return mapping.findForward("finnish");
                }
                Activity act_bean = activity_lookup(session, form);
                if (act_bean == null) {
                    System.err.println("act_bean not found");
                    return mapping.findForward("finnish");
                }
                FundingItem item = act_bean.getFndItem(org_id, form.item_idx);
                if (item == null) {
                    System.err.println("funding item not found");
                    return mapping.findForward("finnish");
                }
                form.setItem((FundingItem) item.clone());
                form.update_person_ids();
                prepare_form_data(request, form);
                return mapping.findForward("editor_jsp");
            } else if ("new".equals(form.action)) {
                System.out.println("new funding item!");
                form.reset_data();
                String key = HttpUtils.getValidTrimedString(request, "key", null);
                form.key = key;
                long org_id = HttpUtils.getLong(request, "org_id", -1);
                if (key == null || org_id == -1) {
                    System.out.println("\t\tkey || org_id!");
                    return mapping.findForward("finnish");
                }
                Activity act_bean = activity_lookup(session, form);
                if (act_bean == null) {
                    System.err.println("\t\tactivity bean null!");
                    return mapping.findForward("finnish");
                }
                OrganizationInv org_inv = act_bean.getOrgByLRIId(OrganizationRole.FINANCING_ORG_ROLE, org_id);
                form.getItem().setOrg(org_inv.getOrg());
                prepare_form_data(request, form);
                return mapping.findForward("editor_jsp");
            }
        }
        if ("adddisbursements".equals(form.event)) {
            form.populate(request, token);
            form.item.getDisbursements().add(new FundingTableItem("D"));
            return mapping.findForward("editor_jsp");
        } else if ("deldisbursements".equals(form.event)) {
            form.populate(request, token);
            int[] idxs = HttpUtils.getInts(request, "disbsidxs");
            ArrayList items = form.item.getDisbursements();
            if (idxs != null && items.size() > 0) {
                Arrays.sort(idxs);
                for (int i = idxs.length - 1; i >= 0; i--) {
                    if (idxs[i] >= 0 && idxs[i] < items.size()) items.remove(idxs[i]);
                }
            }
            return mapping.findForward("editor_jsp");
        } else if ("addcommitments".equals(form.event)) {
            form.populate(request, token);
            form.item.getCommitments().add(new FundingTableItem("C"));
            return mapping.findForward("editor_jsp");
        } else if ("delcommitments".equals(form.event)) {
            form.populate(request, token);
            int[] idxs = HttpUtils.getInts(request, "comsidxs");
            ArrayList items = form.item.getCommitments();
            if (idxs != null && items.size() > 0) {
                Arrays.sort(idxs);
                for (int i = idxs.length - 1; i >= 0; i--) {
                    if (idxs[i] >= 0 && idxs[i] < items.size()) items.remove(idxs[i]);
                }
            }
            return mapping.findForward("editor_jsp");
        }
        if ("save".equals(form.event)) {
            Activity act_bean = FundingEditorAction.activity_lookup(session, form);
            form.populate(request, token);
            if (form.item_idx >= 0) {
                act_bean.updateFndItem(form.item, form.item_idx);
            } else {
                act_bean.addFndItem(form.item);
            }
            return mapping.findForward("finnish");
        }
        prepare_form_data(request, form);
        return mapping.findForward("finnish");
    }

    static Activity activity_lookup(HttpSession session, FundingForm form) {
        Hashtable ed_ctxs = (Hashtable) session.getAttribute(EditActivityServlet.ED_CTXS);
        if (ed_ctxs == null) {
            System.err.println("\t\tactivity bean ctxs!");
            return null;
        }
        Hashtable ed_ctx = (Hashtable) ed_ctxs.get(form.key);
        Activity act_bean = (Activity) ed_ctx.get(EditActivityServlet.PNAME_BEAN);
        return act_bean;
    }

    public void prepare_form_data(HttpServletRequest request, FundingForm form) {
        ServletContext application = this.getServlet().getServletContext();
        PersistenceToken token = PersistenceManager.tokenLookup(application);
        PersonDB acc = PersonDB.getManager(token);
        request.setAttribute("organization", form.getItem().getOrg());
        try {
            Vector persons = acc.getByOrg(String.valueOf(form.getOrgId()));
            if (persons != null && persons.size() > 0) {
                request.setAttribute("persons", persons);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

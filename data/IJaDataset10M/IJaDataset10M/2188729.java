package ro.gateway.aida.fnd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ro.gateway.aida.srv.EditorServlet;
import ro.gateway.aida.utils.HttpUtils;
import ro.gateway.aida.utils.Utils;

/**
 * <p>Title: Romanian AIDA</p>
 * <p>Description: :D application</p>
 * <p>Copyright: Copyright (comparator) 2003</p>
 * <p>Company: Romania Development Gateway </p>
 * @author Mihai Popoaei, mihai_popoaei@yahoo.com, smike@intellisource.ro
 * @version 1.0-* @version $Id: FundingYearlyEditorServlet.java,v 1.1 2004/10/24 23:37:14 mihaipostelnicu Exp $
 */
public class FundingYearlyEditorServlet extends EditorServlet {

    protected void other_action_performed(HttpServletRequest request, HttpServletResponse response, String action) throws IOException, ServletException {
    }

    protected void start_editing(ServletContext application, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.start_editing(application, request, response);
        HttpSession session = request.getSession();
        Hashtable bean = (Hashtable) request.getAttribute(PNAME_BEAN);
        String key = (String) bean.get(PNAME_KEY);
        String action = (String) bean.get(PNAME_ACTION);
        String box = HttpUtils.getValidTrimedString(request, "box", null);
        if (box == null) {
            return;
        }
        Object obox = session.getAttribute(box);
        if ((obox == null) || (!(obox instanceof Hashtable))) {
            return;
        }
        bean.put(PNAME_BOX, ((Hashtable) obox).get(PNAME_BOX_LIST));
        if (PV_ACTION_NEW.equals(action)) {
            FundingYearlyItem item = new FundingYearlyItem();
            bean.put(PNAME_BEAN, item);
        } else if (PV_ACTION_EDIT.equals(action)) {
            ArrayList items = (ArrayList) ((Hashtable) obox).get(PNAME_BOX_LIST);
            int index = HttpUtils.getInt(request, "index", -1);
            if (index == -1) {
                cancel(request, response);
                return;
            }
            FundingYearlyItem item = (FundingYearlyItem) items.get(index);
            bean.put(PNAME_BEAN, item);
        } else {
            cancel(request, response);
        }
    }

    protected void cancel(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String key = (String) request.getAttribute(PNAME_KEY);
        Hashtable beans = (Hashtable) request.getSession().getAttribute(getPNAME_BEANS());
        Hashtable bean = (Hashtable) request.getAttribute(PNAME_BEAN);
        if ((key == null) || (bean == null)) {
            response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "bad[::] request");
            return;
        }
        beans.remove(key);
        request.setAttribute(PNAME_SCREEN, PV_SCREEN_CANCEL);
        request.getRequestDispatcher(JSP_PAGE).forward(request, response);
        return;
    }

    protected void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute(PNAME_SCREEN, PV_SCR_EDIT);
        request.getRequestDispatcher(JSP_PAGE).forward(request, response);
    }

    protected void save(ServletContext application, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    protected void confirm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Hashtable bean = (Hashtable) request.getAttribute(PNAME_BEAN);
        FundingYearlyItem item = (FundingYearlyItem) bean.get(PNAME_BEAN);
        int z_ap = Utils.getInt(request, "zsdate", -1);
        int l_ap = Utils.getInt(request, "lsdate", -1);
        int a_ap = Utils.getInt(request, "asdate", -1);
        Calendar cal = Calendar.getInstance();
        if ((z_ap != -1) && (l_ap != -1) && (a_ap != -1)) {
            cal.set(a_ap, l_ap, z_ap, 0, 0, 0);
            item.setStart_date(cal.getTimeInMillis());
        } else {
            item.setStart_date(-1);
        }
        item.setAmount(HttpUtils.getLong(request, "amount", 0));
        item.setCurrency(HttpUtils.getValidTrimedString(request, "currency", null));
        String key = (String) request.getAttribute(PNAME_KEY);
        if (!item.isValid()) {
            response.sendRedirect("efndyearly?err=true&action=edit&key=" + key);
            return;
        }
        if (PV_ACTION_NEW.equals(bean.get(PNAME_ACTION))) {
            ArrayList box = (ArrayList) bean.get(PNAME_BOX);
            box.add(item);
        }
        Hashtable beans = (Hashtable) request.getSession().getAttribute(getPNAME_BEANS());
        beans.remove(key);
        request.setAttribute(PNAME_SCREEN, PV_SCREEN_SAVED);
        request.getRequestDispatcher(JSP_PAGE).forward(request, response);
        return;
    }

    public String getJSP_PAGE() {
        return JSP_PAGE;
    }

    public String getPNAME_BEANS() {
        return PNAME_BEANS;
    }

    private static final String JSP_PAGE = "/fnd/fnd_yearly_item_editor.jsp";

    public static final String PNAME_BEANS = "FND_YEARLY_EDITING_ITEMS";

    public static final String PNAME_FUNDING_ITEM = "FUNDING_YEARLY_ITEM";

    public static final String PNAME_ERRORS = "errors";

    public static final String PNAME_BOX = "theybox";

    public static final String PNAME_BOX_LIST = "box_items";

    public static final String PV_SCREEN_CANCEL = "SCR_CANCEL";

    public static final String PV_SCREEN_SAVED = "SCR_SAVED";
}

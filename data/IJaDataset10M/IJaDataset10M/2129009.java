package com.centraview.account.item;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class DisplayNewItemHandler extends org.apache.struts.action.Action {

    public static final String GLOBAL_FORWARD_failure = "failure";

    private static final String FORWARD_newitem = ".view.accounting.item_detail";

    private static String FORWARD_final = GLOBAL_FORWARD_failure;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        try {
            ItemForm itemForm = (ItemForm) form;
            if (request.getParameter("actionType") == null) {
                itemForm = ItemForm.clearForm(itemForm);
            }
            ItemHome itemHome = (ItemHome) CVUtility.getHomeObject("com.centraview.account.item.ItemHome", "Item");
            Item itemRemote = itemHome.create();
            itemRemote.setDataSource(dataSource);
            Vector itemTypeVec = itemRemote.getItemType();
            Vector glAcntVec = itemRemote.getGLAccountType();
            Vector taxClassVec = itemRemote.getTaxClassType();
            itemForm.setItemtypevec(itemTypeVec);
            itemForm.setGlaccountvec(glAcntVec);
            itemForm.setTaxclassvec(taxClassVec);
            request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.ITEM);
            request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.ADD);
            request.setAttribute("itemform", itemForm);
            FORWARD_final = FORWARD_newitem;
        } catch (Exception e) {
            System.out.println("[Exception][DisplayNewItemHandler.execute] Exception Thrown: " + e);
            e.printStackTrace();
            FORWARD_final = GLOBAL_FORWARD_failure;
        }
        return (mapping.findForward(FORWARD_final));
    }
}

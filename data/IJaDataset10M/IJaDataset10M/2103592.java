package com.centraview.account.item;

import java.text.DecimalFormat;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SaveNewItemHandler extends org.apache.struts.action.Action {

    public static final String GLOBAL_FORWARD_failure = "failure";

    private static final String FORWARD_save = ".view.accounting.item.save";

    private static final String FORWARD_savenew = ".view.accounting.item.savenew";

    private static final String FORWARD_saveclose = ".view.accounting.item.saveclose";

    private static final String FORWARD_cancel = "cancel";

    private static String FORWARD_final = GLOBAL_FORWARD_failure;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        try {
            String typeOfSave = "cancel";
            if (request.getParameter("typeofsave") != null) {
                typeOfSave = request.getParameter("typeofsave");
            }
            if (typeOfSave.equals("cancel")) {
                FORWARD_final = FORWARD_saveclose;
                request.setAttribute("body", "list");
                return (mapping.findForward(FORWARD_final));
            }
            ItemHome itemHome = (ItemHome) CVUtility.getHomeObject("com.centraview.account.item.ItemHome", "Item");
            Item itemRemote = itemHome.create();
            itemRemote.setDataSource(dataSource);
            ItemVO itemVO = new ItemVO();
            ItemForm itemForm = (ItemForm) form;
            itemVO.setSku(itemForm.getSku());
            itemVO.setItemName(itemForm.getItemname());
            if (itemForm.getItemtypeid() != null && itemForm.getItemtypeid().length() > 0) {
                itemVO.setItemTypeId(Integer.parseInt(itemForm.getItemtypeid()));
            }
            itemVO.setItemDesc(itemForm.getItemdesc());
            if (itemForm.getGlaccountid() != null) {
                itemVO.setGlAccountId(Integer.parseInt(itemForm.getGlaccountid()));
            }
            if (itemForm.getTaxclassid() != null) {
                itemVO.setTaxClassId(Integer.parseInt(itemForm.getTaxclassid()));
            }
            if (itemForm.getSubitemid() != null && (itemForm.getSubitemid().length() > 0)) {
                itemVO.setSubItemOfId(Integer.parseInt(itemForm.getSubitemid()));
            }
            if (itemForm.getManufacturerid() != null && itemForm.getManufacturerid().length() > 0) {
                itemVO.setManufacturerid(Integer.parseInt(itemForm.getManufacturerid()));
            }
            if (itemForm.getVendorid() != null && itemForm.getVendorid().length() > 0) {
                itemVO.setVendorid(Integer.parseInt(itemForm.getVendorid()));
            }
            DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
            nf.setDecimalSeparatorAlwaysShown(false);
            if (itemForm.getPrice() != null && itemForm.getPrice().length() > 0) {
                double price = (nf.parse(itemForm.getPrice())).doubleValue();
                itemVO.setPrice(price);
            }
            if (itemForm.getCost() != null && itemForm.getCost().length() > 0) {
                double cost = (nf.parse(itemForm.getCost())).doubleValue();
                itemVO.setCost(cost);
            }
            if (itemForm.getLinktoinventory() != null && itemForm.getLinktoinventory().length() > 0) {
                itemVO.setLinkToInventory(itemForm.getLinktoinventory());
            }
            if (itemForm.getQtyonorder() != null && itemForm.getQtyonorder().length() > 0) {
                itemVO.setQtyOnOrder(Integer.parseInt(itemForm.getQtyonorder()));
            }
            if (itemForm.getQtyonbackorder() != null && itemForm.getQtyonbackorder().length() > 0) {
                itemVO.setQtyOnBackOrder(Integer.parseInt(itemForm.getQtyonbackorder()));
            }
            HttpSession session = request.getSession();
            int individualID = ((UserObject) session.getAttribute("userobject")).getIndividualID();
            itemVO.setCreatedBy(individualID);
            int itemId = itemRemote.addItem(individualID, itemVO);
            ((ItemForm) form).setItemid("" + itemId);
            Vector itemTypeVec = itemRemote.getItemType();
            Vector glAcntVec = itemRemote.getGLAccountType();
            Vector taxClassVec = itemRemote.getTaxClassType();
            ((ItemForm) form).setItemtypevec(itemTypeVec);
            ((ItemForm) form).setGlaccountvec(glAcntVec);
            ((ItemForm) form).setTaxclassvec(taxClassVec);
            if (typeOfSave.equals("save")) {
                FORWARD_final = FORWARD_save;
                request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.EDIT);
                request.setAttribute("itemId", "" + itemId);
            } else if (typeOfSave.equals("savenew")) {
                FORWARD_final = FORWARD_savenew;
                request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.ADD);
                request.removeAttribute("itemform");
            } else if (typeOfSave.equals("saveclose")) {
                FORWARD_final = FORWARD_saveclose;
                request.removeAttribute("itemform");
            }
            request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.ITEM);
        } catch (Exception e) {
            System.out.println("[Exception][SaveNewItemHandler] Exception thrown in execute():" + e);
            FORWARD_final = GLOBAL_FORWARD_failure;
        }
        return (mapping.findForward(FORWARD_final));
    }
}

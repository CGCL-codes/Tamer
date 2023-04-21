package org.posterita.struts.stock;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ecs.xhtml.caption;
import org.apache.ecs.xhtml.table;
import org.apache.ecs.xhtml.td;
import org.apache.ecs.xhtml.th;
import org.apache.ecs.xhtml.tr;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.compiere.model.MInventory;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.posterita.Constants;
import org.posterita.beans.InventoryCartBean;
import org.posterita.beans.InventoryLineBean;
import org.posterita.beans.ItemBean;
import org.posterita.beans.POSHistoryBean;
import org.posterita.businesslogic.POSTerminalManager;
import org.posterita.businesslogic.core.ChargeManager;
import org.posterita.businesslogic.performanceanalysis.POSReportManager;
import org.posterita.businesslogic.performanceanalysis.ReportDateManager;
import org.posterita.businesslogic.stock.InternalUseInventoryManager;
import org.posterita.businesslogic.stock.InventoryCartManager;
import org.posterita.businesslogic.stock.InventoryManager;
import org.posterita.businesslogic.stock.StockManager;
import org.posterita.core.TmkJSPEnv;
import org.posterita.core.TrxPrefix;
import org.posterita.exceptions.ApplicationException;
import org.posterita.exceptions.InputQuantityLessThanZeroException;
import org.posterita.exceptions.OperationException;
import org.posterita.exceptions.ProductNotFoundException;
import org.posterita.exceptions.ProductNotOnPriceListException;
import org.posterita.exceptions.QuantityNotAvailableException;
import org.posterita.exceptions.UOMValuePrecisionNotValidException;
import org.posterita.form.InventoryLineForm;
import org.posterita.lib.UdiConstants;
import org.posterita.struts.core.DefaultForm;
import org.posterita.struts.pos.POSDispatchAction;

public class InternalUseInventoryAction extends POSDispatchAction {

    public static final String INIT_STOCK_CART = "initStockCart";

    public ActionForward initStockCart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException {
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd != null) return fwd;
        Properties ctx = TmkJSPEnv.getCtx(request);
        int chargeId = ChargeManager.loadCharge(ctx, UdiConstants.SCRAPPED_CHARGE, null);
        Env.setContext(ctx, Constants.SCRAPPED_CHARGE_ID, chargeId);
        return mapping.findForward(INIT_STOCK_CART);
    }

    public ActionForward getScrappedCart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws OperationException, IOException, ParseException {
        DefaultForm df = (DefaultForm) form;
        String shoppingcartHTML = InternalUseInventoryManager.getScrappedCartAsHTML(request);
        PrintWriter writer = response.getWriter();
        writer.write(shoppingcartHTML);
        ActionMessages errors = (ActionMessages) request.getAttribute("org.apache.struts.action.ERROR");
        if (errors != null && !errors.isEmpty()) {
            MessageResources messageResources = getResources(request);
            Iterator<ActionMessage> iter = errors.get();
            while (iter.hasNext()) {
                ActionMessage message = iter.next();
                String key = message.getKey();
                Object[] values = message.getValues();
                String errMsg = messageResources.getMessage(key, values);
                writer.write("<script>showErrorMessage('" + errMsg + "',searchElement)</script>");
            }
        }
        writer.close();
        return null;
    }

    public static final String ADD_TO_SCRAPPED_CART = "addToScrappedCart";

    public ActionForward addToScrappedCart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException {
        Properties ctx = TmkJSPEnv.getCtx(request);
        InventoryLineForm df = (InventoryLineForm) form;
        InventoryLineBean bean = (InventoryLineBean) df.getBean();
        String scrappedCart = Constants.SCRAPPED_CART;
        String scrappedCartItems = Constants.SCRAPPED_CART_ITEMS;
        InventoryCartBean cartBean = (InventoryCartBean) request.getSession().getAttribute(scrappedCart);
        try {
            if (bean.getProductId() == null && bean.getBarCode() == null) {
                postGlobalError("error.barcode.required", request);
                return mapping.getInputForward();
            }
            BigDecimal qty = bean.getQtyCount();
            if (qty == null) {
                throw new InputQuantityLessThanZeroException("");
            }
            cartBean = StockManager.addToInventoryCart(ctx, bean, cartBean, false, Boolean.parseBoolean(bean.getIfAdd()), false);
        } catch (InputQuantityLessThanZeroException e) {
            postGlobalError("error.invalid.inputQty", request);
            return mapping.getInputForward();
        } catch (ProductNotFoundException e) {
            postGlobalError("error.product.not.found", e.getMessage(), request);
            return mapping.getInputForward();
        } catch (QuantityNotAvailableException e) {
            postGlobalError("error.quantity.notAvailable", e.getMessage(), request);
            return mapping.getInputForward();
        } catch (ProductNotOnPriceListException e) {
            postGlobalError("error.product.price.not.found", e.getMessage(), request);
            return mapping.getInputForward();
        } catch (UOMValuePrecisionNotValidException e) {
            postGlobalError("error.precision", e.getMessage(), request);
            return mapping.getInputForward();
        }
        String currSymboleSales = POSTerminalManager.getDefaultSalesCurrency(ctx).getCurSymbol();
        request.setAttribute(Constants.CURRENCY_SYMBOLE, currSymboleSales);
        request.getSession().setAttribute(scrappedCart, cartBean);
        request.getSession().setAttribute(scrappedCartItems, cartBean.getItems());
        df.setQtyAndItem("");
        return mapping.findForward(ADD_TO_SCRAPPED_CART);
    }

    public static final String INCREMENT_QTY = "incrementQty";

    public ActionForward incrementQty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InventoryLineForm of = (InventoryLineForm) form;
        if (of.getQuantity() == null) {
            of.setQuantity("1");
        }
        addToScrappedCart(mapping, form, request, response);
        return getScrappedCart(mapping, form, request, response);
    }

    public static final String DECREMENT_QTY = "decrementQty";

    public ActionForward decrementQty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InventoryLineForm of = (InventoryLineForm) form;
        if (of.getQtyCount() == null) {
            of.setQuantity("1");
        }
        addToScrappedCart(mapping, form, request, response);
        return getScrappedCart(mapping, form, request, response);
    }

    public ActionForward addProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException, ParseException {
        InventoryLineForm of = (InventoryLineForm) form;
        if (of.getQtyCount() == null) {
            of.setQtyCount("1");
        }
        addToScrappedCart(mapping, form, request, response);
        return getScrappedCart(mapping, form, request, response);
    }

    public ActionForward clearCart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Properties ctx = TmkJSPEnv.getCtx(request);
        Object inventoryIdAsStr = request.getSession().getAttribute(Constants.INVENTORY_ID);
        Integer inventoryId = 0;
        if (inventoryIdAsStr != null) {
            inventoryId = Integer.parseInt(inventoryIdAsStr.toString());
        }
        if (inventoryId != 0) {
            MInventory inventory = new MInventory(ctx, inventoryId, null);
            inventory.delete(true);
        }
        InternalUseInventoryManager.clearScrappedcart(ctx, request);
        return getScrappedCart(mapping, form, request, response);
    }

    public static final String UPDATE_QTY = "updateQty";

    public ActionForward updateQty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Properties ctx = TmkJSPEnv.getCtx(request);
        InventoryLineForm of = (InventoryLineForm) form;
        if (of.getQtyCount() == null) {
            of.setQtyCount("1");
        }
        InventoryLineBean bean = (InventoryLineBean) of.getBean();
        Integer product_id = bean.getProductId();
        bean.setQtyCount(new BigDecimal(of.getQtyCount()));
        Integer priceListId = bean.getPriceListId();
        of.populate(bean);
        ArrayList<ItemBean> items = (ArrayList<ItemBean>) request.getSession().getAttribute(Constants.SCRAPPED_CART_ITEMS);
        StockManager.updateItemFromInventoryList(ctx, priceListId, items, product_id, bean.getQtyCount().intValue());
        request.getSession().setAttribute(Constants.SCRAPPED_CART_ITEMS, items);
        return getScrappedCart(mapping, form, request, response);
    }

    public static final String COMPLETE_INVENTORY_ADJUSTMENT = "completeInventoryAdjustment";

    public ActionForward completeInventoryAdjustment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException {
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd != null) return fwd;
        Properties ctx = TmkJSPEnv.getCtx(request);
        Trx trx = Trx.get(TrxPrefix.getPrefix(), true);
        InventoryLineForm If = (InventoryLineForm) form;
        InventoryLineBean bean = (InventoryLineBean) If.getBean();
        int chargeId = Env.getContextAsInt(ctx, Constants.SCRAPPED_CHARGE_ID);
        ArrayList<ItemBean> items = (ArrayList<ItemBean>) request.getSession().getAttribute(Constants.SCRAPPED_CART_ITEMS);
        try {
            if (bean.getInventoryId() == null || bean.getInventoryId() == 0) {
                MInventory inventory = InternalUseInventoryManager.createInternalUseInventory(ctx, bean.getDescription(), trx.getTrxName());
                bean.setInventoryId(inventory.get_ID());
                bean.setDocumentNo(inventory.getDocumentNo());
                bean.setDocStatus(inventory.getDocStatus());
                inventory.save();
                trx.start();
                for (ItemBean itemBean : items) {
                    InternalUseInventoryManager.addInventoryLine(ctx, bean.getInventoryId(), itemBean.getProductId(), itemBean.getQtyBook(), itemBean.getQtyCount().negate(), chargeId, trx.getTrxName());
                }
                trx.commit();
                InventoryManager.completeStockAdjustment(ctx, bean.getInventoryId());
            } else {
                InventoryCartManager.deleteInventoryLines(ctx, bean.getInventoryId());
                trx.start();
                for (ItemBean itemBean : items) {
                    InternalUseInventoryManager.addInventoryLine(ctx, bean.getInventoryId(), itemBean.getProductId(), itemBean.getQtyBook(), itemBean.getQtyCount().negate(), chargeId, trx.getTrxName());
                }
                trx.commit();
                InventoryManager.completeStockAdjustment(ctx, bean.getInventoryId());
            }
        } catch (OperationException e) {
            trx.rollback();
            trx.close();
            postGlobalError("error.reason.required", e.getMessage(), request);
            return mapping.getInputForward();
        }
        InternalUseInventoryManager.clearScrappedcart(ctx, request);
        return new ActionForward("/ViewCompletedStockAdjustment.do?inventoryId=" + bean.getInventoryId());
    }

    public static final String SAVE_INVENTORY_ADJUSTMENT = "saveInventoryAdjustment";

    public ActionForward saveInventoryAdjustment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException {
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd != null) return fwd;
        Properties ctx = TmkJSPEnv.getCtx(request);
        Trx trx = Trx.get(TrxPrefix.getPrefix(), true);
        InventoryLineForm If = (InventoryLineForm) form;
        InventoryLineBean bean = (InventoryLineBean) If.getBean();
        int chargeId = Env.getContextAsInt(ctx, Constants.SCRAPPED_CHARGE_ID);
        ArrayList<ItemBean> items = (ArrayList<ItemBean>) request.getSession().getAttribute(Constants.SCRAPPED_CART_ITEMS);
        try {
            if (bean.getInventoryId() == null || bean.getInventoryId() == 0) {
                MInventory inventory = InternalUseInventoryManager.createInternalUseInventory(ctx, bean.getDescription(), trx.getTrxName());
                bean.setInventoryId(inventory.get_ID());
                bean.setDocumentNo(inventory.getDocumentNo());
                bean.setDocStatus(inventory.getDocStatus());
                inventory.save();
                trx.start();
                for (ItemBean itemBean : items) {
                    InternalUseInventoryManager.addInventoryLine(ctx, bean.getInventoryId(), itemBean.getProductId(), itemBean.getQtyBook(), itemBean.getQtyCount().negate(), chargeId, trx.getTrxName());
                }
                trx.commit();
            } else {
                InventoryCartManager.deleteInventoryLines(ctx, bean.getInventoryId());
                trx.start();
                for (ItemBean itemBean : items) {
                    InternalUseInventoryManager.addInventoryLine(ctx, bean.getInventoryId(), itemBean.getProductId(), itemBean.getQtyBook(), itemBean.getQtyCount().negate(), chargeId, trx.getTrxName());
                }
                trx.commit();
            }
        } catch (OperationException e) {
            trx.rollback();
            trx.close();
            postGlobalError("errors.cancel", e.getMessage(), request);
            return mapping.getInputForward();
        }
        InternalUseInventoryManager.clearScrappedcart(ctx, request);
        return new ActionForward("/ViewSavedStockAdjustment.do?inventoryId=" + bean.getInventoryId());
    }

    public static final String CREATE_NEW_INVENTORY_ADJUSTMENT = "createNewInventoryAdjustment";

    public ActionForward createNewInventoryAdjustment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException {
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd != null) return fwd;
        Properties ctx = TmkJSPEnv.getCtx(request);
        Trx trx = Trx.get(TrxPrefix.getPrefix(), true);
        InternalUseInventoryManager.clearScrappedcart(ctx, request);
        return mapping.findForward(INIT_STOCK_CART);
    }

    public static final String VIEW_ADJUSTMENT_HISTORY = "viewAdjustmentHistory";

    public ActionForward viewAdjustmentHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException, ParseException {
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd != null) return fwd;
        Properties ctx = TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm) form;
        InventoryLineBean bean = (InventoryLineBean) df.getBean();
        if (bean.getDateRange() == null) {
            bean.setDateRange(Constants.FIXED_DATE_RANGE);
        }
        if (bean.getTimePeriod() == null) {
            bean.setTimePeriod(ReportDateManager.TODAY);
        }
        String dateRange = bean.getDateRange();
        String timePeriod = bean.getTimePeriod();
        Timestamp fromDate = null;
        Timestamp toDate = null;
        Date startDate = null;
        Date endDate = null;
        String subtitle = null;
        if (dateRange.endsWith(Constants.FIXED_DATE_RANGE)) {
            if (timePeriod == null) {
                throw new OperationException("Invalid Parameter for timePeriod. timePeriod is null");
            }
            startDate = ReportDateManager.getStartDateForPeriod(timePeriod);
            endDate = ReportDateManager.getEndDateForPeriod(timePeriod);
            fromDate = new Timestamp(startDate.getTime());
            toDate = new Timestamp(endDate.getTime());
            subtitle = "For " + timePeriod + ": " + startDate;
        } else if (dateRange.endsWith(Constants.CUSTOM_DATE_RANGE)) {
            boolean error = false;
            ActionMessages messages = new ActionMessages();
            ActionMessage message = null;
            if ((bean.getFromDate() == null) || (bean.getFromDate() == "")) {
                message = new ActionMessage("error.required.fromDate");
                messages.add(ActionMessages.GLOBAL_MESSAGE, message);
                error = true;
            }
            if ((bean.getToDate() == null) || (bean.getToDate() == "")) {
                message = new ActionMessage("error.required.toDate");
                messages.add(ActionMessages.GLOBAL_MESSAGE, message);
                error = true;
            }
            if (error) {
                saveErrors(request, messages);
                return mapping.getInputForward();
            } else {
                fromDate = ReportDateManager.getFromDate(bean);
                toDate = ReportDateManager.getToDate(bean);
            }
        }
        if (bean.getOrgId() == null) {
            bean.setOrgId(Env.getAD_Org_ID(ctx));
        }
        ArrayList list = InternalUseInventoryManager.getInternalUseInventoryHistory(ctx, bean, null);
        request.setAttribute(Constants.ADJUSTMENT_HISTORY_LIST, list);
        return mapping.findForward(VIEW_ADJUSTMENT_HISTORY);
    }

    public static final String VIEW_INVENTORY_DETAILS = "viewInventoryDetails";

    public ActionForward viewInventoryDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException {
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd != null) return fwd;
        Properties ctx = TmkJSPEnv.getCtx(request);
        InventoryLineForm If = (InventoryLineForm) form;
        InventoryLineBean bean = (InventoryLineBean) If.getBean();
        ArrayList<InventoryLineBean> list = InternalUseInventoryManager.getInventoryLines(ctx, bean.getInventoryId());
        StringBuffer sb = new StringBuffer();
        String[] headers = { "Product Name", "Qty Book", "Qty Adjusted" };
        table tbl = new table("0", "0", "0", "100%", "");
        tbl.setCellPadding(1);
        tbl.setCellSpacing(0);
        tbl.setClass("alignDetails");
        caption cap = new caption();
        cap.setClass("captionStyle");
        cap.setTagText("Stock Adjustment Details");
        tbl.addElement(cap);
        tr headerRow = new tr();
        headerRow.setClass("salesFiguresHeader");
        for (String header : headers) {
            th h = new th(header);
            headerRow.addElement(h);
        }
        tbl.addElement(headerRow);
        int count = 0;
        if (list != null) {
            for (InventoryLineBean iBean : list) {
                count++;
                tr dataRow = new tr();
                if (count % 2 == 0) {
                    dataRow.setClass("salesFiguresRow");
                }
                dataRow.setID("row" + count);
                String[] columnData = { iBean.getProductName(), iBean.getQtyBook().toString(), iBean.getQtyCount().negate().toString() };
                for (int i = 0; i < columnData.length; i++) {
                    td col = new td(columnData[i]);
                    if (i == columnData.length) {
                        col.setClass("productValue");
                    }
                    dataRow.addElement(col);
                }
                tbl.addElement(dataRow);
            }
        }
        PrintWriter writer = response.getWriter();
        writer.print(tbl.toString());
        writer.flush();
        writer.close();
        return null;
    }
}

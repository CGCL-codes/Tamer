package org.compiere.wstore;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Web Order.
 *
 *  @author Jorg Janke
 *  @version $Id: OrderServlet.java,v 1.2 2006/07/30 00:53:21 jjanke Exp $
 */
public class OrderServlet extends HttpServlet {

    /**	Logging						*/
    private static CLogger log = CLogger.getCLogger(OrderServlet.class);

    /** Name						*/
    public static final String NAME = "orderServlet";

    /**
	 *	Initialize global variables
	 *
	 *  @param config Configuration
	 *  @throws ServletException
	 */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (!WebEnv.initWeb(config)) throw new ServletException("OrderServlet.init");
    }

    /**
	 * Get Servlet information
	 * @return Info
	 */
    public String getServletInfo() {
        return "Adempiere Web Order Servlet";
    }

    /**
	 * Clean up resources
	 */
    public void destroy() {
        log.fine("");
    }

    /**************************************************************************
	 *  Process the HTTP Get request.
	 * 	(logout, deleteCookie)
	 *  Sends Web Request Page
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Get from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
        doPost(request, response);
    }

    /**
	 *  Process the HTTP Post request
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Post from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
        Properties ctx = JSPEnv.getCtx(request);
        HttpSession session = request.getSession(true);
        session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
        WebUser wu = (WebUser) session.getAttribute(WebUser.NAME);
        WebBasket wb = (WebBasket) session.getAttribute(WebBasket.NAME);
        MOrder order = null;
        boolean done = false;
        String url = "/paymentInfo.jsp";
        if (wu == null || !wu.isLoggedIn()) {
            session.setAttribute("CheckOut", "Y");
            url = "/login.jsp";
            done = true;
        } else order = getOrder(request, ctx);
        if (!done && order != null) {
            if (processOrder(request, order)) url = "/orders.jsp"; else {
                WebOrder wo = new WebOrder(order);
                MPayment p = createPayment(session, ctx, wu, wo);
                if (p != null) {
                    session.setAttribute(PaymentServlet.ATTR_PAYMENT, p);
                    session.setAttribute(WebOrder.NAME, wo);
                } else url = "/orders.jsp";
            }
            done = true;
        }
        if (!done && (wb == null || wb.getLineCount() == 0)) {
            url = "/basket.jsp";
            done = true;
        }
        if (!done) {
            WebOrder wo = new WebOrder(wu, wb, ctx);
            if (wo.isInProgress() || wo.isCompleted()) {
                session.removeAttribute(CheckOutServlet.ATTR_CHECKOUT);
                session.removeAttribute(WebBasket.NAME);
                sendEMail(request, ctx, wo, wu);
            }
            if (wo.getGrandTotal().compareTo(Env.ZERO) > 0) {
                session.setAttribute(WebOrder.NAME, wo);
                MPayment p = createPayment(session, ctx, wu, wo);
                if (p == null) {
                    WebUtil.createForwardPage(response, "Payment could not be created", "orders.jsp", 5);
                    return;
                } else session.setAttribute(PaymentServlet.ATTR_PAYMENT, p);
            } else {
                url = "/orders.jsp";
            }
        }
        log.info("Forward to " + url);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

    /**************************************************************************
	 * 	Create Payment, but don't save it
	 * 	@param session session
	 * 	@param ctx context
	 * 	@param wu web user
	 * 	@param wo Order
	 * 	@return Payment
	 */
    private MPayment createPayment(HttpSession session, Properties ctx, WebUser wu, WebOrder wo) {
        MPayment p = new MPayment(ctx, 0, null);
        p.setAD_Org_ID(wo.getAD_Org_ID());
        p.setIsSelfService(true);
        p.setAmount(wo.getC_Currency_ID(), wo.getGrandTotal());
        p.setIsOnline(true);
        p.setC_DocType_ID(true);
        p.setTrxType(MPayment.TRXTYPE_Sales);
        p.setTenderType(MPayment.TENDERTYPE_CreditCard);
        p.setC_Order_ID(wo.getC_Order_ID());
        p.setBP_BankAccount(wu.getBankAccount());
        return p;
    }

    /**
	 *	Get Order
	 *	@param request request
	 * 	@param ctx context
	 *	@return true if processed
	 */
    private MOrder getOrder(HttpServletRequest request, Properties ctx) {
        String para = WebUtil.getParameter(request, "C_Order_ID");
        if (para == null || para.length() == 0) return null;
        int C_Order_ID = 0;
        try {
            C_Order_ID = Integer.parseInt(para);
        } catch (NumberFormatException ex) {
        }
        if (C_Order_ID == 0) return null;
        log.fine("C_Order_ID=" + C_Order_ID);
        return new MOrder(ctx, C_Order_ID, null);
    }

    /**
	 *	Process Order
	 *	@param request request
	 *	@param order order
	 *	@return true if processed/ok
	 */
    private boolean processOrder(HttpServletRequest request, MOrder order) {
        String DocAction = WebUtil.getParameter(request, "DocAction");
        if (DocAction == null || DocAction.length() == 0) return false;
        MDocType dt = MDocType.get(order.getCtx(), order.getC_DocType_ID());
        if (!order.isSOTrx() || order.getGrandTotal().compareTo(Env.ZERO) <= 0 || !MDocType.DOCBASETYPE_SalesOrder.equals(dt.getDocBaseType())) {
            log.warning("Not a valid Sales Order " + order);
            return true;
        }
        log.fine("DocAction=" + DocAction);
        if (!MOrder.DOCACTION_Void.equals(DocAction)) {
            if (MOrder.STATUS_WaitingPayment.equals(order.getDocStatus())) return false;
            if (MDocType.DOCSUBTYPESO_PrepayOrder.equals(dt.getDocSubTypeSO())) return false;
            if (!MOrder.DOCACTION_Complete.equals(DocAction)) {
                log.warning("Invalid DocAction=" + DocAction);
                return true;
            }
        }
        order.setDocAction(DocAction, true);
        boolean ok = order.processIt(DocAction);
        order.save();
        return ok;
    }

    /**
	 * 	Send Order EMail.
	 * 	@param request request
	 * 	@param ctx context
	 * 	@param wo web order
	 * 	@param wu web user
	 */
    private void sendEMail(HttpServletRequest request, Properties ctx, WebOrder wo, WebUser wu) {
        StringBuffer message = new StringBuffer("\n");
        MOrder mo = wo.getOrder();
        if (mo != null) {
            MOrderLine[] ol = mo.getLines(true, null);
            for (int i = 0; i < ol.length; i++) {
                message.append("\n").append(ol[i].getQtyOrdered()).append(" * ").append(ol[i].getName());
                if (ol[i].getDescription() != null) message.append(" - ").append(ol[i].getDescription());
                message.append(" (").append(ol[i].getPriceActual()).append(") = ").append(ol[i].getLineNetAmt());
            }
        }
        message.append("\n\n").append(Msg.getElement(ctx, "C_Order_ID")).append(": ").append(wo.getDocumentNo()).append(" - ").append(Msg.getElement(ctx, "GrandTotal")).append(": ").append(wo.getGrandTotal());
        JSPEnv.sendEMail(request, wu, MMailMsg.MAILMSGTYPE_OrderAcknowledgement, new Object[] { wo.getDocumentNo(), wu.getName(), message.toString() });
    }
}

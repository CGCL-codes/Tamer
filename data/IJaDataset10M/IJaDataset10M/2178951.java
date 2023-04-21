package com.liferay.portlet.shopping.action;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.struts.ActionConstants;
import com.liferay.portlet.shopping.NoSuchOrderException;
import com.liferay.portlet.shopping.service.ShoppingOrderLocalServiceUtil;
import com.liferay.util.HttpUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="PayPalNotificationAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PayPalNotificationAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String invoice = null;
        try {
            if (_log.isDebugEnabled()) {
                _log.debug("Receiving notification from PayPal");
            }
            String query = "cmd=_notify-validate";
            Enumeration enu = req.getParameterNames();
            while (enu.hasMoreElements()) {
                String name = (String) enu.nextElement();
                String value = req.getParameter(name);
                query = query + "&" + name + "=" + HttpUtil.encodeURL(value);
            }
            if (_log.isDebugEnabled()) {
                _log.debug("Sending response to PayPal " + query);
            }
            URL url = new URL("http://www.paypal.com/cgi-bin/webscr");
            URLConnection urlc = url.openConnection();
            urlc.setDoOutput(true);
            urlc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter pw = new PrintWriter(urlc.getOutputStream());
            pw.println(query);
            pw.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String payPalStatus = br.readLine();
            br.close();
            String itemName = ParamUtil.getString(req, "item_name");
            String itemNumber = ParamUtil.getString(req, "item_number");
            invoice = ParamUtil.getString(req, "invoice");
            String txnId = ParamUtil.getString(req, "txn_id");
            String paymentStatus = ParamUtil.getString(req, "payment_status");
            double paymentGross = ParamUtil.getDouble(req, "payment_gross");
            String receiverEmail = ParamUtil.getString(req, "receiver_email");
            String payerEmail = ParamUtil.getString(req, "payer_email");
            if (_log.isDebugEnabled()) {
                _log.debug("Receiving response from PayPal");
                _log.debug("Item name " + itemName);
                _log.debug("Item number " + itemNumber);
                _log.debug("Invoice " + invoice);
                _log.debug("Transaction ID " + txnId);
                _log.debug("Payment status " + paymentStatus);
                _log.debug("Payment gross " + paymentGross);
                _log.debug("Receiver email " + receiverEmail);
                _log.debug("Payer email " + payerEmail);
            }
            if (payPalStatus.equals("VERIFIED")) {
                ShoppingOrderLocalServiceUtil.completeOrder(invoice, txnId, paymentStatus, paymentGross, receiverEmail, payerEmail, true);
            } else if (payPalStatus.equals("INVALID")) {
            }
            return mapping.findForward(ActionConstants.COMMON_NULL);
        } catch (NoSuchOrderException nsoe) {
            _log.error("Order " + invoice + " does not exist");
            return mapping.findForward(ActionConstants.COMMON_NULL);
        } catch (Exception e) {
            req.setAttribute(PageContext.EXCEPTION, e);
            return mapping.findForward(ActionConstants.COMMON_ERROR);
        }
    }

    private static Log _log = LogFactory.getLog(PayPalNotificationAction.class);
}

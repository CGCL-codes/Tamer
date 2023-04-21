package educate.sis.payment;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lebah.portal.action.RequestUtil;
import org.apache.velocity.VelocityContext;
import educate.sis.billing.BillingData;
import educate.sis.registration.StudentData;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1
 */
public class GetPaymentAction implements lebah.portal.action.ActionTemplate {

    public boolean doAction(HttpServletRequest req, HttpServletResponse res, VelocityContext context) throws Exception {
        String student_id = PaymentMainModule.doInit(req, res, context);
        BillingData.createPaymentList(student_id);
        Vector paymentList = BillingData.getPaymentList(student_id);
        context.put("paymentList", paymentList);
        return true;
    }
}

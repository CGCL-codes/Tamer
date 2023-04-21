package com.liferay.portlet.shopping.action;

import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.Constants;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.shopping.util.ShoppingPreferences;
import com.liferay.util.ParamUtil;
import com.liferay.util.StringUtil;
import com.liferay.util.Validator;
import com.liferay.util.servlet.SessionErrors;
import com.liferay.util.servlet.SessionMessages;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="EditConfigurationAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class EditConfigurationAction extends PortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        String cmd = ParamUtil.getString(req, Constants.CMD);
        if (!cmd.equals(Constants.UPDATE)) {
            return;
        }
        ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
        ShoppingPreferences prefs = ShoppingPreferences.getInstance(themeDisplay.getCompanyId(), themeDisplay.getPortletGroupId());
        String tabs2 = ParamUtil.getString(req, "tabs2");
        String tabs3 = ParamUtil.getString(req, "tabs3");
        if (tabs2.equals("payment-settings")) {
            updatePayment(req, prefs);
        } else if (tabs2.equals("shipping-calculation")) {
            updateShippingCalculation(req, prefs);
        } else if (tabs2.equals("insurance-calculation")) {
            updateInsuranceCalculation(req, prefs);
        } else if (tabs2.equals("emails")) {
            if (tabs3.equals("email-from")) {
                updateEmailFrom(req, prefs);
            } else if (tabs3.equals("confirmation-email")) {
                updateEmailOrderConfirmation(req, prefs);
            } else if (tabs3.equals("shipping-email")) {
                updateEmailOrderShipping(req, prefs);
            }
        }
        if (SessionErrors.isEmpty(req)) {
            prefs.store();
            SessionMessages.add(req, config.getPortletName() + ".doConfigure");
        }
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return mapping.findForward("portlet.shopping.edit_configuration");
    }

    protected void updateEmailFrom(ActionRequest req, ShoppingPreferences prefs) throws Exception {
        String emailFromName = ParamUtil.getString(req, "emailFromName");
        String emailFromAddress = ParamUtil.getString(req, "emailFromAddress");
        if (Validator.isNull(emailFromName)) {
            SessionErrors.add(req, "emailFromName");
        } else if (!Validator.isEmailAddress(emailFromAddress)) {
            SessionErrors.add(req, "emailFromAddress");
        } else {
            prefs.setEmailFromName(emailFromName);
            prefs.setEmailFromAddress(emailFromAddress);
        }
    }

    protected void updateEmailOrderConfirmation(ActionRequest req, ShoppingPreferences prefs) throws Exception {
        boolean emailOrderConfirmationEnabled = ParamUtil.getBoolean(req, "emailOrderConfirmationEnabled");
        String emailOrderConfirmationSubject = ParamUtil.getString(req, "emailOrderConfirmationSubject");
        String emailOrderConfirmationBody = ParamUtil.getString(req, "emailOrderConfirmationBody");
        if (Validator.isNull(emailOrderConfirmationSubject)) {
            SessionErrors.add(req, "emailOrderConfirmationSubject");
        } else if (Validator.isNull(emailOrderConfirmationBody)) {
            SessionErrors.add(req, "emailOrderConfirmationBody");
        } else {
            prefs.setEmailOrderConfirmationEnabled(emailOrderConfirmationEnabled);
            prefs.setEmailOrderConfirmationSubject(emailOrderConfirmationSubject);
            prefs.setEmailOrderConfirmationBody(emailOrderConfirmationBody);
        }
    }

    protected void updateEmailOrderShipping(ActionRequest req, ShoppingPreferences prefs) throws Exception {
        boolean emailOrderShippingEnabled = ParamUtil.getBoolean(req, "emailOrderShippingEnabled");
        String emailOrderShippingSubject = ParamUtil.getString(req, "emailOrderShippingSubject");
        String emailOrderShippingBody = ParamUtil.getString(req, "emailOrderShippingBody");
        if (Validator.isNull(emailOrderShippingSubject)) {
            SessionErrors.add(req, "emailOrderShippingSubject");
        } else if (Validator.isNull(emailOrderShippingBody)) {
            SessionErrors.add(req, "emailOrderShippingBody");
        } else {
            prefs.setEmailOrderShippingEnabled(emailOrderShippingEnabled);
            prefs.setEmailOrderShippingSubject(emailOrderShippingSubject);
            prefs.setEmailOrderShippingBody(emailOrderShippingBody);
        }
    }

    protected void updateInsuranceCalculation(ActionRequest req, ShoppingPreferences prefs) throws Exception {
        String insuranceFormula = ParamUtil.getString(req, "insuranceFormula");
        String[] insurance = new String[5];
        for (int i = 0; i < insurance.length; i++) {
            insurance[i] = String.valueOf(ParamUtil.getDouble(req, "insurance" + i));
        }
        prefs.setInsuranceFormula(insuranceFormula);
        prefs.setInsurance(insurance);
    }

    protected void updatePayment(ActionRequest req, ShoppingPreferences prefs) throws Exception {
        String payPalEmailAddress = ParamUtil.getString(req, "payPalEmailAddress");
        String[] ccTypes = StringUtil.split(ParamUtil.getString(req, "ccTypes"));
        String currencyId = ParamUtil.getString(req, "currencyId");
        String taxState = ParamUtil.getString(req, "taxState");
        double taxRate = ParamUtil.getDouble(req, "taxRate") / 100;
        double minOrder = ParamUtil.getDouble(req, "minOrder");
        prefs.setPayPalEmailAddress(payPalEmailAddress);
        prefs.setCcTypes(ccTypes);
        prefs.setCurrencyId(currencyId);
        prefs.setTaxState(taxState);
        prefs.setTaxRate(taxRate);
        prefs.setMinOrder(minOrder);
    }

    protected void updateShippingCalculation(ActionRequest req, ShoppingPreferences prefs) throws Exception {
        String shippingFormula = ParamUtil.getString(req, "shippingFormula");
        String[] shipping = new String[5];
        for (int i = 0; i < shipping.length; i++) {
            shipping[i] = String.valueOf(ParamUtil.getDouble(req, "shipping" + i));
        }
        prefs.setShippingFormula(shippingFormula);
        prefs.setShipping(shipping);
    }
}

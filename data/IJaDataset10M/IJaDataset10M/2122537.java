package com.hisham.parking.permitsales.web;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import com.hisham.util.*;
import com.hisham.parking.permitsales.*;
import com.hisham.parking.web.*;
import org.apache.commons.validator.GenericValidator;
import com.hisham.parking.*;
import com.hisham.creditcard.*;

/**
 * <p>Title: OrderFormClient</p>
 * <p>Description:
 * The object that client needs to fill in for processing permit order
 * Gets the customer names and their corresponding Ids from the database for
 * displaying them on the order from
 * Gets the permit types and cost names and their corresponding Ids from the database for
 * displaying them on the order from
 * Gets the values from the order form and validates them
 * Gets the credit card info from the order form and validates the entire
 * form again</p>
 * <p>Title: Parking Services Online</p>
 * <p>Description: Tools for online account access, sales, citations and appeals</p>
 * @author Ali Hisham Malik
 * @version 1.3
 */
public abstract class PsTransactionInfoForm extends ParkingTransactionInfoForm {

    protected String userRequest = new String();

    protected boolean ignoreErrors;

    private PsMenu psMenu = null;

    public PsTransactionInfoForm() {
        userRequest = "";
        ignoreErrors = true;
    }

    public abstract PsTransactionResponse getPsTransactionResponse();

    public IParkingTransactionResponse getParkingTransactionResponse() {
        return this.getPsTransactionResponse();
    }

    /**
   * @return the total number of orders the customer has place (min is 1)
   */
    public int getOrderCount() {
        return this.getPsTransactionInfo().getPsTransactionItemInfos().size();
    }

    public String getOrderNumber() {
        return this.getPsTransactionInfo().getOrderNumber();
    }

    public void setOrderNumber(String orderNumber) {
        this.getPsTransactionInfo().setOrderNumber(orderNumber);
    }

    /**
   * processes the user request and set the ignoreErrorsFlag
   * to true
   * @param requestString user request e.g. AddOrder
   */
    public void setUserRequest(String requestString) {
        userRequest = requestString;
    }

    public PsTransactionItemInfo getPsTransactionItemInfo(int index) {
        return this.getPsTransactionInfo().getPsTransactionItemInfos().getPsTransactionItemInfo(index);
    }

    public void setPsTransactionItemInfo(int index, PsTransactionItemInfo info) {
        this.getPsTransactionInfo().getPsTransactionItemInfos().set(index, info);
    }

    public String getUserRequest() {
        return userRequest;
    }

    /**
   * function to set the flag variable which indicates whether errors should
   * be allowed to be sent through the getErrorMsg() function
   * * A work around for the client order changes without generating
   * errors on values. Should be set to true by the client when it needs
   * to display the error
   * @param flag set to true if Errors are to be ignored, false otherwise
   */
    public void setIgnoreErrors(boolean flag) {
        ignoreErrors = flag;
    }

    /**
   *
   * @return returns the customer Id which is selected
   */
    public Integer getCustomerId() {
        return new Integer(this.getPsTransactionInfo().getCustomerId());
    }

    /**
   * @see setCustomerId(int customerId)
   * @param customerId customer Id of the selected customer
   */
    public void setCustomerId(Integer customerId) {
        this.setCustomerId(customerId.intValue());
    }

    /**
  * Get the selected customer names from the order form
  * Also initializes the orderNumber if not already present
  * @param customerId customer Id of the selected customer
  */
    public void setCustomerId(int customerId) {
        this.getPsTransactionInfo().setCustomerId(customerId);
    }

    public CreditcardInfo getCreditcard() {
        return this.getCcTransactionInfo().getCreditcard();
    }

    public String getCcName() {
        return this.getCcTransactionInfo().getCreditcard().getCcName();
    }

    public void setCcName(String billingName) {
        this.getCcTransactionInfo().getCreditcard().setCcName(billingName.trim());
    }

    public String getCcNumber() {
        return this.getCcTransactionInfo().getCreditcard().getCcNumber();
    }

    public void setCcNumber(String number) {
        this.getCcTransactionInfo().getCreditcard().setCcNumber(number.trim());
    }

    public void setCcExpMonth(int month) {
        this.getCcTransactionInfo().getCreditcard().setCcExpMonth(month);
    }

    public int getCcExpMonth() {
        return this.getCcTransactionInfo().getCreditcard().getCcExpMonth();
    }

    public void setCcExpYear(int year) {
        this.getCcTransactionInfo().getCreditcard().setCcExpYear(year);
    }

    public int getCcExpYear() {
        return this.getCcTransactionInfo().getCreditcard().getCcExpYear();
    }

    public boolean getIgnoreErrors() {
        return ignoreErrors;
    }

    public boolean processUserRequest() {
        if (userRequest.equals("resetOrder")) {
            for (int i = 0; i < this.getOrderCount(); i++) this.removePermitSalesInfo(i);
            this.setUserRequest("");
        }
        if (userRequest.equals("addOrder")) {
            PsTransactionItemInfoList permitInfos = this.getPsTransactionInfo().getPsTransactionItemInfos();
            permitInfos.addPsTransactionItemInfo((PsTransactionItemInfo) permitInfos.getNewElement());
            return true;
        } else if (userRequest.startsWith("removeOrder")) {
            int orderIndex = Integer.parseInt(userRequest.substring(userRequest.indexOf("#") + 1));
            removePermitSalesInfo(orderIndex);
            return true;
        }
        return false;
    }

    public PsTransactionItemInfo removePermitSalesInfo(int orderIndex) {
        PsTransactionItemInfoList items = this.getPsTransactionInfo().getPsTransactionItemInfos();
        PsTransactionItemInfo permitInfo = items.removePsTransactionItemInfo(orderIndex);
        this.getPsTransactionInfo().setPsTransactionItemInfos(items);
        return permitInfo;
    }

    public String getEmailAddress() {
        return this.getCcTransactionInfo().getBillingInfo().getEmailAddress();
    }

    public void setEmailAddress(String emailAddress) {
        this.getCcTransactionInfo().getBillingInfo().setEmailAddress(emailAddress);
    }

    public abstract PsTransactionInfo getPsTransactionInfo();

    public IParkingTransactionInfo getParkingTransactionInfo() {
        return this.getPsTransactionInfo();
    }

    /**
   * @return the permitSeries which were selected by the user
   */
    public Integer[] getPermitSeriesIds() {
        Integer[] result = new Integer[this.getPsTransactionInfo().getPermitSeriesIds().size()];
        this.getPsTransactionInfo().getPermitSeriesIds().toArray(result);
        return result;
    }

    public void setPermitSeriesIds(Integer[] permitSeriesIds) {
        PsTransactionItemInfoList items = this.getPsTransactionInfo().getPsTransactionItemInfos();
        for (int counter = 0; counter < permitSeriesIds.length; counter++) {
            items.getPsTransactionItemInfo(counter).setPermitSeriesId(permitSeriesIds[counter]);
        }
    }

    /**
   * @return fees for the permitSeries which were selected by the user
   */
    public Double[] getPermitSeriesFees() {
        Double[] result = new Double[this.getPsTransactionInfo().getPermitSeriesFees().size()];
        this.getPsTransactionInfo().getPermitSeriesFees().toArray(result);
        return result;
    }

    public void setPermitSeriesFees(Double[] permitSeriesFees) {
        PsTransactionItemInfoList items = this.getPsTransactionInfo().getPsTransactionItemInfos();
        for (int counter = 0; counter < permitSeriesFees.length; counter++) {
            items.getPsTransactionItemInfo(counter).setPermitSeriesFee(permitSeriesFees[counter]);
        }
    }

    /**
   * gives the sum of permit series fees that will be charged on the credit card
   * @return amount that will be charged on credit card
   */
    public String getCcChargeAmount() {
        return java.text.DecimalFormat.getCurrencyInstance().format(this.getCcTransactionInfo().getTransactionAmount());
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();
        errors.add(this.validatePsTransactionInfos());
        errors.add(this.validateCreditcardInfo());
        return errors;
    }

    public abstract ActionMessages validatePsTransactionInfos();

    /**
   * Checks the Credit Card Info for completeness and validity
   * @return true if credit Card Info is valid and complete, false otherwise
   */
    public ActionMessages validateCreditcardInfo() {
        ActionMessages errors = new ActionMessages();
        if (GenericValidator.isBlankOrNull(this.getCreditcard().getCcName())) {
            errors.add("creditcard.ccName", new ActionMessage("errors.ccName.required"));
        }
        if (GenericValidator.isBlankOrNull(this.getCreditcard().getCcNumber())) {
            errors.add("creditcard.ccNumber", new ActionMessage("errors.ccNumber.required"));
        } else if (GenericValidator.isBlankOrNull(this.getCreditcard().getCcType())) {
            errors.add("creditcard.ccType", new ActionMessage("errors.ccType.required"));
        } else if (!this.getCreditcard().isValidCreditcardNumber()) {
            errors.add("creditcard.ccNumber", new ActionMessage("errors.ccNumber.invalid"));
        }
        if (!this.getCreditcard().isValidExpirationDate()) {
            errors.add("creditcard.ccExpDate", new ActionMessage("errors.ccExpDate.invalid"));
        }
        if (GenericValidator.isBlankOrNull(this.getEmailAddress())) {
            errors.add("emailAddress", new ActionMessage("errors.emailAddress.required"));
        } else if (!GenericValidator.isEmail(this.getEmailAddress())) errors.add("emailAddress", new ActionMessage("errors.emailAddress.invalid"));
        return errors;
    }

    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
    }

    public CcTransactionInfo getCcTransactionInfo() {
        return this.getPsTransactionInfo().getCcTransactionInfo();
    }

    public PsTransactionItemInfoList getPsTransactionItemInfos() {
        return this.getPsTransactionInfo().getPsTransactionItemInfos();
    }

    public PsMenu getPermitSalesMenu() {
        return psMenu;
    }

    public void setPermitSalesMenu(PsMenu psMenu) {
        this.psMenu = psMenu;
    }
}

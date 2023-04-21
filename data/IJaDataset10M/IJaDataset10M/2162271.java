package com.hisham.creditcard.skipjack;

import java.util.*;
import java.io.*;
import com.hisham.creditcard.*;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description:
 * Maps the Skip Jack authorization response to a hash table and
 * assigns to each property value </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Anonymous, Ali Hisham Malik
 * @version 2.0
 */
public class SjTransactionResponse extends CcTransactionResponse implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final int HTML = 1000000;

    public static final int COMMADELIMITED = 1000100;

    protected String sjAuthorizationCode;

    protected String sjCAVVResponseMessage;

    /**
	 *
	 */
    public SjTransactionResponse() {
        this.sjAuthorizationCode = new String();
        this.sjIsApproved = new String();
        this.sjTransactionFileName = new String();
        this.sjAuthorizationDeclinedMessage = new String();
        this.sjAuthorizationResponseCode = new String();
        this.sjAVSResponseCode = new String();
        this.sjCAVVResponseCode = new String();
        this.sjCAVVResponseMessage = new String();
        this.sjCVV2ResponseCode = new String();
        this.sjCVV2ResponseMessage = new String();
        this._hash = new HashMap<String, String>();
        this.sjRequest = new String();
        this.sjReturnCode = new String();
        this.sjSerialNumber = new String();
        this.sjTransactionAmount = new String();
    }

    /**
	 * Parses the comma-delimited response from skipjack
	 *
	 * @param response String
	 */
    public void parse(String response) {
        parseCommaDelimited(response);
    }

    /**
	 * parse the comma-delimited response from Skip Jack
	 * @param response response from skipjack. New line is marked by #
	 *
	 */
    public void parseCommaDelimited(String response) {
        StringTokenizer vars_vals = new StringTokenizer(response, "#");
        String vars = vars_vals.nextToken();
        vars = vars.replaceAll("\",\"", "\"#\"");
        String vals = vars_vals.nextToken();
        vals = vals.replaceAll("\",\"", "\"#\"");
        StringTokenizer varsElements = new StringTokenizer(vars, "#");
        StringTokenizer valsElements = new StringTokenizer(vals, "#");
        while (varsElements.hasMoreTokens()) {
            String varElement = varsElements.nextToken();
            String valElement = valsElements.nextToken();
            varElement = varElement.substring(1, varElement.length() - 1);
            valElement = valElement.substring(1, valElement.length() - 1);
            getHash().put(varElement, valElement);
        }
        mapHashToProperties();
    }

    /**
	 *
	 * mapHashToProperties
	 * comma delimited output includes:
	 * "AUTHCODE","szSerialNumber","szTransactionAmount",
	 * "szAuthorizationDeclinedMessage","szAVSResponseCode",
	 * "szAVSResponseMessage","szOrderNumber","szAuthorizationResponseCode",
	 * "szIsApproved","szCVV2ResponseCode","szCVV2ResponseMessage",
	 * "szReturnCode","szTransactionFileName","szCAVVResponseCode",
	 *
	 */
    public void mapHashToProperties() {
        Map<String, String> hash = getHash();
        this.setOrderNumber((String) hash.get("szOrderNumber"));
        this.setSjAuthorizationCode((String) hash.get("AUTHCODE"));
        this.setSjSerialNumber((String) hash.get("szSerialNumber"));
        this.setSjTransactionAmount((String) hash.get("szTransactionAmount"));
        this.setSjAuthorizationDeclinedMessage((String) hash.get("szAuthorizationDeclinedMessage"));
        this.setSjAVSResponseCode((String) hash.get("szAVSResponseCode"));
        this.setSjAVSResponseMessage((String) hash.get("szAVSResponseMessage"));
        this.setSjAuthorizationResponseCode((String) hash.get("szAuthorizationResponseCode"));
        this.setSjIsApproved((String) hash.get("szIsApproved"));
        this.setSjCVV2ResponseCode((String) hash.get("szCVV2ResponseCode"));
        this.setSjCVV2ResponseMessage((String) hash.get("szCVV2ResponseMessage"));
        this.setSjReturnCode((String) hash.get("szReturnCode"));
        this.setSjTransactionFileName((String) hash.get("szTransactionFileName"));
        this.setSjCAVVResponseCode((String) hash.get("szCAVVResponseCode"));
        this.setSjCAVVResponseMessage((String) hash.get("szCAVVResponseMessage"));
    }

    /**
	 *
	 * Gets returnMessage if returnCode is 1, otherwise gets decline reason from
	 * super class if present, otherwise returns AuthorizeDeclinedReason
	 *
	 * @see getAuthorizedDeclinedReason()
	 * @return String
	 */
    public String getDeclineReason() {
        if (!this.getSjReturnCode().equals("1") && !this.getSjReturnCode().equals("")) {
            return getSjReturnMessage();
        } else if (super.getDeclineReason() == null || super.getDeclineReason().equals("")) return this.getSjAuthorizationDeclinedMessage(); else return super.getDeclineReason();
    }

    public String toString() {
        return (this.getHash().toString());
    }

    protected Map<String, String> _hash;

    public Map<String, String> getHash() {
        return this._hash;
    }

    public boolean isEmpty() {
        return this.getHash().isEmpty();
    }

    protected String sjRequest;

    /**
	 * request section
	 * @param request String
	 */
    protected void setSjRequest(String request) {
        this.sjRequest = request;
    }

    /**
	 * request section
	 * @return String
	 */
    public String getSjRequest() {
        return (this.sjRequest);
    }

    protected String sjSerialNumber;

    /**
	 * maps to szSerialNumber
	 * @param serialNumber String
	 */
    private void setSjSerialNumber(String serialNumber) {
        this.sjSerialNumber = serialNumber;
    }

    /**
	 * maps to szSerialNumber
	 * @return String
	 */
    public String getSjSerialNumber() {
        return (this.sjSerialNumber);
    }

    protected String sjTransactionAmount;

    /**
	 * maps to szTransactionAmount
	 * @param transactionAmount String
	 */
    private void setSjTransactionAmount(String transactionAmount) {
        this.sjTransactionAmount = transactionAmount;
    }

    /**
	 * maps to szTransactionAmount
	 * @return String
	 */
    public String getSjTransactionAmount() {
        try {
            return "" + Double.valueOf(this.sjTransactionAmount).doubleValue() / 100;
        } catch (NumberFormatException e) {
            return this.sjTransactionAmount;
        }
    }

    public double getCcTransactionAmount() {
        return Double.valueOf(this.getSjTransactionAmount()).doubleValue();
    }

    protected String sjAuthorizationDeclinedMessage;

    /**
	 * maps to szAuthorizationDeclinedMessage
	 * @param authorizationDeclinedMessage String
	 */
    private void setSjAuthorizationDeclinedMessage(String authorizationDeclinedMessage) {
        this.sjAuthorizationDeclinedMessage = authorizationDeclinedMessage;
    }

    /**
	 * maps to szAuthorizationDeclinedMessage
	 * @return String
	 */
    public String getSjAuthorizationDeclinedMessage() {
        return (this.sjAuthorizationDeclinedMessage);
    }

    protected String sjAVSResponseCode;

    /**
	 * maps to szAVSResponseCode
	 * @param AVSResponseCode String
	 */
    private void setSjAVSResponseCode(String AVSResponseCode) {
        this.sjAVSResponseCode = AVSResponseCode;
    }

    /**
	 * maps to szAVSResponseCode
	 * @return String
	 */
    public String getSjAVSResponseCode() {
        return (this.sjAVSResponseCode);
    }

    protected String sjAVSResponseMessage;

    /**
	 * maps to szAVSResponseMessage
	 * @param AVSResponseMessage String
	 */
    private void setSjAVSResponseMessage(String AVSResponseMessage) {
        this.sjAVSResponseMessage = AVSResponseMessage;
    }

    /**
	 * maps to szAVSResponseMessage
	 * @return String
	 */
    public String getSjAVSResponseMessage() {
        return (this.sjAVSResponseMessage);
    }

    protected String sjAuthorizationResponseCode;

    /**
	 * maps to szAuthorizationResponseCode
	 * @param authorizationResponseCode String
	 */
    private void setSjAuthorizationResponseCode(String authorizationResponseCode) {
        this.sjAuthorizationResponseCode = authorizationResponseCode;
    }

    /**
	 * maps to szAuthorizationResponseCode
	 * @return String
	 */
    public String getSjAuthorizationResponseCode() {
        return (this.sjAuthorizationResponseCode);
    }

    protected String sjIsApproved;

    /**
	 *
	 * @return String 0 for false, 1 for true
	 */
    public String getSjIsApproved() {
        return sjIsApproved;
    }

    /**
	 * sets IsApproved and successfulTransactionFlag
	 * @param tmpIsApproved String 0 for false, 1 for true
	 */
    private void setSjIsApproved(String tmpIsApproved) {
        sjIsApproved = tmpIsApproved;
        super.setApprovedTransaction(this.sjIsApproved.equals("1"));
    }

    /**
	 *
	 * @return String
	 */
    public String getSjAuthorizationCode() {
        return sjAuthorizationCode;
    }

    /**
	 *
	 * @param authorizationCode String
	 */
    private void setSjAuthorizationCode(String authorizationCode) {
        this.sjAuthorizationCode = authorizationCode;
    }

    protected String sjReturnCode;

    /**
	 * maps to szReturnCode
	 * @param returnCode String
	 */
    private void setSjReturnCode(String returnCode) {
        this.sjReturnCode = returnCode;
    }

    /**
	 * maps to szReturnCode
	 * @return String
	 */
    public String getSjReturnCode() {
        return (this.sjReturnCode);
    }

    protected String sjCVV2ResponseCode;

    /**
	 * maps to szCVV2ResponseCode
	 * @param CVV2ResponseCode String
	 */
    private void setSjCVV2ResponseCode(String CVV2ResponseCode) {
        this.sjCVV2ResponseCode = CVV2ResponseCode;
    }

    /**
	 * maps to szCVV2ResponseCode
	 * @return String
	 */
    public String getSjCVV2ResponseCode() {
        return (this.sjCVV2ResponseCode);
    }

    protected String sjCVV2ResponseMessage;

    /**
	 * maps to CVV2ResponseMessage
	 * @param CVV2ResponseMessage String
	 */
    private void setSjCVV2ResponseMessage(String CVV2ResponseMessage) {
        this.sjCVV2ResponseMessage = CVV2ResponseMessage;
    }

    /**
	 * maps to CVV2ResponseMessage
	 * @return String
	 */
    public String getSjCVV2ResponseMessage() {
        return (this.sjCVV2ResponseMessage);
    }

    protected String sjTransactionFileName;

    /**
	 *
	 * @return String
	 */
    public String getSjTransactionFileName() {
        return sjTransactionFileName;
    }

    /**
	 *
	 * @param transactionFileName String
	 */
    private void setSjTransactionFileName(String transactionFileName) {
        this.sjTransactionFileName = transactionFileName;
    }

    protected String sjCAVVResponseCode;

    /**
	 *
	 * @param CVV2ResponseCode String
	 */
    private void setSjCAVVResponseCode(String CVV2ResponseCode) {
        this.sjCAVVResponseCode = CVV2ResponseCode;
    }

    /**
	 *
	 * @return String
	 */
    public String getSjCAVVResponseCode() {
        return (this.sjCAVVResponseCode);
    }

    /**
	 *
	 * @param CVV2ResponseMessage String
	 */
    private void setSjCAVVResponseMessage(String CVV2ResponseMessage) {
        this.sjCAVVResponseMessage = CVV2ResponseMessage;
    }

    /**
	 *
	 * @return String
	 */
    public String getSjCAVVResponseMessage() {
        return (this.sjCAVVResponseMessage);
    }

    /**
	 * -35  Error invalid credit card number Invalid Credit Card
	 * -37  Error failed communication System Failure
	 * -39  Error length serial number Invalid Field Entry
	 * -51  Error length zip code Invalid Field Entry
	 * -52  Error length shipto zip code Invalid Field Entry
	 * -53  Error length expiration date  Invalid Field Entry
	 * -54  Error length account number date  Invalid Field Entry
	 * -55  Error length street address   Invalid Field Entry
	 * -56  Error length shipto street address  Invalid Field Entry
	 * -57  Error length transaction amount  Invalid Field Entry
	 * -58  Error length name  Invalid Field Entry
	 * -59  Error length location  Invalid Field Entry
	 * -60  Error length state  Invalid Field Entry
	 * -61  Error length shipto state  Invalid Field Entry
	 * -62  Error length order string  Invalid Field Entry
	 * -64  Error invalid phone number  Invalid Field Entry
	 * -65  Error empty name  Invalid Field Entry
	 * -66  Error empty email  Invalid Field Entry
	 * -67 Error empty street address  Invalid Field Entry
	 * -68 Error empty city  Invalid Field Entry
	 * -69 Error empty state  Invalid Field Entry
	 * -79  Error length customer name  Invalid Field Entry
	 * -80  Error length shipto customer name  Invalid Field Entry
	 * -81  Error length customer location  Invalid Field Entry
	 * -82  Error length customer state  Invalid Field Entry
	 * -83  Error length shipto phone  Invalid Field Entry
	 * -84  Pos error duplicate ordernumber Thank You for your Order
	 * -91  Pos_error_CVV2  Invalid Field Entry
	 * -92  Pos_error_Error_Approval_Code Invalid Field Entry
	 * -93  Pos_error_Blind_Credits_Not_Allowed Invalid Field Entry
	 * -94  Pos_error_Blind_Credits_Failed Invalid Field Entry
	 * -95  Pos_error_Voice_Authorizations_Not_Allowed Invalid Field Entry
	 *
	 * @return message corresponding to the returnCode by skipjack
	 */
    public String getSjReturnMessage() {
        try {
            switch(Integer.parseInt(this.sjReturnCode)) {
                case 1:
                    return "Success Success (Valid Data)";
                case -35:
                    return "Error invalid credit card number - Invalid Credit Card";
                case -37:
                    return "Error failed communication System Failure";
                case -39:
                    return "Error length serial number - Invalid Field Entry";
                case -51:
                    return "Error length zip code Invalid Field Entry";
                case -52:
                    return "Error length shipto zip code - Invalid Field Entry";
                case -53:
                    return "Error length expiration date - Invalid Field Entry";
                case -54:
                    return "Error length account number date - Invalid Field Entry";
                case -55:
                    return "Error length street address - Invalid Field Entry";
                case -56:
                    return "Error length shipto street address - Invalid Field Entry";
                case -57:
                    return "Error length transaction amount - Invalid Field Entry";
                case -58:
                    return "Error length name - Invalid Field Entry";
                case -59:
                    return "Error length location - Invalid Field Entry";
                case -60:
                    return "Error length state - Invalid Field Entry";
                case -61:
                    return "Error length shipto state - Invalid Field Entry";
                case -62:
                    return "Error length order string - Invalid Field Entry";
                case -64:
                    return "Error invalid phone number - Invalid Field Entry";
                case -65:
                    return "Error empty name - Invalid Field Entry";
                case -66:
                    return "Error empty email - Invalid Field Entry";
                case -67:
                    return "Error empty street address - Invalid Field Entry";
                case -68:
                    return "Error empty city - Invalid Field Entry";
                case -69:
                    return "Error empty state - Invalid Field Entry";
                case -79:
                    return "Error length customer name - Invalid Field Entry";
                case -80:
                    return "Error length shipto customer name - Invalid Field Entry";
                case -81:
                    return "Error length customer location - Invalid Field Entry";
                case -82:
                    return "Error length customer state - Invalid Field Entry";
                case -83:
                    return "Error length shipto phone - Invalid Field Entry";
                case -84:
                    return "Pos error duplicate ordernumber Thank You for your Order";
                case -91:
                    return "Pos_error_CVV2 - Invalid Field Entry";
                case -92:
                    return "Pos_error_Error_Approval_Code - Invalid Field Entry";
                case -93:
                    return "Pos_error_Blind_Credits_Not_Allowed - Invalid Field Entry";
                case -94:
                    return "Pos_error_Blind_Credits_Failed - Invalid Field Entry";
                case -95:
                    return "Pos_error_Voice_Authorizations_Not_Allowed - Invalid Field Entry";
                default:
                    return "Unknown response code Response Code: " + this.sjReturnCode;
            }
        } catch (NumberFormatException e) {
            return "Number Format Exception in return code: " + this.sjReturnCode;
        }
    }

    public boolean isDuplicateProcessedTransaction() {
        return "-84".equals(this.getSjRequest());
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }
}

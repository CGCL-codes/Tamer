package com.google.checkout.orderprocessing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.google.checkout.AbstractCheckoutRequest;
import com.google.checkout.MerchantConstants;
import com.google.checkout.util.Constants;
import com.google.checkout.util.Utils;

/**
 * This class contains methods that construct &lt;send-buyer-message&gt; API
 * requests.
 */
public class SendBuyerMessageRequest extends AbstractCheckoutRequest {

    private Document document;

    private Element root;

    /**
	 * Constructor which takes an instance of MerchantConstants.
	 * 
	 * @param merchantConstants
	 *            The MerchantConstants.
	 * 
	 * @see MerchantConstants
	 */
    public SendBuyerMessageRequest(MerchantConstants merchantConstants) {
        super(merchantConstants);
        document = Utils.newEmptyDocument();
        root = (Element) document.createElementNS(Constants.checkoutNamespace, "send-buyer-message");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", Constants.checkoutNamespace);
        document.appendChild(root);
    }

    /**
	 * Constructor which takes an instance of MerchantConstants, Google Order
	 * Number and Message.
	 * 
	 * @param merchantConstants
	 *            The MerchantConstants.
	 * @param googleOrderNo
	 *            The Google Order Number.
	 * @param message
	 *            The Message.
	 * 
	 * @see MerchantConstants
	 */
    public SendBuyerMessageRequest(MerchantConstants merchantConstants, String googleOrderNo, String message) {
        this(merchantConstants);
        this.setGoogleOrderNo(googleOrderNo);
        this.setMessage(message);
    }

    /**
	 * Constructor which takes an instance of MerchantConstants, Google Order
	 * Number, Message and Send Email flag.
	 * 
	 * @param merchantConstants
	 *            The MerchantConstants.
	 * @param googleOrderNo
	 *            The Google Order Number.
	 * @param message
	 *            The Message.
	 * @param sendEmail
	 *            The Send Email flag.
	 * 
	 * @see MerchantConstants
	 */
    public SendBuyerMessageRequest(MerchantConstants merchantConstants, String googleOrderNo, String message, boolean sendEmail) {
        this(merchantConstants, googleOrderNo, message);
        this.setGoogleOrderNo(googleOrderNo);
        this.setMessage(message);
        this.setSendEmail(sendEmail);
    }

    /**
	 * Determine whether the message is within the string length limits.
	 * 
	 * @param message
	 *            The Message.
	 * @return True or false.
	 */
    public boolean isWithinMessageStringLimits(String message) {
        int lenStrMessage = message.length();
        if (lenStrMessage <= Constants.messageStrLimit) return true; else return false;
    }

    public String getXml() {
        return Utils.documentToString(document);
    }

    public String getXmlPretty() {
        return Utils.documentToStringPretty(document);
    }

    public String getPostUrl() {
        return merchantConstants.getRequestUrl();
    }

    /**
	 * Return the Google Order Number, which is the value of the
	 * google-order-number attribute on the root tag.
	 * 
	 * @return The Google Order Number.
	 */
    public String getGoogleOrderNo() {
        return root.getAttribute("google-order-number");
    }

    /**
	 * Return the message which is to be sent to the buyer. This is the value of
	 * the &lt;message&gt; tag.
	 * 
	 * @return The message.
	 */
    public String getMessage() {
        return Utils.getElementStringValue(document, root, "message");
    }

    /**
	 * True if an email is to be sent to the buyer. This is the value of the
	 * &lt;send-email&gt; tag.
	 * 
	 * @return The boolean value.
	 */
    public boolean isSendEmail() {
        return Utils.getElementBooleanValue(document, root, "send-email");
    }

    /**
	 * Set the Google Order Number, which is the value of the
	 * google-order-number attribute on the root tag.
	 * 
	 * @param googleOrderNo
	 *            The Google Order Number.
	 */
    public void setGoogleOrderNo(String googleOrderNo) {
        root.setAttribute("google-order-number", googleOrderNo);
    }

    /**
	 * Set the message which is to be sent to the customer. This is the value of
	 * the &lt;message&gt; tag.
	 * 
	 * @param message
	 *            The message.
	 */
    public void setMessage(String message) {
        if (!isWithinMessageStringLimits(message)) {
            message = "";
            System.err.println(Constants.messageErrorString);
        }
        Utils.findElementAndSetElseCreateAndSet(document, root, "message", message);
    }

    /**
	 * True if an email is to be sent to the buyer. This is the value of the
	 * &lt;send-email&gt; tag.
	 * 
	 * @param sendEmail
	 *            The boolean value.
	 */
    public void setSendEmail(boolean sendEmail) {
        Utils.findElementAndSetElseCreateAndSet(document, root, "send-email", sendEmail);
    }
}

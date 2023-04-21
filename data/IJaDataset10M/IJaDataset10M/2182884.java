package com.google.checkout.orderprocessing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.google.checkout.AbstractCheckoutRequest;
import com.google.checkout.MerchantConstants;
import com.google.checkout.util.Constants;
import com.google.checkout.util.Utils;

/**
 * This class contains methods that construct &lt;process-order&gt; API
 * requests.
 */
public class ProcessOrderRequest extends AbstractCheckoutRequest {

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
    public ProcessOrderRequest(MerchantConstants merchantConstants) {
        super(merchantConstants);
        document = Utils.newEmptyDocument();
        root = (Element) document.createElementNS(Constants.checkoutNamespace, "process-order");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", Constants.checkoutNamespace);
        document.appendChild(root);
    }

    /**
	 * Constructor which takes an instance of MerchantConstants and the Google
	 * Order Number.
	 * 
	 * @param merchantConstants
	 *            The MerchantConstants.
	 * @param googleOrderNo
	 *            The Google Order Number.
	 * 
	 * @see MerchantConstants
	 */
    public ProcessOrderRequest(MerchantConstants merchantConstants, String googleOrderNo) {
        this(merchantConstants);
        this.setGoogleOrderNo(googleOrderNo);
    }

    public String getXml() {
        return Utils.documentToString(document);
    }

    public String getXmlPretty() {
        return Utils.documentToStringPretty(document);
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
	 * Set the Google Order Number, which is the value of the
	 * google-order-number attribute on the root tag.
	 * 
	 * @param googleOrderNo
	 *            The Google Order Number.
	 */
    public void setGoogleOrderNo(String googleOrderNo) {
        root.setAttribute("google-order-number", googleOrderNo);
    }

    public String getPostUrl() {
        return merchantConstants.getRequestUrl();
    }
}

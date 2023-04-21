package com.google.checkout.util;

/**
 * @author ksim
 * @version 1.0 - ksim - March 6th, 2007 - Initial Version
 * @version 1.1 - ksim - March 10th, 2007 - Updated with more constants as
 *          necessary
 */
public class Constants {

    public static final String ampReplaceStr = "&#x26;";

    public static final String lessThanReplaceStr = "&#x3c;";

    public static final String greaterThanReplaceStr = "&#x3e;";

    public static final String ampStr = "&";

    public static final String lessThanStr = "<";

    public static final String greaterThanStr = ">";

    public static final String sandboxPostURL = "https://sandbox.google.com/checkout/cws/v2/Merchant/{0}/request";

    public static final String checkoutPostURL = "https://checkout.google.com/cws/v2/Merchant/{0}/request";

    public static final String checkoutNamespace = "http://checkout.google.com/schema/2";

    public static final int refundStrLimit = 140;

    public static final String refundErrorString = new StringBuffer("The refund string limits have been exceeded.  The reason and comment cannot exceed ").append(refundStrLimit).append(" characters.").toString();

    public static final int cancelStrLimit = 140;

    public static final String cancelErrorString = new StringBuffer("The cancel string limits have been exceeded.  The reason and comment cannot exceed ").append(refundStrLimit).append(" characters.").toString();

    public static final int messageStrLimit = 255;

    public static final String messageErrorString = new StringBuffer("The message string limits have been exceeded.  The message cannot exceed ").append(messageStrLimit).append(" characters.").toString();
}

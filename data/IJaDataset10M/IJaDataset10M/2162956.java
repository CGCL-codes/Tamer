package org.zoolu.sip.header;

/** SipHeaders extends class sip.header.SipHeaders by adding new SIP header names. */
public class SipHeaders extends BaseSipHeaders {

    /** String "Refer-To" */
    public static final String Refer_To = "Refer-To";

    /** Whether <i>str</i> is "Refer-To" */
    public static boolean isReferTo(String str) {
        return same(str, Refer_To);
    }

    /** String "Referred-By" */
    public static final String Referred_By = "Referred-By";

    /** Whether <i>str</i> is "Referred-By" */
    public static boolean isReferredBy(String str) {
        return same(str, Referred_By);
    }

    /** String "Event" */
    public static final String Event = "Event";

    /** String "o" */
    public static final String Event_short = "o";

    /** Whether <i>str</i> is an Event field */
    public static boolean isEvent(String str) {
        return same(str, Event) || same(str, Event_short);
    }

    /** String "Allow-Events" */
    public static final String Allow_Events = "Allow-Events";

    /** Whether <i>str</i> is "Allow-Events" */
    public static boolean isAllowEvents(String str) {
        return same(str, Allow_Events);
    }

    /** String "Subscription-State" */
    public static final String Subscription_State = "Subscription-State";

    /** Whether <i>str</i> is an Subscription_State field */
    public static boolean isSubscriptionState(String str) {
        return same(str, Subscription_State);
    }
}

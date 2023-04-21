package twilio.client;

public class CallFlags {

    public static final int INBOUND = 1;

    public static final int INITIATED_BY_API = 2;

    public static final int INITIATED_BY_DIAL_VERB = 4;

    public static boolean callWasInbound(int flags) {
        return (flags | INBOUND) == INBOUND;
    }

    public static boolean callWasInitiatedByApi(int flags) {
        return (flags | INITIATED_BY_API) == INITIATED_BY_API;
    }

    public static boolean callWasInitiatedByDialVerb(int flags) {
        return (flags | INITIATED_BY_DIAL_VERB) == INITIATED_BY_DIAL_VERB;
    }
}

package org.quickfix;

public class DefaultMessageFactory implements org.quickfix.MessageFactory {

    private org.quickfix.fix40.MessageFactory fix40Factory = new org.quickfix.fix40.MessageFactory();

    private org.quickfix.fix41.MessageFactory fix41Factory = new org.quickfix.fix41.MessageFactory();

    private org.quickfix.fix42.MessageFactory fix42Factory = new org.quickfix.fix42.MessageFactory();

    public Message create(String beginString, String msgType) {
        if ("FIX.4.0".equals(beginString)) {
            return fix40Factory.create(beginString, msgType);
        }
        if ("FIX.4.1".equals(beginString)) {
            return fix41Factory.create(beginString, msgType);
        }
        if ("FIX.4.2".equals(beginString)) {
            return fix42Factory.create(beginString, msgType);
        }
        return new Message();
    }
}

package org.privale.utils.network;

public class IntStringSendTrans extends IntSendTrans {

    public IntStringSendTrans(QueueHandler hand, Transaction next, String str) {
        super(hand, next, 0);
        StringSendTrans s = new StringSendTrans(hand, next, str);
        super.setNext(s);
        Setup(str);
    }

    public void setNext(Transaction trans) {
        StringSendTrans s = (StringSendTrans) getNext();
        s.setNext(trans);
    }

    public void Setup(String str) {
        if (str != null) {
            Setup(str.length());
            StringSendTrans s = (StringSendTrans) getNext();
            s.Setup(str);
        }
    }
}

package org.privale.utils.network;

public class IntBufReadTrans extends IntReadTrans implements ReadComplete {

    private QueueHandler Hand;

    private Transaction Next;

    private ReadComplete Comp;

    public IntBufReadTrans(QueueHandler hand, Transaction next, ReadComplete comp) {
        super(hand, next, comp);
        Hand = hand;
        Next = next;
        Comp = comp;
        Reset();
        super.setReadComplete(this);
    }

    public void Reset() {
        super.Reset();
        BufReadTrans br = new BufReadTrans(Hand, Next, 10, Comp);
        super.setNext(br);
    }

    public void setext(Transaction trans) {
        BufReadTrans br = (BufReadTrans) getNext();
        br.setNext(trans);
    }

    public void setReadComplete(ReadComplete comp) {
        BufReadTrans br = (BufReadTrans) getNext();
        br.setReadComplete(comp);
    }

    public void Complete(Transaction trans, Object readobj) {
        int len = (Integer) readobj;
        if (len <= 0) {
            super.setNext(Next);
        } else {
            BufReadTrans br = (BufReadTrans) getNext();
            br.Setup(len);
        }
    }
}

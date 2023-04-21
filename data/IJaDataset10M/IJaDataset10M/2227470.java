package benchmark.cute.original.concurrent.tmn;

import benchmark.cute.original.concurrent.Message;

public class TMNMessage extends Message {

    public int from;

    public int to;

    public int dest;

    public int nonce;

    public int key;

    public TMNMessage() {
        invalidate();
    }

    public void invalidate() {
        to = TMN.EMPTY;
        from = TMN.EMPTY;
        nonce = TMN.EMPTY;
    }

    public void print() {
        System.out.print("from ");
        TMN.print_id(from);
        System.out.print(" to ");
        TMN.print_id(to);
        System.out.print(" dest ");
        TMN.print_id(dest);
        System.out.print(" nonce ");
        TMN.print_id(nonce);
        System.out.print(" key ");
        TMN.print_id(key);
        System.out.print("\n");
    }
}

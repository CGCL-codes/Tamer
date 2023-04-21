package org.openemed.LQS;

public final class NoCommonSubtype extends Exception {

    public NoCommonSubtype() {
    }

    public NoCommonSubtype(String _reason) {
        super(_reason);
    }
}

package com.dukesoftware.utils.datamodel.history;

import com.dukesoftware.utils.datamodel.history.HNode.NullHNode;

public class HConnection {

    private final HNode from;

    private final HNode to;

    private HConnection nextConnection;

    private HConnection prevConnection;

    public HConnection(HNode from, HNode to) {
        this.from = from;
        this.to = to;
        this.nextConnection = NullHConnection.getInstance();
        this.prevConnection = NullHConnection.getInstance();
    }

    public void setNextConnection(HConnection nextConnection) {
        this.nextConnection = nextConnection;
    }

    public void setPrevConnection(HConnection prevConnection) {
        this.prevConnection = prevConnection;
    }

    public HConnection getNextConnection() {
        return nextConnection;
    }

    public HConnection getPrevConnection() {
        return prevConnection;
    }

    @Override
    public String toString() {
        return from.toString() + "->" + to.toString();
    }

    public static final class NullHConnection extends HConnection {

        private static final NullHConnection singleton = new NullHConnection();

        public static final NullHConnection getInstance() {
            return singleton;
        }

        private NullHConnection() {
            super(NullHNode.getInstance(), NullHNode.getInstance());
            super.setPrevConnection(this);
            super.setNextConnection(this);
        }

        public final void setNextConnection(HConnection nextConnection) {
        }

        public final void setPrevConnection(HConnection nextConnection) {
        }
    }
}

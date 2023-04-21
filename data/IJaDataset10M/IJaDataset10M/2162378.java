package drcl.intserv.scheduler;

import drcl.intserv.*;

public class SpecR_GR extends SpecR implements SpecR_Direct {

    public int bw;

    public int buffer = 0;

    int mtu = 0;

    public SpecR_GR() {
    }

    public SpecR_GR(int bw_, int buffer_, int mtu_) {
        bw = bw_;
        buffer = buffer_;
        mtu = mtu_;
    }

    public int getBuffer() {
        return buffer;
    }

    public void setBuffer(int buffer_) {
        buffer = buffer_;
    }

    public void setBW(int bw_) {
        bw = bw_;
    }

    public int getBW() {
        return bw;
    }

    public SpecR merge(SpecR rspec_) {
        if (!(rspec_ instanceof SpecR_GR)) return null;
        SpecR_GR tmp_ = (SpecR_GR) rspec_;
        bw = Math.max(bw, tmp_.bw);
        buffer = Math.max(buffer, tmp_.buffer);
        return this;
    }

    public int compareWith(SpecR rspec_) {
        if (!(rspec_ instanceof SpecR_GR)) return Integer.MAX_VALUE;
        SpecR_GR tmp_ = (SpecR_GR) rspec_;
        if (bw == tmp_.bw && buffer == tmp_.buffer) return 0;
        if (bw > tmp_.bw && buffer > tmp_.buffer) return 1;
        if (bw < tmp_.bw && buffer < tmp_.buffer) return -1;
        return Integer.MAX_VALUE;
    }

    public void duplicate(Object source_) {
        if (!(source_ instanceof SpecR_GR)) return;
        SpecR_GR that_ = (SpecR_GR) source_;
        bw = that_.bw;
        buffer = that_.buffer;
        mtu = that_.mtu;
    }

    public void perHopAdjust() {
        buffer -= mtu;
    }

    public String toString() {
        return "handle=" + handle + ",bw=" + getBW() + ",buffer=" + getBuffer() + ",MTU=" + mtu + ",activated=" + activated;
    }
}

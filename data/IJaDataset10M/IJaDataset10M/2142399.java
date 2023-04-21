package ow.routing.koorde;

import java.math.BigInteger;
import ow.id.ID;
import ow.id.IDAddressPair;
import ow.routing.linearwalker.LinearWalkerRoutingContext;

public class KoordeRoutingContext extends LinearWalkerRoutingContext {

    private final int digitBits;

    ID kshift;

    ID i;

    int topBitsOfLastKshift;

    private IDAddressPair ignoredNode = null;

    KoordeRoutingContext(ID kshift, ID i, int digitBits) {
        this(kshift, i, 0, digitBits);
    }

    KoordeRoutingContext(int digitBits) {
        this.digitBits = digitBits;
        this.topBitsOfLastKshift = 0;
    }

    private KoordeRoutingContext(ID kshift, ID i, int topBitsOfLastKshift, int digitBits) {
        this.kshift = kshift;
        this.i = i;
        this.topBitsOfLastKshift = topBitsOfLastKshift;
        this.digitBits = digitBits;
    }

    protected KoordeRoutingContext(KoordeRoutingContext c) {
        super(c);
        this.kshift = c.kshift;
        this.i = c.i;
        this.topBitsOfLastKshift = c.topBitsOfLastKshift;
        this.digitBits = c.digitBits;
    }

    public ID getKshift() {
        return this.kshift;
    }

    public ID getI() {
        return this.i;
    }

    public int getTopBitsOfLastKshift() {
        return this.topBitsOfLastKshift;
    }

    public void update() {
        int idSize = this.kshift.getSize();
        ID nextKshift;
        ID nextI;
        int topBitsOfKshift = this.kshift.getBits(idSize * 8 - this.digitBits, this.digitBits);
        nextKshift = this.kshift.shiftLeft(this.digitBits);
        BigInteger iInteger = this.i.toBigInteger().shiftLeft(this.digitBits).or(BigInteger.valueOf(topBitsOfKshift));
        nextI = ID.getID(iInteger, idSize);
        this.kshift = nextKshift;
        this.i = nextI;
        this.topBitsOfLastKshift = topBitsOfKshift;
    }

    public void uninitialize() {
        this.kshift = null;
        this.i = null;
        this.topBitsOfLastKshift = -1;
    }

    public boolean isInitialized() {
        return this.kshift != null;
    }

    public IDAddressPair getIgnoredNode() {
        return this.ignoredNode;
    }

    public IDAddressPair setIgnoredNode(IDAddressPair p) {
        IDAddressPair old = this.ignoredNode;
        this.ignoredNode = p;
        return old;
    }

    public int hashCode() {
        return this.kshift.hashCode() ^ this.i.hashCode();
    }

    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (o instanceof KoordeRoutingContext) {
            KoordeRoutingContext c = (KoordeRoutingContext) o;
            if (this.isInitialized()) {
                if (c.isInitialized() && this.kshift.equals(c.kshift) && this.i.equals(c.i)) return true;
            } else if (!c.isInitialized()) return true;
        }
        return false;
    }

    public String toString() {
        return this.toString(0);
    }

    public String toString(int verboseLevel) {
        StringBuilder sb = new StringBuilder();
        sb.append("[kshift:").append(this.kshift.toString(verboseLevel)).append(",i:").append(this.i.toString(verboseLevel)).append("]");
        return sb.toString();
    }

    public KoordeRoutingContext clone() {
        return new KoordeRoutingContext(this);
    }
}

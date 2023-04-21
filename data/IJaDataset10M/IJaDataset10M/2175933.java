package net.sf.signs.intermediate;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import net.sf.signs.*;
import net.sf.signs.gates.InterpreterComponent;

@SuppressWarnings("serial")
public class OperationMUX extends Operation {

    private static final boolean dump = false;

    private Operation a, b, s;

    public OperationMUX(Operation a_, Operation b_, Operation s_, IntermediateObject parent_, SourceLocation location_) {
        super(parent_, location_);
        a = a_;
        b = b_;
        s = s_;
        a.setParent(this);
        b.setParent(this);
        s.setParent(this);
    }

    @Override
    protected SigType doComputeType(SigType typeHint_, OperationCache cache_) throws SignsException {
        SigType type;
        SigType at = a.computeType(typeHint_, cache_);
        SigType bt = b.computeType(typeHint_, cache_);
        if (!at.isCompatible(bt)) throw new SignsException("Type mismatch in mux operands.", location);
        SigType st = s.computeType(SigType.bit, cache_);
        if (st.getWidth() != 1) throw new SignsException("MUX sel not boolean (bit).", location);
        type = at;
        if (type == null) {
            System.out.println("Alert! failed to compute type.");
        }
        return type;
    }

    @Override
    public Signal doElaborate(NetList nl_, OperationCache cache_) throws SignsException {
        if (dump) System.out.println("elaborating: MUX @" + this.hashCode() + " cnt:" + cnt);
        if (s.isConstant(cache_)) {
            int sel = s.getInt(cache_);
            if (sel == 0) return a.elaborate(cache_); else return b.elaborate(cache_);
        } else {
            SignalBit sel = (SignalBit) s.elaborate(cache_);
            Signal res = null;
            if (a.isConstant(cache_)) {
                String s = a.getStringConstant(cache_);
                Signal sb = b.elaborate(cache_);
                SigType type = sb.getType();
                if (sb instanceof SignalAggregate) {
                    SignalAggregate resbus = new SignalAggregate(nl_, null, type, null, this);
                    nl_.add(resbus);
                    SignalAggregate bbus = (SignalAggregate) sb;
                    int w = sb.getType().getWidth();
                    for (int i = 0; i < w; i++) {
                        char c = s.charAt(i);
                        SignalBit bbit = bbus.getSignal(i);
                        if (c == Value.BIT_Z) resbus.add(nl_.placeBTS4(bbit, sel, this)); else resbus.add(nl_.placeMux(nl_.placeLiteral(c, this), bbit, sel, this));
                    }
                    res = resbus;
                } else {
                    SignalBit bbit = (SignalBit) sb;
                    char c = s.charAt(0);
                    if (c == Value.BIT_Z) res = nl_.placeBTS4(bbit, sel, this); else {
                        res = nl_.placeMux(nl_.placeLiteral(c, this), bbit, sel, this);
                    }
                }
            } else {
                Signal sa = a.elaborate(cache_);
                Signal sb = b.elaborate(cache_);
                Signal ss = s.elaborate(cache_);
                res = nl_.placeMux(sa, sb, ss, this);
            }
            return res;
        }
    }

    public SignalBit elaborateAsTarget(NetList nl) throws SemanticException, SignsException, SignsException {
        throw new SemanticException("elaborate error: not a valid/implemented target. (OperationMUX)", location);
    }

    @Override
    public Clock getClock(OperationCache cache_) throws SignsException {
        return null;
    }

    @Override
    public boolean isConstant(OperationCache cache_) {
        return false;
    }

    @Override
    public String getStringConstant(OperationCache cache_) throws SignsException {
        return null;
    }

    @Override
    public BigInteger getBigIntConstant(OperationCache cache_) throws SignsException {
        return null;
    }

    protected Operation cloneOperation(ArrayList expOperands_) {
        Operation a = (Operation) expOperands_.get(0);
        Operation b = (Operation) expOperands_.get(1);
        Operation s = (Operation) expOperands_.get(2);
        return new OperationMUX(a, b, s, getParent(), location);
    }

    public String toString() {
        return "OperationMUX(a=" + a + ", b=" + b + ", s=" + s + "@" + this.hashCode() + " cnt: " + this.cnt + ")";
    }

    @Override
    public void dump(PrintStream out, int i) {
        printSpaces(out, i);
        out.println("OperationMUX, cnt=" + cnt);
        s.dump(out, i + 2);
        a.dump(out, i + 2);
        b.dump(out, i + 2);
    }

    @Override
    public void generateCode(InterpreterComponent interpreter_, OperationCache cache_) throws SignsException {
        throw new SignsException("Sorry, OperationMUX.generateCode is not implemented yet.", location);
    }

    @Override
    public boolean containsVars(Bindings bindings_) throws SignsException {
        if (a.containsVars(bindings_)) return true;
        if (b.containsVars(bindings_)) return true;
        if (s.containsVars(bindings_)) return true;
        return false;
    }

    @Override
    public Operation resolveVars(Bindings bindings_, Clock clock_, OperationCache cache_) throws SignsException {
        if (!containsVars(bindings_)) return this;
        Operation a = this.a.resolveVars(bindings_, clock_, cache_);
        Operation b = this.b.resolveVars(bindings_, clock_, cache_);
        Operation s = this.s.resolveVars(bindings_, clock_, cache_);
        return new OperationMUX(a, b, s, getParent(), getLocation());
    }

    @Override
    public int getNumChildren() {
        return 3;
    }

    @Override
    public IntermediateObject getChild(int idx_) {
        switch(idx_) {
            case 0:
                return a;
            case 1:
                return b;
            case 2:
                return s;
        }
        return null;
    }

    @Override
    public boolean isSynthesizable() {
        if (!a.isSynthesizable()) return false;
        if (!b.isSynthesizable()) return false;
        if (!s.isSynthesizable()) return false;
        return true;
    }
}

package net.sf.signs.intermediate;

import java.math.BigInteger;
import net.sf.signs.NetList;
import net.sf.signs.SignsException;
import net.sf.signs.SigType;
import net.sf.signs.Signal;
import net.sf.signs.SourceLocation;
import net.sf.signs.gates.InterpreterComponent;

@SuppressWarnings("serial")
public class OperationSignal extends Operation {

    private Signal s;

    public OperationSignal(Signal s_, IntermediateObject parent_, SourceLocation location_) {
        super(parent_, location_);
        s = s_;
    }

    @Override
    protected SigType doComputeType(SigType typeHint_, OperationCache cache_) throws SignsException {
        return s.getType();
    }

    @Override
    public Signal doElaborate(NetList nl_, OperationCache cache_) throws SignsException {
        return s;
    }

    @Override
    public void generateCode(InterpreterComponent interpreter_, OperationCache cache_) throws SignsException {
        throw new SignsException("Sorry, OperationSignal.generateCode not implemented yet.", location);
    }

    @Override
    public boolean isConstant(OperationCache cache_) throws SignsException, SignsException {
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

    @Override
    public Clock getClock(OperationCache cache_) throws SignsException {
        return null;
    }

    @Override
    public boolean containsVars(Bindings bindings_) throws SignsException {
        return false;
    }

    @Override
    public Operation resolveVars(Bindings bindings_, Clock clock_, OperationCache cache_) throws SignsException {
        return this;
    }

    public String toString() {
        return "OperationSignal(" + s + ")" + "@" + Integer.toHexString(hashCode()) + " cnt=" + cnt;
    }

    @Override
    public int getNumChildren() {
        return 0;
    }

    @Override
    public IntermediateObject getChild(int idx_) {
        return null;
    }

    @Override
    public boolean isSynthesizable() {
        return true;
    }
}

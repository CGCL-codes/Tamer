package net.sf.signs.intermediate;

import net.sf.signs.SignsException;
import net.sf.signs.SigType;
import net.sf.signs.Signal;
import net.sf.signs.SourceLocation;
import net.sf.signs.gates.InterpreterComponent;

@SuppressWarnings("serial")
public class NameExtensionAttribute extends NameExtension {

    private String id;

    public NameExtensionAttribute(String id_, Signature signature_, Operation exp_, IntermediateObject parent_, SourceLocation location_) {
        super(parent_, location_);
        id = id_;
    }

    public SigType computeType(SigType type_, OperationCache cache_) throws SignsException {
        if (id.equals("EVENT")) return SigType.bit;
        if (id.equals("DELAYED")) return SigType.bit;
        if (id.equals("STABLE")) return SigType.bit;
        throw new SignsException("NameExtensionAttribute.computeType not implemented yet.\nSorry.", location);
    }

    public Signal elaborate(Signal s_, OperationCache cache_) throws SignsException {
        throw new SignsException("NameExtensionAttribute.elaborate not implemented yet.\nSorry.", location);
    }

    public Operation elaborate(Operation s_, Bindings bindings_, Clock clock_, OperationCache cache_) throws SignsException {
        throw new SignsException("NameExtensionAttribute.elaborate not implemented yet.\nSorry.", location);
    }

    public String getId() {
        return id;
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
    public int computeOffset(int off_) throws SignsException {
        throw new SignsException("Sorry, not implemented yet.", location);
    }

    @Override
    public Operation computeEnable(int off_, Operation enable_, Bindings bindings_, Clock clock_, OperationCache cache_) throws SignsException {
        throw new SignsException("Sorry, not implemented yet.", location);
    }

    @Override
    public boolean containsVars(Bindings bindings_) throws SignsException {
        return false;
    }

    @Override
    public void generateCode(InterpreterComponent comp_, OperationCache cache_) throws SignsException {
        comp_.addStmtAttribute(id, this);
    }

    @Override
    public void generateCodeAsTarget(InterpreterComponent interpreter_, OperationCache cache_) throws SignsException {
        throw new SignsException("Sorry, not implemented yet.", location);
    }

    @Override
    public boolean isSynthesizable() {
        return false;
    }
}

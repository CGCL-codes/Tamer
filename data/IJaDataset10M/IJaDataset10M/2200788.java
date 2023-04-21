package net.sf.signs.intermediate;

import java.io.PrintStream;
import net.sf.signs.*;

@SuppressWarnings("serial")
public class SignalDeclaration extends BlockDeclarativeItem {

    public static final int KIND_NONE = 0;

    public static final int KIND_REGISTER = 1;

    public static final int KIND_BUS = 2;

    private TypeDefinition td;

    private Operation initialValue;

    public SignalDeclaration(String id_, TypeDefinition td_, int kind_, Operation initialValue_, IntermediateObject parent_, SourceLocation location_) {
        super(id_, parent_, location_);
        setType(td_);
        location = location_;
        initialValue = initialValue_;
        if (initialValue != null) initialValue.setParent(this);
    }

    public TypeDefinition getType() {
        return td;
    }

    public void setId(String string) {
        id = string;
    }

    public void setType(TypeDefinition td_) {
        td = td_;
        td.setParent(this);
    }

    @Override
    public void elaborate(OperationCache cache_) throws SignsException {
        SigType type = td.elaborate(cache_);
        Signal s = getResolver().createSignal(id, type, this, cache_.getNetList());
        if (initialValue != null) {
            initialValue.computeType(type, cache_);
            String value = Value.convert(initialValue.getBigIntConstant(cache_), type.getWidth());
            if (s instanceof SignalAggregate) {
                SignalAggregate bus = (SignalAggregate) s;
                int n = bus.getNumSignals();
                for (int i = 0; i < n; i++) {
                    SignalBit bit = bus.getSignal(i);
                    bit.setInitialValue(value.charAt(n - i - 1));
                }
            } else {
                SignalBit bit = (SignalBit) s;
                bit.setInitialValue(value.charAt(0));
            }
        }
    }

    public void dump() {
        System.out.println("  id=" + id + " type=" + td);
    }

    public void dump(PrintStream out_) {
        out_.println("Signal Declaration id=" + id + " type=" + td);
    }

    @Override
    public int getNumChildren() {
        return 2;
    }

    @Override
    public IntermediateObject getChild(int idx_) {
        switch(idx_) {
            case 0:
                return initialValue;
            case 1:
                return td;
        }
        return null;
    }
}

package net.sf.signs.intermediate;

import java.util.ArrayList;
import net.sf.signs.SigType;
import net.sf.signs.SignsException;
import net.sf.signs.SourceLocation;
import net.sf.signs.Value;
import net.sf.signs.gates.InterpreterComponent;

@SuppressWarnings("serial")
public class Waveform extends IntermediateObject {

    private ArrayList<WaveformElement> elements;

    public Waveform(IntermediateObject parent_, SourceLocation location_) {
        super(parent_, location_);
        elements = new ArrayList<WaveformElement>();
    }

    public Waveform(WaveformElement we_, IntermediateObject parent_, SourceLocation location_) {
        this(parent_, location_);
        add(we_);
    }

    public void add(WaveformElement we_) {
        elements.add(we_);
        we_.setParent(this);
    }

    public int getNumElements() {
        return elements.size();
    }

    public WaveformElement getElement(int idx_) {
        return (WaveformElement) elements.get(idx_);
    }

    public boolean isSynthesizable() {
        int n = getNumElements();
        if (n != 1) return false;
        return getElement(0).isSynthesizable();
    }

    public void generateCode(Target target_, InterpreterComponent interpreter_, OperationCache cache_) throws SignsException {
        long delayOffset = 0;
        int n = getNumElements();
        for (int i = 0; i < n; i++) {
            WaveformElement we = getElement(i);
            Operation delay = we.getDelay();
            if (delay != null) {
                SigType type = delay.computeType(SigType.timeType, cache_);
                long d = delay.getLong(cache_) - delayOffset;
                if (d < 0) throw new SignsException("Delay clauses have to be in ascending order.", we.getLocation());
                interpreter_.addStmtPush(Value.convert(d, type.getWidth()), type, we);
                interpreter_.addStmtWaitFor(we);
                delayOffset += d;
            } else if (delayOffset > 0) {
                throw new SignsException("Delay clauses have to be in ascending order.", we.getLocation());
            }
            SigType type = target_.computeType(cache_);
            we.computeType(type, cache_);
            Operation value = we.getValue();
            target_.generateCode(interpreter_, cache_);
            value.generateCode(interpreter_, cache_);
            interpreter_.addStmtDrive(this);
        }
    }

    @Override
    public int getNumChildren() {
        return elements.size();
    }

    @Override
    public IntermediateObject getChild(int idx_) {
        return elements.get(idx_);
    }
}

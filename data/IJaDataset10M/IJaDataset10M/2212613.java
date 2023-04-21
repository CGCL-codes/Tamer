package emulator.hardware.bits;

public class SharedCollectorBit extends SharedBit {

    BitCollector collector;

    int index;

    public SharedCollectorBit(BitCollector collector, int index) {
        this.collector = collector;
        this.index = index;
    }

    @Override
    public synchronized boolean getValue() {
        return super.getValue();
    }

    @Override
    public synchronized void setValue(boolean value) {
        super.setValue(value);
        collector.setBit(index, value);
    }

    @Override
    public synchronized void toggle() {
        super.toggle();
        collector.setBit(index, getValue());
    }
}

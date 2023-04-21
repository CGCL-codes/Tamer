package nl.kbna.dioscuri.module.cpu32;

public abstract class RealModeTemplateBlock implements RealModeCodeBlock {

    protected static final ProcessorException exceptionDE = new ProcessorException(Processor.PROC_EXCEPTION_DE, true);

    protected static final ProcessorException exceptionGP = new ProcessorException(Processor.PROC_EXCEPTION_GP, true);

    protected static final ProcessorException exceptionSS = new ProcessorException(Processor.PROC_EXCEPTION_SS, true);

    protected static final ProcessorException exceptionUD = new ProcessorException(Processor.PROC_EXCEPTION_UD, true);

    protected static final boolean[] parityMap;

    static {
        parityMap = new boolean[256];
        for (int i = 0; i < 256; i++) {
            boolean val = true;
            for (int j = 0; j < 8; j++) if ((0x1 & (i >> j)) == 1) val = !val;
            parityMap[i] = val;
        }
    }

    public String getDisplayString() {
        return getClass().getName();
    }

    public boolean handleMemoryRegionChange(int startAddress, int endAddress) {
        return false;
    }

    public String toString() {
        return "ByteCodeCompiled RealModeUBlock";
    }
}

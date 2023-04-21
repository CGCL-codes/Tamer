package emulator.support;

public interface OperandEval {

    String getDetails(CpuState cpu_state);

    boolean isAddress();

    int getAddress(CpuState cpu_state);

    boolean isIndirect();
}

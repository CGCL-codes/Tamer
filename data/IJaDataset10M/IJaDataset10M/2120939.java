package com.lambelly.lambnes.platform.cpu;

/**
 *
 * @author thomasmccarthy
 */
public interface CentralProcessingUnit {

    public int processNextInstruction();

    public int getAccumulator();

    public void setAccumulator(int accumulator);

    public int getX();

    public void setX(int x);

    public int getY();

    public void setY(int y);

    public Flags getFlags();

    public void setFlags(Flags flags);

    public void pushStatus(boolean brk);

    public void pullStatus();

    public void resetRegisters();
}

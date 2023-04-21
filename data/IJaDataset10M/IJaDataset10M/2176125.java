package mcujavasource.mcu;

/**
 *
 */
public abstract class Spi {

    private boolean transferCompletedFired;

    private boolean enabled;

    private boolean lsbFirst;

    private boolean master;

    private boolean inversedClockPolarity;

    private boolean inversedClockPhase;

    private int prescaling;

    private int data;

    public abstract void addSpiListener(SpiListener listener);

    public abstract void init(boolean master);

    public abstract int simpleIo(int data);

    public boolean isTransferCompletedFired() {
        return transferCompletedFired;
    }

    public void setTransferCompletedFired(boolean transferCompletedFired) {
        this.transferCompletedFired = transferCompletedFired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLsbFirst() {
        return lsbFirst;
    }

    @InitialConfiguration
    public void setLsbFirst(boolean lsbFirst) {
        this.lsbFirst = lsbFirst;
    }

    public boolean isMaster() {
        return master;
    }

    @InitialConfiguration
    public void setMaster(boolean master) {
        this.master = master;
    }

    public boolean isInversedClockPolarity() {
        return inversedClockPolarity;
    }

    @InitialConfiguration
    public void setInversedClockPolarity(boolean inversedClockPolarity) {
        this.inversedClockPolarity = inversedClockPolarity;
    }

    public boolean isInversedClockPhase() {
        return inversedClockPhase;
    }

    @InitialConfiguration
    public void setInversedClockPhase(boolean inversedClockPhase) {
        this.inversedClockPhase = inversedClockPhase;
    }

    @InitialConfiguration
    public void setPrescaling(int prescaling) {
        this.prescaling = prescaling;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}

package bagaturchess.search.impl.uci_adaptor.timemanagement.controllers;

import bagaturchess.search.impl.uci_adaptor.timemanagement.ITimeController;

public abstract class TimeController_BaseImpl implements ITimeController {

    private long startTime;

    public TimeController_BaseImpl() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void newIteration() {
    }

    @Override
    public void newPVLine(int eval, int depth, int move) {
    }

    @Override
    public boolean hasTime() {
        return true;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        String result = "";
        result += "TimeController[" + this.getClass().getName() + "] " + "tillnow=" + (System.currentTimeMillis() - getStartTime());
        return result;
    }
}

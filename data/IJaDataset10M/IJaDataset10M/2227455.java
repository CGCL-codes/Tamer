package org.dancres.blitz.entry;

/**
   Manages operation and instance counters
 */
class CountersImpl implements Counters {

    private OpSwitchListener theOpSwitchListener;

    private InstanceSwitchListener theInstanceSwitchListener;

    CountersImpl(String aType, int anInitialCount) {
        theOpSwitchListener = new OpSwitchListener(aType);
        theInstanceSwitchListener = new InstanceSwitchListener(aType, anInitialCount);
    }

    public int getInstanceCount() {
        return theInstanceSwitchListener.getTotal();
    }

    public void didRead() {
        theOpSwitchListener.didRead();
    }

    public void didTake() {
        theOpSwitchListener.didTake();
        theInstanceSwitchListener.took();
    }

    public void didWrite() {
        theOpSwitchListener.didWrite();
        theInstanceSwitchListener.wrote();
    }

    public void didPurge() {
        theInstanceSwitchListener.took();
    }

    void destroy() {
        theOpSwitchListener.destroy();
        theInstanceSwitchListener.destroy();
    }
}

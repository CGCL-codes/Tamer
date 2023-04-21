package core;

/**
 * @autor MasterM
 * @see
 * 
 * */
public class Timer {

    private Game parent;

    private long timerID;

    private long interval;

    private long timeUntilTrigger;

    private boolean enabled;

    public Timer(Game newParent, long newTimerID, long newInterval) {
        parent = newParent;
        timerID = newTimerID;
        interval = newInterval;
        timeUntilTrigger = interval;
        enabled = true;
    }

    public long GetTimerID() {
        return timerID;
    }

    public long GetInterval() {
        return interval;
    }

    public void SetInterval(long newInterval) {
        interval = newInterval;
        Reset();
    }

    public void Reset() {
        timeUntilTrigger = interval;
    }

    public boolean IsEnabled() {
        return enabled;
    }

    public void Enable() {
        enabled = true;
    }

    public void Disable() {
        enabled = false;
    }

    public void Update(long timeDiff) {
        if (!enabled) return;
        if (timeUntilTrigger <= timeDiff) {
            timeDiff %= interval;
            timeUntilTrigger = interval - timeDiff;
            parent.GetInput().QueueEvent(EventType.EventType_Timer, this);
        } else timeUntilTrigger -= timeDiff;
    }
}

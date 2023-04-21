package org.jpc.j2se;

import org.jpc.emulator.*;
import org.jpc.support.Clock;
import java.io.*;
import org.javanile.wrapper.java.util.PriorityQueue;
import org.javanile.wrapper.java.util.logging.*;
import org.jpc.emulator.processor.Processor;

/**
 * 
 * @author Ian Preston
 */
public class VirtualClock extends AbstractHardwareComponent implements Clock {

    public static final long IPS = Processor.IPS;

    public static final long NSPI = 10 * 1000000000L / IPS;

    private static final Logger LOGGING = Logger.getLogger(VirtualClock.class.getName());

    private PriorityQueue timers;

    private volatile boolean ticksEnabled;

    private long ticksOffset;

    private long ticksStatic;

    private long currentTime;

    private long totalTicks = 0;

    private static final boolean REALTIME = false;

    public VirtualClock() {
        timers = new PriorityQueue(20);
        ticksEnabled = false;
        ticksOffset = 0;
        ticksStatic = 0;
        currentTime = getSystemTimer();
    }

    public void saveState(DataOutput output) throws IOException {
        output.writeBoolean(ticksEnabled);
        output.writeLong(ticksOffset);
        output.writeLong(getTime());
    }

    public void loadState(DataInput input, PC pc) throws IOException {
        ticksEnabled = input.readBoolean();
        ticksOffset = input.readLong();
        ticksStatic = input.readLong();
    }

    public synchronized Timer newTimer(TimerResponsive object) {
        Timer tempTimer = new Timer(object, this);
        return tempTimer;
    }

    private boolean process() {
        Timer tempTimer;
        tempTimer = timers.peek();
        if ((tempTimer == null) || !tempTimer.check(getTime())) return false; else return true;
    }

    public synchronized void update(Timer object) {
        timers.remove(object);
        if (object.enabled()) {
            timers.offer(object);
        }
    }

    public long getTime() {
        if (ticksEnabled) {
            return this.getRealTime() + ticksOffset;
        } else {
            return ticksStatic;
        }
    }

    private long getRealTime() {
        return currentTime;
    }

    public long getTickRate() {
        return IPS * 10;
    }

    public long getTicks() {
        return totalTicks;
    }

    public void pause() {
        if (ticksEnabled) {
            ticksStatic = getTime();
            ticksEnabled = false;
        }
    }

    public void resume() {
        if (!ticksEnabled) {
            ticksOffset = ticksStatic - getRealTime();
            ticksEnabled = true;
        }
    }

    public void reset() {
        this.pause();
        ticksOffset = 0;
        ticksStatic = 0;
    }

    public String toString() {
        return "Virtual Clock";
    }

    private long getSystemTimer() {
        return System.nanoTime();
    }

    public void updateNowAndProcess() {
        if (REALTIME) {
            currentTime = getSystemTimer();
            if (process()) {
                return;
            }
            Timer tempTimer;
            synchronized (this) {
                tempTimer = timers.peek();
            }
            long expiry = tempTimer.getExpiry();
            try {
                Thread.sleep(Math.min((expiry - getTime()) / 1000000, 100));
            } catch (InterruptedException ex) {
                Logger.getLogger(VirtualClock.class.getName()).log(Level.SEVERE, null, ex);
            }
            totalTicks += (expiry - ticksOffset - currentTime) / NSPI;
            currentTime = getSystemTimer();
            tempTimer.check(getTime());
        } else {
            Timer tempTimer;
            synchronized (this) {
                tempTimer = timers.peek();
            }
            long expiry = tempTimer.getExpiry();
            try {
                Thread.sleep(Math.min((expiry - getTime()) / 1000000, 100));
            } catch (InterruptedException ex) {
                Logger.getLogger(VirtualClock.class.getName()).log(Level.SEVERE, null, ex);
            }
            totalTicks += (expiry - ticksOffset - currentTime) / NSPI;
            currentTime = expiry - ticksOffset;
            tempTimer.check(getTime());
        }
    }

    public void updateAndProcess(int instructions) {
        totalTicks += instructions;
        if (REALTIME) currentTime = getSystemTimer(); else currentTime += instructions * NSPI;
        process();
    }
}

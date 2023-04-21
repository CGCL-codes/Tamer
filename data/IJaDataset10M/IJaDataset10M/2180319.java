package jogamp.newt;

import com.jogamp.common.util.ArrayHashSet;
import com.jogamp.common.util.IntIntHashMap;
import com.jogamp.common.util.locks.RecursiveLock;
import com.jogamp.newt.Screen;
import com.jogamp.newt.ScreenMode;
import com.jogamp.newt.event.ScreenModeListener;
import java.util.ArrayList;
import java.util.HashMap;

public class ScreenModeStatus {

    private static boolean DEBUG = Screen.DEBUG;

    private RecursiveLock lock = new RecursiveLock();

    private ArrayHashSet screenModes;

    private IntIntHashMap screenModesIdx2NativeIdx;

    private ScreenMode currentScreenMode;

    private ScreenMode originalScreenMode;

    private ArrayList listener = new ArrayList();

    private static HashMap screenFQN2ScreenModeStatus = new HashMap();

    private static RecursiveLock screen2ScreenModeStatusLock = new RecursiveLock();

    protected static void mapScreenModeStatus(String screenFQN, ScreenModeStatus sms) {
        screen2ScreenModeStatusLock.lock();
        try {
            ScreenModeStatus _sms = (ScreenModeStatus) screenFQN2ScreenModeStatus.get(screenFQN);
            if (null != _sms) {
                throw new RuntimeException("ScreenModeStatus " + _sms + " already mapped to " + screenFQN);
            }
            screenFQN2ScreenModeStatus.put(screenFQN, sms);
            if (DEBUG) {
                System.err.println("ScreenModeStatus.map " + screenFQN + " -> " + sms);
            }
        } finally {
            screen2ScreenModeStatusLock.unlock();
        }
    }

    /**
     * @param screen the prev user
     * @return true if mapping is empty, ie no more usage of the mapped ScreenModeStatus
     */
    protected static void unmapScreenModeStatus(String screenFQN) {
        screen2ScreenModeStatusLock.lock();
        try {
            ScreenModeStatus sms = (ScreenModeStatus) screenFQN2ScreenModeStatus.remove(screenFQN);
            if (DEBUG) {
                System.err.println("ScreenModeStatus.unmap " + screenFQN + " -> " + sms);
            }
        } finally {
            screen2ScreenModeStatusLock.unlock();
        }
    }

    protected static ScreenModeStatus getScreenModeStatus(String screenFQN) {
        screen2ScreenModeStatusLock.lock();
        try {
            return (ScreenModeStatus) screenFQN2ScreenModeStatus.get(screenFQN);
        } finally {
            screen2ScreenModeStatusLock.unlock();
        }
    }

    protected static void lockScreenModeStatus() {
        screen2ScreenModeStatusLock.lock();
    }

    protected static void unlockScreenModeStatus() {
        screen2ScreenModeStatusLock.unlock();
    }

    public ScreenModeStatus(ArrayHashSet screenModes, IntIntHashMap screenModesIdx2NativeIdx) {
        this.screenModes = screenModes;
        this.screenModesIdx2NativeIdx = screenModesIdx2NativeIdx;
    }

    protected final void setOriginalScreenMode(ScreenMode originalScreenMode) {
        this.originalScreenMode = originalScreenMode;
        this.currentScreenMode = originalScreenMode;
    }

    public final ScreenMode getOriginalScreenMode() {
        return originalScreenMode;
    }

    public final ScreenMode getCurrentScreenMode() {
        lock();
        try {
            return currentScreenMode;
        } finally {
            unlock();
        }
    }

    public final boolean isOriginalMode() {
        lock();
        try {
            if (null != currentScreenMode && null != originalScreenMode) {
                return currentScreenMode.hashCode() == originalScreenMode.hashCode();
            }
            return true;
        } finally {
            unlock();
        }
    }

    protected final ArrayHashSet getScreenModes() {
        return screenModes;
    }

    protected final IntIntHashMap getScreenModesIdx2NativeIdx() {
        return screenModesIdx2NativeIdx;
    }

    protected final int addListener(ScreenModeListener l) {
        lock();
        try {
            listener.add(l);
            if (DEBUG) {
                System.err.println("ScreenModeStatus.addListener (size: " + listener.size() + "): " + l);
            }
            return listener.size();
        } finally {
            unlock();
        }
    }

    protected final int removeListener(ScreenModeListener l) {
        lock();
        try {
            if (!listener.remove(l)) {
                throw new RuntimeException("ScreenModeListener " + l + " not contained");
            }
            if (DEBUG) {
                System.err.println("ScreenModeStatus.removeListener (size: " + listener.size() + "): " + l);
            }
            return listener.size();
        } finally {
            unlock();
        }
    }

    protected final void fireScreenModeChangeNotify(ScreenMode desiredScreenMode) {
        lock();
        try {
            for (int i = 0; i < listener.size(); i++) {
                ((ScreenModeListener) listener.get(i)).screenModeChangeNotify(desiredScreenMode);
            }
        } finally {
            unlock();
        }
    }

    protected void fireScreenModeChanged(ScreenMode currentScreenMode, boolean success) {
        lock();
        try {
            if (success) {
                this.currentScreenMode = currentScreenMode;
            }
            for (int i = 0; i < listener.size(); i++) {
                ((ScreenModeListener) listener.get(i)).screenModeChanged(currentScreenMode, success);
            }
        } finally {
            unlock();
        }
    }

    protected final void lock() throws RuntimeException {
        lock.lock();
    }

    protected final void unlock() throws RuntimeException {
        lock.unlock();
    }
}

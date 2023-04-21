package org.jmol.multitouch;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point3f;
import org.jmol.api.Interface;
import org.jmol.api.JmolTouchSimulatorInterface;
import org.jmol.util.Logger;
import org.jmol.viewer.ActionManager;
import org.jmol.viewer.JmolConstants;
import org.jmol.viewer.Viewer;
import org.jmol.viewer.binding.Binding;
import com.sparshui.GestureType;

public class ActionManagerMT extends ActionManager implements JmolMultiTouchClient {

    private JmolMultiTouchAdapter adapter;

    private JmolTouchSimulatorInterface simulator;

    private int groupID;

    private int simulationPhase;

    private boolean resetNeeded = true;

    private long lastLogTime = 0;

    @Override
    public void setViewer(Viewer viewer, String commandOptions) {
        super.setViewer(viewer, commandOptions);
        mouseWheelFactor = 1.02f;
        boolean isSparsh = (commandOptions.indexOf("-multitouch-sparshui") >= 0);
        boolean isSimulated = (commandOptions.indexOf("-multitouch-sparshui-simulated") >= 0);
        boolean isJNI = (commandOptions.indexOf("-multitouch-jni") >= 0);
        boolean isMP = (commandOptions.indexOf("-multitouch-mp") >= 0);
        boolean isTablet = (commandOptions.indexOf("-multitouch-tab") >= 0);
        if (isMP || isTablet) {
            haveMultiTouchInput = true;
            groupID = 0;
        } else {
            groupID = ((int) (Math.random() * 0xFFFFFF)) << 4;
        }
        if (isTablet) return;
        String className = (isSparsh ? "multitouch.sparshui.JmolSparshClientAdapter" : "multitouch.jni.JmolJniClientAdapter");
        adapter = (JmolMultiTouchAdapter) Interface.getOptionInterface(className);
        Logger.info("ActionManagerMT SparshUI groupID=" + groupID);
        Logger.info("ActionManagerMT adapter = " + adapter);
        if (isSparsh) {
            startSparshUIService(isSimulated);
        } else if (isJNI) {
            adapter.setMultiTouchClient(viewer, this, false);
        }
        setBinding(binding);
        xyRange = 10;
    }

    private void startSparshUIService(boolean isSimulated) {
        haveMultiTouchInput = false;
        if (adapter == null) return;
        if (simulator != null) {
            simulator.dispose();
            simulator = null;
        }
        if (isSimulated) Logger.error("ActionManagerMT -- for now just using touch simulation.\nPress CTRL-LEFT and then draw two traces on the window.");
        isMultiTouchClient = adapter.setMultiTouchClient(viewer, this, isSimulated);
        isMultiTouchServer = adapter.isServer();
        if (isSimulated) {
            simulator = (JmolTouchSimulatorInterface) Interface.getInterface("com.sparshui.inputdevice.JmolTouchSimulator");
            if (simulator != null) {
                Logger.info("ActionManagerMT simulating SparshUI");
                simulator.startSimulator(viewer.getDisplay());
            }
        }
    }

    @Override
    protected void setBinding(Binding newBinding) {
        super.setBinding(newBinding);
        binding.unbindMouseAction(Binding.RIGHT);
        if (simulator != null && binding != null) {
            binding.unbindJmolAction(ACTION_center);
            binding.unbind(Binding.CTRL + Binding.LEFT + Binding.SINGLE_CLICK, null);
            binding.bind(Binding.CTRL + Binding.LEFT + Binding.SINGLE_CLICK, ACTION_multiTouchSimulation);
        }
    }

    @Override
    public void clear() {
        simulationPhase = 0;
        resetNeeded = true;
        super.clear();
    }

    private boolean doneHere;

    @Override
    public void dispose() {
        Logger.debug("ActionManagerMT -- dispose");
        doneHere = true;
        if (adapter != null) adapter.dispose();
        if (simulator != null) simulator.dispose();
        super.dispose();
    }

    public static final int DRAG_GESTURE = 0;

    public static final int MULTI_POINT_DRAG_GESTURE = 1;

    public static final int ROTATE_GESTURE = 2;

    public static final int SPIN_GESTURE = 3;

    public static final int TOUCH_GESTURE = 4;

    public static final int ZOOM_GESTURE = 5;

    public static final int DBLCLK_GESTURE = 6;

    public static final int FLICK_GESTURE = 7;

    public static final int RELATIVE_DRAG_GESTURE = 8;

    public static final int INVALID_GESTURE = 9;

    private static final GestureType TWO_POINT_GESTURE = new GestureType("org.jmol.multitouch.sparshui.TwoPointGesture");

    private static final GestureType SINGLE_POINT_GESTURE = new GestureType("org.jmol.multitouch.sparshui.SinglePointGesture");

    public static final int DRIVER_NONE = -2;

    public static final int SERVICE_LOST = -1;

    public static final int DRAG_EVENT = 0;

    public static final int ROTATE_EVENT = 1;

    public static final int SPIN_EVENT = 2;

    public static final int TOUCH_EVENT = 3;

    public static final int ZOOM_EVENT = 4;

    public static final int DBLCLK_EVENT = 5;

    public static final int FLICK_EVENT = 6;

    public static final int RELATIVE_DRAG_EVENT = 7;

    public static final int CLICK_EVENT = 8;

    private static final String[] eventNames = new String[] { "drag", "rotate", "spin", "touch", "zoom", "double-click", "flick", "relative-drag", "click" };

    public static final int BIRTH = 0;

    public static final int DEATH = 1;

    public static final int MOVE = 2;

    public static final int CLICK = 3;

    private static String getEventName(int i) {
        try {
            return eventNames[i];
        } catch (Exception e) {
            return "?";
        }
    }

    public List<GestureType> getAllowedGestures(int groupID) {
        if (groupID != this.groupID || !viewer.allowMultiTouch()) return null;
        List<GestureType> list = new ArrayList<GestureType>();
        list.add(TWO_POINT_GESTURE);
        list.add(SINGLE_POINT_GESTURE);
        return list;
    }

    public int getGroupID(int x, int y) {
        int gid = 0;
        try {
            if (viewer.hasFocus() && x >= 0 && y >= 0 && x < viewer.getScreenWidth() && y < viewer.getScreenHeight()) gid = groupID;
            if (resetNeeded) {
                gid |= 0x10000000;
                resetNeeded = false;
            }
        } catch (Exception e) {
        }
        return gid;
    }

    boolean mouseDown;

    @Override
    public void processEvent(int groupID, int eventType, int touchID, int iData, Point3f pt, long time) {
        if (Logger.debugging) Logger.info(this + " time=" + time + " groupID=" + groupID + " " + Integer.toHexString(groupID) + " eventType=" + eventType + "(" + getEventName(eventType) + ") iData=" + iData + " pt=" + pt);
        switch(eventType) {
            case DRAG_EVENT:
                if (iData == 2) {
                    checkMotion(JmolConstants.CURSOR_MOVE);
                    viewer.translateXYBy((int) pt.x, (int) pt.y);
                    logEvent("Drag", pt);
                }
                break;
            case DRIVER_NONE:
                if (simulator == null) haveMultiTouchInput = false;
                Logger.error("SparshUI reports no driver present");
                viewer.log("SparshUI reports no driver present -- setting haveMultiTouchInput FALSE");
                break;
            case ROTATE_EVENT:
                checkMotion(JmolConstants.CURSOR_MOVE);
                viewer.rotateZBy((int) pt.z, Integer.MAX_VALUE, Integer.MAX_VALUE);
                logEvent("Rotate", pt);
                break;
            case SERVICE_LOST:
                viewer.log("Jmol SparshUI client reports service lost -- " + (doneHere ? "not " : "") + " restarting");
                if (!doneHere) startSparshUIService(simulator != null);
                break;
            case TOUCH_EVENT:
                haveMultiTouchInput = true;
                if (touchID == Integer.MAX_VALUE) {
                    mouseDown = false;
                    clearMouseInfo();
                    break;
                }
                switch(iData) {
                    case BIRTH:
                        mouseDown = true;
                        super.mouseAction(Binding.PRESSED, time, (int) pt.x, (int) pt.y, 0, Binding.LEFT);
                        break;
                    case MOVE:
                        super.mouseAction(mouseDown ? Binding.DRAGGED : Binding.MOVED, time, (int) pt.x, (int) pt.y, 0, Binding.LEFT);
                        break;
                    case DEATH:
                        mouseDown = false;
                        super.mouseAction(Binding.RELEASED, time, (int) pt.x, (int) pt.y, 0, Binding.LEFT);
                        break;
                    case CLICK:
                        super.mouseAction(Binding.CLICKED, time, (int) pt.x, (int) pt.y, 1, Binding.LEFT);
                        break;
                }
                break;
            case ZOOM_EVENT:
                float scale = pt.z;
                if (scale == -1 || scale == 1) {
                    zoomByFactor((int) scale, Integer.MAX_VALUE, Integer.MAX_VALUE);
                    logEvent("Zoom", pt);
                }
                break;
        }
    }

    private void logEvent(String type, Point3f pt) {
        if (!viewer.getLogGestures()) return;
        long time = System.currentTimeMillis();
        if (time - lastLogTime > 10000) {
            viewer.log("$NOW$ multitouch " + type + " pt= " + pt);
            lastLogTime = time;
        }
    }

    @Override
    public void mouseAction(int action, long time, int x, int y, int count, int modifiers) {
        switch(action) {
            case Binding.MOVED:
                if (haveMultiTouchInput) return;
                adapter.mouseMoved(x, y);
                break;
            case Binding.WHEELED:
            case Binding.CLICKED:
                break;
            case Binding.DRAGGED:
                if (simulator != null && simulationPhase > 0) {
                    setCurrent(time, x, y, modifiers);
                    simulator.mouseDragged(time, x, y);
                    return;
                }
                break;
            case Binding.PRESSED:
                if (simulator != null) {
                    int maction = Binding.getMouseAction(1, modifiers);
                    if (binding.isBound(maction, ACTION_multiTouchSimulation)) {
                        setCurrent(0, x, y, modifiers);
                        viewer.setFocus();
                        if (simulationPhase++ == 0) simulator.startRecording();
                        simulator.mousePressed(time, x, y);
                        return;
                    }
                    simulationPhase = 0;
                }
                break;
            case Binding.RELEASED:
                if (simulator != null && simulationPhase > 0) {
                    setCurrent(time, x, y, modifiers);
                    viewer.spinXYBy(0, 0, 0);
                    simulator.mouseReleased(time, x, y);
                    if (simulationPhase >= 2) {
                        resetNeeded = true;
                        simulator.endRecording();
                        simulationPhase = 0;
                    }
                    return;
                }
                break;
        }
        if (!haveMultiTouchInput) super.mouseAction(action, time, x, y, count, modifiers);
    }

    @Override
    protected float getExitRate() {
        long dt = dragGesture.getTimeDifference(2);
        System.out.println("ActionManMT getExitRate " + dt + " " + MININUM_GESTURE_DELAY_MILLISECONDS);
        return (dt > (MININUM_GESTURE_DELAY_MILLISECONDS << 3) ? 0 : dragGesture.getSpeedPixelsPerMillisecond(2, 1));
    }

    @Override
    protected float getDegrees(int delta, int i) {
        int dim = (i == 0 ? viewer.getScreenWidth() : viewer.getScreenHeight());
        return ((float) delta) / dim * 180 * mouseDragFactor;
    }
}

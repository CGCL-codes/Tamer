package jpen.provider.osx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import jpen.PButton;
import jpen.PKind;
import jpen.PLevel;

final class CocoaAccess {

    static final Logger L = Logger.getLogger(CocoaAccess.class.getName());

    protected static final float RADIANS_PER_DEGREE = (float) (Math.PI / 180);

    protected static final float HALF_PI = (float) (Math.PI / 2);

    protected static final float TILT_TO_RADIANS = 64 * RADIANS_PER_DEGREE;

    private boolean active = false;

    private CocoaProvider cocoaProvider = null;

    public CocoaAccess() {
    }

    static native int getNativeBuild();

    public void start() {
        if (!active) {
            active = true;
            startup();
            setProximityEventsEnabled(true);
        }
    }

    public void stop() {
        if (active) {
            active = false;
            shutdown();
        }
    }

    public void finalize() throws Throwable {
        stop();
        super.finalize();
    }

    public void enable() {
        setTabletEventsEnabled(true);
    }

    public void disable() {
        setTabletEventsEnabled(false);
    }

    protected native void setTabletEventsEnabled(boolean enabled);

    protected native void setProximityEventsEnabled(boolean enabled);

    protected native void setScrollEventsEnabled(boolean enabled);

    protected native void setGestureEventsEnabled(boolean enabled);

    public void setProvider(final CocoaProvider _cocoaProvider) {
        cocoaProvider = _cocoaProvider;
    }

    private native void startup();

    private native void shutdown();

    public static final int WACOM_VENDOR_ID = 0x56A;

    public static final int WACOM_CAPABILITY_DEVICEIDMASK = 0x0001;

    public static final int WACOM_CAPABILITY_ABSXMASK = 0x0002;

    public static final int WACOM_CAPABILITY_ABSYMASK = 0x0004;

    public static final int WACOM_CAPABILITY_VENDOR1MASK = 0x0008;

    public static final int WACOM_CAPABILITY_VENDOR2MASK = 0x0010;

    public static final int WACOM_CAPABILITY_VENDOR3MASK = 0x0020;

    public static final int WACOM_CAPABILITY_BUTTONSMASK = 0x0040;

    public static final int WACOM_CAPABILITY_TILTXMASK = 0x0080;

    public static final int WACOM_CAPABILITY_TILTYMASK = 0x0100;

    public static final int WACOM_CAPABILITY_TILTMASK = WACOM_CAPABILITY_TILTXMASK | WACOM_CAPABILITY_TILTYMASK;

    public static final int WACOM_CAPABILITY_ABSZMASK = 0x0200;

    public static final int WACOM_CAPABILITY_PRESSUREMASK = 0x0400;

    public static final int WACOM_CAPABILITY_TANGENTIALPRESSUREMASK = 0x0800;

    public static final int WACOM_CAPABILITY_ORIENTINFOMASK = 0x1000;

    public static final int WACOM_CAPABILITY_ROTATIONMASK = 0x2000;

    public static final int WACOM_POINTER_TYPE_MASK = 0xF06;

    public static final int WACOM_POINTER_TYPE_GENERAL_STYLUS = 0x804;

    public static final int WACOM_POINTER_TYPE_AIRBRUSH = 0x802;

    public static final int WACOM_POINTER_TYPE_GENERAL_MOUSE = 0x902;

    public static final int WACOM_POINTER_TYPE_PRO_MOUSE = 0x006;

    public static final int WACOM_POINTER_TYPE_ROTATION_STYLUS = 0x004;

    public static final int WACOM_POINTER_TYPE_I_Standard_Stylus = 0x822;

    public static final int WACOM_POINTER_TYPE_I_Inking_Stylus = 0x812;

    public static final int WACOM_POINTER_TYPE_I_Stroke_Stylus = 0x832;

    public static final int WACOM_POINTER_TYPE_I_Grip_Stylus = 0x842;

    public static final int WACOM_POINTER_TYPE_I_Airbrush = 0x912;

    public static final int WACOM_POINTER_TYPE_I_4D_Mouse = 0x094;

    public static final int WACOM_POINTER_TYPE_I_Lens_Cursor = 0x096;

    public static final int WACOM_POINTER_TYPE_I2_Standard_Grip_Stylus = 0x852;

    public static final int WACOM_POINTER_TYPE_I2_Classic_Stylus = 0x822;

    public static final int WACOM_POINTER_TYPE_I2_Inking_Stylus = 0x812;

    public static final int WACOM_POINTER_TYPE_I2_Stroke_Stylus = 0x832;

    public static final int WACOM_POINTER_TYPE_I2_Designer_Stylus = 0x842;

    public static final int WACOM_POINTER_TYPE_I2_Airbrush = 0x912;

    public static final int WACOM_POINTER_TYPE_I2_2D_Mouse = 0x007;

    public static final int WACOM_POINTER_TYPE_I2_4D_Mouse = 0x094;

    public static final int WACOM_POINTER_TYPE_I2_Lens_Cursor = 0x096;

    public static final int WACOM_POINTER_TYPE_I3_Standard_Grip_Stylus = 0x823;

    public static final int WACOM_POINTER_TYPE_I3_Rotation_Stylus_Art_Pen = 0x885;

    public static final int WACOM_POINTER_TYPE_I3_Inking_Stylus = 0x801;

    public static final int WACOM_POINTER_TYPE_I3_Airbrush = 0x913;

    public static final int WACOM_POINTER_TYPE_I3_2D_mouse = 0x017;

    public static final int WACOM_POINTER_TYPE_I3_Lens_Cursor = 0x097;

    public static final int WACOM_POINTER_TYPE_Graphire_Stylus = 0x022;

    public static final int WACOM_POINTER_TYPE_Graphire_Mouse = 0x296;

    protected static final int NS_MODIFIER_AlphaShiftKeyMask = 1 << 16;

    protected static final int NS_MODIFIER_ShiftKeyMask = 1 << 17;

    protected static final int NS_MODIFIER_ControlKeyMask = 1 << 18;

    protected static final int NS_MODIFIER_AlternateKeyMask = 1 << 19;

    protected static final int NS_MODIFIER_CommandKeyMask = 1 << 20;

    protected static final int NS_MODIFIER_NumericPadKeyMask = 1 << 21;

    protected static final int NS_MODIFIER_HelpKeyMask = 1 << 22;

    protected static final int NS_MODIFIER_FunctionKeyMask = 1 << 23;

    protected static final int NS_EVENT_TYPE_LeftMouseDown = 1;

    protected static final int NS_EVENT_TYPE_LeftMouseUp = 2;

    protected static final int NS_EVENT_TYPE_RightMouseDown = 3;

    protected static final int NS_EVENT_TYPE_RightMouseUp = 4;

    protected static final int NS_EVENT_TYPE_MouseMoved = 5;

    protected static final int NS_EVENT_TYPE_LeftMouseDragged = 6;

    protected static final int NS_EVENT_TYPE_RightMouseDragged = 7;

    protected static final int NS_EVENT_TYPE_MouseEntered = 8;

    protected static final int NS_EVENT_TYPE_MouseExited = 9;

    protected static final int NS_EVENT_TYPE_ScrollWheel = 22;

    protected static final int NS_EVENT_TYPE_TabletPoint = 23;

    protected static final int NS_EVENT_TYPE_TabletProximity = 24;

    protected static final int NS_EVENT_TYPE_OtherMouseDown = 25;

    protected static final int NS_EVENT_TYPE_OtherMouseUp = 26;

    protected static final int NS_EVENT_TYPE_OtherMouseDragged = 27;

    protected static final int NS_EVENT_TYPE_EventTypeGesture = 29;

    protected static final int NS_EVENT_TYPE_EventTypeMagnify = 30;

    protected static final int NS_EVENT_TYPE_EventTypeSwipe = 31;

    protected static final int NS_EVENT_TYPE_EventTypeRotate = 18;

    protected static final int NS_EVENT_TYPE_EventTypeBeginGesture = 19;

    protected static final int NS_EVENT_TYPE_EventTypeEndGesture = 20;

    protected static final int NSUnknownPointingDevice = 0;

    protected static final int NSPenPointingDevice = 1;

    protected static final int NSCursorPointingDevice = 2;

    protected static final int NSEraserPointingDevice = 3;

    protected static final int NSPenTipMask = 1;

    protected static final int NSPenLowerSideMask = 2;

    protected static final int NSPenUpperSideMask = 4;

    private CocoaDevice device = null;

    protected void postProximityEvent(double eventTimeSeconds, int cocoaModifierFlags, int capabilityMask, int deviceID, boolean enteringProximity, int pointingDeviceID, int pointingDeviceSerialNumber, int pointingDeviceType, int systemTabletID, int tabletID, long uniqueID, int vendorID, int vendorPointingDeviceType) {
        if (enteringProximity) {
            switch(pointingDeviceType) {
                case NSPenPointingDevice:
                    device = cocoaProvider.getDevice(PKind.Type.STYLUS);
                    break;
                case NSCursorPointingDevice:
                    device = cocoaProvider.getDevice(PKind.Type.CURSOR);
                    break;
                case NSEraserPointingDevice:
                    device = cocoaProvider.getDevice(PKind.Type.ERASER);
                    break;
                case NSUnknownPointingDevice:
                default:
                    device = cocoaProvider.getDevice(PKind.Type.CURSOR);
                    break;
            }
        } else {
            device = cocoaProvider.getDevice(PKind.Type.CURSOR);
        }
        if (vendorID == WACOM_VENDOR_ID) {
        }
    }

    private Collection<PLevel> levels = new ArrayList<PLevel>(8);

    /**
	 * Note that proximity events are not generated when switching between the mouse and tablet.
	 */
    protected void postEvent(final int type, final double eventTimeSeconds, final int cocoaModifierFlags, final float screenX, final float screenY, final boolean tabletEvent, final int absoluteX, final int absoluteY, final int absoluteZ, final int buttonMask, final float pressure, final float rotation, final float tiltX, final float tiltY, final float tangentialPressure) {
        invokeOnEventThread(new Runnable() {

            public void run() {
                if (device == null) {
                    device = cocoaProvider.getDevice(PKind.Type.CURSOR);
                }
                if (tabletEvent && device.getType() == PKind.Type.CURSOR) {
                    device = cocoaProvider.getDevice(PKind.Type.STYLUS);
                }
                if (!device.getEnabled()) return;
                long deviceTime = (long) (eventTimeSeconds * 1000);
                switch(type) {
                    case NS_EVENT_TYPE_LeftMouseDown:
                        scheduleButtonEvent(deviceTime, PButton.Type.LEFT, true);
                        break;
                    case NS_EVENT_TYPE_LeftMouseUp:
                        scheduleButtonEvent(deviceTime, PButton.Type.LEFT, false);
                        break;
                    case NS_EVENT_TYPE_RightMouseDown:
                        scheduleButtonEvent(deviceTime, PButton.Type.RIGHT, true);
                        break;
                    case NS_EVENT_TYPE_RightMouseUp:
                        scheduleButtonEvent(deviceTime, PButton.Type.RIGHT, false);
                        break;
                    case NS_EVENT_TYPE_OtherMouseDown:
                        scheduleButtonEvent(deviceTime, PButton.Type.CENTER, true);
                        break;
                    case NS_EVENT_TYPE_OtherMouseUp:
                        scheduleButtonEvent(deviceTime, PButton.Type.CENTER, false);
                        break;
                }
                levels.clear();
                levels.add(new PLevel(PLevel.Type.X, screenX));
                levels.add(new PLevel(PLevel.Type.Y, screenY));
                levels.add(new PLevel(PLevel.Type.TILT_X, tiltX * TILT_TO_RADIANS));
                levels.add(new PLevel(PLevel.Type.TILT_Y, -tiltY * TILT_TO_RADIANS));
                levels.add(new PLevel(PLevel.Type.PRESSURE, pressure));
                levels.add(new PLevel(PLevel.Type.SIDE_PRESSURE, tangentialPressure));
                levels.add(new PLevel(PLevel.Type.ROTATION, rotation * RADIANS_PER_DEGREE));
                cocoaProvider.getPenManager().scheduleLevelEvent(device, deviceTime, levels, true);
            }
        });
    }

    protected void postScrollEvent(double eventTimeSeconds, int cocoaModifierFlags, float screenX, float screenY, boolean isDeviceDelta, float deltaX, float deltaY) {
    }

    protected void postMagnifyEvent(double eventTimeSeconds, int cocoaModifierFlags, float screenX, float screenY, float magnificationFactor) {
    }

    protected void postSwipeEvent(double eventTimeSeconds, int cocoaModifierFlags, float screenX, float screenY, float deltaX, float deltaY) {
    }

    protected void postRotateEvent(double eventTimeSeconds, int cocoaModifierFlags, float screenX, float screenY, float rotationDegrees) {
    }

    protected void invokeOnEventThread(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    private void scheduleButtonEvent(long deviceTime, PButton.Type type, boolean pressed) {
        cocoaProvider.getPenManager().scheduleButtonEvent(device, deviceTime, new PButton(type.ordinal(), pressed));
    }
}

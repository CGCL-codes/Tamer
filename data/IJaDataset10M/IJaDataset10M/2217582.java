package Racing.com;

import android.content.Context;
import android.hardware.SensorEvent;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

/**
 * A simple GLSurfaceView sub-class that demonstrate how to perform
 * OpenGL ES 2.0 rendering into a GL Surface. Note the following important
 * details:
 *
 * - The class must use a custom context factory to enable 2.0 rendering.
 *   See ContextFactory class definition below.
 *
 * - The class must use a custom EGLConfigChooser to be able to select
 *   an EGLConfig that supports 2.0. This is done by providing a config
 *   specification to eglChooseConfig() that has the attribute
 *   EGL10.ELG_RENDERABLE_TYPE containing the EGL_OPENGL_ES2_BIT flag
 *   set. See ConfigChooser class definition below.
 *
 * - The class must select the surface's format, then choose an EGLConfig
 *   that matches it exactly (with regards to red/green/blue/alpha channels
 *   bit depths). Failure to do so would result in an EGL_BAD_MATCH error.
 */
class GL2JNIView extends GLSurfaceView {

    private static String TAG = "GL2JNIView";

    private static final boolean DEBUG = false;

    private final float TOUCH_SCALE_FACTOR = 1;

    /** Accuracy configuration */
    private static float threshold = 0.2f;

    private static int interval = 1000;

    private static Sensor sensor;

    private static SensorManager sensorManager;

    private static AcceleratorListener listener;

    /** indicates whether or not Accelerometer Sensor is supported */
    private static Boolean supported;

    /** indicates whether or not Accelerometer Sensor is running */
    private static boolean running = false;

    public static boolean isListening() {
        return running;
    }

    /**
     * Unregisters listeners
     */
    public static void stopListening() {
        running = false;
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Returns true if at least one Accelerometer sensor is available
     */
    public static boolean isSupported() {
        if (supported == null) {
            if (RacingActivity.getContext() != null) {
                sensorManager = (SensorManager) RacingActivity.getContext().getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
                supported = new Boolean(sensors.size() > 0);
            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }

    /**
     * Configure the listener for shaking
     * @param threshold
     *             minimum acceleration variation for considering shaking
     * @param interval
     *             minimum interval between to shake events
     */
    public static void configure(int threshold, int interval) {
        GL2JNIView.threshold = threshold;
        GL2JNIView.interval = interval;
    }

    /**
     * Registers a listener and start listening
     * @param accelerometerListener
     *             callback for accelerometer events
     */
    public static void startListening(AcceleratorListener accelerometerListener) {
        sensorManager = (SensorManager) RacingActivity.getContext().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sensor = sensors.get(0);
            running = sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
            listener = accelerometerListener;
        }
    }

    /**
     * Configures threshold and interval
     * And registers a listener and start listening
     * @param accelerometerListener
     *             callback for accelerometer events
     * @param threshold
     *             minimum acceleration variation for considering shaking
     * @param interval
     *             minimum interval between to shake events
     */
    public static void startListening(AcceleratorListener accelerometerListener, int threshold, int interval) {
        configure(threshold, interval);
        startListening(accelerometerListener);
    }

    /**
     * The listener that listen to events from the accelerometer listener
     */
    private static SensorEventListener sensorEventListener = new SensorEventListener() {

        private long now = 0;

        private long timeDiff = 0;

        private long lastUpdate = 0;

        private long lastShake = 0;

        private float x = 0;

        private float y = 0;

        private float z = 0;

        private float lastX = 0;

        private float lastY = 0;

        private float lastZ = 0;

        private float force = 0;

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            now = event.timestamp;
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            if (lastUpdate == 0) {
                lastUpdate = now;
                lastShake = now;
                lastX = x;
                lastY = y;
                lastZ = z;
            } else {
                timeDiff = now - lastUpdate;
                if (timeDiff > 0) {
                    force = Math.abs(x + y + z - lastX - lastY - lastZ) / timeDiff;
                    if (force > threshold) {
                        if (now - lastShake >= interval) {
                            listener.onShake(force);
                        }
                        lastShake = now;
                    }
                    lastX = x;
                    lastY = y;
                    lastZ = z;
                    lastUpdate = now;
                }
            }
            listener.onAccelerationChanged(x, y, z);
        }
    };

    static GL2JNILib instance = new GL2JNILib();

    public GL2JNIView(Context context) {
        super(context);
        instance.initSound(context);
        init(false, 8, 8);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        int state = 0;
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                state = 1;
                break;
            case MotionEvent.ACTION_MOVE:
                state = 4;
                break;
            case MotionEvent.ACTION_UP:
                state = 2;
                break;
        }
        ;
        GL2JNILib.pointerEvent(state, (int) (event.getX() * 800 / getWidth()), (int) (event.getY() * 480 / getHeight()));
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent evt) {
        System.out.println("onKeyUp(int keyCode, KeyEvent evt) = " + keyCode);
        int state = 2;
        int kc = 38;
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                state = 1;
                kc = 38;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                state = 1;
                kc = 39;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                state = 1;
                kc = 40;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                state = 1;
                kc = 41;
                break;
            case KeyEvent.KEYCODE_A:
                state = 1;
                break;
            case KeyEvent.KEYCODE_Z:
                state = 1;
                break;
            case KeyEvent.KEYCODE_MENU:
                System.out.println("Click Event Here " + evt.getAction());
                state = 1;
                kc = keyCode;
                break;
        }
        GL2JNILib.keyEvent(state, kc);
        return true;
    }

    private void init(boolean translucent, int depth, int stencil) {
        if (translucent) {
            this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
        setEGLContextFactory(new ContextFactory());
        setEGLConfigChooser(translucent ? new ConfigChooser(8, 8, 8, 8, depth, stencil) : new ConfigChooser(5, 6, 5, 0, depth, stencil));
        setRenderer(new Renderer());
    }

    private static class ContextFactory implements GLSurfaceView.EGLContextFactory {

        private static int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {
            Log.w(TAG, "creating OpenGL ES 2.0 context");
            checkEglError("Before eglCreateContext", egl);
            int[] attrib_list = { EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE };
            EGLContext context = egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
            checkEglError("After eglCreateContext", egl);
            return context;
        }

        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
            egl.eglDestroyContext(display, context);
        }
    }

    private static void checkEglError(String prompt, EGL10 egl) {
        int error;
        while ((error = egl.eglGetError()) != EGL10.EGL_SUCCESS) {
            Log.e(TAG, String.format("%s: EGL error: 0x%x", prompt, error));
        }
    }

    private static class ConfigChooser implements GLSurfaceView.EGLConfigChooser {

        public ConfigChooser(int r, int g, int b, int a, int depth, int stencil) {
            mRedSize = r;
            mGreenSize = g;
            mBlueSize = b;
            mAlphaSize = a;
            mDepthSize = depth;
            mStencilSize = stencil;
        }

        private static int EGL_OPENGL_ES2_BIT = 4;

        private static int[] s_configAttribs2 = { EGL10.EGL_RED_SIZE, 4, EGL10.EGL_GREEN_SIZE, 4, EGL10.EGL_BLUE_SIZE, 4, EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT, EGL10.EGL_NONE };

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            int[] num_config = new int[1];
            egl.eglChooseConfig(display, s_configAttribs2, null, 0, num_config);
            int numConfigs = num_config[0];
            if (numConfigs <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] configs = new EGLConfig[numConfigs];
            egl.eglChooseConfig(display, s_configAttribs2, configs, numConfigs, num_config);
            if (DEBUG) {
                printConfigs(egl, display, configs);
            }
            return chooseConfig(egl, display, configs);
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            for (EGLConfig config : configs) {
                int d = findConfigAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE, 0);
                int s = findConfigAttrib(egl, display, config, EGL10.EGL_STENCIL_SIZE, 0);
                if (d < mDepthSize || s < mStencilSize) continue;
                int r = findConfigAttrib(egl, display, config, EGL10.EGL_RED_SIZE, 0);
                int g = findConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0);
                int b = findConfigAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE, 0);
                int a = findConfigAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE, 0);
                if (r == mRedSize && g == mGreenSize && b == mBlueSize && a == mAlphaSize) return config;
            }
            return null;
        }

        private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
            if (egl.eglGetConfigAttrib(display, config, attribute, mValue)) {
                return mValue[0];
            }
            return defaultValue;
        }

        private void printConfigs(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            int numConfigs = configs.length;
            Log.w(TAG, String.format("%d configurations", numConfigs));
            for (int i = 0; i < numConfigs; i++) {
                Log.w(TAG, String.format("Configuration %d:\n", i));
                printConfig(egl, display, configs[i]);
            }
        }

        private void printConfig(EGL10 egl, EGLDisplay display, EGLConfig config) {
            int[] attributes = { EGL10.EGL_BLUE_SIZE, EGL10.EGL_GREEN_SIZE, EGL10.EGL_RED_SIZE, EGL10.EGL_DEPTH_SIZE, EGL10.EGL_STENCIL_SIZE, EGL10.EGL_CONFIG_CAVEAT, EGL10.EGL_CONFIG_ID, EGL10.EGL_LEVEL, EGL10.EGL_MAX_PBUFFER_HEIGHT, EGL10.EGL_MAX_PBUFFER_PIXELS, EGL10.EGL_MAX_PBUFFER_WIDTH, EGL10.EGL_NATIVE_RENDERABLE, EGL10.EGL_NATIVE_VISUAL_ID, EGL10.EGL_NATIVE_VISUAL_TYPE, 0x3030, EGL10.EGL_SAMPLES, EGL10.EGL_SAMPLE_BUFFERS, EGL10.EGL_SURFACE_TYPE, EGL10.EGL_TRANSPARENT_TYPE, EGL10.EGL_TRANSPARENT_RED_VALUE, EGL10.EGL_TRANSPARENT_GREEN_VALUE, EGL10.EGL_TRANSPARENT_BLUE_VALUE, 0x3039, 0x303A, 0x303B, 0x303C, EGL10.EGL_LUMINANCE_SIZE, EGL10.EGL_ALPHA_MASK_SIZE, EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RENDERABLE_TYPE, 0x3042 };
            String[] names = { "EGL_BUFFER_SIZE", "EGL_ALPHA_SIZE", "EGL_BLUE_SIZE", "EGL_GREEN_SIZE", "EGL_RED_SIZE", "EGL_DEPTH_SIZE", "EGL_STENCIL_SIZE", "EGL_CONFIG_CAVEAT", "EGL_CONFIG_ID", "EGL_LEVEL", "EGL_MAX_PBUFFER_HEIGHT", "EGL_MAX_PBUFFER_PIXELS", "EGL_MAX_PBUFFER_WIDTH", "EGL_NATIVE_RENDERABLE", "EGL_NATIVE_VISUAL_ID", "EGL_NATIVE_VISUAL_TYPE", "EGL_PRESERVED_RESOURCES", "EGL_SAMPLES", "EGL_SAMPLE_BUFFERS", "EGL_SURFACE_TYPE", "EGL_TRANSPARENT_TYPE", "EGL_TRANSPARENT_RED_VALUE", "EGL_TRANSPARENT_GREEN_VALUE", "EGL_TRANSPARENT_BLUE_VALUE", "EGL_BIND_TO_TEXTURE_RGB", "EGL_BIND_TO_TEXTURE_RGBA", "EGL_MIN_SWAP_INTERVAL", "EGL_MAX_SWAP_INTERVAL", "EGL_LUMINANCE_SIZE", "EGL_ALPHA_MASK_SIZE", "EGL_COLOR_BUFFER_TYPE", "EGL_RENDERABLE_TYPE", "EGL_CONFORMANT" };
            int[] value = new int[1];
            for (int i = 0; i < attributes.length; i++) {
                int attribute = attributes[i];
                String name = names[i];
                if (egl.eglGetConfigAttrib(display, config, attribute, value)) {
                    Log.w(TAG, String.format("  %s: %d\n", name, value[0]));
                } else {
                    while (egl.eglGetError() != EGL10.EGL_SUCCESS) ;
                }
            }
        }

        protected int mRedSize;

        protected int mGreenSize;

        protected int mBlueSize;

        protected int mAlphaSize;

        protected int mDepthSize;

        protected int mStencilSize;

        private int[] mValue = new int[1];
    }

    private static class Renderer implements GLSurfaceView.Renderer {

        long startTime = System.currentTimeMillis();

        long endTime;

        public void onDrawFrame(GL10 gl) {
            endTime = System.currentTimeMillis();
            long dt = endTime - startTime;
            try {
                if (dt < 33) Thread.sleep(33 - dt);
            } catch (Exception e) {
            }
            ;
            startTime = System.currentTimeMillis();
            GL2JNILib.draw();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            System.out.println("Init The App");
            GL2JNILib.init(GL2JNIView.instance);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }
    }
}

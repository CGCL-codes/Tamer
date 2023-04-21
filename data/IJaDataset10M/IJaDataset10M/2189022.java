package org.gstreamer.lowlevel;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gstreamer.GObject;
import org.gstreamer.Gst;
import org.gstreamer.MiniObject;
import org.gstreamer.lowlevel.annotations.HasSubtype;
import com.sun.jna.Pointer;

/**
 *
 */
public abstract class NativeObject extends org.gstreamer.lowlevel.Handle {

    private static final Logger logger = Logger.getLogger(NativeObject.class.getName());

    private static final Level LIFECYCLE = Level.FINE;

    protected static class Initializer {

        public final Pointer ptr;

        public final boolean needRef, ownsHandle;

        public Initializer() {
            this.ptr = null;
            this.needRef = false;
            this.ownsHandle = false;
        }

        public Initializer(Pointer ptr, boolean needRef, boolean ownsHandle) {
            this.ptr = ptr;
            this.needRef = needRef;
            this.ownsHandle = ownsHandle;
        }
    }

    protected static final Initializer defaultInit = new Initializer();

    protected static Initializer initializer(Pointer ptr) {
        return initializer(ptr, false, true);
    }

    protected static Initializer initializer(Pointer ptr, boolean needRef, boolean ownsHandle) {
        if (ptr == null) {
            throw new IllegalArgumentException("Invalid native pointer");
        }
        return new Initializer(ptr, needRef, ownsHandle);
    }

    /** Creates a new instance of NativeObject */
    protected NativeObject(final Initializer init) {
        logger.entering("NativeObject", "<init>", new Object[] { init });
        if (init == null) {
            throw new IllegalArgumentException("Initializer cannot be null");
        }
        logger.log(LIFECYCLE, "Creating " + getClass().getSimpleName() + " (" + init.ptr + ")");
        nativeRef = new NativeRef(this);
        this.handle = init.ptr;
        this.ownsHandle.set(init.ownsHandle);
        if (GObject.class.isAssignableFrom(getClass())) {
            getInstanceMap().put(init.ptr, nativeRef);
        }
    }

    protected abstract void disposeNativeHandle(Pointer ptr);

    public void dispose() {
        logger.log(LIFECYCLE, "Disposing object " + getClass().getName() + " = " + handle);
        if (!disposed.getAndSet(true)) {
            getInstanceMap().remove(handle, nativeRef);
            if (ownsHandle.get()) {
                disposeNativeHandle(handle);
            }
            valid.set(false);
        }
    }

    @Override
    protected void invalidate() {
        logger.log(LIFECYCLE, "Invalidating object " + this + " = " + handle());
        getInstanceMap().remove(handle(), nativeRef);
        disposed.set(true);
        ownsHandle.set(false);
        valid.set(false);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            logger.log(LIFECYCLE, "Finalizing " + getClass().getSimpleName() + " (" + handle + ")");
            dispose();
        } finally {
            super.finalize();
        }
    }

    @Override
    protected Object nativeValue() {
        return handle();
    }

    protected Pointer handle() {
        if (!valid.get()) {
            throw new IllegalStateException("Native object has been disposed");
        }
        return handle;
    }

    public Pointer getNativeAddress() {
        return handle;
    }

    protected boolean isDisposed() {
        return disposed.get();
    }

    protected static NativeObject instanceFor(Pointer ptr) {
        WeakReference<NativeObject> ref = getInstanceMap().get(ptr);
        if (ref != null && ref.get() == null) {
            getInstanceMap().remove(ptr);
        }
        return ref != null ? ref.get() : null;
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls) {
        return objectFor(ptr, cls, true);
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, boolean needRef) {
        return objectFor(ptr, cls, needRef, true);
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, boolean needRef, boolean ownsHandle) {
        return objectFor(ptr, cls, needRef ? 1 : 0, ownsHandle);
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, int refAdjust, boolean ownsHandle) {
        logger.entering("NativeObject", "instanceFor", new Object[] { ptr, refAdjust, ownsHandle });
        if (ptr == null) {
            return null;
        }
        NativeObject obj = GObject.class.isAssignableFrom(cls) ? NativeObject.instanceFor(ptr) : null;
        if (obj != null && cls.isInstance(obj)) {
            if (refAdjust < 0) {
                ((RefCountedObject) obj).unref();
            }
            return cls.cast(obj);
        }
        if (GObject.class.isAssignableFrom(cls) || MiniObject.class.isAssignableFrom(cls)) {
            cls = classFor(ptr, cls);
        }
        try {
            Constructor<T> constructor = cls.getDeclaredConstructor(Initializer.class);
            T retVal = constructor.newInstance(initializer(ptr, refAdjust > 0, ownsHandle));
            return retVal;
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <T extends NativeObject> Class<T> classFor(Pointer ptr, Class<T> defaultClass) {
        Class<? extends NativeObject> cls = GstTypes.classFor(ptr);
        if (cls != null && cls.isAnnotationPresent(HasSubtype.class)) {
            cls = (Class<T>) SubtypeMapper.subtypeFor(cls, ptr);
        }
        return (cls != null && defaultClass.isAssignableFrom(cls)) ? (Class<T>) cls : defaultClass;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NativeObject && ((NativeObject) o).handle.equals(handle);
    }

    @Override
    public int hashCode() {
        return handle.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + handle() + ")";
    }

    public void disown() {
        logger.log(LIFECYCLE, "Disowning " + handle());
        ownsHandle.set(false);
    }

    static {
        Gst.addStaticShutdownTask(new Runnable() {

            public void run() {
                System.gc();
                int gcCount = 20;
                while (!getInstanceMap().isEmpty() && gcCount-- > 0) {
                    try {
                        Thread.sleep(10);
                        System.gc();
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
                for (Object o : getInstanceMap().values().toArray()) {
                    NativeObject obj = ((NativeRef) o).get();
                    if (obj != null && !obj.disposed.get()) {
                        obj.dispose();
                    }
                }
            }
        });
    }

    private static final ConcurrentMap<Pointer, NativeRef> getInstanceMap() {
        return StaticData.instanceMap;
    }

    static class NativeRef extends WeakReference<NativeObject> {

        public NativeRef(NativeObject obj) {
            super(obj);
        }
    }

    private final AtomicBoolean disposed = new AtomicBoolean(false);

    private final AtomicBoolean valid = new AtomicBoolean(true);

    private final Pointer handle;

    protected final AtomicBoolean ownsHandle = new AtomicBoolean(false);

    private final NativeRef nativeRef;

    private static final class StaticData {

        private static final ConcurrentMap<Pointer, NativeRef> instanceMap = new ConcurrentHashMap<Pointer, NativeRef>();

        static {
            Gst.addStaticShutdownTask(new Runnable() {

                public void run() {
                    System.gc();
                    int gcCount = 20;
                    while (!getInstanceMap().isEmpty() && gcCount-- > 0) {
                        try {
                            Thread.sleep(10);
                            System.gc();
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                    for (Object o : getInstanceMap().values().toArray()) {
                        NativeObject obj = ((NativeRef) o).get();
                        if (obj != null && !obj.disposed.get()) {
                            obj.dispose();
                        }
                    }
                }
            });
        }
    }
}

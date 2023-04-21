package org.gstreamer.lowlevel;

/**
 * A {@link NativeObject} that has an associated reference count
 * @author wayne
 */
public abstract class RefCountedObject extends NativeObject {

    /** Creates a new instance of RefCountedObject */
    protected RefCountedObject(Initializer init) {
        super(init);
        if (init.ownsHandle && init.needRef) {
            ref();
        }
    }

    protected abstract void ref();

    protected abstract void unref();
}

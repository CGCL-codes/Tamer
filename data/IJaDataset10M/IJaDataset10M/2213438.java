package org.rococoa.contrib.appkit;

import org.rococoa.ObjCClass;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSObject;

/** NSOperation from Cocoa.
 *
 */
public abstract class NSOperation extends NSObject {

    public static final _Class CLASS = Rococoa.createClass(NSOperation.class.getSimpleName(), _Class.class);

    public interface _Class extends ObjCClass {

        public NSOperation alloc();
    }

    public abstract NSOperation init();

    public abstract void start();

    public abstract void main();

    public abstract void cancel();

    public abstract void waitUntilFinished();

    public abstract boolean isCancelled();

    public abstract boolean isExecuting();

    public abstract boolean isFinished();

    public abstract boolean isConcurrent();

    public abstract boolean isReady();
}

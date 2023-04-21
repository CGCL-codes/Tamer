package org.eclipse.swt.python;

import org.eclipse.swt.browser.ProgressListener;

public class ProgressAdapter implements ProgressListener {

    private long pythonObject;

    public ProgressAdapter() {
    }

    public void pythonExtension(long pythonObject) {
        this.pythonObject = pythonObject;
    }

    public long pythonExtension() {
        return this.pythonObject;
    }

    public void finalize() throws Throwable {
        pythonDecRef();
    }

    public native void pythonDecRef();

    public native void changed(org.eclipse.swt.browser.ProgressEvent arg0);

    public native void completed(org.eclipse.swt.browser.ProgressEvent arg0);
}

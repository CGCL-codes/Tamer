package sun.mtask.xlet;

import javax.microedition.xlet.*;
import javax.microedition.xlet.ixc.*;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Vector;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.reflect.Constructor;
import java.rmi.AccessException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import sun.misc.CDCAppClassLoader;
import com.sun.xlet.XletLifecycleHandler;

public class PXletManager implements XletLifecycleHandler {

    private static final int DEFAULT_XLET_WIDTH = 300;

    private static final int DEFAULT_XLET_HEIGHT = 300;

    private XletFrame xletFrame;

    private PXletContextImpl context;

    private Class xletClass;

    private Xlet xlet;

    private PXletStateQueue xletQueue;

    ThreadGroup threadGroup;

    private Object stateGuard = new Object();

    private XletState currentState = XletState.UNLOADED;

    private static boolean verbose = (System.getProperty("pxletmanager.verbose") != null) && (System.getProperty("pxletmanager.verbose").toLowerCase().equals("true"));

    protected PXletManager(ClassLoader xletClassLoader, String laf, String lafTheme, String mainClass, String[] args) throws ClassNotFoundException {
        xletClass = xletClassLoader.loadClass(mainClass);
        if (xletClass == null || (!(Xlet.class).isAssignableFrom(xletClass))) {
            throw new IllegalArgumentException("Attempt to run a non-Xlet class: " + mainClass);
        }
        context = new PXletContextImpl(mainClass, args, this);
        this.xletClass = xletClass;
        xletFrame = new XletFrame("Xlet Frame: " + mainClass, this, laf, lafTheme);
        threadGroup = new ThreadGroup(Thread.currentThread().getThreadGroup(), "Xlet Thread Group ");
        xletQueue = new PXletStateQueue(this);
        xletQueue.push(XletState.LOADED);
    }

    public Container getContainer() {
        return xletFrame.getContainer();
    }

    public void postInitXlet() {
        xletQueue.push(DesiredXletState.INITIALIZE);
    }

    public void postStartXlet() {
        xletQueue.push(XletState.ACTIVE);
    }

    public void postPauseXlet() {
        xletQueue.push(XletState.PAUSED);
    }

    public void postDestroyXlet(boolean unconditional) {
        if (!unconditional) {
            xletQueue.push(DesiredXletState.CONDITIONAL_DESTROY);
        } else {
            xletQueue.push(XletState.DESTROYED);
        }
    }

    public void setState(XletState state) {
        synchronized (stateGuard) {
            if (currentState == XletState.DESTROYED) return;
            currentState = state;
            stateGuard.notifyAll();
        }
        if (state == XletState.DESTROYED) {
            if (verbose) {
                System.err.println("@@PXletManager setting xlet state to DESTROYED");
            }
            System.exit(0);
        }
    }

    public int getState() {
        XletState state = getXletState();
        if (state == XletState.LOADED) {
            return LOADED;
        } else if (state == XletState.PAUSED) {
            return PAUSED;
        } else if (state == XletState.ACTIVE) {
            return ACTIVE;
        } else if (state == XletState.DESTROYED) {
            return DESTROYED;
        } else {
            return UNKNOWN;
        }
    }

    public XletState getXletState() {
        return currentState;
    }

    boolean requestCompleted = false;

    public void handleRequest(XletState desiredState) {
        XletState targetState = currentState;
        requestCompleted = false;
        try {
            synchronized (stateGuard) {
                if (desiredState == XletState.LOADED) {
                    if (currentState != XletState.UNLOADED) return;
                    targetState = XletState.LOADED;
                    Class[] types = new Class[0];
                    Constructor m = xletClass.getConstructor(types);
                    xlet = (Xlet) m.newInstance(new Object[0]);
                } else if (desiredState == DesiredXletState.INITIALIZE) {
                    if (currentState != XletState.LOADED) return;
                    targetState = XletState.PAUSED;
                    try {
                        xlet.initXlet(context);
                    } catch (XletStateChangeException xsce) {
                        targetState = XletState.DESTROYED;
                        try {
                            xlet.destroyXlet(true);
                        } catch (XletStateChangeException xsce2) {
                        }
                    }
                } else if (desiredState == XletState.ACTIVE) {
                    if (currentState != XletState.PAUSED) return;
                    targetState = XletState.ACTIVE;
                    try {
                        xlet.startXlet();
                    } catch (XletStateChangeException xsce) {
                        targetState = currentState;
                    }
                } else if (desiredState == XletState.PAUSED) {
                    if (currentState != XletState.ACTIVE) return;
                    targetState = XletState.PAUSED;
                    xlet.pauseXlet();
                } else if (desiredState == DesiredXletState.CONDITIONAL_DESTROY) {
                    if (currentState == XletState.DESTROYED) return;
                    targetState = XletState.DESTROYED;
                    try {
                        xlet.destroyXlet(false);
                    } catch (XletStateChangeException xsce) {
                        if (verbose) {
                            System.err.println("XLET REFUSED TO GO AWAY\n");
                        }
                        targetState = currentState;
                    }
                } else if (desiredState == XletState.DESTROYED) {
                    targetState = XletState.DESTROYED;
                    if (currentState == XletState.DESTROYED) return;
                    try {
                        xlet.destroyXlet(true);
                    } catch (XletStateChangeException xsce) {
                    }
                }
                setState(targetState);
            }
        } catch (Throwable e) {
            if (verbose) {
                if (verbose) {
                    System.err.println("EXCEPTION DURING XLET STATE HANDLING: " + e);
                }
            }
            e.printStackTrace();
            if (targetState == XletState.DESTROYED) {
                setState(XletState.DESTROYED);
            } else {
                handleRequest(XletState.DESTROYED);
            }
        }
        requestCompleted = true;
    }

    public static XletLifecycleHandler createXlet(String mainClass, String laf, String lafTheme) throws IOException {
        return (XletLifecycleHandler) createXlet(mainClass, laf, lafTheme, new String[] { "." }, new String[] {});
    }

    public static PXletManager createXlet(String mainClass, String laf, String lafTheme, String[] paths, String[] args) throws IOException {
        Vector v = new Vector();
        for (int i = 0; i < paths.length; i++) {
            URL url = parseURL(paths[i]);
            if (url != null) {
                v.add(url);
            }
        }
        PXletClassLoader cl = new PXletClassLoader((URL[]) v.toArray(new URL[0]), null);
        try {
            return new PXletManager(cl, laf, lafTheme, mainClass, args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException("Cannot find class " + mainClass);
        }
    }

    /**
     * Following the relevant RFC, construct a valid URL based on the passed in
     * string.
     *
     * @param url  A string which represents either a relative or absolute URL,
     *             or a string that could be an absolute or relative path.
     * @return     A URL when the passed in string can be interpreted according
     *             to the RFC.  <code>null</code> otherwise.
     */
    private static URL parseURL(String url) {
        URL u = null;
        try {
            if (url.startsWith(File.separator)) {
                u = new File(url).toURL();
            } else if (url.indexOf(':') <= 1) {
                u = new File(System.getProperty("user.dir"), url).getCanonicalFile().toURL();
            } else {
                if (url.startsWith("file:") && url.replace(File.separatorChar, '/').indexOf('/') == -1) {
                    String fname = url.substring("file:".length());
                    if (fname.length() > 0) {
                        u = new File(System.getProperty("user.dir"), fname).toURL();
                    } else {
                        u = new URL(url);
                    }
                } else {
                    u = new URL(url);
                }
            }
        } catch (IOException e) {
            if (verbose) {
                System.err.println("error in parsing: " + url);
            }
        }
        return u;
    }
}

class PXletClassLoader extends CDCAppClassLoader {

    public PXletClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}

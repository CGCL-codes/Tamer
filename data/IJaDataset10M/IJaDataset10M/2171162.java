package org.eclipse.swt.browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.browser.OS;
import org.eclipse.swt.internal.xhtml.Element;
import org.eclipse.swt.internal.xhtml.document;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

/**
 * Instances of this class implement the browser user interface
 * metaphor.  It allows the user to visualize and navigate through
 * HTML documents.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @since 3.0
 * 
 * @j2sPrefix
 * $WTC$$.registerCSS ("$wt.browser.Browser");
 */
public class Browser extends Composite {

    boolean back, forward, navigate, delaySetText;

    boolean addressBar = true, menuBar = true, statusBar = true, toolBar = true;

    int info;

    int globalDispatch;

    String html;

    CloseWindowListener[] closeWindowListeners = new CloseWindowListener[0];

    LocationListener[] locationListeners = new LocationListener[0];

    OpenWindowListener[] openWindowListeners = new OpenWindowListener[0];

    ProgressListener[] progressListeners = new ProgressListener[0];

    StatusTextListener[] statusTextListeners = new StatusTextListener[0];

    TitleListener[] titleListeners = new TitleListener[0];

    VisibilityWindowListener[] visibilityWindowListeners = new VisibilityWindowListener[0];

    private String url;

    static final int BeforeNavigate2 = 0xfa;

    static final int CommandStateChange = 0x69;

    static final int DocumentComplete = 0x103;

    static final int NavigateComplete2 = 0xfc;

    static final int NewWindow2 = 0xfb;

    static final int OnMenuBar = 0x100;

    static final int OnStatusBar = 0x101;

    static final int OnToolBar = 0xff;

    static final int OnVisible = 0xfe;

    static final int ProgressChange = 0x6c;

    static final int RegisterAsBrowser = 0x228;

    static final int StatusTextChange = 0x66;

    static final int TitleChange = 0x71;

    static final int WindowClosing = 0x107;

    static final int WindowSetHeight = 0x10b;

    static final int WindowSetLeft = 0x108;

    static final int WindowSetResizable = 0x106;

    static final int WindowSetTop = 0x109;

    static final int WindowSetWidth = 0x10a;

    static final String ABOUT_BLANK = "about:blank";

    static final String URL_DIRECTOR = "http://download.macromedia.com/pub/shockwave/cabs/director/sw.cab";

    static final String PACKAGE_PREFIX = "org.eclipse.swt.browser.";

    private Element browserHandle;

    /**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for browser creation</li>
 * </ul>
 * 
 * @see Widget#getStyle
 * 
 * @since 3.0
 */
    public Browser(Composite parent, int style) {
        super(parent, style & ~SWT.BORDER);
    }

    /**	 
 * Adds the listener to the collection of listeners who will be
 * notified when the window hosting the receiver should be closed.
 * <p>
 * This notification occurs when a javascript command such as
 * <code>window.close</code> gets executed by a <code>Browser</code>.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
    public void addCloseWindowListener(CloseWindowListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        CloseWindowListener[] newCloseWindowListeners = new CloseWindowListener[closeWindowListeners.length + 1];
        System.arraycopy(closeWindowListeners, 0, newCloseWindowListeners, 0, closeWindowListeners.length);
        closeWindowListeners = newCloseWindowListeners;
        closeWindowListeners[closeWindowListeners.length - 1] = listener;
    }

    /**	 
 * Adds the listener to the collection of listeners who will be
 * notified when the current location has changed or is about to change.
 * <p>
 * This notification typically occurs when the application navigates
 * to a new location with {@link #setUrl(String)} or when the user
 * activates a hyperlink.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
    public void addLocationListener(LocationListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        LocationListener[] newLocationListeners = new LocationListener[locationListeners.length + 1];
        System.arraycopy(locationListeners, 0, newLocationListeners, 0, locationListeners.length);
        locationListeners = newLocationListeners;
        locationListeners[locationListeners.length - 1] = listener;
    }

    /**	 
 * Adds the listener to the collection of listeners who will be
 * notified when a new window needs to be created.
 * <p>
 * This notification occurs when a javascript command such as
 * <code>window.open</code> gets executed by a <code>Browser</code>.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
    public void addOpenWindowListener(OpenWindowListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        OpenWindowListener[] newOpenWindowListeners = new OpenWindowListener[openWindowListeners.length + 1];
        System.arraycopy(openWindowListeners, 0, newOpenWindowListeners, 0, openWindowListeners.length);
        openWindowListeners = newOpenWindowListeners;
        openWindowListeners[openWindowListeners.length - 1] = listener;
    }

    /**	 
 * Adds the listener to the collection of listeners who will be
 * notified when a progress is made during the loading of the current 
 * URL or when the loading of the current URL has been completed.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
    public void addProgressListener(ProgressListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        ProgressListener[] newProgressListeners = new ProgressListener[progressListeners.length + 1];
        System.arraycopy(progressListeners, 0, newProgressListeners, 0, progressListeners.length);
        progressListeners = newProgressListeners;
        progressListeners[progressListeners.length - 1] = listener;
    }

    /**	 
 * Adds the listener to the collection of listeners who will be
 * notified when the status text is changed.
 * <p>
 * The status text is typically displayed in the status bar of
 * a browser application.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
    public void addStatusTextListener(StatusTextListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        StatusTextListener[] newStatusTextListeners = new StatusTextListener[statusTextListeners.length + 1];
        System.arraycopy(statusTextListeners, 0, newStatusTextListeners, 0, statusTextListeners.length);
        statusTextListeners = newStatusTextListeners;
        statusTextListeners[statusTextListeners.length - 1] = listener;
    }

    /**	 
 * Adds the listener to the collection of listeners who will be
 * notified when the title of the current document is available
 * or has changed.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
    public void addTitleListener(TitleListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        TitleListener[] newTitleListeners = new TitleListener[titleListeners.length + 1];
        System.arraycopy(titleListeners, 0, newTitleListeners, 0, titleListeners.length);
        titleListeners = newTitleListeners;
        titleListeners[titleListeners.length - 1] = listener;
    }

    /**	 
 * Adds the listener to the collection of listeners who will be
 * notified when a window hosting the receiver needs to be displayed
 * or hidden.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
    public void addVisibilityWindowListener(VisibilityWindowListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        VisibilityWindowListener[] newVisibilityWindowListeners = new VisibilityWindowListener[visibilityWindowListeners.length + 1];
        System.arraycopy(visibilityWindowListeners, 0, newVisibilityWindowListeners, 0, visibilityWindowListeners.length);
        visibilityWindowListeners = newVisibilityWindowListeners;
        visibilityWindowListeners[visibilityWindowListeners.length - 1] = listener;
    }

    /**
 * Navigate to the previous session history item.
 *
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see #forward
 * 
 * @since 3.0
 */
    public boolean back() {
        checkWidget();
        if (!back) return false;
        if (browserHandle != null && browserHandle.contentWindow != null) {
            try {
                browserHandle.contentWindow.history.back();
                forward = true;
                return true;
            } catch (Error e) {
                return false;
            }
        }
        return false;
    }

    protected void checkSubclass() {
        String name = getClass().getName();
        int index = name.lastIndexOf('.');
        if (!name.substring(0, index + 1).equals(PACKAGE_PREFIX)) {
            SWT.error(SWT.ERROR_INVALID_SUBCLASS);
        }
    }

    protected void createHandle() {
        super.createHandle();
        browserHandle = document.createElement("IFRAME");
        browserHandle.className = "browser-default";
        browserHandle.style.border = "0 none transparent";
        if (OS.isIE) {
            browserHandle.setAttribute("frameBorder", "0");
        }
        handle.appendChild(browserHandle);
    }

    /**
 * Execute the specified script.
 *
 * <p>
 * Execute a script containing javascript commands in the context of the current document. 
 * 
 * @param script the script with javascript commands
 *  
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the script is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.1
 */
    public boolean execute(String script) {
        checkWidget();
        if (script == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        {
        }
        return true;
    }

    /**
 * Navigate to the next session history item.
 *
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @see #back
 * 
 * @since 3.0
 */
    public boolean forward() {
        checkWidget();
        if (!forward) return false;
        if (browserHandle != null && browserHandle.contentWindow != null) {
            try {
                browserHandle.contentWindow.history.forward();
                return true;
            } catch (Error e) {
                return false;
            }
        }
        return false;
    }

    /**
 * Returns <code>true</code> if the receiver can navigate to the 
 * previous session history item, and <code>false</code> otherwise.
 *
 * @return the receiver's back command enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #back
 */
    public boolean isBackEnabled() {
        checkWidget();
        return back;
    }

    /**
 * Returns <code>true</code> if the receiver can navigate to the 
 * next session history item, and <code>false</code> otherwise.
 *
 * @return the receiver's forward command enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #forward
 */
    public boolean isForwardEnabled() {
        checkWidget();
        return forward;
    }

    /**
 * Returns the current URL.
 *
 * @return the current URL or an empty <code>String</code> if there is no current URL
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see #setUrl
 * 
 * @since 3.0
 */
    public String getUrl() {
        checkWidget();
        return handle.contentWindow.location;
    }

    /**
 * Refresh the current page.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
    public void refresh() {
        checkWidget();
        if (browserHandle != null) {
            browserHandle.src = url;
        }
    }

    protected void releaseHandle() {
        if (browserHandle != null) {
            OS.destroyHandle(browserHandle);
            browserHandle = null;
        }
        super.releaseHandle();
    }

    /**	 
 * Removes the listener from the collection of listeners who will
 * be notified when the window hosting the receiver should be closed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
    public void removeCloseWindowListener(CloseWindowListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (closeWindowListeners.length == 0) return;
        int index = -1;
        for (int i = 0; i < closeWindowListeners.length; i++) {
            if (listener == closeWindowListeners[i]) {
                index = i;
                break;
            }
        }
        if (index == -1) return;
        if (closeWindowListeners.length == 1) {
            closeWindowListeners = new CloseWindowListener[0];
            return;
        }
        CloseWindowListener[] newCloseWindowListeners = new CloseWindowListener[closeWindowListeners.length - 1];
        System.arraycopy(closeWindowListeners, 0, newCloseWindowListeners, 0, index);
        System.arraycopy(closeWindowListeners, index + 1, newCloseWindowListeners, index, closeWindowListeners.length - index - 1);
        closeWindowListeners = newCloseWindowListeners;
    }

    /**	 
 * Removes the listener from the collection of listeners who will
 * be notified when the current location is changed or about to be changed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
    public void removeLocationListener(LocationListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (locationListeners.length == 0) return;
        int index = -1;
        for (int i = 0; i < locationListeners.length; i++) {
            if (listener == locationListeners[i]) {
                index = i;
                break;
            }
        }
        if (index == -1) return;
        if (locationListeners.length == 1) {
            locationListeners = new LocationListener[0];
            return;
        }
        LocationListener[] newLocationListeners = new LocationListener[locationListeners.length - 1];
        System.arraycopy(locationListeners, 0, newLocationListeners, 0, index);
        System.arraycopy(locationListeners, index + 1, newLocationListeners, index, locationListeners.length - index - 1);
        locationListeners = newLocationListeners;
    }

    /**	 
 * Removes the listener from the collection of listeners who will
 * be notified when a new window needs to be created.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
    public void removeOpenWindowListener(OpenWindowListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (openWindowListeners.length == 0) return;
        int index = -1;
        for (int i = 0; i < openWindowListeners.length; i++) {
            if (listener == openWindowListeners[i]) {
                index = i;
                break;
            }
        }
        if (index == -1) return;
        if (openWindowListeners.length == 1) {
            openWindowListeners = new OpenWindowListener[0];
            return;
        }
        OpenWindowListener[] newOpenWindowListeners = new OpenWindowListener[openWindowListeners.length - 1];
        System.arraycopy(openWindowListeners, 0, newOpenWindowListeners, 0, index);
        System.arraycopy(openWindowListeners, index + 1, newOpenWindowListeners, index, openWindowListeners.length - index - 1);
        openWindowListeners = newOpenWindowListeners;
    }

    /**	 
 * Removes the listener from the collection of listeners who will
 * be notified when a progress is made during the loading of the current 
 * URL or when the loading of the current URL has been completed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
    public void removeProgressListener(ProgressListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (progressListeners.length == 0) return;
        int index = -1;
        for (int i = 0; i < progressListeners.length; i++) {
            if (listener == progressListeners[i]) {
                index = i;
                break;
            }
        }
        if (index == -1) return;
        if (progressListeners.length == 1) {
            progressListeners = new ProgressListener[0];
            return;
        }
        ProgressListener[] newProgressListeners = new ProgressListener[progressListeners.length - 1];
        System.arraycopy(progressListeners, 0, newProgressListeners, 0, index);
        System.arraycopy(progressListeners, index + 1, newProgressListeners, index, progressListeners.length - index - 1);
        progressListeners = newProgressListeners;
    }

    /**	 
 * Removes the listener from the collection of listeners who will
 * be notified when the status text is changed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
    public void removeStatusTextListener(StatusTextListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (statusTextListeners.length == 0) return;
        int index = -1;
        for (int i = 0; i < statusTextListeners.length; i++) {
            if (listener == statusTextListeners[i]) {
                index = i;
                break;
            }
        }
        if (index == -1) return;
        if (statusTextListeners.length == 1) {
            statusTextListeners = new StatusTextListener[0];
            return;
        }
        StatusTextListener[] newStatusTextListeners = new StatusTextListener[statusTextListeners.length - 1];
        System.arraycopy(statusTextListeners, 0, newStatusTextListeners, 0, index);
        System.arraycopy(statusTextListeners, index + 1, newStatusTextListeners, index, statusTextListeners.length - index - 1);
        statusTextListeners = newStatusTextListeners;
    }

    /**	 
 * Removes the listener from the collection of listeners who will
 * be notified when the title of the current document is available
 * or has changed.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
    public void removeTitleListener(TitleListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (titleListeners.length == 0) return;
        int index = -1;
        for (int i = 0; i < titleListeners.length; i++) {
            if (listener == titleListeners[i]) {
                index = i;
                break;
            }
        }
        if (index == -1) return;
        if (titleListeners.length == 1) {
            titleListeners = new TitleListener[0];
            return;
        }
        TitleListener[] newTitleListeners = new TitleListener[titleListeners.length - 1];
        System.arraycopy(titleListeners, 0, newTitleListeners, 0, index);
        System.arraycopy(titleListeners, index + 1, newTitleListeners, index, titleListeners.length - index - 1);
        titleListeners = newTitleListeners;
    }

    /**	 
 * Removes the listener from the collection of listeners who will
 * be notified when a window hosting the receiver needs to be displayed
 * or hidden.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
    public void removeVisibilityWindowListener(VisibilityWindowListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        if (visibilityWindowListeners.length == 0) return;
        int index = -1;
        for (int i = 0; i < visibilityWindowListeners.length; i++) {
            if (listener == visibilityWindowListeners[i]) {
                index = i;
                break;
            }
        }
        if (index == -1) return;
        if (visibilityWindowListeners.length == 1) {
            visibilityWindowListeners = new VisibilityWindowListener[0];
            return;
        }
        VisibilityWindowListener[] newVisibilityWindowListeners = new VisibilityWindowListener[visibilityWindowListeners.length - 1];
        System.arraycopy(visibilityWindowListeners, 0, newVisibilityWindowListeners, 0, index);
        System.arraycopy(visibilityWindowListeners, index + 1, newVisibilityWindowListeners, index, visibilityWindowListeners.length - index - 1);
        visibilityWindowListeners = newVisibilityWindowListeners;
    }

    /**
 * Renders HTML.
 * 
 * <p>
 * The html parameter is Unicode encoded since it is a java <code>String</code>.
 * As a result, the HTML meta tag charset should not be set. The charset is implied
 * by the <code>String</code> itself.
 * 
 * @param html the HTML content to be rendered
 *
 * @return true if the operation was successful and false otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the html is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *  
 * @see #setUrl
 * 
 * @since 3.0
 */
    public boolean setText(String html) {
        checkWidget();
        if (html == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        boolean blankLoading = this.html != null;
        this.html = html;
        if (blankLoading) return true;
        if (browserHandle != null) {
            iframeDocumentWrite(browserHandle, html);
        }
        this.html = null;
        return true;
    }

    /**
 * @j2sNative
return function () {
	try {
		var doc = handle.contentWindow.document;
		doc.open ();
		if (O$.isIE && window["xss.domain.enabled"] == true
				&& domain != null && domain.length > 0) {
			doc.domain = domain;
		}
		doc.write (html);
		doc.close ();
		handle = null;
	} catch (e) {
		window.setTimeout (arguments.callee, 25);
	}
};
 */
    native Object generateLazyIframeWriting(Object handle, String domain, String html);

    /**
 * @param handle
 * @param html
 * @j2sNative
var handle = arguments[0];
var html = arguments[1];
var domain = null;
try {
	domain = document.domain;
} catch (e) {}
if (O$.isIE && window["xss.domain.enabled"] == true
		&& domain != null && domain.length > 0) {
	document.domain = domain;
}
if (handle.contentWindow != null) {
	if (O$.isIE && window["xss.domain.enabled"] == true
			&& domain != null && domain.length > 0) {
		handle.contentWindow.location = "javascript:document.open();document.domain='" + domain + "';document.close();void(0);";
	} else {
		handle.contentWindow.location = "about:blank";
	}
} else { // Opera
	handle.src = "about:blank";
}
try {
	var doc = handle.contentWindow.document;
	doc.open ();
	if (O$.isIE && window["xss.domain.enabled"] == true
			&& domain != null && domain.length > 0) {
		doc.domain = domain;
	}
	doc.write (html);
	doc.close ();
} catch (e) {
	if (O$.isIE && (domain == null || domain.length == 0)
			&& e.message != null && e.message.indexOf ("Access is denied") != -1) {
		var jsHTML = html.replaceAll("\\\\", "\\\\\\\\")
				.replaceAll("\r", "\\\\r")
				.replaceAll("\n", "\\\\n")
				.replaceAll("\"", "\\\\\"");
		handle.src = "javascript:document.open();document.write (\"" + jsHTML + "\");document.close();void(0);";
		// In IE 8.0, it is still failing ...
	} else {
		window.setTimeout (this.generateLazyIframeWriting (handle, domain, html), 25);
	}
}
 */
    private native void iframeDocumentWrite(Object handle, String html);

    /**
 * Loads a URL.
 * 
 * @param url the URL to be loaded
 *
 * @return true if the operation was successful and false otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the url is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *  
 * @see #getUrl
 * 
 * @since 3.0
 */
    public boolean setUrl(String url) {
        checkWidget();
        if (url == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        html = null;
        this.url = url;
        if (browserHandle != null) {
            browserHandle.src = url;
        }
        back = true;
        return true;
    }

    /**
 * Stop any loading and rendering activity.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
    public void stop() {
        checkWidget();
        if (browserHandle != null) {
            if (browserHandle.contentWindow != null) {
                browserHandle.contentWindow.stop();
            } else {
            }
        }
    }

    protected boolean useNativeScrollBar() {
        return true;
    }
}

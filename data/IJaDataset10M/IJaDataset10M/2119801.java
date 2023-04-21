package org.eclipse.jface.text;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * This manager controls the layout, content, and visibility of an information
 * control in reaction to mouse hover events issued by the text widget of a
 * text viewer. It overrides <code>computeInformation</code>, so that the
 * computation is performed in a dedicated background thread. This implies
 * that the used <code>ITextHover</code> objects must be capable of
 * operating in a non-UI thread.
 *
 * @since 2.0
 */
class TextViewerHoverManager extends AbstractHoverInformationControlManager implements IWidgetTokenKeeper, IWidgetTokenKeeperExtension {

    /**
	 * Priority of the hovers managed by this manager.
	 * Default value: <code>0</code>;
	 * @since 3.0
	 */
    public static final int WIDGET_PRIORITY = 0;

    /** The text viewer */
    private TextViewer fTextViewer;

    /** The hover information computation thread */
    private Thread fThread;

    /** The stopper of the computation thread */
    private ITextListener fStopper;

    /** Internal monitor */
    private Object fMutex = new Object();

    /** The currently shown text hover. */
    private volatile ITextHover fTextHover;

    /**
	 * Tells whether the next mouse hover event
	 * should be processed.
	 * @since 3.0
	 */
    private boolean fProcessMouseHoverEvent = true;

    /**
	 * Internal mouse move listener.
	 * @since 3.0
	 */
    private MouseMoveListener fMouseMoveListener;

    /**
	 * Internal view port listener.
	 * @since 3.0
	 */
    private IViewportListener fViewportListener;

    /**
	 * Creates a new text viewer hover manager specific for the given text viewer.
	 * The manager uses the given information control creator.
	 *
	 * @param textViewer the viewer for which the controller is created
	 * @param creator the information control creator
	 */
    public TextViewerHoverManager(TextViewer textViewer, IInformationControlCreator creator) {
        super(creator);
        fTextViewer = textViewer;
        fStopper = new ITextListener() {

            public void textChanged(TextEvent event) {
                synchronized (fMutex) {
                    if (fThread != null) {
                        fThread.interrupt();
                        fThread = null;
                    }
                }
            }
        };
        fViewportListener = new IViewportListener() {

            public void viewportChanged(int verticalOffset) {
                fProcessMouseHoverEvent = false;
            }
        };
        fTextViewer.addViewportListener(fViewportListener);
        fMouseMoveListener = new MouseMoveListener() {

            public void mouseMove(MouseEvent event) {
                fProcessMouseHoverEvent = true;
            }
        };
        fTextViewer.getTextWidget().addMouseMoveListener(fMouseMoveListener);
    }

    /**
	 * Determines all necessary details and delegates the computation into
	 * a background thread.
	 */
    protected void computeInformation() {
        if (!fProcessMouseHoverEvent) {
            setInformation(null, null);
            return;
        }
        Point location = getHoverEventLocation();
        int offset = computeOffsetAtLocation(location.x, location.y);
        if (offset == -1) {
            setInformation(null, null);
            return;
        }
        final ITextHover hover = fTextViewer.getTextHover(offset, getHoverEventStateMask());
        if (hover == null) {
            setInformation(null, null);
            return;
        }
        final IRegion region = hover.getHoverRegion(fTextViewer, offset);
        if (region == null) {
            setInformation(null, null);
            return;
        }
        final Rectangle area = JFaceTextUtil.computeArea(region, fTextViewer);
        if (area == null || area.isEmpty()) {
            setInformation(null, null);
            return;
        }
        if (fThread != null) {
            setInformation(null, null);
            return;
        }
        fThread = new Thread("Text Viewer Hover Presenter") {

            public void run() {
                boolean hasFinished = false;
                try {
                    if (fThread != null) {
                        Object information;
                        try {
                            if (hover instanceof ITextHoverExtension2) information = ((ITextHoverExtension2) hover).getHoverInfo2(fTextViewer, region); else information = hover.getHoverInfo(fTextViewer, region);
                        } catch (ArrayIndexOutOfBoundsException x) {
                            information = null;
                        }
                        if (hover instanceof ITextHoverExtension) setCustomInformationControlCreator(((ITextHoverExtension) hover).getHoverControlCreator()); else setCustomInformationControlCreator(null);
                        setInformation(information, area);
                        if (information != null) fTextHover = hover;
                    } else {
                        setInformation(null, null);
                    }
                    hasFinished = true;
                } catch (RuntimeException ex) {
                    String PLUGIN_ID = "org.eclipse.jface.text";
                    ILog log = Platform.getLog(Platform.getBundle(PLUGIN_ID));
                    log.log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, "Unexpected runtime error while computing a text hover", ex));
                } finally {
                    synchronized (fMutex) {
                        if (fTextViewer != null) fTextViewer.removeTextListener(fStopper);
                        fThread = null;
                        if (!hasFinished) setInformation(null, null);
                    }
                }
            }
        };
        fThread.setDaemon(true);
        fThread.setPriority(Thread.MIN_PRIORITY);
        synchronized (fMutex) {
            fTextViewer.addTextListener(fStopper);
            fThread.start();
        }
    }

    /**
	 * As computation is done in the background, this method is
	 * also called in the background thread. Delegates the control
	 * flow back into the UI thread, in order to allow displaying the
	 * information in the information control.
	 */
    protected void presentInformation() {
        if (fTextViewer == null) return;
        StyledText textWidget = fTextViewer.getTextWidget();
        if (textWidget != null && !textWidget.isDisposed()) {
            Display display = textWidget.getDisplay();
            if (display == null) return;
            display.asyncExec(new Runnable() {

                public void run() {
                    doPresentInformation();
                }
            });
        }
    }

    protected void doPresentInformation() {
        super.presentInformation();
    }

    /**
	 * Computes the document offset underlying the given text widget coordinates.
	 * This method uses a linear search as it cannot make any assumption about
	 * how the document is actually presented in the widget. (Covers cases such
	 * as bidirectional text.)
	 *
	 * @param x the horizontal coordinate inside the text widget
	 * @param y the vertical coordinate inside the text widget
	 * @return the document offset corresponding to the given point
	 */
    private int computeOffsetAtLocation(int x, int y) {
        try {
            StyledText styledText = fTextViewer.getTextWidget();
            int widgetOffset = styledText.getOffsetAtLocation(new Point(x, y));
            Point p = styledText.getLocationAtOffset(widgetOffset);
            if (p.x > x) widgetOffset--;
            if (fTextViewer instanceof ITextViewerExtension5) {
                ITextViewerExtension5 extension = (ITextViewerExtension5) fTextViewer;
                return extension.widgetOffset2ModelOffset(widgetOffset);
            }
            return widgetOffset + fTextViewer._getVisibleRegionOffset();
        } catch (IllegalArgumentException e) {
            return -1;
        }
    }

    protected void showInformationControl(Rectangle subjectArea) {
        if (fTextViewer != null && fTextViewer.requestWidgetToken(this, WIDGET_PRIORITY)) super.showInformationControl(subjectArea); else if (DEBUG) System.out.println("TextViewerHoverManager#showInformationControl(..) did not get widget token");
    }

    protected void hideInformationControl() {
        try {
            fTextHover = null;
            super.hideInformationControl();
        } finally {
            if (fTextViewer != null) fTextViewer.releaseWidgetToken(this);
        }
    }

    void replaceInformationControl(boolean takeFocus) {
        if (fTextViewer != null) fTextViewer.releaseWidgetToken(this);
        super.replaceInformationControl(takeFocus);
    }

    protected void handleInformationControlDisposed() {
        try {
            super.handleInformationControlDisposed();
        } finally {
            if (fTextViewer != null) fTextViewer.releaseWidgetToken(this);
        }
    }

    public boolean requestWidgetToken(IWidgetTokenOwner owner) {
        fTextHover = null;
        super.hideInformationControl();
        return true;
    }

    public boolean requestWidgetToken(IWidgetTokenOwner owner, int priority) {
        if (priority > WIDGET_PRIORITY) {
            fTextHover = null;
            super.hideInformationControl();
            return true;
        }
        return false;
    }

    public boolean setFocus(IWidgetTokenOwner owner) {
        if (!hasInformationControlReplacer()) return false;
        IInformationControl iControl = getCurrentInformationControl();
        if (canReplace(iControl)) {
            if (cancelReplacingDelay()) replaceInformationControl(true);
            return true;
        }
        return false;
    }

    /**
	 * Returns the currently shown text hover or <code>null</code> if no text
	 * hover is shown.
	 *
	 * @return the currently shown text hover or <code>null</code>
	 */
    protected ITextHover getCurrentTextHover() {
        return fTextHover;
    }

    public void dispose() {
        if (fTextViewer != null) {
            fTextViewer.removeViewportListener(fViewportListener);
            fViewportListener = null;
            StyledText st = fTextViewer.getTextWidget();
            if (st != null && !st.isDisposed()) st.removeMouseMoveListener(fMouseMoveListener);
            fMouseMoveListener = null;
        }
        super.dispose();
    }
}

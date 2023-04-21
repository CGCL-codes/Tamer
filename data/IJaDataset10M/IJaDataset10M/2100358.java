package com.aptana.ide.editor.js.contentassist;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.text.AbstractInformationControlManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.IViewportListener;
import org.eclipse.jface.text.IWidgetTokenKeeper;
import org.eclipse.jface.text.IWidgetTokenKeeperExtension;
import org.eclipse.jface.text.IWidgetTokenOwner;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.information.IInformationPresenterExtension;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.IInformationProviderExtension;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * @author Paul Colton
 *
 */
public class JSInformationPresenter extends AbstractInformationControlManager implements IInformationPresenter, IInformationPresenterExtension, IWidgetTokenKeeper, IWidgetTokenKeeperExtension {

    public static final int WIDGET_PRIORITY = 5;

    /**
	 * Internal information control closer. Listens to several events issued by its subject control
	 * and closes the information control when necessary.
	 */
    class Closer implements IInformationControlCloser, ControlListener, MouseListener, FocusListener, IViewportListener, KeyListener {

        /** The subject control. */
        private Control fSubjectControl;

        /** The information control. */
        private IInformationControl fInformationControlToClose;

        /** Indicates whether this closer is active. */
        private boolean fIsActive = false;

        /**
		 * @see IInformationControlCloser#setSubjectControl(Control)
		 */
        public void setSubjectControl(Control control) {
            fSubjectControl = control;
        }

        /**
		 * @see IInformationControlCloser#setInformationControl(IInformationControl)
		 */
        public void setInformationControl(IInformationControl control) {
            fInformationControlToClose = control;
        }

        /**
		 * @see IInformationControlCloser#start(Rectangle)
		 */
        public void start(Rectangle informationArea) {
            if (fIsActive) {
                return;
            }
            fIsActive = true;
            if (fSubjectControl != null && !fSubjectControl.isDisposed()) {
                fSubjectControl.addControlListener(this);
                fSubjectControl.addMouseListener(this);
                fSubjectControl.addFocusListener(this);
                fSubjectControl.addKeyListener(this);
            }
            if (fInformationControlToClose != null) {
                fInformationControlToClose.addFocusListener(this);
            }
            fTextViewer.addViewportListener(this);
        }

        /**
		 * @see IInformationControlCloser#stop()
		 */
        public void stop() {
            if (!fIsActive) {
                return;
            }
            fIsActive = false;
            fTextViewer.removeViewportListener(this);
            if (fInformationControlToClose != null) {
                fInformationControlToClose.removeFocusListener(this);
            }
            hideInformationControl();
            if (fSubjectControl != null && !fSubjectControl.isDisposed()) {
                fSubjectControl.removeControlListener(this);
                fSubjectControl.removeMouseListener(this);
                fSubjectControl.removeFocusListener(this);
                fSubjectControl.removeKeyListener(this);
            }
        }

        /**
		 * @see ControlListener#controlResized(ControlEvent)
		 */
        public void controlResized(ControlEvent e) {
            stop();
        }

        /**
		 * @see ControlListener#controlMoved(ControlEvent)
		 */
        public void controlMoved(ControlEvent e) {
            stop();
        }

        /**
		 * @see MouseListener#mouseDown(MouseEvent)
		 */
        public void mouseDown(MouseEvent e) {
            stop();
        }

        /**
		 * @see MouseListener#mouseUp(MouseEvent)
		 */
        public void mouseUp(MouseEvent e) {
        }

        /**
		 * @see MouseListener#mouseDoubleClick(MouseEvent)
		 */
        public void mouseDoubleClick(MouseEvent e) {
            stop();
        }

        /**
		 * @see FocusListener#focusGained(FocusEvent)
		 */
        public void focusGained(FocusEvent e) {
        }

        /**
		 * @see FocusListener#focusLost(FocusEvent)
		 */
        public void focusLost(FocusEvent e) {
            Display d = fSubjectControl.getDisplay();
            d.asyncExec(new Runnable() {

                public void run() {
                    if (fInformationControlToClose == null || !fInformationControlToClose.isFocusControl()) {
                        stop();
                    }
                }
            });
        }

        /**
		 * @see org.eclipse.jface.text.IViewportListener#viewportChanged(int)
		 */
        public void viewportChanged(int topIndex) {
            stop();
        }

        /**
		 * @see KeyListener#keyPressed(KeyEvent)
		 */
        public void keyPressed(KeyEvent e) {
            stop();
        }

        /**
		 * @see KeyListener#keyReleased(KeyEvent)
		 */
        public void keyReleased(KeyEvent e) {
        }
    }

    /** The text viewer this information presenter works on */
    private ITextViewer fTextViewer;

    /** The map of <code>IInformationProvider</code> objects */
    private Map fProviders;

    /** The offset to override selection. */
    private int fOffset = -1;

    /**
	 * The document partitioning for this information presenter.
	 * @since 3.0
	 */
    private String fPartitioning;

    /**
	 * Creates a new information presenter that uses the given information control creator.
	 * The presenter is not installed on any text viewer yet. By default, an information
	 * control closer is set that closes the information control in the event of key strokes,
	 * resizing, moves, focus changes, mouse clicks, and disposal - all of those applied to
	 * the information control's parent control. Also, the setup ensures that the information
	 * control when made visible will request the focus. By default, the default document
	 * partitioning {@link IDocumentExtension3#DEFAULT_PARTITIONING} is used.
	 *
	 * @param creator the information control creator to be used
	 */
    public JSInformationPresenter(IInformationControlCreator creator) {
        super(creator);
        setCloser(new Closer());
        takesFocusWhenVisible(true);
        fPartitioning = IDocumentExtension3.DEFAULT_PARTITIONING;
    }

    /**
	 * Sets the document partitioning to be used by this information presenter.
	 *
	 * @param partitioning the document partitioning to be used by this information presenter
	 * @since 3.0
	 */
    public void setDocumentPartitioning(String partitioning) {
        fPartitioning = partitioning;
    }

    /**
	 * @see org.eclipse.jface.text.information.IInformationPresenterExtension#getDocumentPartitioning()
	 * @since 3.0
	 */
    public String getDocumentPartitioning() {
        return fPartitioning;
    }

    /**
	 * Registers a given information provider for a particular content type.
	 * If there is already a provider registered for this type, the new provider
	 * is registered instead of the old one.
	 *
	 * @param provider the information provider to register, or <code>null</code> to remove an existing one
	 * @param contentType the content type under which to register
	 */
    public void setInformationProvider(IInformationProvider provider, String contentType) {
        if (fProviders == null) {
            fProviders = new HashMap();
        }
        if (provider == null) {
            fProviders.remove(contentType);
        } else {
            fProviders.put(contentType, provider);
        }
    }

    /**
	 * @see IInformationPresenter#getInformationProvider(String)
	 */
    public IInformationProvider getInformationProvider(String contentType) {
        if (fProviders == null) {
            return null;
        }
        return (IInformationProvider) fProviders.get(contentType);
    }

    /**
	 * Sets a offset to override the selection. Setting the value to <code>-1</code> will disable
	 * overriding.
	 *
	 * @param offset the offset to override selection or <code>-1</code>
	 */
    public void setOffset(int offset) {
        fOffset = offset;
    }

    /**
	 * @see AbstractInformationControlManager#computeInformation()
	 */
    protected void computeInformation() {
        int offset = fOffset < 0 ? fTextViewer.getSelectedRange().x : fOffset;
        if (offset == -1) {
            return;
        }
        fOffset = -1;
        IInformationProvider provider = null;
        try {
            String contentType = TextUtilities.getContentType(fTextViewer.getDocument(), getDocumentPartitioning(), offset, true);
            provider = getInformationProvider(contentType);
        } catch (BadLocationException x) {
        }
        if (provider == null) {
            return;
        }
        IRegion subject = provider.getSubject(fTextViewer, offset);
        if (subject == null) {
            return;
        }
        if (provider instanceof IInformationProviderExtension2) {
            setCustomInformationControlCreator(((IInformationProviderExtension2) provider).getInformationPresenterControlCreator());
        } else {
            setCustomInformationControlCreator(null);
        }
        if (provider instanceof IInformationProviderExtension) {
            IInformationProviderExtension extension = (IInformationProviderExtension) provider;
            setInformation(extension.getInformation2(fTextViewer, subject), computeArea(subject));
        } else {
            setInformation(provider.getInformation(fTextViewer, subject), computeArea(subject));
        }
    }

    /**
	 * Determines the graphical area covered by the given text region.
	 *
	 * @param region the region whose graphical extend must be computed
	 * @return the graphical extend of the given region
	 */
    private Rectangle computeArea(IRegion region) {
        int start = 0;
        int end = 0;
        IRegion widgetRegion = modelRange2WidgetRange(region);
        if (widgetRegion != null) {
            start = widgetRegion.getOffset();
            end = widgetRegion.getOffset() + widgetRegion.getLength();
        }
        StyledText styledText = fTextViewer.getTextWidget();
        Rectangle bounds;
        if (end > 0 && start < end) {
            bounds = styledText.getTextBounds(start, end - 1);
        } else {
            GC gc = new GC(styledText);
            int width = gc.getFontMetrics().getAverageCharWidth();
            gc.dispose();
            Point loc = styledText.getLocationAtOffset(start);
            bounds = new Rectangle(loc.x, loc.y, width, styledText.getLineHeight());
        }
        return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
	 * Translated the given range in the viewer's document into the corresponding
	 * range of the viewer's widget.
	 *
	 * @param region the range in the viewer's document
	 * @return the corresponding widget range
	 * @since 2.1
	 */
    private IRegion modelRange2WidgetRange(IRegion region) {
        if (fTextViewer instanceof ITextViewerExtension5) {
            ITextViewerExtension5 extension = (ITextViewerExtension5) fTextViewer;
            return extension.modelRange2WidgetRange(region);
        }
        IRegion visibleRegion = fTextViewer.getVisibleRegion();
        int start = region.getOffset() - visibleRegion.getOffset();
        int end = start + region.getLength();
        if (end > visibleRegion.getLength()) {
            end = visibleRegion.getLength();
        }
        return new Region(start, end - start);
    }

    /**
	 * @see IInformationPresenter#install(ITextViewer)
	 */
    public void install(ITextViewer textViewer) {
        fTextViewer = textViewer;
        install(fTextViewer.getTextWidget());
    }

    /**
	 * @see IInformationPresenter#uninstall()
	 */
    public void uninstall() {
        dispose();
    }

    /**
	 * @see AbstractInformationControlManager#showInformationControl(Rectangle)
	 */
    protected void showInformationControl(Rectangle subjectArea) {
        super.showInformationControl(subjectArea);
    }

    /**
	 * @see AbstractInformationControlManager#hideInformationControl()
	 */
    protected void hideInformationControl() {
        try {
            super.hideInformationControl();
        } finally {
            if (fTextViewer instanceof IWidgetTokenOwner) {
                IWidgetTokenOwner owner = (IWidgetTokenOwner) fTextViewer;
                owner.releaseWidgetToken(this);
            }
        }
    }

    /**
	 * @see AbstractInformationControlManager#handleInformationControlDisposed()
	 */
    protected void handleInformationControlDisposed() {
        try {
            super.handleInformationControlDisposed();
        } finally {
            if (fTextViewer instanceof IWidgetTokenOwner) {
                IWidgetTokenOwner owner = (IWidgetTokenOwner) fTextViewer;
                owner.releaseWidgetToken(this);
            }
        }
    }

    /**
	 * @see org.eclipse.jface.text.IWidgetTokenKeeper#requestWidgetToken(IWidgetTokenOwner)
	 */
    public boolean requestWidgetToken(IWidgetTokenOwner owner) {
        return false;
    }

    /**
	 * @see org.eclipse.jface.text.IWidgetTokenKeeperExtension#requestWidgetToken(org.eclipse.jface.text.IWidgetTokenOwner, int)
	 * @since 3.0
	 */
    public boolean requestWidgetToken(IWidgetTokenOwner owner, int priority) {
        return false;
    }

    /**
	 * @see org.eclipse.jface.text.IWidgetTokenKeeperExtension#setFocus(org.eclipse.jface.text.IWidgetTokenOwner)
	 * @since 3.0
	 */
    public boolean setFocus(IWidgetTokenOwner owner) {
        return false;
    }
}

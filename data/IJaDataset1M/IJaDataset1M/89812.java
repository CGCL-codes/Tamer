package org.eclipse.jface.internal.text.html;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.text.IDelayedInputChangeProvider;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.jface.text.IInputChangedListener;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

/**
 * Displays HTML information in a {@link org.eclipse.swt.browser.Browser} widget.
 * <p>
 * This {@link IInformationControlExtension2} expects {@link #setInput(Object)} to be
 * called with an argument of type {@link BrowserInformationControlInput}.
 * </p>
 * <p>
 * Moved into this package from <code>org.eclipse.jface.internal.text.revisions</code>.</p>
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.</p>
 * <p>
 * Current problems:
 * <ul>
 * 	<li>the size computation is too small</li>
 * 	<li>focusLost event is not sent - see https://bugs.eclipse.org/bugs/show_bug.cgi?id=84532</li>
 * </ul>
 * </p>
 * 
 * @since 3.2
 */
public class BrowserInformationControl extends AbstractInformationControl implements IInformationControlExtension2, IDelayedInputChangeProvider {

    /**
	 * Tells whether the SWT Browser widget and hence this information
	 * control is available.
	 *
	 * @param parent the parent component used for checking or <code>null</code> if none
	 * @return <code>true</code> if this control is available
	 */
    public static boolean isAvailable(Composite parent) {
        if (!fgAvailabilityChecked) {
            try {
                Browser browser = new Browser(parent, SWT.NONE);
                browser.dispose();
                fgIsAvailable = true;
                Slider sliderV = new Slider(parent, SWT.VERTICAL);
                Slider sliderH = new Slider(parent, SWT.HORIZONTAL);
                int width = sliderV.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
                int height = sliderH.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
                fgScrollBarSize = new Point(width, height);
                sliderV.dispose();
                sliderH.dispose();
            } catch (SWTError er) {
                fgIsAvailable = false;
            } finally {
                fgAvailabilityChecked = true;
            }
        }
        return fgIsAvailable;
    }

    /**
	 * Minimal size constraints.
	 * @since 3.2
	 */
    private static final int MIN_WIDTH = 80;

    private static final int MIN_HEIGHT = 50;

    /**
	 * Availability checking cache.
	 */
    private static boolean fgIsAvailable = false;

    private static boolean fgAvailabilityChecked = false;

    /**
	 * Cached scroll bar width and height
	 * @since 3.4
	 */
    private static Point fgScrollBarSize;

    /** The control's browser widget */
    private Browser fBrowser;

    /** Tells whether the browser has content */
    private boolean fBrowserHasContent;

    /** Text layout used to approximate size of content when rendered in browser */
    private TextLayout fTextLayout;

    /** Bold text style */
    private TextStyle fBoldStyle;

    private BrowserInformationControlInput fInput;

    /**
	 * <code>true</code> iff the browser has completed loading of the last
	 * input set via {@link #setInformation(String)}.
	 * @since 3.4
	 */
    private boolean fCompleted = false;

    /**
	 * The listener to be notified when a delayed location changing event happened.
	 * @since 3.4
	 */
    private IInputChangedListener fDelayedInputChangeListener;

    /**
	 * The listeners to be notified when the input changed.
	 * @since 3.4
	 */
    private ListenerList fInputChangeListeners = new ListenerList(ListenerList.IDENTITY);

    /**
	 * The symbolic name of the font used for size computations, or <code>null</code> to use dialog font.
	 * @since 3.4
	 */
    private final String fSymbolicFontName;

    /**
	 * Creates a browser information control with the given shell as parent.
	 * 
	 * @param parent the parent shell
	 * @param symbolicFontName the symbolic name of the font used for size computations
	 * @param resizable <code>true</code> if the control should be resizable
	 * @since 3.4
	 */
    public BrowserInformationControl(Shell parent, String symbolicFontName, boolean resizable) {
        super(parent, resizable);
        fSymbolicFontName = symbolicFontName;
        create();
    }

    /**
	 * Creates a browser information control with the given shell as parent.
	 * 
	 * @param parent the parent shell
	 * @param symbolicFontName the symbolic name of the font used for size computations
	 * @param statusFieldText the text to be used in the optional status field
	 *            or <code>null</code> if the status field should be hidden
	 * @since 3.4
	 */
    public BrowserInformationControl(Shell parent, String symbolicFontName, String statusFieldText) {
        super(parent, statusFieldText);
        fSymbolicFontName = symbolicFontName;
        create();
    }

    /**
	 * Creates a browser information control with the given shell as parent.
	 * 
	 * @param parent the parent shell
	 * @param symbolicFontName the symbolic name of the font used for size computations
	 * @param toolBarManager the manager or <code>null</code> if toolbar is not desired
	 * @since 3.4
	 */
    public BrowserInformationControl(Shell parent, String symbolicFontName, ToolBarManager toolBarManager) {
        super(parent, toolBarManager);
        fSymbolicFontName = symbolicFontName;
        create();
    }

    protected void createContent(Composite parent) {
        fBrowser = new Browser(parent, SWT.NONE);
        Display display = getShell().getDisplay();
        fBrowser.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        fBrowser.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        fBrowser.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (e.character == 0x1B) getShell().dispose();
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        fBrowser.addProgressListener(new ProgressAdapter() {

            public void completed(ProgressEvent event) {
                fCompleted = true;
            }
        });
        fBrowser.setMenu(new Menu(getShell(), SWT.NONE));
        createTextLayout();
    }

    /**
	 * {@inheritDoc}
	 * @deprecated use {@link #setInput(Object)}
	 */
    public void setInformation(final String content) {
        setInput(new BrowserInformationControlInput(null) {

            public String getHtml() {
                return content;
            }

            public String getInputName() {
                return "";
            }

            public Object getInputElement() {
                return content;
            }
        });
    }

    /**
	 * {@inheritDoc} This control can handle {@link String} and
	 * {@link BrowserInformationControlInput}.
	 */
    public void setInput(Object input) {
        Assert.isLegal(input == null || input instanceof String || input instanceof BrowserInformationControlInput);
        if (input instanceof String) {
            setInformation((String) input);
            return;
        }
        fInput = (BrowserInformationControlInput) input;
        String content = null;
        if (fInput != null) content = fInput.getHtml();
        fBrowserHasContent = content != null && content.length() > 0;
        if (!fBrowserHasContent) content = "<html><body ></html>";
        boolean RTL = (getShell().getStyle() & SWT.RIGHT_TO_LEFT) != 0;
        boolean resizable = isResizable();
        String[] styles = null;
        if (RTL && resizable) styles = new String[] { "direction:rtl;", "overflow:scroll;", "word-wrap:break-word;" }; else if (RTL && !resizable) styles = new String[] { "direction:rtl;", "overflow:hidden;", "word-wrap:break-word;" }; else if (!resizable) styles = new String[] { "overflow:hidden;" }; else styles = new String[] { "overflow:scroll;" };
        StringBuffer buffer = new StringBuffer(content);
        HTMLPrinter.insertStyles(buffer, styles);
        content = buffer.toString();
        fCompleted = false;
        fBrowser.setText(content);
        Object[] listeners = fInputChangeListeners.getListeners();
        for (int i = 0; i < listeners.length; i++) ((IInputChangedListener) listeners[i]).inputChanged(fInput);
    }

    public void setVisible(boolean visible) {
        Shell shell = getShell();
        if (shell.isVisible() == visible) return;
        if (!visible) {
            super.setVisible(false);
            setInput(null);
            return;
        }
        final Display display = shell.getDisplay();
        display.timerExec(100, new Runnable() {

            public void run() {
                fCompleted = true;
            }
        });
        while (!fCompleted) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        shell = getShell();
        if (shell == null || shell.isDisposed()) return;
        if ("win32".equals(SWT.getPlatform())) shell.moveAbove(null);
        super.setVisible(true);
    }

    public void setSize(int width, int height) {
        fBrowser.setRedraw(false);
        try {
            super.setSize(width, height);
        } finally {
            fBrowser.setRedraw(true);
        }
    }

    /**
	 * Creates and initializes the text layout used
	 * to compute the size hint.
	 * 
	 * @since 3.2
	 */
    private void createTextLayout() {
        fTextLayout = new TextLayout(fBrowser.getDisplay());
        Font font = fSymbolicFontName == null ? JFaceResources.getDialogFont() : JFaceResources.getFont(fSymbolicFontName);
        fTextLayout.setFont(font);
        fTextLayout.setWidth(-1);
        FontData[] fontData = font.getFontData();
        for (int i = 0; i < fontData.length; i++) fontData[i].setStyle(SWT.BOLD);
        font = new Font(getShell().getDisplay(), fontData);
        fBoldStyle = new TextStyle(font, null, null);
        fTextLayout.setText("    ");
        int tabWidth = fTextLayout.getBounds().width;
        fTextLayout.setTabs(new int[] { tabWidth });
        fTextLayout.setText("");
    }

    public void dispose() {
        if (fTextLayout != null) {
            fTextLayout.dispose();
            fTextLayout = null;
        }
        if (fBoldStyle != null) {
            fBoldStyle.font.dispose();
            fBoldStyle = null;
        }
        fBrowser = null;
        super.dispose();
    }

    public Point computeSizeHint() {
        Point sizeConstraints = getSizeConstraints();
        Rectangle trim = computeTrim();
        int height = trim.height;
        TextPresentation presentation = new TextPresentation();
        HTML2TextReader reader = new HTML2TextReader(new StringReader(fInput.getHtml()), presentation);
        String text;
        try {
            text = reader.getString();
        } catch (IOException e) {
            text = "";
        }
        fTextLayout.setText(text);
        fTextLayout.setWidth(sizeConstraints == null ? SWT.DEFAULT : sizeConstraints.x - trim.width);
        Iterator iter = presentation.getAllStyleRangeIterator();
        while (iter.hasNext()) {
            StyleRange sr = (StyleRange) iter.next();
            if (sr.fontStyle == SWT.BOLD) fTextLayout.setStyle(fBoldStyle, sr.start, sr.start + sr.length - 1);
        }
        Rectangle bounds = fTextLayout.getBounds();
        int lineCount = fTextLayout.getLineCount();
        int textWidth = 0;
        for (int i = 0; i < lineCount; i++) {
            Rectangle rect = fTextLayout.getLineBounds(i);
            int lineWidth = rect.x + rect.width;
            if (i == 0) lineWidth += fInput.getLeadingImageWidth();
            textWidth = Math.max(textWidth, lineWidth);
        }
        bounds.width = textWidth;
        fTextLayout.setText("");
        int minWidth = bounds.width;
        height = height + bounds.height;
        minWidth += 15;
        height += 15;
        if (sizeConstraints != null) {
            if (sizeConstraints.x != SWT.DEFAULT) minWidth = Math.min(sizeConstraints.x, minWidth + trim.width);
            if (sizeConstraints.y != SWT.DEFAULT) height = Math.min(sizeConstraints.y, height);
        }
        int width = Math.max(MIN_WIDTH, minWidth);
        height = Math.max(MIN_HEIGHT, height);
        return new Point(width, height);
    }

    public Rectangle computeTrim() {
        Rectangle trim = super.computeTrim();
        if (isResizable()) {
            boolean RTL = (getShell().getStyle() & SWT.RIGHT_TO_LEFT) != 0;
            if (RTL) {
                trim.x -= fgScrollBarSize.x;
            }
            trim.width += fgScrollBarSize.x;
            trim.height += fgScrollBarSize.y;
        }
        return trim;
    }

    /**
	 * Adds the listener to the collection of listeners who will be
	 * notified when the current location has changed or is about to change.
	 * 
	 * @param listener the location listener
	 * @since 3.4
	 */
    public void addLocationListener(LocationListener listener) {
        fBrowser.addLocationListener(listener);
    }

    public void setForegroundColor(Color foreground) {
        super.setForegroundColor(foreground);
        fBrowser.setForeground(foreground);
    }

    public void setBackgroundColor(Color background) {
        super.setBackgroundColor(background);
        fBrowser.setBackground(background);
    }

    public boolean hasContents() {
        return fBrowserHasContent;
    }

    /**
	 * Adds a listener for input changes to this input change provider.
	 * Has no effect if an identical listener is already registered.
	 * 
	 * @param inputChangeListener the listener to add
	 * @since 3.4
	 */
    public void addInputChangeListener(IInputChangedListener inputChangeListener) {
        Assert.isNotNull(inputChangeListener);
        fInputChangeListeners.add(inputChangeListener);
    }

    /**
	 * Removes the given input change listener from this input change provider.
	 * Has no effect if an identical listener is not registered.
	 * 
	 * @param inputChangeListener the listener to remove
	 * @since 3.4
	 */
    public void removeInputChangeListener(IInputChangedListener inputChangeListener) {
        fInputChangeListeners.remove(inputChangeListener);
    }

    public void setDelayedInputChangeListener(IInputChangedListener inputChangeListener) {
        fDelayedInputChangeListener = inputChangeListener;
    }

    /**
	 * Tells whether a delayed input change listener is registered.
	 * 
	 * @return <code>true</code> iff a delayed input change
	 *         listener is currently registered
	 * @since 3.4
	 */
    public boolean hasDelayedInputChangeListener() {
        return fDelayedInputChangeListener != null;
    }

    /**
	 * Notifies listeners of a delayed input change.
	 * 
	 * @param newInput the new input, or <code>null</code> to request cancellation
	 * @since 3.4
	 */
    public void notifyDelayedInputChange(Object newInput) {
        if (fDelayedInputChangeListener != null) fDelayedInputChangeListener.inputChanged(newInput);
    }

    public String toString() {
        String style = (getShell().getStyle() & SWT.RESIZE) == 0 ? "fixed" : "resizeable";
        return super.toString() + " -  style: " + style;
    }

    /**
	 * @return the current browser input or <code>null</code>
	 */
    public BrowserInformationControlInput getInput() {
        return fInput;
    }

    public Point computeSizeConstraints(int widthInChars, int heightInChars) {
        if (fSymbolicFontName == null) return null;
        GC gc = new GC(fBrowser);
        Font font = fSymbolicFontName == null ? JFaceResources.getDialogFont() : JFaceResources.getFont(fSymbolicFontName);
        gc.setFont(font);
        int width = gc.getFontMetrics().getAverageCharWidth();
        int height = gc.getFontMetrics().getHeight();
        gc.dispose();
        return new Point(widthInChars * width, heightInChars * height);
    }
}

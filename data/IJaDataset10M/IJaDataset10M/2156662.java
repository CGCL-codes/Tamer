package org.emftext.language.models.resource.model.ui;

public class ModelBrowserInformationControl extends org.eclipse.jface.text.AbstractInformationControl implements org.eclipse.jface.text.IInformationControlExtension2, org.eclipse.jface.text.IDelayedInputChangeProvider {

    public static boolean isAvailable(org.eclipse.swt.widgets.Composite parent) {
        if (!fgAvailabilityChecked) {
            try {
                org.eclipse.swt.browser.Browser browser = new org.eclipse.swt.browser.Browser(parent, org.eclipse.swt.SWT.NONE);
                browser.dispose();
                fgIsAvailable = true;
                org.eclipse.swt.widgets.Slider sliderV = new org.eclipse.swt.widgets.Slider(parent, org.eclipse.swt.SWT.VERTICAL);
                org.eclipse.swt.widgets.Slider sliderH = new org.eclipse.swt.widgets.Slider(parent, org.eclipse.swt.SWT.HORIZONTAL);
                int width = sliderV.computeSize(org.eclipse.swt.SWT.DEFAULT, org.eclipse.swt.SWT.DEFAULT).x;
                int height = sliderH.computeSize(org.eclipse.swt.SWT.DEFAULT, org.eclipse.swt.SWT.DEFAULT).y;
                fgScrollBarSize = new org.eclipse.swt.graphics.Point(width, height);
                sliderV.dispose();
                sliderH.dispose();
            } catch (org.eclipse.swt.SWTError er) {
                fgIsAvailable = false;
            } finally {
                fgAvailabilityChecked = true;
            }
        }
        return fgIsAvailable;
    }

    private static final int MIN_WIDTH = 80;

    private static final int MIN_HEIGHT = 50;

    private static boolean fgIsAvailable = false;

    private static boolean fgAvailabilityChecked = false;

    private static org.eclipse.swt.graphics.Point fgScrollBarSize;

    private org.eclipse.swt.browser.Browser fBrowser;

    private boolean fBrowserHasContent;

    private org.eclipse.swt.graphics.TextLayout fTextLayout;

    private org.eclipse.swt.graphics.TextStyle fBoldStyle;

    private org.emftext.language.models.resource.model.ui.ModelDocBrowserInformationControlInput fInput;

    private boolean fCompleted = false;

    private org.eclipse.jface.text.IInputChangedListener fDelayedInputChangeListener;

    private org.eclipse.core.runtime.ListenerList fInputChangeListeners = new org.eclipse.core.runtime.ListenerList(org.eclipse.core.runtime.ListenerList.IDENTITY);

    private final String fSymbolicFontName;

    public ModelBrowserInformationControl(org.eclipse.swt.widgets.Shell parent, String symbolicFontName, boolean resizable) {
        super(parent, resizable);
        fSymbolicFontName = symbolicFontName;
        create();
    }

    public ModelBrowserInformationControl(org.eclipse.swt.widgets.Shell parent, String symbolicFontName, String statusFieldText) {
        super(parent, statusFieldText);
        fSymbolicFontName = symbolicFontName;
        create();
    }

    public ModelBrowserInformationControl(org.eclipse.swt.widgets.Shell parent, String symbolicFontName, org.eclipse.jface.action.ToolBarManager toolBarManager) {
        super(parent, toolBarManager);
        fSymbolicFontName = symbolicFontName;
        create();
    }

    protected void createContent(org.eclipse.swt.widgets.Composite parent) {
        fBrowser = new org.eclipse.swt.browser.Browser(parent, org.eclipse.swt.SWT.NONE);
        org.eclipse.swt.widgets.Display display = getShell().getDisplay();
        fBrowser.setForeground(display.getSystemColor(org.eclipse.swt.SWT.COLOR_INFO_FOREGROUND));
        fBrowser.setBackground(display.getSystemColor(org.eclipse.swt.SWT.COLOR_INFO_BACKGROUND));
        fBrowser.addKeyListener(new org.eclipse.swt.events.KeyListener() {

            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.character == 0x1B) dispose();
            }

            public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
            }
        });
        fBrowser.addProgressListener(new org.eclipse.swt.browser.ProgressAdapter() {

            public void completed(org.eclipse.swt.browser.ProgressEvent event) {
                fCompleted = true;
            }
        });
        fBrowser.addOpenWindowListener(new org.eclipse.swt.browser.OpenWindowListener() {

            public void open(org.eclipse.swt.browser.WindowEvent event) {
                event.required = true;
            }
        });
        fBrowser.setMenu(new org.eclipse.swt.widgets.Menu(getShell(), org.eclipse.swt.SWT.NONE));
        createTextLayout();
    }

    public void setInput(Object input) {
        org.eclipse.core.runtime.Assert.isLegal(input == null || input instanceof String || input instanceof org.emftext.language.models.resource.model.ui.ModelDocBrowserInformationControlInput);
        if (input instanceof String) {
            setInformation((String) input);
            return;
        }
        fInput = (org.emftext.language.models.resource.model.ui.ModelDocBrowserInformationControlInput) input;
        String content = null;
        if (fInput != null) content = fInput.getHtml();
        fBrowserHasContent = content != null && content.length() > 0;
        if (!fBrowserHasContent) content = "<html><body ></html>";
        boolean RTL = (getShell().getStyle() & org.eclipse.swt.SWT.RIGHT_TO_LEFT) != 0;
        boolean resizable = isResizable();
        String[] styles = null;
        if (RTL && resizable) {
            styles = new String[] { "direction:rtl;", "overflow:scroll;", "word-wrap:break-word;" };
        } else if (RTL && !resizable) {
            styles = new String[] { "direction:rtl;", "overflow:hidden;", "word-wrap:break-word;" };
        } else if (!resizable) {
            styles = new String[] { "overflow:hidden;" };
        } else {
            styles = new String[] { "overflow:scroll;" };
        }
        StringBuffer buffer = new StringBuffer(content);
        org.emftext.language.models.resource.model.ui.ModelHTMLPrinter.insertStyles(buffer, styles);
        content = buffer.toString();
        fCompleted = false;
        fBrowser.setText(content);
        Object[] listeners = fInputChangeListeners.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            ((org.eclipse.jface.text.IInputChangedListener) listeners[i]).inputChanged(fInput);
        }
    }

    public void setVisible(boolean visible) {
        org.eclipse.swt.widgets.Shell shell = getShell();
        if (shell.isVisible() == visible) {
            return;
        }
        if (!visible) {
            super.setVisible(false);
            setInput(null);
            return;
        }
        final org.eclipse.swt.widgets.Display display = shell.getDisplay();
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
        if (shell == null || shell.isDisposed()) {
            return;
        }
        if ("win32".equals(org.eclipse.swt.SWT.getPlatform())) {
            shell.moveAbove(null);
        }
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

    private void createTextLayout() {
        fTextLayout = new org.eclipse.swt.graphics.TextLayout(fBrowser.getDisplay());
        String symbolicFontName = fSymbolicFontName == null ? org.eclipse.jface.resource.JFaceResources.DIALOG_FONT : fSymbolicFontName;
        org.eclipse.swt.graphics.Font font = org.eclipse.jface.resource.JFaceResources.getFont(symbolicFontName);
        fTextLayout.setFont(font);
        fTextLayout.setWidth(-1);
        font = org.eclipse.jface.resource.JFaceResources.getFontRegistry().getBold(symbolicFontName);
        fBoldStyle = new org.eclipse.swt.graphics.TextStyle(font, null, null);
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
        fBrowser = null;
        super.dispose();
    }

    public org.eclipse.swt.graphics.Point computeSizeHint() {
        org.eclipse.swt.graphics.Point sizeConstraints = getSizeConstraints();
        org.eclipse.swt.graphics.Rectangle trim = computeTrim();
        int height = trim.height;
        org.eclipse.jface.text.TextPresentation presentation = new org.eclipse.jface.text.TextPresentation();
        String text;
        try {
            text = org.emftext.language.models.resource.model.ui.ModelHTMLPrinter.html2text(new java.io.StringReader(fInput.getHtml()), presentation);
        } catch (java.io.IOException e) {
            text = "";
        }
        fTextLayout.setText(text);
        fTextLayout.setWidth(sizeConstraints == null ? org.eclipse.swt.SWT.DEFAULT : sizeConstraints.x - trim.width);
        java.util.Iterator<?> iter = presentation.getAllStyleRangeIterator();
        while (iter.hasNext()) {
            org.eclipse.swt.custom.StyleRange sr = (org.eclipse.swt.custom.StyleRange) iter.next();
            if (sr.fontStyle == org.eclipse.swt.SWT.BOLD) {
                fTextLayout.setStyle(fBoldStyle, sr.start, sr.start + sr.length - 1);
            }
        }
        org.eclipse.swt.graphics.Rectangle bounds = fTextLayout.getBounds();
        int lineCount = fTextLayout.getLineCount();
        int textWidth = 0;
        for (int i = 0; i < lineCount; i++) {
            org.eclipse.swt.graphics.Rectangle rect = fTextLayout.getLineBounds(i);
            int lineWidth = rect.x + rect.width;
            if (i == 0) {
                lineWidth += fInput.getLeadingImageWidth();
            }
            textWidth = Math.max(textWidth, lineWidth);
        }
        bounds.width = textWidth;
        fTextLayout.setText("");
        int minWidth = bounds.width;
        height = height + bounds.height;
        minWidth += 15;
        height += 15;
        if (sizeConstraints != null) {
            if (sizeConstraints.x != org.eclipse.swt.SWT.DEFAULT) {
                minWidth = Math.min(sizeConstraints.x, minWidth + trim.width);
            }
            if (sizeConstraints.y != org.eclipse.swt.SWT.DEFAULT) {
                height = Math.min(sizeConstraints.y, height);
            }
        }
        int width = Math.max(MIN_WIDTH, minWidth);
        height = Math.max(MIN_HEIGHT, height);
        org.eclipse.swt.graphics.Point windowSize = new org.eclipse.swt.graphics.Point(width, height);
        return windowSize;
    }

    public org.eclipse.swt.graphics.Rectangle computeTrim() {
        org.eclipse.swt.graphics.Rectangle trim = super.computeTrim();
        if (isResizable()) {
            boolean RTL = (getShell().getStyle() & org.eclipse.swt.SWT.RIGHT_TO_LEFT) != 0;
            if (RTL) {
                trim.x -= fgScrollBarSize.x;
            }
            trim.width += fgScrollBarSize.x;
            trim.height += fgScrollBarSize.y;
        }
        return trim;
    }

    public void addLocationListener(org.eclipse.swt.browser.LocationListener listener) {
        fBrowser.addLocationListener(listener);
    }

    public void setForegroundColor(org.eclipse.swt.graphics.Color foreground) {
        super.setForegroundColor(foreground);
        fBrowser.setForeground(foreground);
    }

    public void setBackgroundColor(org.eclipse.swt.graphics.Color background) {
        super.setBackgroundColor(background);
        fBrowser.setBackground(background);
    }

    public boolean hasContents() {
        return fBrowserHasContent;
    }

    public void addInputChangeListener(org.eclipse.jface.text.IInputChangedListener inputChangeListener) {
        org.eclipse.core.runtime.Assert.isNotNull(inputChangeListener);
        fInputChangeListeners.add(inputChangeListener);
    }

    public void removeInputChangeListener(org.eclipse.jface.text.IInputChangedListener inputChangeListener) {
        fInputChangeListeners.remove(inputChangeListener);
    }

    public void setDelayedInputChangeListener(org.eclipse.jface.text.IInputChangedListener inputChangeListener) {
        fDelayedInputChangeListener = inputChangeListener;
    }

    public boolean hasDelayedInputChangeListener() {
        return fDelayedInputChangeListener != null;
    }

    public void notifyDelayedInputChange(Object newInput) {
        if (fDelayedInputChangeListener != null) fDelayedInputChangeListener.inputChanged(newInput);
    }

    public String toString() {
        String style = (getShell().getStyle() & org.eclipse.swt.SWT.RESIZE) == 0 ? "fixed" : "resizeable";
        return super.toString() + " -  style: " + style;
    }

    public org.emftext.language.models.resource.model.ui.ModelDocBrowserInformationControlInput getInput() {
        return fInput;
    }

    public org.eclipse.swt.graphics.Point computeSizeConstraints(int widthInChars, int heightInChars) {
        if (fSymbolicFontName == null) {
            return null;
        }
        org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC(fBrowser);
        org.eclipse.swt.graphics.Font font = fSymbolicFontName == null ? org.eclipse.jface.resource.JFaceResources.getDialogFont() : org.eclipse.jface.resource.JFaceResources.getFont(fSymbolicFontName);
        gc.setFont(font);
        int width = gc.getFontMetrics().getAverageCharWidth();
        int height = gc.getFontMetrics().getHeight();
        gc.dispose();
        return new org.eclipse.swt.graphics.Point(widthInChars * width, heightInChars * height);
    }
}

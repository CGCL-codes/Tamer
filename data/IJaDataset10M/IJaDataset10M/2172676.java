package org.designerator.common.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author alaska EXPERIMENTAL in DEVELOPMENT
 *
 */
public class SingleSlider extends Canvas implements ISlider {

    private static final String DESCRIPTION = "Gradient Softness ";

    private Composite parent;

    private String text;

    private Color backGroundColor;

    private ISliderKnob currentKnob;

    private int minimum = 0;

    private int maximum = 100;

    private int increment = 1;

    private int selection = 0;

    private int slidery = 0;

    private int sliderx = 0;

    private boolean contains = false;

    private int scaley = 0;

    private int scalex = 0;

    private Image scaleImage;

    private int slidermin;

    private int slidermax;

    private Image hoverimage;

    private int topMargin = 12;

    private int bottomMargin = 3;

    private int sideMargin = 3;

    private int scalewidth;

    private List<SelectionListener> listenerList = new ArrayList<SelectionListener>();

    private int range = 50;

    private String discription = "";

    private int size = 50;

    private boolean invert;

    Shell tip = null;

    Label tipLabel = null;

    private Image knob;

    public SingleSlider(final Composite parent2, int style) {
        super(parent2, style);
        this.parent = parent2;
        createListeners();
    }

    public void initialize() {
        Color outline = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        Color fill = parent.getDisplay().getSystemColor(SWT.COLOR_GRAY);
        Color black = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        Color background = parent.getBackground();
        Image knob = CustomImages.getCustomShape(parent.getDisplay(), CustomImages.HOUSE, 10, 10, background, fill, outline, black);
        Color[] gradient = new Color[2];
        gradient[1] = parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
        gradient[0] = parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
        Image scaleImg = CustomImages.getSimpleScale(parent.getDisplay(), size + 1, 3, null, gradient, true, outline);
        addSliderKnob(new CustomSliderKnob(this, knob, null, null, 0, 0));
        setScaleImage(scaleImg);
        Rectangle sliderRect = currentKnob.getBounds();
        Rectangle scale = scaleImage.getBounds();
        int width = scale.width + sliderRect.width + (sideMargin * 2);
        int height = sliderRect.height + topMargin + bottomMargin;
        Point point = new Point(width, height);
        setSize(point);
        Rectangle clientArea = getClientArea();
        int adjust = sliderRect.width / 2;
        scalex = adjust + (sideMargin);
        scaley = 2 + topMargin;
        scalewidth = scale.width;
        sliderx = slidermin = scalex - adjust;
        slidermax = scalex + scale.width - adjust - 1;
        slidery = scale.height + 1;
        float f = (scalewidth - 1) / 100f;
        float p = currentKnob.getPosition();
        currentKnob.setSliderx((int) (sliderx + (f * p)));
        currentKnob.setSlidery(slidery - 3);
        setToolTipText("");
    }

    private void createListeners() {
        addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                SingleSlider.this.widgetDisposed(e);
                currentKnob.dispose();
            }
        });
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                SingleSlider.this.paintControl(e);
            }
        });
        Listener mouseListener = new Listener() {

            int startX, startY;

            private Point point;

            public void handleEvent(Event e) {
                if (e.type == SWT.MouseDown && e.button == 1) {
                    if (isMouseDown(e)) {
                        setToolTipText(null);
                        contains = true;
                        if (currentKnob != null) {
                            startX = currentKnob.getSliderx() - e.x;
                            selectionChanged(SWT.MouseDown);
                            createToolTip();
                            redraw();
                        }
                    } else {
                        if (currentKnob != null) {
                            currentKnob.setSelected(false);
                        }
                        contains = false;
                    }
                } else if (e.type == SWT.MouseMove && (e.stateMask & SWT.BUTTON1) != 0) {
                    if (contains) {
                        if (currentKnob != null) {
                            int current = e.x + startX;
                            if (current > slidermax + 1 || current < slidermin - 1) {
                                return;
                            }
                            currentKnob.setSliderx(current);
                            setSelection();
                            selectionChanged(SWT.MouseMove);
                            tipLabel.setText(Integer.toString(getSelection() * increment) + "%  ");
                            redraw();
                        }
                    }
                } else if (e.type == SWT.MouseMove) {
                    if (currentKnob != null) {
                        currentKnob.checkMouseover(e);
                    }
                } else if (e.type == SWT.MouseUp) {
                    contains = false;
                    if (currentKnob != null) {
                        currentKnob.setSliding(false);
                        currentKnob.setSelected(false);
                        setSelection();
                        selectionChanged(SWT.MouseUp);
                        if (tip != null) {
                            tip.dispose();
                            tip = null;
                            tipLabel = null;
                        }
                        redraw();
                    }
                } else if (e.type == SWT.MouseExit) {
                    setToolTipText(Integer.toString(getSelection() * increment) + "%");
                }
            }
        };
        addListener(SWT.MouseDown, mouseListener);
        addListener(SWT.MouseMove, mouseListener);
        addListener(SWT.MouseUp, mouseListener);
        addListener(SWT.MouseExit, mouseListener);
    }

    private void createToolTip() {
        setToolTipText("");
        if (tip != null && !tip.isDisposed()) {
            tip.dispose();
        }
        tip = new Shell(parent.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
        tip.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        FillLayout layout = new FillLayout();
        layout.marginWidth = 2;
        tip.setLayout(layout);
        tipLabel = new Label(tip, SWT.NONE);
        tipLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        tipLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        tipLabel.setText(Integer.toString(getSelection() * increment) + "%  ");
        Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point pt = toDisplay(-2, -20);
        tip.setBounds(pt.x, pt.y, size.x, size.y);
        tip.setVisible(true);
    }

    /**
	 * @return the hoverimage
	 */
    public Image getHoverimage() {
        return hoverimage;
    }

    public int getIncrement() {
        return increment;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getRange() {
        return range;
    }

    /**
	 * @return the scaleImage
	 */
    public Image getScaleImage() {
        return scaleImage;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection() {
        if (currentKnob != null) {
            int r = currentKnob.getSliderx() - sideMargin;
            r = r < 0 ? 0 : r;
            r = (int) ((float) range / (float) size * (float) r + 0.5f);
            r = r > range ? range : r;
            if (increment != 1) {
                r = (int) ((float) r / ((float) increment));
            }
            if (invert) {
                selection = range - r;
            } else {
                selection = r;
            }
        }
    }

    /**
	 * @return the sideMargin
	 */
    public int getSideMargin() {
        return sideMargin;
    }

    public Image getSliderImage() {
        return null;
    }

    public String getText() {
        return text;
    }

    /**
	 * @return the topMargin
	 */
    public int getTopMargin() {
        return topMargin;
    }

    public boolean isInvert() {
        return invert;
    }

    private boolean isMouseDown(Event event) {
        contains = false;
        if (currentKnob.contains(event)) {
            currentKnob.setSelected(true);
            contains = true;
        } else {
            currentKnob.setSelected(false);
        }
        return contains;
    }

    void paintControl(PaintEvent e) {
        GC gc = e.gc;
        Rectangle clientArea = getClientArea();
        Image buffer = new Image(getDisplay(), clientArea);
        GC bufferGc = new GC(buffer);
        bufferGc.setBackground(getBackground());
        bufferGc.fillRectangle(clientArea);
        if (scaleImage != null) {
            bufferGc.drawImage(scaleImage, scalex, scaley);
        }
        currentKnob.paint(bufferGc);
        gc.drawImage(buffer, 0, 0);
        buffer.dispose();
        bufferGc.dispose();
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
        int width = 0, height = 0;
        if (scaleImage != null) {
            Rectangle sliderRect = currentKnob.getBounds();
            Rectangle scale = scaleImage.getBounds();
            width = scale.width + sliderRect.width + (sideMargin * 2);
            height = scale.height + sliderRect.height + topMargin + bottomMargin;
        }
        if (wHint != SWT.DEFAULT) width = wHint;
        if (hHint != SWT.DEFAULT) height = hHint;
        Point point = new Point(width, height);
        return point;
    }

    public void removeSelectionListener(SelectionListener listener) {
    }

    public void addSelectionListener(SelectionListener listener) {
        listenerList.add(listener);
    }

    public void addSliderKnob(ISliderKnob knob) {
        currentKnob = (knob);
    }

    public Point getSize() {
        int width = 0, height = 0;
        if (scaleImage != null) {
            Rectangle sliderRect = currentKnob.getBounds();
            Rectangle scale = scaleImage.getBounds();
            width = scale.width + sliderRect.width + (sideMargin * 2);
            height = scale.height + sliderRect.height + topMargin + bottomMargin;
        }
        Point point = new Point(width, height);
        return point;
    }

    private void selectionChanged(int mouseState) {
        for (Iterator<SelectionListener> iterator = listenerList.iterator(); iterator.hasNext(); ) {
            SelectionListener l = iterator.next();
            Event event = new Event();
            event.widget = this;
            event.detail = mouseState;
            l.widgetSelected(new SelectionEvent(event));
        }
    }

    public void setBottomMargin(int i) {
        bottomMargin = i;
    }

    public void setDiscriptionText(String text) {
        discription = text;
    }

    /**
	 * @param hoverimage the hoverimage to set
	 */
    public void setHoverImage(Image hoverimage) {
        this.hoverimage = hoverimage;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public void setRange(int range) {
        this.range = range;
    }

    /**
	 * @param scaleImage the scaleImage to set
	 */
    public void setScaleImage(Image scaleImage) {
        this.scaleImage = scaleImage;
    }

    public void setSelection(int selection) {
        selection = selection > range ? range : selection;
        selection = selection < 0 ? 0 : selection;
        int sel = (int) ((float) selection / (float) range * (float) size + 0.5f);
        currentKnob.setSliderx(sel + sideMargin);
        if (invert) {
            sel = size - sel;
        }
        redraw();
        this.selection = sel;
    }

    /**
	 * @param sideMargin the sideMargin to set
	 */
    public void setSideMargin(int sideMargin) {
        this.sideMargin = sideMargin;
    }

    public void setSliderImage(Image image) {
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setToolTipText(String text) {
        if (text == null) {
            super.setToolTipText(null);
        } else {
            super.setToolTipText(DESCRIPTION + text);
        }
    }

    /**
	 * @param topMargin the topMargin to set
	 */
    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    public void widgetDisposed(DisposeEvent e) {
        dispose();
    }

    public void dispose() {
        if (backGroundColor != null) {
            backGroundColor.dispose();
        }
        if (scaleImage != null && !scaleImage.isDisposed()) {
            scaleImage.dispose();
        }
        super.dispose();
    }

    @Override
    public int computeKnobSelection(ISliderKnob knob) {
        return 0;
    }

    public void redraw() {
        super.redraw();
    }

    public void updateKnobPosition(ISliderKnob knob2, int p) {
    }
}

package com.codethesis.xcetus.gui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 * @author Deyan Rizov
 *
 */
public class Scale {

    /**
	 * 
	 */
    private Display display;

    /**
	 * 
	 */
    private Composite container;

    /**
	 * 
	 */
    private Canvas canvas;

    /**
	 * 
	 */
    private int max;

    /**
	 * 
	 */
    private int min;

    /**
	 * 
	 */
    private int value;

    /**
	 * 
	 */
    private int step;

    /**
	 * 
	 */
    private boolean enabled;

    /**
	 * 
	 */
    private Color borderColor;

    /**
	 * 
	 */
    private Color backgroundColor;

    /**
	 * 
	 */
    private Pattern backgroundPattern;

    /**
	 * 
	 */
    private Color foreground;

    /**
	 * 
	 */
    private Pattern foregroundPattern;

    /**
	 * 
	 */
    private boolean mouseDown;

    /**
	 * 
	 */
    private Runnable modifyHook;

    /**
	 * 
	 * @param parent
	 */
    public Scale(Composite parent) {
        this(parent, SWT.NONE);
    }

    public Scale(Composite parent, int style) {
        container = new Composite(parent, style);
        display = container.getDisplay();
        max = 1000;
        min = 0;
        value = 500;
        step = 10;
        createPartControl();
    }

    public Composite getContainer() {
        return container;
    }

    public void redraw() {
        canvas.redraw();
    }

    /**
	 * @return the enabled
	 */
    public boolean isEnabled() {
        return enabled;
    }

    /**
	 * @param enabled the enabled to set
	 */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
	 * @return the max
	 */
    public int getMax() {
        return max;
    }

    /**
	 * @param max the max to set
	 */
    public void setMax(int max) {
        this.max = max;
    }

    /**
	 * @return the min
	 */
    public int getMin() {
        return min;
    }

    /**
	 * @param min the min to set
	 */
    public void setMin(int min) {
        this.min = min;
    }

    /**
	 * @return the value
	 */
    public int getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(int value) {
        this.value = value;
        redraw();
    }

    /**
	 * @return the step
	 */
    public int getStep() {
        return step;
    }

    /**
	 * @param step the step to set
	 */
    public void setStep(int step) {
        this.step = step;
    }

    /**
	 * @return the modifyHook
	 */
    public Runnable getModifyHook() {
        return modifyHook;
    }

    public boolean isFocusControl() {
        return canvas.isFocusControl();
    }

    public void setFocus() {
        canvas.setFocus();
    }

    /**
	 * @param modifyHook the modifyHook to set
	 */
    public void setModifyHook(Runnable modifyHook) {
        this.modifyHook = modifyHook;
    }

    private void createPartControl() {
        container.setLayout(new FillLayout());
        canvas = new Canvas(container, SWT.NO_BACKGROUND);
        createGraphicsResources();
        canvas.addListener(SWT.Resize, new Listener() {

            @Override
            public void handleEvent(Event event) {
                updatePatterns();
            }
        });
        canvas.addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent e) {
                draw();
            }
        });
        canvas.addListener(SWT.MouseDown, new Listener() {

            @Override
            public void handleEvent(Event event) {
                mouseDown = true;
                if (!enabled) {
                    return;
                }
                value = (int) (((double) event.x / (double) canvas.getSize().x) * (max - min));
                if (event.x == canvas.getSize().x) {
                    value = max;
                }
                redraw();
                if (modifyHook != null) {
                    modifyHook.run();
                }
            }
        });
        canvas.addListener(SWT.MouseUp, new Listener() {

            @Override
            public void handleEvent(Event event) {
                mouseDown = false;
            }
        });
        canvas.addListener(SWT.MouseExit, new Listener() {

            @Override
            public void handleEvent(Event event) {
                mouseDown = false;
            }
        });
        canvas.addListener(SWT.MouseMove, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (!enabled) {
                    return;
                }
                if (mouseDown) {
                    value = (int) (((double) event.x / (double) canvas.getSize().x) * (max - min));
                    if (event.x == canvas.getSize().x) {
                        value = max;
                    }
                    if (value < min) {
                        value = min;
                    }
                    redraw();
                    if (modifyHook != null) {
                        modifyHook.run();
                    }
                }
            }
        });
        canvas.addListener(SWT.KeyDown, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (!enabled) {
                    return;
                }
                switch(event.keyCode) {
                    case 16777220:
                        value += step;
                        if (value > max) {
                            value = max;
                        }
                        redraw();
                        if (modifyHook != null) {
                            modifyHook.run();
                        }
                        break;
                    case 16777219:
                        value -= step;
                        if (value < min) {
                            value = min;
                        }
                        redraw();
                        if (modifyHook != null) {
                            modifyHook.run();
                        }
                        break;
                }
            }
        });
    }

    private void draw() {
        Point size = canvas.getSize();
        Image buffer = new Image(container.getDisplay(), size.x, size.y);
        GC gc = new GC(buffer);
        gc.setAntialias(SWT.ON);
        int selectedToX = (int) (size.x * ((float) value / (max - min)));
        gc.setBackground(backgroundColor);
        gc.fillRoundRectangle(0, 0, size.x, size.y, 3, 3);
        gc.setBackgroundPattern(foregroundPattern);
        gc.fillRoundRectangle(0, 0, selectedToX, size.y, 3, 3);
        gc.setBackground(borderColor);
        gc.setLineWidth(1);
        gc.drawRoundRectangle(0, 0, size.x - 1, size.y - 1, 3, 3);
        gc.dispose();
        gc = new GC(canvas);
        gc.drawImage(buffer, 0, 0);
        gc.dispose();
        buffer.dispose();
    }

    private void createGraphicsResources() {
        borderColor = new Color(display, 0, 0, 0);
        backgroundColor = new Color(display, 255, 255, 255);
        foregroundPattern = new Pattern(display, 0, 0, 0, canvas.getSize().y, new Color(display, 150, 150, 255), new Color(display, 50, 50, 255));
    }

    private void updatePatterns() {
        if (foregroundPattern != null) {
            foregroundPattern.dispose();
            foregroundPattern = new Pattern(display, 0, 0, 0, canvas.getSize().y, new Color(display, 150, 150, 255), new Color(display, 50, 50, 255));
        }
    }
}

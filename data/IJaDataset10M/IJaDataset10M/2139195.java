package com.codename1.ui;

import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.EventDispatcher;

/**
 * The slider component serves both as a slider widget to allow users to select
 * a value on a scale via touch/arrows and also to indicate progress. The slider
 * defaults to percentage display but can represent any positive set of values.
 *
 * @author Shai Almog
 */
public class Slider extends Label {

    private int value;

    private int maxValue = 100;

    private int minValue = 0;

    private boolean vertical;

    private boolean editable;

    private EventDispatcher listeners = new EventDispatcher();

    private int increments = 4;

    private int previousX = -1, previousY = -1;

    private Style sliderFull;

    private Style sliderFullSelected;

    private boolean paintingFull;

    private boolean renderPercentageOnTop;

    private boolean renderValueOnTop;

    private boolean infinite = false;

    private float infiniteDirection = 0.03f;

    private Image thumbImage;

    private String fullUIID = "Slider";

    /**
     * The default constructor uses internal rendering to draw its state
     */
    public Slider() {
        this("Slider", "Slider");
    }

    private Slider(String uiid, String fullUIID) {
        setFocusable(false);
        setUIID(uiid);
        this.fullUIID = fullUIID;
        setAlignment(CENTER);
    }

    /**
     * @inheritDoc
     */
    public void setUIID(String id) {
        super.setUIID(id);
        initStyles(id);
    }

    private void initStyles(String id) {
        sliderFull = getUIManager().getComponentStyle(id + "Full");
        sliderFullSelected = getUIManager().getComponentSelectedStyle(id + "Full");
        initCustomStyle(sliderFull);
        initCustomStyle(sliderFullSelected);
    }

    /**
     * @inheritDoc
     */
    public void initComponent() {
        if (infinite) {
            getComponentForm().registerAnimatedInternal(this);
            if (thumbImage == null) {
                thumbImage = UIManager.getInstance().getThemeImageConstant("sliderThumbImage");
            }
        }
    }

    /**
     * @inheritDoc
     */
    public void deinitialize() {
        if (infinite) {
            Form f = getComponentForm();
            if (f != null) {
                f.deregisterAnimatedInternal(this);
            }
        }
    }

    /**
     * @inheritDoc
     */
    public boolean animate() {
        if (infinite) {
            super.animate();
            float f = (infiniteDirection * ((float) maxValue));
            if (((int) f) == 0) {
                if (f < 0) {
                    f = -1;
                } else {
                    f = 1;
                }
            }
            value += ((int) f);
            if (value >= maxValue) {
                value = maxValue;
                infiniteDirection *= (-1);
            }
            if (value <= 0) {
                value = (byte) 0;
                infiniteDirection *= (-1);
            }
            return true;
        }
        return super.animate();
    }

    /**
     * The infinite slider functionality is used to animate
     * progress for which there is no defined value.
     *
     * @return true for infinite progress
     */
    public boolean isInfinite() {
        return infinite;
    }

    /**
     * Activates/disables the infinite slider functionality used to animate 
     * progress for which there is no defined value.
     * 
     * @param i true for infinite progress
     */
    public void setInfinite(boolean i) {
        if (infinite != i) {
            infinite = i;
            if (isInitialized()) {
                if (i) {
                    getComponentForm().registerAnimatedInternal(this);
                } else {
                    getComponentForm().deregisterAnimatedInternal(this);
                }
            }
        }
    }

    /**
     * Creates an infinite progress slider
     *
     * @return a slider instance that has no end value
     */
    public static Slider createInfinite() {
        Slider s = new Slider();
        s.infinite = true;
        return s;
    }

    /**
     * @inheritDoc
     */
    public void refreshTheme(boolean merge) {
        super.refreshTheme(merge);
        if (sliderFull != null) {
            deinitializeCustomStyle(sliderFull);
            deinitializeCustomStyle(sliderFullSelected);
            initStyles("SliderFull");
        }
    }

    /**
     * Indicates the value of progress made
     *
     * @return the progress on the slider
     */
    public int getProgress() {
        return value;
    }

    /**
     * Indicates the value of progress made, this method is thread safe and
     * can be invoked from any thread although discression should still be kept
     * so one thread doesn't regress progress made by another thread...
     *
     * @param value new value for progress
     */
    public void setProgress(int value) {
        if (this.value != value) {
            fireDataChanged(DataChangedListener.CHANGED, value);
        }
        setProgressInternal(value);
    }

    private void setProgressInternal(int value) {
        this.value = value;
        if (renderValueOnTop) {
            super.setText("" + value);
        } else {
            if (renderPercentageOnTop) {
                super.setText(value + "%");
            } else {
                if (isInitialized()) {
                    repaint();
                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    public Style getStyle() {
        if (paintingFull) {
            if (sliderFull == null) {
                initStyles(fullUIID);
            }
            if (hasFocus()) {
                return sliderFullSelected;
            }
            return sliderFull;
        }
        return super.getStyle();
    }

    /**
     * Return the size we would generally like for the component
     */
    protected Dimension calcPreferredSize() {
        Style style = getStyle();
        int prefW = 0, prefH = 0;
        if (style.getBorder() != null) {
            prefW = Math.max(style.getBorder().getMinimumWidth(), prefW);
            prefH = Math.max(style.getBorder().getMinimumHeight(), prefH);
        }
        if (Display.getInstance().isTouchScreenDevice() && isEditable()) {
            if (vertical) {
                return new Dimension(Math.max(prefW, Font.getDefaultFont().charWidth('X') * 2), Math.max(prefH, Display.getInstance().getDisplayHeight() / 2));
            } else {
                return new Dimension(Math.max(prefW, Display.getInstance().getDisplayWidth() / 2), Math.max(prefH, Font.getDefaultFont().getHeight() * 2));
            }
        } else {
            if (vertical) {
                return new Dimension(Math.max(prefW, Font.getDefaultFont().charWidth('X')), Math.max(prefH, Display.getInstance().getDisplayHeight() / 2));
            } else {
                return new Dimension(Math.max(prefW, Display.getInstance().getDisplayWidth() / 2), Math.max(prefH, Font.getDefaultFont().getHeight()));
            }
        }
    }

    /**
     * Paint the progress indicator
     */
    public void paintBackground(Graphics g) {
        super.paintBackground(g);
        int clipX = g.getClipX();
        int clipY = g.getClipY();
        int clipW = g.getClipWidth();
        int clipH = g.getClipHeight();
        int width = getWidth();
        int height = getHeight();
        int y = getY();
        if (infinite) {
            int blockSize = getWidth() / 5;
            int x = getX() + (int) ((((float) value) / ((float) maxValue - minValue)) * (getWidth() - blockSize));
            g.clipRect(x, y, blockSize, height - 1);
        } else {
            if (vertical) {
                int actualHeight = (int) ((((float) value) / ((float) maxValue - minValue)) * getHeight());
                y += height - actualHeight;
            } else {
                width = (int) ((((float) value) / ((float) maxValue - minValue)) * getWidth());
            }
            g.clipRect(getX(), y, width, height);
        }
        paintingFull = true;
        super.paintBackground(g);
        paintingFull = false;
        g.setClip(clipX, clipY, clipW, clipH);
        if (thumbImage != null && !infinite) {
            if (!vertical) {
                int xPos = getX() + width - thumbImage.getWidth() / 2;
                xPos = Math.max(getX(), xPos);
                xPos = Math.min(getX() + getWidth() - thumbImage.getWidth(), xPos);
                g.drawImage(thumbImage, xPos, y + height / 2 - thumbImage.getHeight() / 2);
            } else {
                int yPos = y + height - thumbImage.getHeight() / 2;
                yPos = Math.max(getY(), yPos);
                yPos = Math.min(getY() + getHeight() - thumbImage.getHeight(), yPos);
                g.drawImage(thumbImage, getX() + width / 2 - thumbImage.getWidth() / 2, yPos);
            }
        }
    }

    /**
     * Indicates the slider is vertical
     * @return true if the slider is vertical
     */
    public boolean isVertical() {
        return vertical;
    }

    /**
     * Indicates the slider is vertical
     * @param vertical true if the slider is vertical
     */
    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    /**
     * Indicates the slider is modifyable
     * @return true if the slider is editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Indicates the slider is modifyable
     * @param editable  true if the slider is editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        setFocusable(editable);
    }

    public void pointerPressed(int x, int y) {
        if (!editable) {
            return;
        }
        if (vertical) {
            y = Math.abs(getHeight() - (y - getAbsoluteY()));
            setProgressInternal((byte) (Math.min(100, ((float) y) / ((float) getHeight()) * 100)));
        } else {
            x = Math.abs(x - getAbsoluteX());
            setProgressInternal((byte) (Math.min(100, ((float) x) / ((float) getWidth()) * 100)));
        }
        if (vertical) {
            if (previousY < y) {
                fireDataChanged(DataChangedListener.ADDED, value);
            } else {
                fireDataChanged(DataChangedListener.REMOVED, value);
            }
            previousY = y;
        } else {
            if (previousX < x) {
                fireDataChanged(DataChangedListener.ADDED, value);
            } else {
                fireDataChanged(DataChangedListener.REMOVED, value);
            }
            previousX = x;
        }
    }

    /**
     * @inheritDoc
     */
    public void pointerDragged(int x, int y) {
        if (!editable) {
            return;
        }
        if (vertical && previousY == -1) {
            previousY = y;
            return;
        }
        if (!vertical && previousX == -1) {
            previousX = x;
            return;
        }
        byte per = 0;
        if (vertical) {
            y = Math.abs(getHeight() - (y - getAbsoluteY()));
            per = (byte) (Math.min(100, ((float) y) / ((float) getHeight()) * 100));
        } else {
            x = Math.abs(x - getAbsoluteX());
            per = (byte) (Math.min(100, ((float) x) / ((float) getWidth()) * 100));
        }
        if (per != getProgress()) {
            setProgressInternal(per);
            if (vertical) {
                if (previousY < y) {
                    fireDataChanged(DataChangedListener.ADDED, value);
                } else {
                    fireDataChanged(DataChangedListener.REMOVED, value);
                }
                previousY = y;
            } else {
                if (previousX < x) {
                    fireDataChanged(DataChangedListener.ADDED, value);
                } else {
                    fireDataChanged(DataChangedListener.REMOVED, value);
                }
                previousX = x;
            }
        }
    }

    /**
     * @inheritDoc
     */
    protected void fireClicked() {
        setHandlesInput(!handlesInput());
    }

    /**
     * @inheritDoc
     */
    protected boolean isSelectableInteraction() {
        return editable;
    }

    /**
     * @inheritDoc
     */
    public void pointerReleased(int x, int y) {
        if (!editable) {
            return;
        }
        previousX = -1;
        previousY = -1;
    }

    /**
     * @inheritDoc
     */
    public void keyPressed(int code) {
        if (editable && handlesInput()) {
            int game = Display.getInstance().getGameAction(code);
            switch(game) {
                case Display.GAME_UP:
                    if (vertical) {
                        setProgressInternal((byte) (Math.min(maxValue, value + increments)));
                        fireDataChanged(DataChangedListener.ADDED, value);
                    } else {
                        setHandlesInput(false);
                    }
                    break;
                case Display.GAME_DOWN:
                    if (vertical) {
                        setProgressInternal((byte) (Math.max(minValue, value - increments)));
                        fireDataChanged(DataChangedListener.REMOVED, value);
                    } else {
                        setHandlesInput(false);
                    }
                    break;
                case Display.GAME_LEFT:
                    if (!vertical) {
                        setProgressInternal((byte) (Math.max(minValue, value - increments)));
                        fireDataChanged(DataChangedListener.REMOVED, value);
                    } else {
                        setHandlesInput(false);
                    }
                    break;
                case Display.GAME_RIGHT:
                    if (!vertical) {
                        setProgressInternal((byte) (Math.min(maxValue, value + increments)));
                        fireDataChanged(DataChangedListener.ADDED, value);
                    } else {
                        setHandlesInput(false);
                    }
                    break;
                case Display.GAME_FIRE:
                    if (!Display.getInstance().isThirdSoftButton()) {
                        fireClicked();
                    }
                    break;
            }
        } else {
            if (!Display.getInstance().isThirdSoftButton() && Display.getInstance().getGameAction(code) == Display.GAME_FIRE) {
                fireClicked();
            }
        }
        super.keyPressed(code);
    }

    /**
     * The increments when the user presses a key to the left/right/up/down etc.
     *
     * @return increment value
     */
    public int getIncrements() {
        return increments;
    }

    /**
     * The increments when the user presses a key to the left/right/up/down etc.
     *
     * @param increments increment value
     */
    public void setIncrements(int increments) {
        this.increments = increments;
    }

    private void fireDataChanged(int event, int val) {
        listeners.fireDataChangeEvent(val, event);
    }

    /**
     * Adds a listener to data changed events
     *
     * @param l new listener
     */
    public void addDataChangedListener(DataChangedListener l) {
        listeners.addListener(l);
    }

    /**
     * Removes a listener from data changed events
     *
     * @param l listener to remove
     */
    public void removeDataChangedListener(DataChangedListener l) {
        listeners.removeListener(l);
    }

    /**
     * Indicates that the value of the slider should be rendered with a percentage sign
     * on top of the slider.
     *
     * @return true if so
     */
    public boolean isRenderPercentageOnTop() {
        return renderPercentageOnTop;
    }

    /**
     * Indicates that the value of the slider should be rendered with a percentage sign
     * on top of the slider.
     *
     * @param renderPercentageOnTop true to render percentages
     */
    public void setRenderPercentageOnTop(boolean renderPercentageOnTop) {
        this.renderPercentageOnTop = renderPercentageOnTop;
    }

    /**
     * @return the renderValueOnTop
     */
    public boolean isRenderValueOnTop() {
        return renderValueOnTop;
    }

    /**
     * @param renderValueOnTop the renderValueOnTop to set
     */
    public void setRenderValueOnTop(boolean renderValueOnTop) {
        this.renderValueOnTop = renderValueOnTop;
    }

    /**
     * @return the maxValue
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return the minValue
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * @param minValue the minValue to set
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * The thumb image is drawn on top of the current progress
     *
     * @return the thumbImage
     */
    public Image getThumbImage() {
        return thumbImage;
    }

    /**
     * The thumb image is drawn on top of the current progress
     * 
     * @param thumbImage the thumbImage to set
     */
    public void setThumbImage(Image thumbImage) {
        this.thumbImage = thumbImage;
    }

    /**
     * @inheritDoc
     */
    boolean shouldBlockSideSwipe() {
        return !vertical;
    }
}

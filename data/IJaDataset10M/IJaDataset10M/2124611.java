package net.sourceforge.geeboss.view.editors.pedal;

import net.sourceforge.geeboss.model.editors.EditorHandler;
import net.sourceforge.geeboss.model.editors.Parameter;
import net.sourceforge.geeboss.util.GCUtil;
import net.sourceforge.geeboss.view.widget.led.LedControl;
import net.sourceforge.geeboss.view.widget.led.RedLed;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Base class for led pedals
 * @author <a href="mailto:fborry@free.fr">Frederic BORRY</a>
 */
public abstract class LedPedal extends Pedal {

    /** State led */
    protected LedControl mStateLed = null;

    /** Foot switch */
    protected FootSwitch mFootSwitch = null;

    /**
     * Create a new LedPedal component
     * @param handler the associated editor handler
     * @param parent the parent component
     * @param style the style flag
     * @param background the background the pedal will be displayed on
     */
    public LedPedal(EditorHandler handler, Composite parent, int style, Image background) {
        super(handler, parent, style, background);
        Parameter onOffState = mEditorHandler.getParameter("OnOffState");
        Point ledLocation = getLedLocation();
        mStateLed = new RedLed(onOffState, this, SWT.NULL, GCUtil.createBackgroundImage(mBackgroundImage, ledLocation.x, ledLocation.y, LedControl.WIDTH, LedControl.HEIGHT));
        mStateLed.setLocation(ledLocation);
        Rectangle footSwitchBounds = getFootSwitchBounds();
        mFootSwitch = new FootSwitch(this, GCUtil.createBackgroundImage(mBackgroundImage, footSwitchBounds.x, footSwitchBounds.y, footSwitchBounds.width, footSwitchBounds.height), getFootSwitchImage(), getFootSwitchPressedImage(), footSwitchBounds);
        mFootSwitch.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event e) {
                mStateLed.toggleState();
                mEditorHandler.fireParameterChanged("OnOffState");
            }
        });
    }

    /**
     * Returns true if the pedal is on
     * @return true if the pedal is on
     */
    public boolean getOnOffState() {
        return mStateLed.getState();
    }

    /**
     * Get the implementation specific led location
     * @return the implementation specific led location
     */
    protected abstract Point getLedLocation();

    /**
     * Get the implementation specific foot switch bounds
     * @return the implementation specific foot switch bounds
     */
    protected abstract Rectangle getFootSwitchBounds();

    /**
     * Get the implementation specific foot switch image
     * @return the implementation specific foot switch image
     */
    protected abstract Image getFootSwitchImage();

    /**
     * Get the implementation specific foot switch pressed image
     * @return the implementation specific foot switch pressed image
     */
    protected abstract Image getFootSwitchPressedImage();

    /**
     * Inner foot switch control
     */
    public static class FootSwitch extends Composite {

        /** The foot switch image layered on the background image */
        private Image mFootSwitchImage;

        /** The foot switch pressed image layered on the background image */
        private Image mFootSwitchPressedImage;

        /** The current button pressed state */
        private boolean mCurrentPressedState;

        /**
         * Construct a new FootSwitch given a background image and bounds
         * @param comp  the parent composite
         * @param background  the background image
         * @param switchImage the image of the switch
         * @param switchPressedImage the image of the pressed switch
         * @param bounds the bounds
         */
        public FootSwitch(Composite comp, Image background, Image switchImage, Image switchPressedImage, Rectangle bounds) {
            super(comp, SWT.NULL);
            setBounds(bounds);
            mFootSwitchImage = GCUtil.layerImages(background, switchImage);
            mFootSwitchPressedImage = GCUtil.layerImages(background, switchPressedImage);
            background.dispose();
            mCurrentPressedState = false;
            addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    redraw();
                }
            });
            addMouseListener(new MouseAdapter() {

                public void mouseDown(MouseEvent e) {
                    mCurrentPressedState = true;
                    redraw();
                }

                public void mouseUp(MouseEvent e) {
                    mCurrentPressedState = false;
                    modified();
                    redraw();
                }
            });
        }

        /**
         * Redraw method override
         */
        public void redraw() {
            Image currentImage = mCurrentPressedState ? mFootSwitchPressedImage : mFootSwitchImage;
            GC gc = new GC(this);
            gc.drawImage(currentImage, 0, 0);
            gc.dispose();
        }

        /**
         * Notify listners about a modification of the value
         */
        private void modified() {
            Event event = new Event();
            this.notifyListeners(SWT.Modify, event);
        }
    }
}

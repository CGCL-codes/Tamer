package br.com.ggagliano.swingutil.tools;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;
import br.com.ggagliano.swingutil.component.Backgroundable;

/**
 * <p>
 * This class manages the background image of a component, that can be changed
 * using a custom list of images and a time interval defined. Once configured,
 * this class automatically changes the actual background image to the next one
 * in the list after the time period specified has passed.
 * </p>
 *
 * <p>
 * Note that a {@link Backgroundable} component and a list of images must be
 * configured for this class to work properly. It is necessary to inform a delay
 * value, that will be used to count time before the starting and between the
 * images replacement. However, if this value is not informed, a default value
 * (30 seconds) will be automatically applied.
 * </p>
 *
 * @author Gabriel Gagliano
 * @since 0.1
 * @version 1.0 12/10/2009
 *
 */
public class SlideShower implements ActionListener {

    private Backgroundable component;

    private List<Image> images;

    private int actualImageIndex = -1;

    private Timer timer;

    private int delay = 30000;

    private boolean running;

    /**
	 * Default constructor. Only creates the object, but do not configure it.
	 */
    public SlideShower() {
        super();
    }

    /**
	 * Creates an instance of the class with a reference to the component that
	 * should have its background image changed.
	 *
	 * @param component
	 *            The component whose background should be manipulated.
	 */
    public SlideShower(Backgroundable component) {
        super();
        this.component = component;
    }

    /**
	 * Creates an instance of the class with a reference to the component that
	 * should have its background image changed and a list of images from where
	 * the background change must be applied.
	 *
	 * @param component
	 *            The component whose background should be manipulated.
	 * @param images
	 *            The images list, in the proper order, to be suited in the
	 *            background of the component.
	 */
    public SlideShower(Backgroundable component, List<Image> images) {
        super();
        this.component = component;
        this.images = images;
    }

    /**
	 * Creates an instance of the class with a reference to the component that
	 * should have its background image changed, also a list of images from
	 * where the background change must be applied and the delay in milliseconds
	 * from the initial and between background changes.
	 *
	 * @param component
	 *            The component whose background should be manipulated.
	 * @param images
	 *            The images list, in the proper order, to be suited in the
	 *            background of the component.
	 * @param delay
	 *            Int value representing the milliseconds from the initial and
	 *            between background changes.
	 */
    public SlideShower(Backgroundable component, List<Image> images, int delay) {
        super();
        this.component = component;
        this.images = images;
        this.delay = delay;
    }

    /**
	 * Retrieves the component configured.
	 *
	 * @return The {@link Backgroundable} component, if defined, or <i>null</i>,
	 *         otherwise.
	 */
    public Backgroundable getComponent() {
        return component;
    }

    /**
	 * Defines the {@link Backgroundable} component to be used.
	 *
	 * @param component
	 *            The component.
	 */
    public void setComponent(Backgroundable component) {
        this.component = component;
    }

    /**
	 * Gets the images list registered to this instance.
	 *
	 * @return The {@link Image} list.
	 */
    public List<Image> getImages() {
        return images;
    }

    /**
	 * Sets the images list that will be used to change the background of the
	 * target component.
	 *
	 * @param images
	 *            The {@link Image} list.
	 */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
	 * Sets the images list (as a list of {@link ImageIcon}) that will be used
	 * to change the background of the target component.
	 *
	 * @param icons
	 *            The {@link ImageIcon} list.
	 */
    public void setImageIcons(List<ImageIcon> icons) {
        List<Image> images = new ArrayList<Image>();
        for (ImageIcon icon : icons) {
            images.add(icon.getImage());
        }
        this.images = images;
    }

    /**
	 * Gets the int value in Milliseconds of the time delay used between the
	 * image changes.
	 *
	 * @return The Milliseconds value.
	 */
    public int getDelay() {
        return delay;
    }

    /**
	 * Sets the value in Milliseconds of the time delay used between the image
	 * changes. If you are updating the value and this instance is already
	 * running, you should make a call to the {@link #restart()} method.
	 *
	 * @param delay
	 *            The Milliseconds value. It must be equals or greater than
	 *            zero. It is recommended a value greater than 1000, for the
	 *            sake of the user, as well as for the sake of the Timer thread
	 *            and code processing.
	 */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
	 * Just informs if the changes is going on or if the real action is not
	 * happening.
	 *
	 * @return True, if it is keeping changing the background. False, otherwise.
	 */
    public boolean isRunning() {
        return running;
    }

    /**
	 * Starts the background changing engine. If it has never been started by
	 * this instance, also initiates the Timer Thread with the informed values.
	 * If no delay value is informed until this call, a default value of 30
	 * seconds will be automatically applied.
	 */
    public void start() {
        if (!this.running) {
            if (this.timer == null) {
                this.timer = new Timer(this.delay, this);
            } else {
                this.timer.setDelay(this.delay);
            }
            if (this.timer != null) {
                this.timer.start();
                this.running = true;
            }
        }
    }

    /**
	 * Stops and starts the background changing action. Before the restart, the
	 * delay value is updated. So, if you have changed it, you should call this
	 * restart method.
	 */
    public void restart() {
        if (this.timer != null) {
            this.timer.stop();
            this.running = false;
        }
        this.start();
    }

    /**
	 * Causes the background image changing action to completely stop.
	 */
    public void stop() {
        if (this.timer != null) {
            this.timer.stop();
            this.running = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.component != null && this.images != null && !this.images.isEmpty()) {
            Image image = this.findNextImage();
            this.component.setBackgroundImage(image);
            if (this.component instanceof JComponent) {
                ((JComponent) this.component).repaint();
            }
        }
    }

    private Image findNextImage() {
        if (this.actualImageIndex++ == this.images.size() - 1) {
            this.actualImageIndex = 0;
        }
        return this.images.get(this.actualImageIndex);
    }
}

package org.easyway.objects.animo;

import java.io.Serializable;
import java.util.Vector;
import org.easyway.interfaces.base.IDestroyable;
import org.easyway.interfaces.base.IPureLoopable;
import org.easyway.interfaces.base.ITexture;
import org.easyway.system.StaticRef;
import org.easyway.utils.Utility;

/**
 * this class can be used to create an animation: a linear sequence of images
 * that change in the time.
 * 
 * @author Daniele Paggi
 * @version 1
 * @see Animo
 */
@Deprecated
public class OldAnimo implements IPureLoopable, IDestroyable, Serializable {

    /**
	 * version
	 */
    private static final long serialVersionUID = -5695125770188316569L;

    /** the image is updated every frame_ms nanoseconds */
    public long frame_ns;

    /** indicates if the image is destroyed or not */
    private boolean destroyed = false;

    /** elasped time */
    private long time;

    /** image list */
    protected Vector<ITexture> list;

    /** the current image to draws */
    protected int currentImageIndex;

    /** indicates if the animation is stopped or not */
    protected boolean stopped;

    /**
	 * creates a new instace of Animo.<br>
	 */
    public OldAnimo() {
        this(30);
    }

    /**
	 * creates a new instace of Animo<br>
	 * The images of animo will be switched every 'speed' nanoseconds
	 * 
	 * @param speed
	 *            the time
	 */
    public OldAnimo(int speed) {
        frame_ns = speed;
        list = new Vector<ITexture>(10);
        currentImageIndex = 0;
        time = 0;
        stopped = true;
    }

    /**
	 * creates a copy of the Animo 'ani'<br>
	 * 
	 * @param ani
	 *            the source Animo
	 */
    public OldAnimo(OldAnimo ani) {
        list = ani.list;
        time = ani.getTime();
        frame_ns = ani.frame_ns;
        currentImageIndex = ani.currentImageIndex;
        stopped = ani.stopped;
    }

    /**
	 * returns a copy
	 * 
	 * @return returns a copy
	 */
    public OldAnimo copy() {
        return new OldAnimo(this);
    }

    public void setSpeed(long speed) {
        frame_ns = speed;
    }

    /**
	 * adds an image to the animation
	 * 
	 * @param spr
	 *            the image to adds
	 */
    public void add(ITexture spr) {
        list.add(spr);
    }

    /**
	 * removes an image from the animation
	 * 
	 * @param index
	 *            the index of image
	 */
    public void remove(int index) {
        if (index < 0 || index >= list.size()) {
            Utility.error("remove(int) in Animo : can't remove texture", "Animo.remove(int)");
            return;
        }
        list.remove(index);
    }

    /**
	 * removes an image from the animation
	 * 
	 * @param spr
	 *            the image to removes
	 */
    public void remove(ITexture spr) {
        list.remove(spr);
    }

    /**
	 * removes all images from the animation
	 * 
	 */
    public void removeAll() {
        list.removeAllElements();
    }

    /**
	 * retusn the number of images that are used in the animation
	 */
    public int getNumberOfImages() {
        return list.size();
    }

    /**
	 * pauses the animation
	 */
    public void stop() {
        stopped = true;
    }

    /** starts the animation from the start */
    public void start() {
        stopped = false;
        currentImageIndex = 0;
        time = 0;
    }

    /** resume the animation */
    public void resume() {
        stopped = false;
    }

    /**
	 * returns if the animation is stopped or not
	 * 
	 * @see #isRunning()
	 */
    public boolean isPaused() {
        return stopped;
    }

    /**
	 * returns if the animation is running or not
	 * 
	 * @see #isPaused()
	 */
    public boolean isRunning() {
        return !stopped;
    }

    /** go to the next frame */
    public void incFrame() {
        if (currentImageIndex == list.size() - 1) {
            currentImageIndex = 0;
        } else ++currentImageIndex;
    }

    /** go to the previews frame */
    public void decFrame() {
        if (currentImageIndex == 0) {
            currentImageIndex = list.size() - 1;
        } else --currentImageIndex;
    }

    /**
	 * loops the animation: auto-update the currentImage of Animo.<br>
	 * This method is usually autoused by the game enigne.
	 */
    public void loop() {
        time += StaticRef.core.getElaspedTime();
        if (!stopped) if (list.size() > 1) if (time >= frame_ns) {
            time = 0;
            incFrame();
        }
    }

    /**
	 * returns the current image of animation
	 */
    public ITexture getCurrentImage() {
        if (currentImageIndex >= list.size()) Utility.error("currentImage of animo >= animo.size()", "Animo.getCurrentImage");
        return list.elementAt(currentImageIndex);
    }

    /**
	 * returns the list index of current image
	 */
    public int getCurrentImageNumber() {
        return currentImageIndex;
    }

    /** changes the image of index position with the 'spr' image */
    public void set(int index, ITexture spr) {
        if (index <= 0) {
            Utility.error("set( int Texture) in Animo", "Animo.set(int,Texture)");
            return;
        }
        list.set(index, spr);
    }

    /** changes the current image */
    public void setCurrentImage(int index) {
        currentImageIndex = index;
    }

    /** gets the image of index position */
    public ITexture get(int index) {
        if (index < 0 || index >= list.size()) {
            Utility.error("get(int) in Animo", "Animo.get(int)");
            return null;
        }
        return list.elementAt(index);
    }

    /**
	 * returns the list of images
	 * 
	 * @return image list
	 */
    public Vector getListOfImages() {
        return list;
    }

    /**
	 * returns the elasped time
	 * 
	 * @return elasped time
	 */
    public long getTime() {
        return time;
    }

    public void destroy() {
        if (destroyed) return;
        destroyed = true;
        stop();
        removeAll();
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}

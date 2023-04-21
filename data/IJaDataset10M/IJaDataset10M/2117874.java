package yager.images;

import yager.world.SceneGraphObject;

/** Represents an image.
 * 
 * An image can be described as a set of pixel values.
 * 
 * @author Ryan Hild (therealfreaker@sourceforge.net)
 */
public abstract class Image extends SceneGraphObject {

    /** Used by the rendering system to mark whether
   * the image needs to be updated into the
   * underlying graphics system.
   */
    protected boolean dirty = true;

    /** Gets the Image's pixel data.
   * @return The image data, in the RGBA unsigned byte format.
   */
    public abstract byte[] getData();

    /** Returns whether the dirty flag is set or not.
   * @return True if the dirty flag is set, false if otherwise.
   */
    public final boolean isDirty() {
        return dirty;
    }

    /** Sets the dirty flag.
   * 
   * The dirty flag is used by the rendering system
   * to mark whether the image needs to be updated 
   * into the underlying graphics system.
   * 
   * @param dirty - If true, sets the Image as dirty, otherwise, as not dirty.
   */
    public final void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}

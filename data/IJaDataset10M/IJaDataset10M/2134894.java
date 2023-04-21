package dsc.awt;

import java.awt.*;

/** Simple component that holds and image.
 *
 * @author Dodekaedron Software Creations, Inc. -- Wraith
 */
public class ImageComponent extends Canvas {

    protected Image image;

    /** Paints the image 
   */
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    /** Creates new component that holds given image.
   *
   * @param i Image to show on the component.
   */
    public ImageComponent(Image i) {
        super();
        image = i;
    }

    /** Gets dimensions of the component (and the image)
   *
   * @return Mininum dimensions of the component.
   */
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    /** Gets dimensions of the component (and the image)
   *
   * @return Preferred dimensions of the component.
   */
    public Dimension getMinimumSize() {
        return new Dimension(image.getWidth(this), image.getHeight(this));
    }

    /** Sets the components image.
   *
   * @param i New image.
   */
    public void setImage(Image i) {
        image = i;
    }

    /** Gets the components image.
   *
   * @return the image for this component.
   */
    protected Image getImage() {
        return image;
    }
}

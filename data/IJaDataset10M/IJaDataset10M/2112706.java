package org.gdi3d.vrmlloader.vrml.field;

/**  Description of the Class */
public class ConstSFImage extends org.gdi3d.vrmlloader.vrml.ConstField {

    org.gdi3d.vrmlloader.impl.ConstSFImage impl;

    /**
     *Constructor for the ConstSFImage object
     *
     *@param  init Description of the Parameter
     */
    public ConstSFImage(org.gdi3d.vrmlloader.impl.ConstSFImage init) {
        impl = init;
    }

    /**
     *  Gets the width attribute of the ConstSFImage object
     *
     *@return  The width value
     */
    public int getWidth() {
        return impl.getWidth();
    }

    /**
     *  Gets the height attribute of the ConstSFImage object
     *
     *@return  The height value
     */
    public int getHeight() {
        return impl.getHeight();
    }

    /**
     *  Gets the components attribute of the ConstSFImage object
     *
     *@return  The components value
     */
    public int getComponents() {
        return impl.getComponents();
    }

    /**
     *  Gets the pixels attribute of the ConstSFImage object
     *
     *@param  pixels Description of the Parameter
     */
    public void getPixels(byte[] pixels) {
        impl.getPixels(pixels);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new ConstSFImage((org.gdi3d.vrmlloader.impl.ConstSFImage) impl.clone());
    }
}

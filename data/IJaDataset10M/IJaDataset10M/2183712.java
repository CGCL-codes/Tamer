package org.gdi3d.vrmlloader.vrml.field;

/**  Description of the Class */
public class ConstMFVec3d extends org.gdi3d.vrmlloader.vrml.ConstMField {

    org.gdi3d.vrmlloader.impl.ConstMFVec3d impl;

    /**
     *Constructor for the ConstMFVec3d object
     *
     *@param  init Description of the Parameter
     */
    public ConstMFVec3d(org.gdi3d.vrmlloader.impl.ConstMFVec3d init) {
        impl = init;
    }

    /**
     *  Gets the value attribute of the ConstMFVec3d object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(double[][] values) {
        impl.getValue(values);
    }

    /**
     *  Gets the value attribute of the ConstMFVec3d object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(double[] values) {
        impl.getValue(values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  values Description of the Parameter
     */
    public void get1Value(int index, double[] values) {
        impl.get1Value(index, values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void get1Value(int index, SFVec3d vec) {
        impl.get1Value(index, vec.impl);
    }

    /**
     *  Gets the size attribute of the ConstMFVec3d object
     *
     *@return  The size value
     */
    public int getSize() {
        return impl.getSize();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new ConstMFVec3d((org.gdi3d.vrmlloader.impl.ConstMFVec3d) impl.clone());
    }
}

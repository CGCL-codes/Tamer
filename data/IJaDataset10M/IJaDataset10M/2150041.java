package org.gdi3d.vrmlloader.vrml.field;

/**  Description of the Class */
public class MFVec3d extends org.gdi3d.vrmlloader.vrml.MField {

    org.gdi3d.vrmlloader.impl.MFVec3d impl;

    /**
     *Constructor for the MFVec3d object
     *
     *@param  init Description of the Parameter
     */
    public MFVec3d(org.gdi3d.vrmlloader.impl.MFVec3d init) {
        super(init);
        impl = init;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new MFVec3d((org.gdi3d.vrmlloader.impl.MFVec3d) impl.clone());
    }

    /**
     *  Gets the size attribute of the MFVec3d object
     *
     *@return  The size value
     */
    public int getSize() {
        return impl.getSize();
    }

    /**  Description of the Method */
    public void clear() {
        impl.clear();
    }

    /**
     *  Description of the Method
     *
     *@param  i Description of the Parameter
     */
    public void delete(int i) {
        impl.delete(i);
    }

    /**
     *Constructor for the MFVec3d object
     *
     *@param  values Description of the Parameter
     */
    public MFVec3d(double[][] values) {
        super(null);
        impl = new org.gdi3d.vrmlloader.impl.MFVec3d(values);
        implField = impl;
    }

    /**
     *Constructor for the MFVec3d object
     *
     *@param  size Description of the Parameter
     *@param  values Description of the Parameter
     */
    public MFVec3d(int size, double[] values) {
        super(null);
        impl = new org.gdi3d.vrmlloader.impl.MFVec3d(size, values);
        implField = impl;
    }

    /**
     *Constructor for the MFVec3d object
     *
     *@param  values Description of the Parameter
     */
    public MFVec3d(double[] values) {
        super(null);
        impl = new org.gdi3d.vrmlloader.impl.MFVec3d(values);
        implField = impl;
    }

    /**
     *  Gets the value attribute of the MFVec3d object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(double[][] values) {
        impl.getValue(values);
    }

    /**
     *  Gets the value attribute of the MFVec3d object
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
     *  Sets the value attribute of the MFVec3d object
     *
     *@param  values The new value value
     */
    public void setValue(double[][] values) {
        impl.setValue(values);
    }

    /**
     *  Sets the value attribute of the MFVec3d object
     *
     *@param  values The new value value
     */
    public void setValue(double[] values) {
        impl.setValue(values);
    }

    /**
     *  Sets the value attribute of the MFVec3d object
     *
     *@param  size The new value value
     *@param  values The new value value
     */
    public void setValue(int size, double[] values) {
        impl.setValue(size, values);
    }

    /**
     *  Sets the value attribute of the MFVec3d object
     *
     *@param  values The new value value
     */
    public void setValue(ConstMFVec3d values) {
        impl.setValue(values.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void set1Value(int index, ConstSFVec3d constvec) {
        impl.set1Value(index, constvec.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void set1Value(int index, SFVec3d vec) {
        impl.set1Value(index, vec.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  z Description of the Parameter
     */
    public void set1Value(int index, double x, double y, double z) {
        impl.set1Value(index, x, y, z);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  constvec Description of the Parameter
     */
    public void insertValue(int index, ConstSFVec3d constvec) {
        impl.insertValue(index, constvec.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void insertValue(int index, SFVec3d vec) {
        impl.insertValue(index, vec.impl);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  z Description of the Parameter
     */
    public void insertValue(int index, double x, double y, double z) {
        impl.insertValue(index, x, y, z);
    }
}

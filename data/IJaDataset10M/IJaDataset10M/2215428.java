package org.gdi3d.vrmlloader.impl;

/**  Description of the Class */
public class ConstMFVec3f extends ConstMField {

    /**
     *Constructor for the ConstMFVec3f object
     *
     *@param  owner Description of the Parameter
     */
    ConstMFVec3f(MFVec3f owner) {
        super(owner);
    }

    /**
     *Constructor for the ConstMFVec3f object
     *
     *@param  values Description of the Parameter
     */
    public ConstMFVec3f(double[][] values) {
        super(new MFVec3f(values));
    }

    public boolean equals(Field other) {
        boolean result = false;
        System.out.println(this.getClass().getName() + ".equals not implemented yet");
        return result;
    }

    /**
     *Constructor for the ConstMFVec3f object
     *
     *@param  size Description of the Parameter
     *@param  values Description of the Parameter
     */
    public ConstMFVec3f(int size, double[] values) {
        super(new MFVec3f(size, values));
    }

    /**
     *  Gets the value attribute of the ConstMFVec3f object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(double[][] values) {
        ((MFVec3f) ownerField).getValue(values);
    }

    /**
     *  Gets the value attribute of the ConstMFVec3f object
     *
     *@param  values Description of the Parameter
     */
    public void getValue(double[] values) {
        ((MFVec3f) ownerField).getValue(values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  values Description of the Parameter
     */
    public void get1Value(int index, double[] values) {
        ((MFVec3f) ownerField).get1Value(index, values);
    }

    /**
     *  Description of the Method
     *
     *@param  index Description of the Parameter
     *@param  vec Description of the Parameter
     */
    public void get1Value(int index, SFVec3f vec) {
        ((MFVec3f) ownerField).get1Value(index, vec);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public synchronized Object clone() {
        return new ConstMFVec3f((MFVec3f) ownerField);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public org.gdi3d.vrmlloader.vrml.Field wrap() {
        return new org.gdi3d.vrmlloader.vrml.field.ConstMFVec3f(this);
    }
}

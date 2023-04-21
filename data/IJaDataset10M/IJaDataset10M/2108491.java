package org.gdi3d.vrmlloader.impl;

/**  Description of the Class */
public class ConstSFNode extends ConstField {

    /**
     *Constructor for the ConstSFNode object
     *
     *@param  owner Description of the Parameter
     */
    ConstSFNode(SFNode owner) {
        super(owner);
    }

    /**
     *Constructor for the ConstSFNode object
     *
     *@param  node Description of the Parameter
     */
    public ConstSFNode(BaseNode node) {
        super(new SFNode(node));
    }

    public boolean equals(Field other) {
        boolean result = false;
        System.out.println(this.getClass().getName() + ".equals not implemented yet");
        return result;
    }

    /**
     *  Gets the value attribute of the ConstSFNode object
     *
     *@return  The value value
     */
    public BaseNode getValue() {
        return ((SFNode) ownerField).getValue();
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new ConstSFNode((SFNode) ownerField);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public org.gdi3d.vrmlloader.vrml.Field wrap() {
        return new org.gdi3d.vrmlloader.vrml.field.ConstSFNode(this);
    }
}

package org.gdi3d.vrmlloader.vrml;

/**  Description of the Class */
public abstract class BaseNode {

    /**  Description of the Field */
    protected org.gdi3d.vrmlloader.impl.BaseNode implBaseNode;

    /**
     *Constructor for the BaseNode object
     *
     *@param  init Description of the Parameter
     */
    protected BaseNode(org.gdi3d.vrmlloader.impl.BaseNode init) {
        implBaseNode = init;
    }

    /**
     *  Gets the type attribute of the BaseNode object
     *
     *@return  The type value
     */
    public abstract String getType();

    /**
     *  Gets the impl attribute of the BaseNode object
     *
     *@return  The impl value
     */
    protected org.gdi3d.vrmlloader.impl.BaseNode getImpl() {
        return implBaseNode;
    }

    /**
     *  Gets the implNode attribute of the BaseNode object
     *
     *@return  The implNode value
     */
    public javax.media.ding3d.Node getImplNode() {
        return implBaseNode.getImplNode();
    }
}

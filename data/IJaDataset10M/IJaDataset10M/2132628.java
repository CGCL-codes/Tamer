package org.gdi3d.vrmlloader.impl;

import java.util.Vector;
import javax.media.ding3d.BoundingSphere;
import javax.media.ding3d.Node;
import javax.media.ding3d.vecmath.Point3d;

/**  Description of the Class */
public class Group extends GroupBase {

    javax.media.ding3d.Group impl;

    /**
     *Constructor for the Group object
     *
     *@param  loader Description of the Parameter
     */
    public Group(Loader loader) {
        super(loader);
    }

    public boolean equals(BaseNode other) {
        boolean result = false;
        System.out.println(this.getClass().getName() + ".equals not implemented yet");
        return result;
    }

    /**
     *Constructor for the Group object
     *
     *@param  loader Description of the Parameter
     *@param  children Description of the Parameter
     *@param  bboxCenter Description of the Parameter
     *@param  bboxSize Description of the Parameter
     */
    Group(Loader loader, MFNode children, SFVec3f bboxCenter, SFVec3f bboxSize) {
        super(loader, children, bboxCenter, bboxSize);
    }

    /**  Description of the Method */
    void initImpl() {
        impl = new javax.media.ding3d.Group();
        implGroup = impl;
        implNode = impl;
        impl.setUserData(new Vector());
        super.replaceChildren();
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@param  g Description of the Parameter
     */
    void initImpl(javax.media.ding3d.Group g) {
        impl = g;
        implGroup = impl;
        implNode = impl;
        impl.setUserData(new Vector());
        super.replaceChildren();
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        super.notifyMethod(eventInName, time);
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        Object o = new Group(loader, (MFNode) children.clone(), (SFVec3f) bboxCenter.clone(), (SFVec3f) bboxSize.clone());
        loader.cleanUp();
        return o;
    }

    /**
     *  Gets the type attribute of the Group object
     *
     *@return  The type value
     */
    public String getType() {
        return "Group";
    }

    /**  Description of the Method */
    void initFields() {
        initGroupBaseFields();
    }
}

package org.ogre4j;

import org.xbig.base.*;

public interface IRaySceneQueryListener extends INativeObject {

    /** 
    Called when a movable objects intersects the ray. **/
    public boolean queryResult(org.ogre4j.IMovableObject obj, float distance);

    /** 
    Called when a world fragment is intersected by the ray. **/
    public boolean queryResult(org.ogre4j.ISceneQuery.IWorldFragment fragment, float distance);
}

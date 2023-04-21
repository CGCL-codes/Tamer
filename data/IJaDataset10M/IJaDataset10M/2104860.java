package org.ogre4j.emittercommands;

import org.xbig.base.*;

public interface ICmdMaxVelocity extends INativeObject, org.ogre4j.IParamCommand {

    /** **/
    public String doGet(VoidPointer target);

    /** **/
    public void doSet(VoidPointer target, String val);
}

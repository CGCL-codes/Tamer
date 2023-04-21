package org.ogre4j.overlayelementcommands;

import org.xbig.base.*;

public interface ICmdWidth extends INativeObject, org.ogre4j.IParamCommand {

    /** **/
    public String doGet(VoidPointer target);

    /** **/
    public void doSet(VoidPointer target, String val);
}

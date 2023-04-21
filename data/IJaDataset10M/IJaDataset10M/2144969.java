package org.ogre4j;

import org.xbig.base.*;

public interface ITextureSourceTranslator extends INativeObject, org.ogre4j.IScriptTranslator {

    /** **/
    public void translate(org.ogre4j.IScriptCompiler compiler, org.ogre4j.IAbstractNodePtr node);
}

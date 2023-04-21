package javax.media.ding3d;

import javax.media.ding3d.vecmath.*;

/**
 * The ShaderAttributeRetained object encapsulates a uniform attribute for a
 * shader programs.
 */
abstract class ShaderAttributeRetained extends NodeComponentRetained {

    /**
     * Name of the shader attribute (immutable)
     */
    String attrName;

    /**
     * Package scope constructor
     */
    ShaderAttributeRetained() {
    }

    void initializeAttrName(String attrName) {
        this.attrName = attrName;
    }

    /**
     * Retrieves the name of this shader attribute.
     *
     * @return the name of this shader attribute
     */
    String getAttributeName() {
        return attrName;
    }

    void initMirrorObject() {
        ((ShaderAttributeObjectRetained) mirror).initializeAttrName(this.attrName);
    }
}

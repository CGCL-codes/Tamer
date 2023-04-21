package com.trapezium.vrml;

import com.trapezium.parse.TokenEnumerator;

/**
 *  Scene graph component representing a field id.  Also includes
 *  all built in fieldId names (these should probably move to VRML97 in
 *  grammar package).  FieldId optimization is to no longer create these
 *  as scene graph component, but set them in a Field object.  This
 *  optimization is not complete.
 *
 *  @author          Johannes N. Johannsen
 *  @version         1.1, 15 Jan 1998
 *
 *  @since           1.0
 */
public class FieldId extends SingleTokenElement {

    String fieldId;

    /** Not exactly identical to VRML97 info, here field ids are stored
     *  independent of their associated node.
     */
    static String[] afieldIds = { "addChildren", "axisOfRotation", "autoOffset", "ambientIntensity", "avatarSize", "attenuation", "appearance" };

    static String[] bfieldIds = { "backUrl", "bottomUrl", "bboxCenter", "bboxSize", "bottomRadius", "bottom", "beginCap", "beamWidth", "bindTime" };

    static String[] cfieldIds = { "children", "collide", "collideTime", "color", "ccw", "colorPerVertex", "creaseAngle", "convex", "crossSection", "coord", "colorIndex", "coordIndex", "center", "cutOffAngle", "choice", "cycleInterval", "cycleTime", "controlPoint" };

    static String[] dfieldIds = { "description", "duration_changed", "diskAngle", "direction", "diffuseColor", "directOutput" };

    static String[] efieldIds = { "enabled", "endCap", "emissiveColor", "enterTime", "exitTime" };

    static String[] ffieldIds = { "frontUrl", "fogType", "family", "fontStyle", "fieldOfView" };

    static String[] gfieldIds = { "groundAngle", "groundColor", "geometry" };

    static String[] hfieldIds = { "height", "horizontal", "headlight", "hitNormal_changed", "hitTexCoord_changed" };

    static String[] ifieldIds = { "isActive", "isBound", "intensity", "image", "isOver", "info" };

    static String[] jfieldIds = { "justify", "jump" };

    static String[] kfieldIds = { "key", "keyValue" };

    static String[] lfieldIds = { "loop", "leftUrl", "language", "leftToRight", "level", "location", "length" };

    static String[] mfieldIds = { "material", "maxAngle", "minAngle", "maxPosition", "minPosition", "mustEvaluate", "maxBack", "maxFront", "minBack", "minFront", "maxExtent" };

    static String[] nfieldIds = { "normal", "normalPerVertex", "normalIndex" };

    static String[] ofieldIds = { "offset", "on", "orientation", "orientation_changed" };

    static String[] pfieldIds = { "pitch", "proxy", "point", "position_changed", "priority" };

    static String[] qfieldIds = { "" };

    static String[] rfieldIds = { "rightUrl", "removeChildren", "radius", "rotation_changed", "repeatS", "repeatT", "range", "rotation" };

    static String[] sfieldIds = { "startTime", "stopTime", "set_bind", "skyAngle", "skyColor", "size", "set_fraction", "side", "set_height", "solid", "set_crossSection", "set_orientation", "set_scale", "set_spine", "scale", "spine", "spacing", "style", "set_colorIndex", "set_coordIndex", "set_normalIndex", "set_texCoordIndex", "shininess", "specularColor", "speed", "source", "spatialize", "string", "scaleOrientation" };

    static String[] tfieldIds = { "texture", "textureTransform", "topUrl", "top", "trackPoint_changed", "texCoord", "topToBottom", "texCoordIndex", "transparency", "type", "translation_changed", "translation", "time", "touchTime", "title" };

    static String[] ufieldIds = { "url", "uDimension", "uKnot", "uOrder", "uTessellation" };

    static String[] vfieldIds = { "value_changed", "visibilityRange", "visibilityLimit", "vector", "vDimension", "vKnot", "vOrder", "vTessellation" };

    static String[] wfieldIds = { "whichChoice", "weight" };

    static String[] xfieldIds = { "xDimension", "xSpacing" };

    static String[] yfieldIds = { "" };

    static String[] zfieldIds = { "zDimension", "zSpacing" };

    static String[][] alphaField = new String[26][];

    static {
        alphaField[0] = afieldIds;
        alphaField[1] = bfieldIds;
        alphaField[2] = cfieldIds;
        alphaField[3] = dfieldIds;
        alphaField[4] = efieldIds;
        alphaField[5] = ffieldIds;
        alphaField[6] = gfieldIds;
        alphaField[7] = hfieldIds;
        alphaField[8] = ifieldIds;
        alphaField[9] = jfieldIds;
        alphaField[10] = kfieldIds;
        alphaField[11] = lfieldIds;
        alphaField[12] = mfieldIds;
        alphaField[13] = nfieldIds;
        alphaField[14] = ofieldIds;
        alphaField[15] = pfieldIds;
        alphaField[16] = qfieldIds;
        alphaField[17] = rfieldIds;
        alphaField[18] = sfieldIds;
        alphaField[19] = tfieldIds;
        alphaField[20] = ufieldIds;
        alphaField[21] = vfieldIds;
        alphaField[22] = wfieldIds;
        alphaField[23] = xfieldIds;
        alphaField[24] = yfieldIds;
        alphaField[25] = zfieldIds;
    }

    ;

    /** class constructor
     *
     *  @param  tokenOffset  the token representing the field id
     *  @param  v  TokenEnumerator associated with token
     */
    public FieldId(int tokenOffset, TokenEnumerator v) {
        super(tokenOffset);
        fieldId = getVRML2name(tokenOffset, v);
        if (!v.isName(tokenOffset)) {
            setError("expected field id here");
            int state = v.getState();
            int nextToken = v.getNextToken();
            if (v.isName(nextToken)) {
                v.setState(state);
            }
        }
        v.breakLineAt(tokenOffset);
    }

    /**
	 *  get a String object representing field.  
	 *  This is an optimization, prevents creation of multiple String
	 *  objects within the TokenEnumerator "toString" method.  Instead
	 *  uses String objects in fieldIds array.
	 */
    public static String getVRML2name(int tokenOffset, TokenEnumerator v) {
        char vFirstChar = v.charAt(tokenOffset, 0);
        if ((vFirstChar >= 'a') && (vFirstChar <= 'z')) {
            String[] fieldIds = alphaField[vFirstChar - 'a'];
            int sz = fieldIds.length;
            int idtableSize = fieldIds.length;
            for (int i = 0; i < idtableSize; i++) {
                if (v.sameAs(fieldIds[i])) {
                    return (fieldIds[i]);
                }
            }
        }
        return (v.toString(tokenOffset));
    }

    /** class constructor */
    public FieldId(TokenEnumerator v) {
        this(v.getNextToken(), v);
    }

    /** class constructor */
    public FieldId(String s) {
        super(-1);
        fieldId = s;
    }

    /** get the field id */
    public String getName() {
        if (fieldId == null) {
            fieldId = super.getName();
        }
        return (fieldId);
    }
}

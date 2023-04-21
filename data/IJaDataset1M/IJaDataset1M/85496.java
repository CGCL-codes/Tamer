package org.gdi3d.vrmlloader.impl;

import javax.media.ding3d.utils.geometry.Text2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.util.Hashtable;
import javax.media.ding3d.BoundingBox;
import javax.media.ding3d.Font3D;
import javax.media.ding3d.QuadArray;
import javax.media.ding3d.Shape3D;
import javax.media.ding3d.Text3D;
import javax.media.ding3d.Transform3D;
import javax.media.ding3d.TransformGroup;
import javax.media.ding3d.vecmath.Color3f;
import javax.media.ding3d.vecmath.Point3d;
import javax.media.ding3d.vecmath.Vector3d;

/**  Description of the Class */
public class Text extends Geometry {

    MFString string;

    SFNode fontStyle;

    MFFloat length;

    SFFloat maxExtent;

    static String vrmlSerif = "SERIF";

    static String vrmlSans = "SANS";

    static String vrmlFixed = "TYPEWRITER";

    static String j3dSerif = "Serif";

    static String j3dSans = "Sans";

    static String j3dFixed = "Courier";

    static String vrmlPlain = "PLAIN";

    static String vrmlBold = "BOLD";

    static String vrmlItalic = "ITALIC";

    static String vrmlBoldItalic = "BOLDITALIC";

    static final int RASTER_SIZE = 9;

    static final int font3dSize = 1;

    static final double tessellationTolerance = 0.02;

    TransformGroup impl = null;

    private String text3d_string = "";

    private javax.media.ding3d.Shape3D text_shape;

    private String fontName = j3dSerif;

    private int fontStyleId = Font.PLAIN;

    /**
	 *Constructor for the Text object
	 *
	 *@param  loader Description of the Parameter
	 */
    public Text(Loader loader) {
        super(loader);
        string = new MFString();
        fontStyle = new SFNode();
        length = new MFFloat();
        maxExtent = new SFFloat(0.0f);
        initFields();
    }

    public boolean equals(BaseNode other) {
        boolean result = false;
        System.out.println(this.getClass().getName() + ".equals not implemented yet");
        return result;
    }

    /**
	 *Constructor for the Text object
	 *
	 *@param  loader Description of the Parameter
	 *@param  string Description of the Parameter
	 *@param  fontStyle Description of the Parameter
	 *@param  length Description of the Parameter
	 *@param  maxExtent Description of the Parameter
	 */
    public Text(Loader loader, MFString string, SFNode fontStyle, MFFloat length, SFFloat maxExtent) {
        super(loader);
        this.string = string;
        this.fontStyle = fontStyle;
        this.length = length;
        this.maxExtent = maxExtent;
        initFields();
    }

    /**
	 *  Description of the Method
	 *
	 *@param  app Description of the Parameter
	 *@return  Description of the Return Value
	 */
    javax.media.ding3d.Node createText2D(javax.media.ding3d.Appearance app) {
        if (loader.debug) {
            System.out.println("Text.createText2D() called");
        }
        if ((string.strings != null) && (string.strings.length > 0)) {
            if (loader.debug) {
                System.out.println("creating Text2D object");
            }
            FontStyle fs = null;
            if ((fontStyle.node != null) && (fontStyle.node instanceof FontStyle)) {
                fs = (FontStyle) fontStyle.node;
            } else {
                fs = new FontStyle(loader);
            }
            if (fs.family.strings.length > 0) {
                boolean found = false;
                for (int i = 0; ((i < fs.family.strings.length) && !found); i++) {
                    if (vrmlSerif.equals(fs.family.strings[i])) {
                        fontName = j3dSerif;
                        found = true;
                    } else if (vrmlSans.equals(fs.family.strings[i])) {
                        fontName = j3dSans;
                        found = true;
                    } else if (vrmlFixed.equals(fs.family.strings[i])) {
                        fontName = j3dFixed;
                        found = true;
                    }
                }
            }
            if (fs.style.string != null) {
                if (vrmlPlain.equals(fs.style.string)) {
                    fontStyleId = Font.PLAIN;
                } else if (vrmlBold.equals(fs.style.string)) {
                    fontStyleId = Font.BOLD;
                } else if (vrmlItalic.equals(fs.style.string)) {
                    fontStyleId = Font.ITALIC;
                } else if (vrmlBoldItalic.equals(fs.style.string)) {
                    fontStyleId = Font.BOLD + Font.ITALIC;
                }
            }
            impl = new TransformGroup();
            text3d_string = string.strings[0];
            text_shape = new javax.media.ding3d.Shape3D();
            text_shape.setAppearance(app);
            impl.addChild(text_shape);
            float scale = fs.size.value;
            Transform3D textXform_scale = new Transform3D();
            textXform_scale.setScale(scale);
            impl.setTransform(textXform_scale);
            implNode = impl;
        }
        return impl;
    }

    private void updateLabelText() {
        if (!text3d_string.equals(string.strings[0])) {
            impl.removeChild(text_shape);
            java.awt.Font font = new java.awt.Font(fontName, fontStyleId, font3dSize);
            Font3D font3d = new Font3D(font, tessellationTolerance, null);
            javax.media.ding3d.vecmath.Point3f tposition = new javax.media.ding3d.vecmath.Point3f();
            javax.media.ding3d.Text3D text3d = new javax.media.ding3d.Text3D(font3d, string.strings[0], tposition, javax.media.ding3d.Text3D.ALIGN_CENTER, javax.media.ding3d.Text3D.PATH_RIGHT);
            text_shape.setGeometry(text3d);
            impl.addChild(text_shape);
            text3d_string = string.strings[0];
        }
    }

    /**
	 *  Gets the implGeom attribute of the Text object
	 *
	 *@return  The implGeom value
	 */
    public javax.media.ding3d.Geometry getImplGeom() {
        return null;
    }

    /**
	 *  Description of the Method
	 *
	 *@return  Description of the Return Value
	 */
    public boolean haveTexture() {
        return false;
    }

    /**
	 *  Gets the numTris attribute of the Text object
	 *
	 *@return  The numTris value
	 */
    public int getNumTris() {
        return 0;
    }

    /**
	 *  Gets the boundingBox attribute of the Text object
	 *
	 *@return  The boundingBox value
	 */
    public javax.media.ding3d.BoundingBox getBoundingBox() {
        return null;
    }

    /**
	 *  Gets the type attribute of the Text object
	 *
	 *@return  The type value
	 */
    public String getType() {
        return "Text";
    }

    /**
	 *  Description of the Method
	 *
	 *@return  Description of the Return Value
	 */
    public Object clone() {
        return new Text(loader, (MFString) string.clone(), (SFNode) fontStyle.clone(), (MFFloat) length.clone(), (SFFloat) maxExtent.clone());
    }

    /**
	 *  Description of the Method
	 *
	 *@param  eventInName Description of the Parameter
	 *@param  time Description of the Parameter
	 */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("string")) {
            updateLabelText();
        } else if (eventInName.equals("route_string")) {
        } else System.out.println("Text: unimplemented event " + eventInName);
    }

    /**  Description of the Method */
    void initFields() {
        string.init(this, FieldSpec, Field.EXPOSED_FIELD, "string");
        fontStyle.init(this, FieldSpec, Field.EXPOSED_FIELD, "fontStyle");
        length.init(this, FieldSpec, Field.EXPOSED_FIELD, "length");
        maxExtent.init(this, FieldSpec, Field.EXPOSED_FIELD, "maxExtent");
    }

    @Override
    javax.media.ding3d.Geometry getImplGeomNormals() {
        return null;
    }
}

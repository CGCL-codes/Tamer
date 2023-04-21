package org.icepdf.core.pobjects;

import org.icepdf.core.pobjects.graphics.*;
import org.icepdf.core.util.Library;
import android.graphics.Bitmap;
import java.util.Enumeration;
import org.icepdf.core.util.Hashtable;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * A resource is a dictionary type as defined by the PDF specification.  It
 * can contain fonts, xobjects, colorspaces, patterns, shading and
 * external graphic states.
 *
 * @since 1.0
 */
public class Resources extends Dictionary {

    private static final Logger logger = Logger.getLogger(Resources.class.toString());

    Hashtable fonts;

    Hashtable xobjects;

    Hashtable colorspaces;

    Hashtable patterns;

    Hashtable shading;

    Hashtable extGStates;

    private Hashtable<String, Bitmap> images = new Hashtable<String, Bitmap>();

    /**
     * @param l
     * @param h
     */
    public Resources(Library l, Hashtable h) {
        super(l, h);
        colorspaces = library.getDictionary(entries, "ColorSpace");
        fonts = library.getDictionary(entries, "Font");
        xobjects = library.getDictionary(entries, "XObject");
        patterns = library.getDictionary(entries, "Pattern");
        shading = library.getDictionary(entries, "Shading");
        extGStates = library.getDictionary(entries, "ExtGState");
    }

    public void dispose(boolean cache) {
        if (images != null) {
            Enumeration shapeContent = images.elements();
            while (shapeContent.hasMoreElements()) {
                Object image = shapeContent.nextElement();
                if (image instanceof Bitmap) {
                    Bitmap tmp = (Bitmap) image;
                    tmp.recycle();
                }
            }
            images.clear();
        }
        if (xobjects != null) {
            Enumeration xobjectContent = xobjects.elements();
            while (xobjectContent.hasMoreElements()) {
                Object tmp = xobjectContent.nextElement();
                if (tmp instanceof Stream) {
                    Stream stream = (Stream) tmp;
                    stream.dispose(cache);
                }
                if (tmp instanceof Reference) {
                    Object reference = library.getObject((Reference) tmp);
                    if (reference instanceof Form) {
                        Form form = (Form) reference;
                        form.dispose(cache);
                    }
                    if (reference instanceof Stream) {
                        Stream stream = (Stream) reference;
                        stream.dispose(cache);
                    }
                }
            }
        }
    }

    /**
     * @param o
     * @return
     */
    public PColorSpace getColorSpace(Object o) {
        if (o == null) {
            return null;
        }
        Object tmp;
        if (colorspaces != null && colorspaces.get(o) != null) {
            tmp = colorspaces.get(o);
            PColorSpace cs = PColorSpace.getColorSpace(library, tmp);
            if (cs != null) {
                cs.init();
            }
            return cs;
        }
        if (patterns != null && patterns.get(o) != null) {
            tmp = patterns.get(o);
            PColorSpace cs = PColorSpace.getColorSpace(library, tmp);
            if (cs != null) {
                cs.init();
            }
            return cs;
        }
        PColorSpace cs = PColorSpace.getColorSpace(library, o);
        if (cs != null) {
            cs.init();
        }
        return cs;
    }

    /**
     * @param s
     * @return
     */
    public org.icepdf.core.pobjects.fonts.Font getFont(String s) {
        org.icepdf.core.pobjects.fonts.Font font = null;
        if (fonts != null) {
            Object ob = fonts.get(s);
            if (ob instanceof org.icepdf.core.pobjects.fonts.Font) {
                font = (org.icepdf.core.pobjects.fonts.Font) ob;
            } else if (ob instanceof Reference) {
                font = (org.icepdf.core.pobjects.fonts.Font) library.getObject((Reference) ob);
            }
        }
        if (font != null) {
            font.init();
        }
        return font;
    }

    /**
     * @param s
     * @param fill
     * @return
     */
    public Bitmap getImage(String s, int fill) {
        Bitmap image = images.get(s);
        if (image != null) {
            return image;
        }
        Stream st = (Stream) library.getObject(xobjects, s);
        if (st == null) {
            return null;
        }
        if (!st.isImageSubtype()) {
            return null;
        }
        try {
            image = st.getImage(fill, this, true);
        } catch (Exception e) {
            logger.log(Level.FINE, "Error getting image by name: " + s, e);
        }
        if (image != null && !st.isImageMask()) {
            images.put(s, image);
        }
        return image;
    }

    /**
     * @param s
     * @return
     */
    public boolean isForm(String s) {
        Object o = library.getObject(xobjects, s);
        return o instanceof Form;
    }

    /**
     * Gets the Form XObject specified by the named reference.
     *
     * @param nameReference name of resourse to retreive.
     * @return if the named reference is found return it, otherwise return null;
     */
    public Form getForm(String nameReference) {
        Form formXObject = null;
        Object tempForm = library.getObject(xobjects, nameReference);
        if (tempForm instanceof Form) {
            formXObject = (Form) tempForm;
        }
        return formXObject;
    }

    /**
     * Retrieves a Pattern object given the named resource.  This can be
     * call for a fill, text fill or do image mask.
     *
     * @param name of object to find.
     * @return tiling or shading type pattern object.  If not constructor is
     *         found, then null is returned.
     */
    public Pattern getPattern(String name) {
        if (patterns != null) {
            Object attribute = library.getObject(patterns, name);
            if (attribute != null && attribute instanceof TilingPattern) {
                return (TilingPattern) attribute;
            } else if (attribute != null && attribute instanceof Hashtable) {
                return ShadingPattern.getShadingPattern(library, (Hashtable) attribute);
            }
        }
        return null;
    }

    /**
     * Gets the shadding pattern based on a shading dictionary name,  similar
     * to getPattern but is only called for the 'sh' token.
     *
     * @param name name of shading dictionary
     * @return associated shading pattern if any.
     */
    public ShadingPattern getShading(String name) {
        if (shading != null) {
            Object shadingDictionary = library.getObject(shading, name);
            if (shadingDictionary != null && shadingDictionary instanceof Hashtable) {
                return ShadingPattern.getShadingPattern(library, entries, (Hashtable) shadingDictionary);
            }
        }
        return null;
    }

    /**
     * Returns the ExtGState object which has the specified reference name.
     *
     * @param namedReference name of ExtGState object to try and find.
     * @return ExtGState which contains the named references ExtGState attrbutes,
     *         if the namedReference could not be found null is returned.
     */
    public ExtGState getExtGState(String namedReference) {
        ExtGState gsState = null;
        if (extGStates != null) {
            Object attribute = library.getObject(extGStates, namedReference);
            if (attribute instanceof Hashtable) {
                gsState = new ExtGState(library, (Hashtable) attribute);
            } else if (attribute instanceof Reference) {
                gsState = new ExtGState(library, (Hashtable) library.getObject((Reference) attribute));
            }
        }
        return gsState;
    }
}

package com.itextpdf.text.pdf.parser;

import java.io.IOException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfReader;

/**
 * Represents image data from a PDF
 * @since 5.0.1
 */
public class ImageRenderInfo {

    /** The coordinate transformation matrix that was in effect when the image was rendered */
    private final Matrix ctm;

    /** A reference to the image XObject */
    private final PdfIndirectReference ref;

    /** A reference to an inline image */
    private final InlineImageInfo inlineImageInfo;

    /** the color space associated with the image */
    private final PdfDictionary colorSpaceDictionary;

    /** the image object to be rendered, if it has been parsed already.  Null otherwise. */
    private PdfImageObject imageObject = null;

    private ImageRenderInfo(Matrix ctm, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary) {
        this.ctm = ctm;
        this.ref = ref;
        this.inlineImageInfo = null;
        this.colorSpaceDictionary = colorSpaceDictionary;
    }

    private ImageRenderInfo(Matrix ctm, InlineImageInfo inlineImageInfo, PdfDictionary colorSpaceDictionary) {
        this.ctm = ctm;
        this.ref = null;
        this.inlineImageInfo = inlineImageInfo;
        this.colorSpaceDictionary = colorSpaceDictionary;
    }

    /**
     * Create an ImageRenderInfo object based on an XObject (this is the most common way of including an image in PDF)
     * @param ctm the coordinate transformation matrix at the time the image is rendered
     * @param ref a reference to the image XObject
     * @return the ImageRenderInfo representing the rendered XObject
     * @since 5.0.1
     */
    public static ImageRenderInfo createForXObject(Matrix ctm, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary) {
        return new ImageRenderInfo(ctm, ref, colorSpaceDictionary);
    }

    /**
     * Create an ImageRenderInfo object based on inline image data.  This is nowhere near completely thought through
     * and really just acts as a placeholder.
     * @param ctm the coordinate transformation matrix at the time the image is rendered
     * @param imageObject the image object representing the inline image
     * @return the ImageRenderInfo representing the rendered embedded image
     * @since 5.0.1
     */
    protected static ImageRenderInfo createForEmbeddedImage(Matrix ctm, InlineImageInfo inlineImageInfo, PdfDictionary colorSpaceDictionary) {
        ImageRenderInfo renderInfo = new ImageRenderInfo(ctm, inlineImageInfo, colorSpaceDictionary);
        return renderInfo;
    }

    /**
     * Gets an object containing the image dictionary and bytes.
     * @return an object containing the image dictionary and byte[]
     * @since 5.0.2
     */
    public PdfImageObject getImage() throws IOException {
        prepareImageObject();
        return imageObject;
    }

    private void prepareImageObject() throws IOException {
        if (imageObject != null) return;
        if (ref != null) {
            PRStream stream = (PRStream) PdfReader.getPdfObject(ref);
            imageObject = new PdfImageObject(stream, colorSpaceDictionary);
        } else if (inlineImageInfo != null) {
            imageObject = new PdfImageObject(inlineImageInfo.getImageDictionary(), inlineImageInfo.getSamples(), colorSpaceDictionary);
        }
    }

    /**
     * @return a vector in User space representing the start point of the xobject
     */
    public Vector getStartPoint() {
        return new Vector(0, 0, 1).cross(ctm);
    }

    /**
     * @return The coordinate transformation matrix active when this image was rendered.  Coordinates are in User space.
     * @since 5.0.3
     */
    public Matrix getImageCTM() {
        return ctm;
    }

    /**
     * @return the size of the image, in User space units
     * @since 5.0.3
     */
    public float getArea() {
        return ctm.getDeterminant();
    }

    /**
     * @return an indirect reference to the image
     * @since 5.0.2
     */
    public PdfIndirectReference getRef() {
        return ref;
    }
}

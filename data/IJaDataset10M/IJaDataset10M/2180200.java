package org.pdfclown.documents.contents.xObjects;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.Document;
import org.pdfclown.objects.PdfDictionary;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfInteger;
import org.pdfclown.objects.PdfName;
import org.pdfclown.objects.PdfStream;
import org.pdfclown.util.NotImplementedException;

/**
  Image external object [PDF:1.6:4.8.4].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @version 0.1.1, 11/01/11
*/
@PDF(VersionEnum.PDF10)
public final class ImageXObject extends XObject {

    public ImageXObject(Document context, PdfStream baseDataObject) {
        super(context, baseDataObject);
        baseDataObject.getHeader().put(PdfName.Subtype, PdfName.Image);
    }

    /**
    <span style="color:red">For internal use only.</span>
  */
    public ImageXObject(PdfDirectObject baseObject) {
        super(baseObject);
    }

    /**
    Gets the number of bits per color component.
  */
    public int getBitsPerComponent() {
        return ((PdfInteger) getBaseDataObject().getHeader().get(PdfName.BitsPerComponent)).getRawValue();
    }

    @Override
    public ImageXObject clone(Document context) {
        throw new NotImplementedException();
    }

    /**
    Gets the color space in which samples are specified.
  */
    public String getColorSpace() {
        return ((PdfName) getBaseDataObject().getHeader().get(PdfName.ColorSpace)).getRawValue();
    }

    @Override
    public AffineTransform getMatrix() {
        Dimension2D size = getSize();
        return new AffineTransform(1 / size.getWidth(), 0, 0, 1 / size.getHeight(), 0, 0);
    }

    /**
    Gets the size of the image (in samples).
  */
    @Override
    public Dimension2D getSize() {
        PdfDictionary header = getBaseDataObject().getHeader();
        return new Dimension(((PdfInteger) header.get(PdfName.Width)).getRawValue(), ((PdfInteger) header.get(PdfName.Height)).getRawValue());
    }

    @Override
    public void setSize(Dimension2D value) {
        throw new UnsupportedOperationException();
    }
}

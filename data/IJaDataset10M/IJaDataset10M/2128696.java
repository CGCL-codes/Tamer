package org.pdfclown.documents.interaction.annotations;

import java.awt.geom.Rectangle2D;
import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.contents.colorSpaces.DeviceRGBColor;
import org.pdfclown.objects.PdfArray;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfName;
import org.pdfclown.objects.PdfNumber;
import org.pdfclown.util.NotImplementedException;

/**
  Abstract shape annotation.

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.0.7
  @version 0.1.1, 11/09/11
*/
@PDF(VersionEnum.PDF13)
public abstract class Shape extends Annotation {

    protected Shape(Page page, Rectangle2D box, PdfName subtype) {
        super(page.getDocument(), subtype, box, page);
    }

    protected Shape(PdfDirectObject baseObject) {
        super(baseObject);
    }

    @Override
    public Shape clone(Document context) {
        throw new NotImplementedException();
    }

    /**
    Gets the color with which to fill the interior of the annotation's shape.
  */
    public DeviceRGBColor getFillColor() {
        PdfArray fillColorObject = (PdfArray) getBaseDataObject().get(PdfName.IC);
        return fillColorObject != null ? new DeviceRGBColor(((PdfNumber<?>) fillColorObject.get(0)).getDoubleValue(), ((PdfNumber<?>) fillColorObject.get(1)).getDoubleValue(), ((PdfNumber<?>) fillColorObject.get(2)).getDoubleValue()) : null;
    }

    /**
    @see #getFillColor()
  */
    public void setFillColor(DeviceRGBColor value) {
        getBaseDataObject().put(PdfName.IC, value.getBaseDataObject());
    }
}

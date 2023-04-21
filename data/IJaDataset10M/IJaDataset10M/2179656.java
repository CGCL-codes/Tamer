package org.pdfclown.documents.contents.colorSpaces;

import java.awt.Paint;
import java.util.List;
import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.contents.IContentContext;
import org.pdfclown.objects.PdfArray;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfStream;
import org.pdfclown.util.NotImplementedException;

@PDF(VersionEnum.PDF13)
public final class ICCBasedColorSpace extends ColorSpace<PdfArray> {

    ICCBasedColorSpace(PdfDirectObject baseObject) {
        super(baseObject);
    }

    @Override
    public ICCBasedColorSpace clone(Document context) {
        throw new NotImplementedException();
    }

    @Override
    public Color<?> getColor(List<PdfDirectObject> components, IContentContext context) {
        return new DeviceRGBColor(components);
    }

    @Override
    public int getComponentCount() {
        return 0;
    }

    @Override
    public Color<?> getDefaultColor() {
        return DeviceGrayColor.Default;
    }

    @Override
    public Paint getPaint(Color<?> color) {
        return new java.awt.Color(0, 0, 0);
    }

    public PdfStream getProfile() {
        return (PdfStream) getBaseDataObject().resolve(1);
    }
}

package org.pdfclown.documents.contents;

import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.contents.layers.Layer;
import org.pdfclown.documents.contents.layers.LayerMembership;
import org.pdfclown.files.File;
import org.pdfclown.objects.PdfDictionary;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfName;
import org.pdfclown.objects.PdfObjectWrapper;
import org.pdfclown.util.NotImplementedException;

/**
  Private information meaningful to the program (application or plugin extension)
  creating the marked content [PDF:1.6:10.5.1].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.1.0
  @version 0.1.1, 06/08/11
*/
@PDF(VersionEnum.PDF12)
public class PropertyList extends PdfObjectWrapper<PdfDictionary> {

    /**
    Wraps the specified base object into a property list object.

    @param baseObject Base object of a property list object.
    @return Property list object corresponding to the base object.
  */
    public static PropertyList wrap(PdfDirectObject baseObject) {
        if (baseObject == null) return null;
        PdfName type = (PdfName) ((PdfDictionary) File.resolve(baseObject)).get(PdfName.Type);
        if (Layer.TypeName.equals(type)) return new Layer(baseObject); else if (LayerMembership.TypeName.equals(type)) return new LayerMembership(baseObject); else return new PropertyList(baseObject);
    }

    public PropertyList(Document context, PdfDictionary baseDataObject) {
        super(context, baseDataObject);
    }

    public PropertyList(PdfDirectObject baseObject) {
        super(baseObject);
    }

    @Override
    public PropertyList clone(Document context) {
        throw new NotImplementedException();
    }
}

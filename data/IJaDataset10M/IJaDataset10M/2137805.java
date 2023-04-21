package org.pdfclown.documents.contents.entities;

import org.pdfclown.documents.Document;
import org.pdfclown.documents.contents.IContentEntity;
import org.pdfclown.documents.contents.composition.PrimitiveComposer;
import org.pdfclown.documents.contents.objects.ContentObject;
import org.pdfclown.documents.contents.xObjects.XObject;

/**
  Abstract specialized graphic object.

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @version 0.1.0
*/
public abstract class Entity implements IContentEntity {

    public abstract ContentObject toInlineObject(PrimitiveComposer composer);

    public abstract XObject toXObject(Document context);
}

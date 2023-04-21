package org.pdfclown.documents.contents.objects;

import org.pdfclown.documents.contents.IContentContext;
import org.pdfclown.documents.contents.Resources;
import org.pdfclown.objects.PdfName;
import org.pdfclown.objects.PdfObjectWrapper;

/**
  Resource reference.

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.1.1
  @version 0.1.1, 11/01/11
*/
public interface IResourceReference<TResource extends PdfObjectWrapper<?>> {

    /**
    Gets the resource name.

    @see #getResource(IContentContext)
    @see Resources
  */
    PdfName getName();

    /**
    Gets the referenced resource.
    <p>Whether a {@link #getName() resource name} is available or not, it can be respectively either
    shared or private.</p>

    @param context Content context.
  */
    TResource getResource(IContentContext context);

    /**
    @see #getName()
  */
    void setName(PdfName value);
}

package org.pdfclown.documents.interaction;

import org.pdfclown.documents.interaction.actions.Action;
import org.pdfclown.documents.interaction.navigation.document.Destination;
import org.pdfclown.objects.PdfObjectWrapper;

/**
  Link.

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.0.8
  @version 0.1.2, 02/04/12
*/
public interface ILink {

    /**
    Gets the link target.

    @return Either a {@link Destination} or an {@link Action}.
  */
    public PdfObjectWrapper<?> getTarget();

    /**
    @see #getTarget()
  */
    public void setTarget(PdfObjectWrapper<?> value);
}

package com.phloc.commons.microdom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a complete document.
 * 
 * @author philip
 */
public interface IMicroDocument extends IMicroNodeWithChildren {

    /**
   * @return <code>true</code> if the document is standalone (having neither DTD
   *         nor scheme) or <code>false</code> otherwise
   */
    boolean isStandalone();

    /**
   * @return May be <code>null</code>.
   */
    @Nullable
    IMicroDocumentType getDocType();

    /**
   * @return May be <code>null</code>.
   */
    @Nullable
    IMicroElement getDocumentElement();

    @Nonnull
    IMicroDocument getClone();
}

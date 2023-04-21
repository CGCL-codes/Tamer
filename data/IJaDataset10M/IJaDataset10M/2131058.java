package com.phloc.html.hc.html5;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import com.phloc.commons.microdom.EMicroNodeType;
import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.AbstractMicroNode;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.EHTMLElement;

/**
 * HTML5 document type representation
 * 
 * @author philip
 */
@Immutable
public final class HTML5DocumentType extends AbstractMicroNode implements IMicroDocumentType {

    public HTML5DocumentType() {
    }

    @Nonnull
    public EMicroNodeType getType() {
        return EMicroNodeType.DOCUMENT_TYPE;
    }

    @Nonnull
    public String getNodeName() {
        return "#doctype";
    }

    @Nonnull
    public String getQualifiedName() {
        return EHTMLElement.HTML.getElementName();
    }

    @Nullable
    public String getPublicID() {
        return null;
    }

    @Nullable
    public String getSystemID() {
        return null;
    }

    @Nonnull
    public String getHTMLRepresentation() {
        return "<!DOCTYPE " + getQualifiedName() + ">";
    }

    @Nonnull
    public IMicroDocumentType getClone() {
        return new HTML5DocumentType();
    }

    public boolean isEqualContent(@Nullable final IMicroNode aNode) {
        return aNode instanceof HTML5DocumentType;
    }

    @Override
    public String toString() {
        return new ToStringGenerator(this).toString();
    }
}

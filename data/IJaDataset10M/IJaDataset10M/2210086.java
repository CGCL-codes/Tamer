package au.gov.naa.digipres.xena.plugin.html;

import au.gov.naa.digipres.xena.kernel.type.XenaFileType;

/**
 * Type representing Xena XHTML file type.
 *
 */
public class XenaHtmlFileType extends XenaFileType {

    public XenaHtmlFileType() {
    }

    @Override
    public String getTag() {
        return "html";
    }

    @Override
    public String getNamespaceUri() {
        return null;
    }
}

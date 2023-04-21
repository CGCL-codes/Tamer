package au.gov.naa.digipres.xena.plugin.office.wordprocessor;

import au.gov.naa.digipres.xena.plugin.office.OfficeFileType;

/**
 * Type to represent the SXW file type (word processor format in early versions of OpenOffice.org)
 *
 */
public class SxwFileType extends OfficeFileType {

    @Override
    public String getName() {
        return "OpenOffice.org v1 Document";
    }

    @Override
    public String getMimeType() {
        return "application/vnd.sun.xml.writer";
    }

    @Override
    public String getOfficeConverterName() {
        return "writer8";
    }

    @Override
    public String fileExtension() {
        return "sxw";
    }

    @Override
    public String getTextConverterName() {
        return "Text";
    }

    @Override
    public String getODFExtension() {
        return "odt";
    }
}

package au.gov.naa.digipres.xena.plugin.office.presentation;

import au.gov.naa.digipres.xena.plugin.office.OfficeFileType;

/**
 * Type to represent the SXI file type (presentation format in early versions of OpenOffice.org)
 *
 */
public class SxiFileType extends OfficeFileType {

    @Override
    public String getName() {
        return "OpenOffice.org v1 Presentation";
    }

    @Override
    public String getMimeType() {
        return "application/vnd.sun.xml.impress";
    }

    @Override
    public String getOfficeConverterName() {
        return "impress8";
    }

    @Override
    public String fileExtension() {
        return "sxi";
    }

    @Override
    public String getTextConverterName() {
        throw new IllegalStateException("OpenOffice.org does not have a plain text converter for presentations. " + "This file type should not have been linked to a TextNormaliser in the OfficePlugin!");
    }

    @Override
    public String getODFExtension() {
        return "odp";
    }
}

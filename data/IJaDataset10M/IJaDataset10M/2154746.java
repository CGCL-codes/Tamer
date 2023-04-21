package au.gov.naa.digipres.xena.plugin.office.presentation;

import au.gov.naa.digipres.xena.plugin.office.OfficeFileType;

/**
 * Type to represent a MS PowerPoint file.
 *
 */
public class PowerpointFileType extends OfficeFileType {

    public PowerpointFileType() {
    }

    @Override
    public String getName() {
        return "Powerpoint";
    }

    @Override
    public String getMimeType() {
        return "application/vnd.ms-powerpoint";
    }

    @Override
    public String getOfficeConverterName() {
        return "impress8";
    }

    @Override
    public String fileExtension() {
        return "ppt";
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

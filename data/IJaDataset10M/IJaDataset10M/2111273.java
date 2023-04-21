package au.gov.naa.digipres.xena.plugin.audio;

import au.gov.naa.digipres.xena.kernel.type.FileType;

public class PcmType extends FileType {

    public PcmType() {
        super();
    }

    @Override
    public String getName() {
        return "PCM";
    }

    @Override
    public String getMimeType() {
        return null;
    }
}

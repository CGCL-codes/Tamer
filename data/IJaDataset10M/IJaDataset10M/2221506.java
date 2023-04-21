package au.gov.naa.digipres.xena.plugin.audio;

import au.gov.naa.digipres.xena.kernel.type.FileType;

public class WavType extends FileType {

    public WavType() {
        super();
    }

    @Override
    public String getName() {
        return "WAV";
    }

    @Override
    public String getMimeType() {
        return "audio/wav";
    }
}

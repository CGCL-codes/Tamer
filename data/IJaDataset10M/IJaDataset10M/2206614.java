package entagged.audioformats.wav.util;

import entagged.audioformats.EncodingInfo;
import entagged.audioformats.exceptions.*;
import java.io.*;

public class WavInfoReader {

    public EncodingInfo read(RandomAccessFile raf) throws CannotReadException, IOException {
        EncodingInfo info = new EncodingInfo();
        if (raf.length() < 12) {
            throw new CannotReadException("This is not a WAV File (<12 bytes)");
        }
        byte[] b = new byte[12];
        raf.read(b);
        WavRIFFHeader wh = new WavRIFFHeader(b);
        if (wh.isValid()) {
            b = new byte[24];
            raf.read(b);
            WavFormatHeader wfh = new WavFormatHeader(b);
            if (wfh.isValid()) {
                info.setPreciseLength(((float) raf.length() - (float) 36) / wfh.getBytesPerSecond());
                info.setChannelNumber(wfh.getChannelNumber());
                info.setSamplingRate(wfh.getSamplingRate());
                info.setEncodingType("WAV-RIFF " + wfh.getBitrate() + " bits");
                info.setExtraEncodingInfos("");
                info.setBitrate(wfh.getBytesPerSecond() * 8 / 1000);
                info.setVbr(false);
            } else {
                throw new CannotReadException("Wav Format Header not valid");
            }
        } else {
            throw new CannotReadException("Wav RIFF Header not valid");
        }
        return info;
    }
}

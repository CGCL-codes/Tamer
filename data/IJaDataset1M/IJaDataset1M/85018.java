package org.spantus.extractor;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import org.spantus.core.extractor.IExtractorConfig;
import org.spantus.core.extractor.IExtractorInputReader;
import org.spantus.core.extractor.SignalFormat;
import org.spantus.utils.Assert;

/**
 * 
 * @author Mindaugas Greibus
 * 
 * @since 0.0.1
 * Created Jun 3, 2009
 *
 */
public abstract class ExtractorsFactory {

    public static final Integer DEFAULT_WINDOW_LENGHT = 33;

    public static final Integer DEFAULT_WINDOW_OVERLAP = 66;

    public static IExtractorInputReader createReader(SignalFormat format) {
        ExtractorInputReader reader = new ExtractorInputReader();
        reader.setConfig(createConfig(format));
        return reader;
    }

    public static IExtractorInputReader createReader(IExtractorConfig extractorConfig) {
        ExtractorInputReader reader = new ExtractorInputReader();
        reader.setConfig(extractorConfig);
        return reader;
    }

    public static IExtractorInputReader createReader(AudioFormat format, int windowLengthInMilSec, int overlapInPerc) {
        ExtractorInputReader reader = new ExtractorInputReader();
        reader.setConfig(createConfig(format, windowLengthInMilSec, overlapInPerc));
        return reader;
    }

    public static IExtractorInputReader createReader(AudioFormat format) {
        ExtractorInputReader reader = new ExtractorInputReader();
        reader.setConfig(createConfig(format, DEFAULT_WINDOW_LENGHT, DEFAULT_WINDOW_OVERLAP));
        return reader;
    }

    public static IExtractorConfig createConfig(AudioFormat format, int windowLengthInMilSec, int overlapInPerc) {
        return ExtractorConfigUtil.defaultConfig((double) format.getSampleRate(), windowLengthInMilSec, overlapInPerc);
    }

    public static IExtractorConfig createConfig(SignalFormat format) {
        return ExtractorConfigUtil.defaultConfig(format.getSampleRate());
    }

    public static IExtractorInputReader createReader(AudioFileFormat format, int windowLengthInMilSec, int overlapInPerc) {
        Assert.isTrue(format != null, "audio file format cannot be null");
        return createReader(format.getFormat(), windowLengthInMilSec, overlapInPerc);
    }

    public static IExtractorInputReader createReader(AudioFileFormat format) {
        Assert.isTrue(format != null, "audio file format cannot be null");
        return createReader(format.getFormat(), DEFAULT_WINDOW_LENGHT, DEFAULT_WINDOW_OVERLAP);
    }

    public static IExtractorInputReader createNormalizedReader() {
        return new ExtractorInputReader();
    }
}

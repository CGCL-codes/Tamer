package jwbroek.util.properties;

import java.util.Properties;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;

/**
 * PropertyHandler for {@link javax.sound.sampled.AudioFileFormat.Type}s.
 * @author jwbroek
 */
public final class AudioFileFormatTypePropertyHandler implements PropertyHandler<AudioFileFormat.Type> {

    /**
   * The logger for this class.
   */
    private static final Logger logger = Logger.getLogger(AudioFileFormatTypePropertyHandler.class.getCanonicalName());

    /**
   * The singleton instance of this class.
   */
    private static AudioFileFormatTypePropertyHandler instance = new AudioFileFormatTypePropertyHandler();

    /**
   * This constructor is only meant to be called by AudioFileFormatTypePropertyHandler itself, as
   * AudioFileFormatTypePropertyHandler is a singleton class.
   */
    private AudioFileFormatTypePropertyHandler() {
        AudioFileFormatTypePropertyHandler.logger.entering(AudioFileFormatTypePropertyHandler.class.getCanonicalName(), "AudioFileFormatTypePropertyHandler()");
        AudioFileFormatTypePropertyHandler.logger.exiting(AudioFileFormatTypePropertyHandler.class.getCanonicalName(), "AudioFileFormatTypePropertyHandler()");
    }

    /**
   * Get an instance of this class.
   * @return An instance of this class.
   */
    public static AudioFileFormatTypePropertyHandler getInstance() {
        AudioFileFormatTypePropertyHandler.logger.entering(AudioFileFormatTypePropertyHandler.class.getCanonicalName(), "getInstance()");
        AudioFileFormatTypePropertyHandler.logger.exiting(AudioFileFormatTypePropertyHandler.class.getCanonicalName(), "getInstance()", AudioFileFormatTypePropertyHandler.instance);
        return AudioFileFormatTypePropertyHandler.instance;
    }

    /**
   * Convert the value to a String that can be used in a {@link Properties} instance.
   * @param value
   * @return A conversion of the value to a string that can be used in a {@link Properties} instance.
   */
    public String toProperty(final AudioFileFormat.Type value) {
        AudioFileFormatTypePropertyHandler.logger.entering(AudioFileFormatTypePropertyHandler.class.getCanonicalName(), "toProperty(AudioFileFormat.Type)", value);
        final String result = value.toString();
        AudioFileFormatTypePropertyHandler.logger.exiting(AudioFileFormatTypePropertyHandler.class.getCanonicalName(), "toProperty(AudioFileFormat.Type)", result);
        return result;
    }

    /**
   * Convert the value from a {@link Properties} instance into a AudioFileFormat.Type instance.
   * @param value
   * @return A conversion of the value from a {@link Properties} instance into a AudioFileFormat.Type instance.
   * @throws CannotConvertPropertyException When the value could not be converted.
   */
    public AudioFileFormat.Type fromProperty(final String value) throws CannotConvertPropertyException {
        AudioFileFormatTypePropertyHandler.logger.entering(AudioFileFormatTypePropertyHandler.class.getCanonicalName(), "fromProperty(String)", value);
        final AudioFileFormat.Type result;
        if ("AIFC".equals(value)) {
            result = AudioFileFormat.Type.AIFC;
        } else if ("AIFF".equals(value)) {
            result = AudioFileFormat.Type.AIFF;
        } else if ("AU".equals(value)) {
            result = AudioFileFormat.Type.AU;
        } else if ("SND".equals(value)) {
            result = AudioFileFormat.Type.SND;
        } else if ("WAVE".equals(value)) {
            result = AudioFileFormat.Type.WAVE;
        } else {
            final CannotConvertPropertyException exception = new CannotConvertPropertyException("Cannot convert to AudioFileFormat.Type: '" + value + "'");
            AudioFileFormatTypePropertyHandler.logger.throwing(AudioFileFormatTypePropertyHandler.class.getCanonicalName(), "fromProperty(String)", exception);
            throw exception;
        }
        AudioFileFormatTypePropertyHandler.logger.exiting(AudioFileFormatTypePropertyHandler.class.getCanonicalName(), "fromProperty(String)", result);
        return result;
    }
}

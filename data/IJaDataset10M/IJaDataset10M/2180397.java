package javax.sound.sampled.spi;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;

/**
 * This abstract class provides an API for writing audio files.  Concrete
 * subclasses implement the methods declared here.
 * @since 1.3
 */
public abstract class AudioFileWriter {

    /**
   * Creat a new audio file writer.
   */
    public AudioFileWriter() {
    }

    /**
   * Return an array of all audio file format types supported by this
   * provider.
   */
    public abstract AudioFileFormat.Type[] getAudioFileTypes();

    /**
   * Return an array of all the audio file format types supported by this
   * provider, which can be written given the input stream.
   * @param ais the audio input stream
   */
    public abstract AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream ais);

    /**
   * Return true if the indicated type is supported by this provider.
   * @param type the audio file format type
   */
    public boolean isFileTypeSupported(AudioFileFormat.Type type) {
        AudioFileFormat.Type[] types = getAudioFileTypes();
        for (int i = 0; i < types.length; ++i) {
            if (type.equals(types[i])) return true;
        }
        return false;
    }

    /**
   * Return true if the indicated type is supported by this provider,
   * and can be written from the given audio input stream.
   * @param type the audio file format type
   * @param ais the audio input stream to write
   */
    public boolean isFileTypeSupported(AudioFileFormat.Type type, AudioInputStream ais) {
        AudioFileFormat.Type[] types = getAudioFileTypes(ais);
        for (int i = 0; i < types.length; ++i) {
            if (type.equals(types[i])) return true;
        }
        return false;
    }

    /**
   * Write audio data to a file.
   * @param ais the audio input stream to write
   * @param type the desired audio file format type
   * @param out the file to write to
   * @return the number of bytes written
   * @throws IOException if an I/O error occurs when writing
   */
    public abstract int write(AudioInputStream ais, AudioFileFormat.Type type, File out) throws IOException;

    /**
   * Write audio data to an output stream.
   * @param ais the audio input stream to write
   * @param type the desired audio file format type
   * @param os the output stream
   * @return the number of bytes written
   * @throws IOException if an I/O error occurs when writing
   */
    public abstract int write(AudioInputStream ais, AudioFileFormat.Type type, OutputStream os) throws IOException;
}

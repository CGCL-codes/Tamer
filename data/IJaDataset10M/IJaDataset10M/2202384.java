package org.kc7bfi.jflac.sound.spi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.tritonus.share.sampled.file.TAudioFileReader;
import org.kc7bfi.jflac.Constants;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.io.BitInputStream;
import org.kc7bfi.jflac.io.BitOutputStream;
import org.kc7bfi.jflac.metadata.StreamInfo;

/**
 * Provider for Flac audio file reading services. This implementation can parse
 * the format information from Flac audio file, and can produce audio input
 * streams from files of this type.
 *
 * @author Marc Gimpel, Wimba S.A. (marc@wimba.com)
 * @author Besmir Beqiri
 * @version $Revision: 1.8 $
 */
public class FlacAudioFileReader extends TAudioFileReader {

    private static final boolean DEBUG = false;

    private FLACDecoder decoder;

    private StreamInfo streamInfo;

    private static final int INITAL_READ_LENGTH = 64000;

    private static final int MARK_LIMIT = INITAL_READ_LENGTH + 1;

    public FlacAudioFileReader() {
        super(MARK_LIMIT, true);
    }

    /**
     * Obtains the audio file format of the File provided. The File must point
     * to valid audio file data.
     *
     * @param file
     *            the File from which file format information should be
     *            extracted.
     * @return an AudioFileFormat object describing the audio file format.
     * @exception UnsupportedAudioFileException
     *                if the File does not point to a valid audio file data
     *                recognized by the system.
     * @exception IOException
     *                if an I/O exception occurs.
     */
    @Override
    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return getAudioFileFormat(inputStream, (int) file.length());
        } finally {
            inputStream.close();
        }
    }

    /**
     * Obtains an audio input stream from the URL provided. The URL must point
     * to valid audio file data.
     *
     * @param url
     *            the URL for which the AudioInputStream should be constructed.
     * @return an AudioInputStream object based on the audio file data pointed
     *         to by the URL.
     * @exception UnsupportedAudioFileException
     *                if the File does not point to a valid audio file data
     *                recognized by the system.
     * @exception IOException
     *                if an I/O exception occurs.
     */
    @Override
    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStream = url.openStream();
        try {
            return getAudioFileFormat(inputStream);
        } finally {
            inputStream.close();
        }
    }

    /**
     * Obtains an audio input stream from the input stream provided.
     *
     * @param stream
     *            the input stream from which the AudioInputStream should be
     *            constructed.
     * @return an AudioInputStream object based on the audio file data contained
     *         in the input stream.
     * @exception UnsupportedAudioFileException
     *                if the File does not point to a valid audio file data
     *                recognized by the system.
     * @exception IOException
     *                if an I/O exception occurs.
     */
    @Override
    public AudioFileFormat getAudioFileFormat(InputStream stream) throws UnsupportedAudioFileException, IOException {
        return getAudioFileFormat(stream, AudioSystem.NOT_SPECIFIED);
    }

    /**
     * Return the AudioFileFormat from the given InputStream.
     *
     * @param stream
     *            the input stream from which the AudioInputStream should be
     *            constructed.
     * @param medialength
     * @return an AudioInputStream object based on the audio file data contained
     *         in the input stream.
     * @exception UnsupportedAudioFileException
     *                if the File does not point to a valid audio file data
     *                recognized by the system.
     * @exception IOException
     *                if an I/O exception occurs.
     */
    @Override
    public AudioFileFormat getAudioFileFormat(InputStream stream, long lFileLengthInBytes) throws UnsupportedAudioFileException, IOException {
        Map<String, Object> aff_properties = new HashMap<String, Object>();
        Map<String, Object> af_properties = new HashMap<String, Object>();
        int channels = AudioSystem.NOT_SPECIFIED;
        int bitsPerSample = AudioSystem.NOT_SPECIFIED;
        int sampleRate = AudioSystem.NOT_SPECIFIED;
        long totalSamples = AudioSystem.NOT_SPECIFIED;
        int duration = AudioSystem.NOT_SPECIFIED;
        int frameSizeMin = AudioSystem.NOT_SPECIFIED;
        int frameSizeMax = AudioSystem.NOT_SPECIFIED;
        int blockSizeMin = AudioSystem.NOT_SPECIFIED;
        int blockSizeMax = AudioSystem.NOT_SPECIFIED;
        try {
            decoder = new FLACDecoder(stream);
            streamInfo = decoder.readStreamInfo();
            if (streamInfo == null) {
                if (DEBUG) {
                    System.out.println("FLAC file reader: no stream info found");
                }
                throw new UnsupportedAudioFileException("No StreamInfo found");
            }
            bitsPerSample = streamInfo.getBitsPerSample();
            channels = streamInfo.getChannels();
            sampleRate = streamInfo.getSampleRate();
            totalSamples = streamInfo.getTotalSamples();
            duration = Math.round(totalSamples / sampleRate);
            frameSizeMin = streamInfo.getMinFrameSize();
            frameSizeMax = streamInfo.getMaxFrameSize();
            blockSizeMin = streamInfo.getMinBlockSize();
            blockSizeMax = streamInfo.getMaxBlockSize();
            aff_properties.put("flac.bitpersample", new Integer(bitsPerSample));
            aff_properties.put("flac.channels", new Integer(channels));
            aff_properties.put("flac.sampleRate", new Integer(sampleRate));
            aff_properties.put("flac.totalSamples", new Long(totalSamples));
            aff_properties.put("duration", new Long(duration * 1000000L));
            aff_properties.put("flac.framesize.min", new Integer(frameSizeMin));
            aff_properties.put("flac.framesize.max", new Integer(frameSizeMax));
            aff_properties.put("flac.blocksize.min", new Integer(blockSizeMin));
            aff_properties.put("flac.blocksize.max", new Integer(blockSizeMax));
            af_properties.put("bitrate", new Integer(AudioSystem.NOT_SPECIFIED));
            af_properties.put("vbr", Boolean.FALSE);
            af_properties.put("quality", new Integer(100));
        } catch (IOException ioe) {
            if (DEBUG) {
                System.out.println("FLAC file reader: not a FLAC stream");
            }
            throw new UnsupportedAudioFileException(ioe.getMessage());
        }
        AudioFormat format = new FlacAudioFormat(FlacEncoding.FLAC, sampleRate, bitsPerSample, channels, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, false, af_properties);
        if (DEBUG) {
            System.out.println("FLAC file reader: got stream with format " + format);
        }
        return new FlacAudioFileFormat(FlacFileFormatType.FLAC, format, AudioSystem.NOT_SPECIFIED, (int) lFileLengthInBytes, aff_properties);
    }

    /**
     * Obtains an audio input stream from the File provided. The File must point
     * to valid audio file data.
     *
     * @param file
     *            the File for which the AudioInputStream should be constructed.
     * @return an AudioInputStream object based on the audio file data pointed
     *         to by the File.
     * @exception UnsupportedAudioFileException
     *                if the File does not point to a valid audio file data
     *                recognized by the system.
     * @exception IOException
     *                if an I/O exception occurs.
     */
    @Override
    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        InputStream inputStream = new FileInputStream(file);
        try {
            return getAudioInputStream(inputStream, (int) file.length());
        } catch (UnsupportedAudioFileException e) {
            inputStream.close();
            throw e;
        } catch (IOException e) {
            inputStream.close();
            throw e;
        }
    }

    /**
     * Obtains an audio input stream from the URL provided. The URL must point
     * to valid audio file data.
     *
     * @param url
     *            the URL for which the AudioInputStream should be constructed.
     * @return an AudioInputStream object based on the audio file data pointed
     *         to by the URL.
     * @exception UnsupportedAudioFileException
     *                if the File does not point to a valid audio file data
     *                recognized by the system.
     * @exception IOException
     *                if an I/O exception occurs.
     */
    @Override
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStream = url.openStream();
        try {
            return getAudioInputStream(inputStream);
        } catch (UnsupportedAudioFileException e) {
            inputStream.close();
            throw e;
        } catch (IOException ex) {
            inputStream.close();
            throw ex;
        }
    }

    /**
     * Obtains an audio input stream from the input stream provided. The stream
     * must point to valid audio file data.
     *
     * @param stream
     *            the input stream from which the AudioInputStream should be
     *            constructed.
     * @return an AudioInputStream object based on the audio file data contained
     *         in the input stream.
     * @exception UnsupportedAudioFileException
     *                if the File does not point to a valid audio file data
     *                recognized by the system.
     * @exception IOException
     *                if an I/O exception occurs.
     */
    @Override
    public AudioInputStream getAudioInputStream(InputStream stream) throws UnsupportedAudioFileException, IOException {
        return getAudioInputStream(stream, AudioSystem.NOT_SPECIFIED);
    }

    /**
     * Obtains an audio input stream from the input stream provided. The stream
     * must point to valid audio file data.
     *
     * @param inputStream
     *            the input stream from which the AudioInputStream should be
     *            constructed.
     * @param medialength
     * @return an AudioInputStream object based on the audio file data contained
     *         in the input stream.
     * @exception UnsupportedAudioFileException
     *                if the File does not point to a valid audio file data
     *                recognized by the system.
     * @exception IOException
     *                if an I/O exception occurs.
     */
    protected AudioInputStream getAudioInputStream(InputStream inputStream, int medialength) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat audioFileFormat = getAudioFileFormat(inputStream, medialength);
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        BitOutputStream bitOutStream = new BitOutputStream(byteOutStream);
        bitOutStream.writeByteBlock(Constants.STREAM_SYNC_STRING, Constants.STREAM_SYNC_STRING.length);
        streamInfo.write(bitOutStream, false);
        BitInputStream bis = decoder.getBitInputStream();
        int bytesLeft = bis.getInputBytesUnconsumed();
        byte[] b = new byte[bytesLeft];
        bis.readByteBlockAlignedNoCRC(b, bytesLeft);
        byteOutStream.write(b);
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
        SequenceInputStream sequenceInputStream = new SequenceInputStream(byteInStream, inputStream);
        return new AudioInputStream(sequenceInputStream, audioFileFormat.getFormat(), audioFileFormat.getFrameLength());
    }
}

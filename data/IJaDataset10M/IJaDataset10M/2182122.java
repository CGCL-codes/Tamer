package com.safi.workshop.audio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PCMToGSMConverter {

    public static InputStream convert(InputStream stream) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat sourceFileFormat = AudioSystem.getAudioFileFormat(stream);
        AudioFormat.Encoding targetEncoding = new AudioFormat.Encoding("GSM0610");
        AudioFileFormat.Type fileType = new AudioFileFormat.Type("GSM", "gsm");
        AudioInputStream sourceStream = null;
        sourceStream = AudioSystem.getAudioInputStream(stream);
        if (sourceStream == null) {
            throw new IOException("Couldn't acquire input stream");
        }
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat.Encoding encoding = sourceFormat.getEncoding();
        if (sourceFileFormat.getType().toString().equals(fileType.toString())) {
            return stream;
        }
        if (!(encoding.equals(AudioFormat.Encoding.PCM_SIGNED) || encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED))) {
            stream = convertToPCM(sourceStream, sourceFormat);
            sourceStream.close();
            sourceStream = AudioSystem.getAudioInputStream(stream);
        }
        float sourceRate = sourceFormat.getSampleRate();
        AudioInputStream ais = null;
        if (sourceRate != 8000f) {
            AudioFileFormat.Type targetFileType = sourceFileFormat.getType();
            AudioFormat targetFormat = new AudioFormat(sourceFormat.getEncoding(), 8000f, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), sourceFormat.getFrameSize(), 8000f, sourceFormat.isBigEndian());
            System.out.println("desired target format: " + targetFormat);
            AudioInputStream targetStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
            System.out.println("targetStream: " + targetStream);
            int nWrittenBytes = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            nWrittenBytes = AudioSystem.write(targetStream, targetFileType, bos);
            System.out.println("Written bytes: " + nWrittenBytes);
            stream = new ByteArrayInputStream(bos.toByteArray());
            ais = AudioSystem.getAudioInputStream(stream);
            sourceFormat = ais.getFormat();
        } else {
            ais = sourceStream;
        }
        AudioInputStream gsmAIS = AudioSystem.getAudioInputStream(targetEncoding, ais);
        int nWrittenFrames = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        nWrittenFrames = AudioSystem.write(gsmAIS, fileType, bos);
        return new ByteArrayInputStream(bos.toByteArray());
    }

    private static InputStream convertToPCM(AudioInputStream sourceStream, AudioFormat sourceFormat) throws IOException {
        AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000f, 16, 1, sourceFormat.getFrameSize(), 8000f, sourceFormat.isBigEndian());
        AudioInputStream targetStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
        int nWrittenBytes = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        nWrittenBytes = AudioSystem.write(targetStream, AudioFileFormat.Type.WAVE, bos);
        System.out.println("Written bytes: " + nWrittenBytes);
        return new ByteArrayInputStream(bos.toByteArray());
    }

    /**
   * @param args
   */
    public static void main(String[] args) {
        try {
            File inFile = new File(args[0]);
            File gsmFile = new File(args[1]);
            System.out.println("Writing to " + gsmFile);
            FileInputStream fis = new FileInputStream(inFile);
            InputStream decorated = new BufferedInputStream(fis);
            InputStream in = convert(decorated);
            FileOutputStream out = new FileOutputStream(gsmFile);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

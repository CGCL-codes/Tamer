package org.heartstorming.bada.media;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.BitstreamException;
import java.io.*;

public class Mp3 {

    Bitstream bitstream;

    Header header;

    File file;

    String filePath;

    String fileName;

    long fileLength;

    int version;

    int layer;

    int SFIndex;

    int mode;

    int channels;

    int frameSize;

    int frequency;

    float frameRate;

    int totalFrames;

    long totalMS;

    private Mp3(String pathname) throws FileNotFoundException, BitstreamException {
        filePath = pathname;
        file = new File(pathname);
        fileName = file.getName();
        fileLength = file.length();
        bitstream = new Bitstream(new BufferedInputStream(new FileInputStream(file)));
        header = bitstream.readFrame();
        version = header.version();
        layer = header.layer();
        SFIndex = header.sample_frequency();
        mode = header.mode();
        channels = mode == 3 ? 1 : 2;
        frameSize = header.calculate_framesize();
        if (frameSize < 0) throw new RuntimeException("Invalid frameSize : " + frameSize);
        frequency = header.frequency();
        frameRate = (float) ((1.0 / (header.ms_per_frame())) * 1000.0);
        if (frameRate < 0) throw new RuntimeException("Invalid frameRate : " + frameRate);
        totalFrames = header.max_number_of_frames((int) fileLength);
        totalMS = Math.round(header.total_ms((int) fileLength));
    }

    public static Mp3 getInstance(String pathname) {
        Mp3 mp3 = null;
        try {
            mp3 = new Mp3(pathname);
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException(fnfe.getMessage());
        } catch (BitstreamException bse) {
            throw new RuntimeException(bse.getMessage());
        }
        return mp3;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("bada.media.Mp3[");
        sb.append("name=" + fileName).append("|");
        sb.append("length=" + fileLength).append("|");
        sb.append("version=" + version).append("|");
        sb.append("layer=" + layer).append("|");
        sb.append("SFIndex=" + SFIndex).append("|");
        sb.append("mode=" + mode).append("|");
        sb.append("channels=" + channels).append("|");
        sb.append("frameSize=" + frameSize).append("|");
        sb.append("frequency=" + frequency).append("|");
        sb.append("frameRate=" + frameRate).append("|");
        sb.append("totalFrames=" + totalFrames).append("|");
        sb.append("totalMS=" + totalMS).append("|");
        sb.append("]");
        return sb.toString();
    }

    public int ms_2_frame(long currentMS) {
        if (totalMS == 0) return -1;
        long frame = (long) currentMS * totalFrames / totalMS;
        return (int) frame;
    }

    public String getName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public long getTotalMS() {
        return totalMS;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public long getLength() {
        return fileLength;
    }

    public String getFilePath() {
        return filePath;
    }
}

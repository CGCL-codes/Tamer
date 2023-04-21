package net.sourceforge.recman.backend.thumbs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sourceforge.recman.backend.Config;
import net.sourceforge.recman.backend.format.RecordingFormat;
import net.sourceforge.recman.backend.manager.pojo.Recording;
import net.sourceforge.recman.backend.thumbs.exception.ThumbnailException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.videolan.jvlc.JVLC;
import org.videolan.jvlc.MediaDescriptor;
import org.videolan.jvlc.MediaPlayer;
import org.videolan.jvlc.Video;

/**
 * VLCThumbnailManagerImpl
 * 
 * @author Marcus Kessel
 * 
 */
public class VLCThumbnailManagerImpl implements ThumbnailManager {

    private JVLC jvlc;

    private String thumbnailLocation;

    private int width;

    private int height;

    private int picsPerRecording;

    /**
     * Constructor
     * 
     * @throws IOException
     */
    public VLCThumbnailManagerImpl() throws IOException {
        this.setThumbnailLocation(Config.get().getThumbnailDirectory());
        this.picsPerRecording = Config.get().getThumbnailAmount();
        this.width = Config.get().getThumbnailWidth();
        this.height = Config.get().getThumbnailHeight();
        String[] vlcParameter = new String[] { "--no-audio", "--vout=dummy", "--no-snapshot-preview", "--no-overlay", "--crop=1:1" };
        jvlc = new JVLC(vlcParameter);
    }

    public void setThumbnailLocation(String thumbnailLocation) throws IOException {
        File dir = new File(thumbnailLocation);
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!dir.exists() || !dir.canWrite()) {
            throw new IOException("Cannot create/write given thumbnail directory: " + thumbnailLocation);
        }
        this.thumbnailLocation = thumbnailLocation;
    }

    /**
     * Major parts of this method come from here:
     * http://www.neophob.com/serendipity
     * /index.php?/archives/166-jVLC-Fun-with-VLC-4-Java.html
     * 
     * @param media
     * @param thumbnailDir
     * @param picsPerMovie
     * @param begin
     * @return
     * @throws ThumbnailException
     */
    public List<File> create(File media, File thumbnailDir, float picsPerMovie, int begin) throws ThumbnailException {
        MediaDescriptor mediaDescriptor = new MediaDescriptor(jvlc, media.getAbsolutePath());
        MediaPlayer mp = mediaDescriptor.getMediaPlayer();
        mp.play();
        int retry = 0;
        while (!mp.isPlaying()) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new ThumbnailException(e);
            }
            retry++;
            if (retry > 12) {
                throw new ThumbnailException("Cannot create thumbnail from rec: " + media.getAbsolutePath());
            }
        }
        mp.pause();
        List<File> thumbs = new ArrayList<File>();
        Video vid = new Video(jvlc);
        float timeStep = 1f / picsPerMovie;
        for (int n = 0; n < picsPerMovie; n++) {
            mp.setPosition(n * timeStep);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new ThumbnailException(e);
            }
            mp.pause();
            while (mp.getPosition() != n * timeStep) {
                mp.setPosition(n * timeStep);
                mp.pause();
            }
            File thumb = new File(thumbnailDir, (begin + n + 1) + ".png");
            vid.getSnapshot(mp, thumb.getAbsolutePath(), width, height);
            if (!thumb.exists()) {
            }
            thumbs.add(thumb);
        }
        mp.stop();
        mediaDescriptor.release();
        return thumbs;
    }

    @Override
    public List<File> create(Recording recording, boolean forceNew) throws ThumbnailException {
        File thumbnailDir = new File(thumbnailLocation, recording.getId());
        if (!thumbnailDir.exists()) {
            thumbnailDir.mkdirs();
        } else {
            if (!forceNew) {
                return Collections.emptyList();
            } else {
                File[] files = thumbnailDir.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        file.delete();
                    }
                }
            }
        }
        File[] parts = new File[recording.getParts()];
        float sumLength = 0;
        for (int i = 0; i < recording.getParts(); i++) {
            parts[i] = RecordingFormat.getAbsolutePath(recording.getPath(), (i + 1), recording.isTs());
            sumLength += parts[i].length();
        }
        int[] picsPerParts = new int[parts.length];
        int sumPics = 0;
        for (int i = 0; i < parts.length; i++) {
            picsPerParts[i] = (int) ((parts[i].length() / sumLength) * this.picsPerRecording);
            sumPics += picsPerParts[i];
        }
        int rest = this.picsPerRecording - sumPics;
        if (rest > 0) {
            picsPerParts[picsPerParts.length - 1] += rest;
        }
        List<File> thumbs = new ArrayList<File>();
        int begin = 0;
        for (int i = 0; i < parts.length; i++) {
            thumbs.addAll(this.create(parts[i], thumbnailDir, picsPerParts[i], begin));
            begin += picsPerParts[i];
        }
        return thumbs;
    }

    @Override
    public void remove(Recording recording) throws ThumbnailException {
        File thumbnailDir = new File(thumbnailLocation, recording.getId());
        File[] files = thumbnailDir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Override
    public byte[] takeShot(Recording recording, long seconds, int width, int height) throws ThumbnailException {
        return null;
    }

    @Override
    public boolean hasThumbnails(Recording recording) throws ThumbnailException {
        File path = new File(this.thumbnailLocation, recording.getId());
        if (path.exists() && path.isDirectory()) {
            if (path.listFiles().length >= this.picsPerRecording) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte[] getThumbnail(String recordingId, int number) throws ThumbnailException {
        File thumbnail = new File(this.thumbnailLocation, recordingId + "/" + number + ".png");
        if (!thumbnail.exists() || !thumbnail.canRead()) {
            throw new ThumbnailException("No readable thumbnail found at: " + thumbnail.getAbsolutePath());
        }
        FileInputStream input = null;
        try {
            input = new FileInputStream(thumbnail);
            return IOUtils.toByteArray(input);
        } catch (FileNotFoundException e) {
            throw new ThumbnailException(e);
        } catch (IOException e) {
            throw new ThumbnailException(e);
        }
    }

    @Override
    public int countThumbnails(String recordingId) throws ThumbnailException {
        File path = new File(this.thumbnailLocation, recordingId);
        return FileUtils.listFiles(path, new String[] { "png" }, false).size();
    }
}

package ch.idsia.benchmark.mario.engine;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.ReplayerOptions;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, firstName_at_idsia_dot_ch
 * Date: May 5, 2009
 * Time: 9:34:33 PM
 * Package: ch.idsia.utils
 */
public class Replayer {

    private ZipFile zf = null;

    private ZipEntry ze = null;

    private BufferedInputStream fis;

    private ReplayerOptions options;

    public Replayer(String replayOptions) {
        this.options = new ReplayerOptions(replayOptions);
    }

    public boolean openNextReplayFile() throws IOException {
        String fileName = options.getNextReplayFile();
        if (fileName == null) return false;
        if (!fileName.endsWith(".zip")) fileName += ".zip";
        zf = new ZipFile(fileName);
        ze = null;
        fis = null;
        try {
            openFile("chunks");
            options.setChunks((Queue) readObject());
        } catch (Exception ignored) {
        }
        return true;
    }

    public void openFile(String filename) throws Exception {
        ze = zf.getEntry(filename);
        if (ze == null) throw new Exception("[Mario AI EXCEPTION] : File <" + filename + "> not found in the archive");
    }

    private void openBufferedInputStream() throws IOException {
        fis = new BufferedInputStream(zf.getInputStream(ze));
    }

    public boolean[] readAction() throws IOException {
        if (fis == null) openBufferedInputStream();
        boolean[] buffer = new boolean[Environment.numberOfKeys];
        byte actions = (byte) fis.read();
        for (int i = 0; i < Environment.numberOfKeys; i++) {
            if ((actions & (1 << i)) > 0) buffer[i] = true; else buffer[i] = false;
        }
        if (actions == -1) buffer = null;
        return buffer;
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(zf.getInputStream(ze));
        Object res = ois.readObject();
        return res;
    }

    public void closeFile() throws IOException {
    }

    public void closeReplayFile() throws IOException {
        zf.close();
    }

    public boolean hasMoreChunks() {
        return options.hasMoreChunks();
    }

    public int actionsFileSize() {
        int size = (int) ze.getSize();
        if (size == -1) size = Integer.MAX_VALUE;
        return size;
    }

    public ReplayerOptions.Interval getNextIntervalInMarioseconds() {
        return options.getNextIntervalInMarioseconds();
    }

    public ReplayerOptions.Interval getNextIntervalInTicks() {
        return options.getNextIntervalInTicks();
    }
}

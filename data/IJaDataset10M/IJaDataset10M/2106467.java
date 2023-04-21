package net.sf.jaer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.jaer.aemonitor.AEPacketRaw;
import net.sf.jaer.eventio.AEDataFile;
import net.sf.jaer.eventio.AEFileInputStream;
import net.sf.jaer.eventio.AEFileInputStreamInterface;
import net.sf.jaer.graphics.AEViewer;
import net.sf.jaer.graphics.AbstractAEPlayer;
import net.sf.jaer.util.IndexFileFilter;
import net.sf.jaer.util.SubclassFinder;

/**
 * Synchronized playback and control of such playback is not
 * totally straightforward because of the bursty nature of
 * AER - there are no frames to synchronize
 * on and you obviously cannot sync on event number.
 * <p>
 * This class synchronizes multiple viewer players.
 * It assumes one is the master (whichever the user controls)
 * and coordinates viewers synchronously
 * so that all viewers can present a consistent view.
 * <p>
 * To achieve this, each viewer encapsulates its playback functionality on an AEPlayer
 *  class instance that is controlled either by the Viewer GUI (the user)
 * or by JAERViewer through its own SyncPlayer.
 * <p>
 * There is a single SyncPlayer and multiple AEViwer.AEPlayers, one for each viewer.
 * The user opens an index file to play back multiple files. The files each play in their own AEViewer.
 * <p>
 *
 * The Players share a common interface so this is achieved by
 * returning the correct object within AEViewer depending on
 * whether the views are synchronized.
 *
 * <p>
 * The individual threads doing the rendering in each AEViewer are
 * barricaded by the CyclicBarrier here. Each time an AEViewer asks
 * for synchronized events using getNextPacket, the call
 * here to SyncPlayer blocks until all threads asking for events have
 * gotten them. Then rendering in each thread happens normally.
 */
public class SyncPlayer extends AbstractAEPlayer implements PropertyChangeListener {

    JAERViewer outer;

    JFileChooser fileChooser;

    int currentTime = 0;

    File lastFile;

    volatile CyclicBarrier barrier;

    int numPlayers = 0;

    private ArrayList<AEViewer> playingViewers = new ArrayList<AEViewer>();

    static Preferences prefs = Preferences.userNodeForPackage(SyncPlayer.class);

    public SyncPlayer(AEViewer viewer, JAERViewer outer) {
        super(viewer);
        this.outer = outer;
    }

    public boolean isChoosingFile() {
        return fileChooser != null && fileChooser.isVisible();
    }

    /** this call shows a file chooser for index files: files containing
     * information on which AE data files go together. This method is only called when an index file is opened.
     */
    public void openAEInputFileDialog() {
        fileChooser = new JFileChooser();
        IndexFileFilter filter = new IndexFileFilter();
        String lastFilePath = prefs.get("JAERViewer.lastFile", "");
        lastFile = new File(lastFilePath);
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(lastFile);
        setPaused(true);
        int retValue = fileChooser.showOpenDialog(null);
        if (retValue == JFileChooser.APPROVE_OPTION) {
            try {
                lastFile = fileChooser.getSelectedFile();
                startPlayback(lastFile);
                lastFilePath = lastFile.getPath();
                prefs.put("JAERViewer.lastFile", lastFilePath);
            } catch (IOException fnf) {
                fnf.printStackTrace();
            }
        }
        fileChooser = null;
        setPaused(false);
    }

    /** @return a simple class name (no package header) parsed from a .dat filename as the part before the "-"
     */
    String parseClassnameFromFilename(String filename) {
        StringBuilder className = new StringBuilder();
        int i = 0;
        while (i < filename.length() && filename.charAt(i) != '-') {
            className.append(filename.charAt(i));
            i++;
        }
        log.info("filename " + filename + " parses to chip class name " + className.toString());
        return className.toString();
    }

    synchronized void makeBarrier() {
        if (numPlayers < 1) {
            log.warning("cannot make barrier for " + numPlayers + " viewers - something is wrong");
            log.warning("disabling sychronized playback because probably multiple viewers are active but we are not playing set of sychronized files");
            outer.getToggleSyncEnabledAction().actionPerformed(null);
            return;
        }
        log.info("making barrier for " + this);
        barrier = new CyclicBarrier(numPlayers, new Runnable() {

            public void run() {
                barrier.reset();
                setTime(getTime() + getTimesliceUs());
            }
        });
    }

    /** this call starts playback on the supplied index file, starting playback in each viewer appropriately.
    If the file is not an index file, then the first available viewer is called to start playback of the data file.
     * @param indexFile the .index file containing the filenames to play
     */
    @Override
    public void startPlayback(File indexFile) throws IOException {
        super.startPlayback(indexFile);
        log.info("Starting synchronized playback of files in indexFile=" + indexFile);
        stopPlayback();
        if (!indexFile.getName().endsWith(AEDataFile.INDEX_FILE_EXTENSION) && !indexFile.getName().endsWith(AEDataFile.OLD_INDEX_FILE_EXTENSION)) {
            log.info(indexFile + " doesn\'t appear to be an index file pointing to a set of data files because it does't end with the correct extension (.aeidx or .index), opening it in the first viewer and setting sync enabled false");
            AEViewer v = outer.getViewers().get(0);
            if (outer.isSyncEnabled()) {
                JOptionPane.showMessageDialog(v, "<html>You are opening a single data file so synchronization has been disabled<br>To reenable, use File/Synchronization enabled</html>");
                outer.getToggleSyncEnabledAction().actionPerformed(null);
            }
            v.aePlayer.startPlayback(indexFile);
            return;
        }
        getPlayingViewers().clear();
        HashMap<File, AEViewer> map = new HashMap<File, AEViewer>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(indexFile));
            String filename;
            ArrayList<AEViewer> dontUseAgain = new ArrayList<AEViewer>();
            while ((filename = reader.readLine()) != null) {
                File file = new File(indexFile.getParentFile(), filename);
                if (!file.isFile()) {
                    JOptionPane.showMessageDialog(null, file + " from index file doesn\'t exist", "Missing data file", JOptionPane.WARNING_MESSAGE);
                    reader.close();
                    return;
                }
                String className = parseClassnameFromFilename(filename);
                AEViewer vToUse = null;
                for (AEViewer v : outer.getViewers()) {
                    if (!dontUseAgain.contains(v) && v.getAeChipClass().getSimpleName().equals(className)) {
                        vToUse = v;
                        dontUseAgain.add(v);
                        break;
                    }
                }
                if (vToUse == null) {
                    log.info("no AEViewer found for " + filename + ", making new one");
                    vToUse = new AEViewer(outer);
                    dontUseAgain.add(vToUse);
                    vToUse.setVisible(true);
                }
                map.put(file, vToUse);
                log.info("mapped " + file + " to viewer " + vToUse);
            }
            if (reader != null) {
                reader.close();
            }
            numPlayers = map.size();
            log.info(Thread.currentThread() + " constructing barrier for " + numPlayers + " synchronized players");
            makeBarrier();
            for (Entry<File, AEViewer> e : map.entrySet()) {
                AEViewer v = e.getValue();
                File f = e.getKey();
                log.info("Starting playback of File " + f + " in viewer " + v.getTitle());
                String className = parseClassnameFromFilename(f.getName());
                Class chipClass = getChipClassFromSimpleName(className);
                v.setAeChipClass(chipClass);
                v.aePlayer.stopPlayback();
                v.aePlayer.startPlayback(e.getKey());
                v.aePlayer.getAEInputStream().getSupport().addPropertyChangeListener("rewind", this);
                getPlayingViewers().add(v);
            }
            initTime();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outer.setPlayBack(true);
    }

    /** stops playback on all players */
    public void stopPlayback() {
        log.info(Thread.currentThread() + " stopping playback");
        for (AEViewer v : outer.getViewers()) {
            v.aePlayer.stopPlayback();
        }
        outer.setPlayBack(false);
    }

    void initTime() {
        int minTime = Integer.MAX_VALUE;
        for (AEViewer v : outer.getViewers()) {
            try {
                int t = v.aePlayer.getAEInputStream().getFirstTimestamp();
                if (t < minTime) {
                    minTime = t;
                }
            } catch (NullPointerException e) {
                log.warning("NullPointerException when initializing time for viewer " + v);
            }
        }
        log.info("JAERViewer.SyncPlayer.initialized time min value found: " + minTime);
        setTime(minTime);
    }

    /** rewinds all players */
    public void rewind() {
        for (AEViewer v : outer.getViewers()) {
            v.aePlayer.rewind();
        }
        initTime();
    }

    /** pauses all players */
    @Override
    public void pause() {
        setPaused(true);
    }

    /** resumes all players */
    @Override
    public void resume() {
        setPaused(false);
    }

    static final int SYNC_PLAYER_TIMEOUT_SEC = 3;

    /** Returns next packet of AE data to the caller, which is a
     * particular AEPlayer owned by an AEViewer.  getNextPacket is called via the ViewLoop run() loop thread of that AEViewer.
     * The packet is synchronized in event time if synchronized playback is enabled.
     * @return a raw packet of events
     */
    public AEPacketRaw getNextPacket(AbstractAEPlayer player) {
        int[] currentTimes = new int[getPlayingViewers().size()];
        int i = 0;
        try {
            for (AEViewer v : getPlayingViewers()) {
                currentTimes[i++] = v.aePlayer.getTime();
            }
        } catch (ConcurrentModificationException e) {
            log.warning("caught " + e.toString() + " when finding current packet times from all viewers");
        }
        int maxtime = Integer.MIN_VALUE;
        for (int t : currentTimes) {
            if (t > maxtime) {
                maxtime = t;
            }
        }
        setTime(maxtime);
        AEPacketRaw ae = player.getNextPacket(player);
        if (numPlayers == 1) {
            return ae;
        }
        try {
            if (barrier == null) {
                makeBarrier();
            }
            if (barrier == null) {
                return null;
            }
            int awaitVal = barrier.await();
        } catch (InterruptedException e) {
            log.warning(Thread.currentThread() + " interrupted");
        } catch (BrokenBarrierException ignore) {
        }
        return ae;
    }

    public float getFractionalPosition() {
        throw new UnsupportedOperationException();
    }

    public void mark() throws IOException {
        for (AEViewer v : outer.getViewers()) {
            v.aePlayer.mark();
        }
    }

    public void unmark() {
        for (AEViewer v : outer.getViewers()) {
            v.aePlayer.unmark();
        }
    }

    public boolean isMarkSet() {
        for (AEViewer v : outer.getViewers()) {
            if (!v.aePlayer.isMarkSet()) {
                return false;
            }
        }
        return true;
    }

    public long position(AEFileInputStreamInterface stream) {
        return stream.position();
    }

    public int position() {
        throw new UnsupportedOperationException();
    }

    public void position(int event, AEFileInputStreamInterface stream) {
        stream.position(event);
    }

    public AEPacketRaw readPacketByNumber(int n) throws IOException {
        throw new UnsupportedOperationException();
    }

    public AEPacketRaw readPacketByNumber(int n, AEFileInputStreamInterface stream) throws IOException {
        return stream.readPacketByNumber(n);
    }

    public AEPacketRaw readPacketByTime(int dt) throws IOException {
        throw new UnsupportedOperationException();
    }

    public AEPacketRaw readPacketByTime(int dt, AEFileInputStreamInterface stream) throws IOException {
        return stream.readPacketByTime(dt);
    }

    public long size(AEFileInputStream stream) {
        return stream.size();
    }

    public long size() {
        throw new UnsupportedOperationException();
    }

    public void setFractionalPosition(float frac) {
        for (AEViewer v : outer.getViewers()) {
            v.aePlayer.setFractionalPosition(frac);
        }
    }

    /** Sets all viewers to the same time.
     * @param time current playback time relative to start in us */
    public void setTime(int time) {
        currentTime = time;
        try {
            for (AEViewer v : getPlayingViewers()) {
                v.aePlayer.setTime(getTime());
            }
        } catch (ConcurrentModificationException e) {
            log.warning("couldn\'t set time on a viewer because of exception " + e.getMessage());
        }
    }

    /** @return current playback time relative to start in us */
    public int getTime() {
        return currentTime;
    }

    /** will throw UnsupportedOperationException since the correct call is to getNextPacket(player). */
    public AEPacketRaw getNextPacket() {
        throw new UnsupportedOperationException();
    }

    /** always returns null,  bince this is a sync player for multiple viewers */
    public AEFileInputStream getAEInputStream() {
        return null;
    }

    /** JAERViewer gets PropertyChangeEvent from the AEPlayer in the AEViewers. This method presently only logs this event.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("rewind")) {
            for (AEViewer v : outer.getViewers()) {
                v.getChip().getRenderer().resetFrame(v.getChip().getRenderer().getGrayValue());
            }
            log.info("rewind PropertyChangeEvent received by " + this + " from " + evt.getSource());
        }
    }

    public void doSingleStep() {
        setPaused(true);
        for (AEViewer v : outer.getViewers()) {
            v.doSingleStep();
        }
    }

    public void adjustTimesliceForRealtimePlayback() {
    }

    @Override
    public void setDoSingleStepEnabled(boolean b) {
    }

    /**
     * Returns the list of viewers involved in this playback.
     * @return the playingViewers
     */
    public ArrayList<AEViewer> getPlayingViewers() {
        return playingViewers;
    }

    private static List<String> chipClassNames;

    /** Returns AEChip class from simple name. If chip classes have not
     * yet been cached, waits until they exist.
     *
     * @param className, e.g. DVS128.
     * @return class for AEChip.
     */
    private Class getChipClassFromSimpleName(String className) {
        Class deviceClass = null;
        if (getChipClassNames() == null) {
            cacheChipClassNames();
        }
        for (String s : chipClassNames) {
            int ind = s.lastIndexOf('.');
            String s2 = s.substring(ind + 1);
            if (s2.equals(className)) {
                try {
                    deviceClass = Class.forName(s);
                    log.info("found class " + deviceClass + " for className " + className);
                    break;
                } catch (ClassNotFoundException e) {
                    log.warning(e.getMessage());
                }
            }
        }
        if (deviceClass == null) {
            log.warning("no chip class for chip className=" + className);
        }
        return deviceClass;
    }

    private void cacheChipClassNames() {
        chipClassNames = SubclassFinder.findSubclassesOf(net.sf.jaer.chip.AEChip.class.getName());
    }

    public static List<String> getChipClassNames() {
        return chipClassNames;
    }
}

package pgbennett.jampal;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import pgbennett.speech.*;
import java.util.regex.*;
import pgbennett.id3.*;
import java.lang.reflect.*;
import javax.sound.sampled.spi.*;

public class AudioPlayer implements Runnable {

    MainFrame mainFrame;

    Thread thread;

    MP3InputStream mp3Stream;

    volatile char state;

    UpdateSlider updateSlider = new UpdateSlider();

    NextSong nextSong = new NextSong();

    int selectedRow;

    int exceptionCount;

    int startPosition;

    /** Playing time in seconds */
    long playingTime;

    long startTime = 0;

    long playedMicros = -1;

    long playedTime = 0;

    long playedMicrosAtStart;

    String playingTimeDisplay;

    SourceDataLine line;

    long prevSeconds;

    Vector speechPatterns;

    Vector speechReplace;

    String speechTemplate;

    Announce announce = null;

    LineDrain lineDrain;

    Vector titleVec = new Vector();

    StringBuffer fullAnnouncement = new StringBuffer();

    String announcementLanguage;

    boolean useSameLine = false;

    AudioPlayer(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        state = 'S';
        int ix;
        Properties props = mainFrame.library.attributes.libraryProperties;
        speechTemplate = props.getProperty("speechtemplate");
        speechPatterns = new Vector();
        speechReplace = new Vector();
        for (ix = 1; ; ix++) {
            String pattern = props.getProperty("speech" + ix + "a");
            if (pattern == null) break; else {
                speechPatterns.add(Pattern.compile(pattern));
                String replace = props.getProperty("speech" + ix + "b");
                if (replace == null) replace = "";
                speechReplace.add(replace);
            }
        }
    }

    void play() {
        String fileName;
        playedMicros = -1;
        useSameLine = false;
        updateSlider.adjusting = false;
        updateSlider.prevPosition = -1;
        if (state != 'E') exceptionCount = 0;
        try {
            if (thread != null) {
                if (thread.isAlive()) {
                    if (state == 'P' || state == 'Q') state = 'S';
                    thread.join(30000);
                    if (thread.isAlive()) throw new Exception("The Song that is Playing will not end.");
                    if (state == 'S') mainFrame.playSlider.setValue(0);
                }
            }
            if (state == 'A' || state == 'M') {
                startPosition = mainFrame.playSlider.getValue();
                mp3Stream.setupFile(startPosition);
                useSameLine = true;
            } else if (state == 'E') {
                startPosition = mp3Stream.setupFile(-1);
                useSameLine = true;
            } else {
                exceptionCount = 0;
                fileName = null;
                LibraryTrack track = null;
                selectedRow = mainFrame.mainTable.getSelectedRow();
                if (selectedRow < 0) selectedRow = mainFrame.library.trackVec.indexOf(mainFrame.trackLastPlaying);
                if (selectedRow < 0) selectedRow = 0;
                if (mainFrame.library.attributes.libraryType == 'P') {
                    mainFrame.editorUpdateCount = 2;
                    mainFrame.timer.addActionListener(mainFrame.editorUpdateActionListener);
                    mainFrame.editorUpdateActionListening = true;
                    mainFrame.mainTable.clearSelection();
                    mainFrame.mainTable.addRowSelectionInterval(selectedRow, selectedRow);
                }
                track = validateSongFile(selectedRow, true);
                fileName = track.columns[mainFrame.library.attributes.fileNameCol];
                int ix = mainFrame.library.trackVec.indexOf(mainFrame.trackLastPlaying);
                if (ix >= 0) mainFrame.model.fireTableRowsUpdated(ix, ix);
                mainFrame.trackLastPlaying = track;
                ix = mainFrame.library.trackVec.indexOf(track);
                if (ix >= 0) mainFrame.model.fireTableRowsUpdated(ix, ix);
                mp3Stream = new MP3InputStream(fileName);
                startPosition = mainFrame.playSlider.getValue();
                String title = fileName;
                String artist = "File Read Error";
                try {
                    mp3Stream.setupFile(startPosition);
                    title = mp3Stream.mp3.getMp3Field(MP3File.COL_TITLE, null);
                    artist = mp3Stream.mp3.getMp3Field(MP3File.COL_ARTIST, null);
                    if (mainFrame.parentFrame == null || mainFrame.library.attributes.libraryType != 'P' || mainFrame.library.readOnly) gettitleVec(mp3Stream.mp3, true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ErrorMessage msg = new ErrorMessage();
                    msg.errorMessage = ex.toString();
                    javax.swing.SwingUtilities.invokeLater(msg);
                    mp3Stream.mp3 = null;
                    if (++errorDialogCount >= 5) {
                        stop();
                        return;
                    }
                }
                mainFrame.playLabel.setText(title + " - " + artist);
                mainFrame.frame.setTitle(title + " - " + artist + " - " + mainFrame.shortName + " - jampal");
                if (mainFrame.announceMenuItem.isSelected()) state = 'Q';
            }
            thread = new Thread(this, "audioplayer");
            if (state != 'Q') state = 'P';
            thread.setPriority(Thread.currentThread().getPriority() + 1);
            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            state = 'S';
            JOptionPane.showMessageDialog(mainFrame.frame, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static final int[] titleSearchCols = { MP3File.COL_TITLE, MP3File.COL_ARTIST, MP3File.COL_ALBUM };

    static final int[] albumSearchCols = { MP3File.COL_ALBUM, MP3File.COL_TRACK };

    LibraryTrack validateSongFile(int row, boolean doFollow) throws Exception {
        String fileName;
        selectedRow = row;
        LibraryTrack track = (LibraryTrack) mainFrame.library.trackVec.get(selectedRow);
        fileName = track.columns[mainFrame.library.attributes.fileNameCol];
        if (mainFrame.parentFrame != null && !mainFrame.library.readOnly) {
            boolean follow = false;
            boolean first = true;
            boolean updated = false;
            boolean fileReplace = false;
            String nextFileName = fileName;
            String trackNum = null;
            while (follow || first) {
                String foundFileName = null;
                fileReplace = false;
                if (first) {
                    File file = new File(fileName);
                    if (!file.isFile()) {
                        int titleCol = mainFrame.library.attributes.findColumnNum(MP3File.COL_TITLE, null);
                        int artistCol = mainFrame.library.attributes.findColumnNum(MP3File.COL_ARTIST, null);
                        int albumCol = mainFrame.library.attributes.findColumnNum(MP3File.COL_ALBUM, null);
                        String title = track.columns[titleCol];
                        String artist = track.columns[artistCol];
                        String album = track.columns[albumCol];
                        String[] searchValues = { title, artist, album };
                        foundFileName = mainFrame.parentFrame.model.findTrackByValues(searchValues, titleSearchCols, false, 0x770);
                        fileReplace = true;
                    }
                }
                if (follow) {
                    int albumCol = mainFrame.library.attributes.findColumnNum(MP3File.COL_ALBUM, null);
                    int trackCol = mainFrame.library.attributes.findColumnNum(MP3File.COL_TRACK, null);
                    String album = track.columns[albumCol];
                    if (trackNum == null) trackNum = track.columns[trackCol];
                    int x = Integer.parseInt(trackNum);
                    trackNum = String.valueOf(x + 1);
                    if (trackNum.length() == 1) trackNum = "0" + trackNum;
                    String[] searchValues = { album, trackNum };
                    foundFileName = mainFrame.model.findTrackByValues(searchValues, albumSearchCols, true, 0x44);
                    if (foundFileName == null) foundFileName = mainFrame.parentFrame.model.findTrackByValues(searchValues, albumSearchCols, true, 0x44);
                }
                if (foundFileName != null) {
                    File file = new File(foundFileName);
                    if (file.isFile()) {
                        mainFrame.library.insertBefore = selectedRow + 1;
                        mainFrame.library.firstInserted = -1;
                        mainFrame.library.numberInserted = 0;
                        mainFrame.library.selectedRows = null;
                        LibraryTrack nextTrack = null;
                        if (fileReplace) {
                            mainFrame.library.updateTrackFileName(track, file);
                            nextTrack = mainFrame.library.trackVec.get(selectedRow);
                        } else {
                            LibraryTrack newTrack = new LibraryTrack();
                            newTrack.init(file, mainFrame.library.attributes, track.alternateNum);
                            String follFileName = null;
                            if (selectedRow + 1 < mainFrame.library.trackVec.size()) {
                                nextTrack = mainFrame.library.trackVec.get(selectedRow + 1);
                                follFileName = nextTrack.columns[mainFrame.library.attributes.fileNameCol];
                            }
                            if (!foundFileName.equals(follFileName)) {
                                mainFrame.library.addEntry(newTrack, null);
                                nextTrack = newTrack;
                            }
                        }
                        updated = true;
                        foundFileName = mainFrame.library.attributes.normalizeFileName(file);
                        selectedRow = mainFrame.library.trackVec.indexOf(nextTrack);
                        if (fileReplace) {
                            mainFrame.library.copyDummyCols(track, nextTrack);
                            track = nextTrack;
                        }
                        nextFileName = foundFileName;
                    }
                }
                if (mainFrame.library.attributes.libraryType == 'P' && doFollow) {
                    try {
                        if (nextFileName != null) {
                            File file = new File(nextFileName);
                            MP3File mp3 = new MP3File();
                            mp3.init(file, MP3File.BOTH_TAGS);
                            String data = mp3.getMp3Field(MP3File.COL_ID3V2TAG, "TXXXjampal");
                            follow = (data.toLowerCase().indexOf('f') != -1);
                            if (gettitleVec(mp3, nextFileName.equalsIgnoreCase(fileName))) follow = false;
                        } else follow = false;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        follow = false;
                    }
                }
                first = false;
                nextFileName = null;
            }
            if (updated) {
                mainFrame.library.insertBefore = -1;
                selectedRow = mainFrame.library.trackVec.indexOf(track);
                if (SwingUtilities.isEventDispatchThread()) {
                    mainFrame.model.resetSort(false);
                    mainFrame.model.fireTableDataChanged();
                    mainFrame.mainTable.addRowSelectionInterval(selectedRow, selectedRow);
                }
            }
        }
        return track;
    }

    /**
     * Store titles in a vector and keep track of "follow" requests
     * Called at the start of each song. If there is a string of follows
     * 3 or four, returning true here suppresses searching a second or 
     * third time for the following song.
     * @return true if this track was already stored in the vector before
     */
    boolean gettitleVec(MP3File mp3, boolean playFirst) throws Exception {
        announcementLanguage = mp3.getMp3Field(MP3File.COL_ID3V2TAG, "TLAN");
        int leng = announcementLanguage.length();
        if (leng > 3) announcementLanguage = announcementLanguage.substring(leng - 3, leng);
        StringBuffer announcementBuf = new StringBuffer();
        StringTokenizer tok = new StringTokenizer(speechTemplate, "()", true);
        String fileName = mp3.getFileName();
        fileName = mainFrame.library.attributes.fixFileNameCase(fileName);
        while (tok.hasMoreTokens()) {
            String nextTok = tok.nextToken();
            if ("(".equals(nextTok)) {
                if (tok.hasMoreTokens()) {
                    boolean error = false;
                    String tagfield = tok.nextToken();
                    int fieldType;
                    String extendedId = null;
                    int spacePos = tagfield.indexOf(' ');
                    if (spacePos != -1) {
                        extendedId = tagfield.substring(spacePos + 1);
                        tagfield = tagfield.substring(0, spacePos);
                    }
                    String optionVal = FrameDictionary.getSingleton().frameProperties.getProperty(tagfield);
                    try {
                        fieldType = Integer.parseInt(optionVal);
                    } catch (Exception ex) {
                        fieldType = -1;
                    }
                    FrameAttributes att = null;
                    switch(fieldType) {
                        case MP3File.COL_ID3V2TAG:
                            if (extendedId == null) {
                                error = true;
                                announcementBuf.append(" ERROR - Missing ID3V2 frame at end of options ");
                                break;
                            }
                            if (extendedId.length() >= 4) att = (FrameAttributes) FrameDictionary.getSingleton().frameTypes.get(extendedId.substring(0, 4));
                            if (att == null) {
                                error = true;
                                announcementBuf.append(" ERROR - Invalid ID3V2 frame " + extendedId + " ");
                                break;
                            }
                            int pos = 4;
                            if (att.langReq) {
                                String lang = "";
                                if (extendedId.length() >= pos + 3) {
                                    lang = extendedId.substring(pos, pos + 3);
                                    pos += 3;
                                }
                                if (!lang.matches("[a-z][a-z][a-z]")) {
                                    error = true;
                                    announcementBuf.append(" ERROR - Language code must be 3 lowercase letters " + extendedId + " ");
                                    break;
                                }
                            }
                            if (att.type == 'I') {
                                error = true;
                                announcementBuf.append(" I cannot announce a picture ");
                                break;
                            }
                            if (!att.descReq && att.type != 'I') {
                                if (extendedId.length() > pos) {
                                    error = true;
                                    announcementBuf.append(" ERROR - This ID3V2 frame does not require a description: " + extendedId + " ");
                                    break;
                                }
                            }
                    }
                    if (!error) {
                        String value;
                        try {
                            value = mp3.getMp3Field(fieldType, extendedId);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            if (extendedId == null) extendedId = "";
                            value = "Invalid value " + tagfield + " " + extendedId;
                        }
                        announcementBuf.append(value);
                    }
                }
            } else if (")".equals(nextTok)) {
            } else {
                announcementBuf.append(nextTok);
            }
        }
        String announcement = announcementBuf.toString();
        if (playFirst) {
            if (titleVec.remove(fileName)) {
                useSameLine = true;
                return true;
            }
        } else {
            if (titleVec.contains(fileName)) {
                return true;
            }
        }
        useSameLine = false;
        if (playFirst) titleVec.clear(); else titleVec.add(fileName);
        int ix;
        for (ix = 0; ix < speechPatterns.size(); ix++) {
            Pattern pattern = (Pattern) speechPatterns.get(ix);
            if (pattern == null) break;
            Matcher matcher = pattern.matcher(announcement);
            String replace = "";
            if (speechReplace.size() > ix) replace = (String) speechReplace.get(ix);
            if (replace == null) replace = "";
            announcement = matcher.replaceAll(replace);
        }
        if (fullAnnouncement.length() > 0) fullAnnouncement.append(". ");
        fullAnnouncement.append(announcement);
        return false;
    }

    void pause() {
        state = 'A';
    }

    void stop() {
        if (state != 'P') {
            state = 'S';
            updateSlider.position = 0;
            updateSlider.timeDisplay = "";
            updateSlider.actionPerformed(null);
        }
        state = 'S';
    }

    void next() {
        if (state == 'P') state = 'N'; else {
            nextSong.increment = 1;
            nextSong.run();
        }
    }

    void prev() {
        if (state == 'P') state = 'R'; else {
            nextSong.increment = -1;
            nextSong.run();
        }
    }

    int nSampleSizeInBits = 16;

    String strMixerName = null;

    int nInternalBufferSize = AudioSystem.NOT_SPECIFIED;

    int bufferSecs = 1;

    AudioFileReader pluginAudioFileReader;

    boolean pluginTried;

    public synchronized void run() {
        if (lineDrain == null) System.gc();
        long bytesPerSecond = 44100 * 4;
        if (mp3Stream.mp3 == null) {
            if (mainFrame.library.attributes.libraryType == 'P' && state != 'S') {
                state = 'N';
                nextSong.increment = 1;
                javax.swing.SwingUtilities.invokeLater(nextSong);
            } else state = 'S';
            return;
        }
        Thread waitFor = null;
        if (!mainFrame.continuousMenuItem.isSelected() || useSameLine) {
            waitFor = lineDrain;
            if (waitFor != null) {
                synchronized (lineSync) {
                    waitFor.interrupt();
                }
                try {
                    waitFor.join();
                } catch (InterruptedException ex) {
                }
            }
        }
        announce = null;
        if (state == 'Q') {
            state = 'P';
            if (fullAnnouncement.length() > 0) {
                try {
                    announce = new Announce(fullAnnouncement.toString());
                    announce.start();
                    if (!mainFrame.continuousMenuItem.isSelected() && state == 'P') announce.join();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        fullAnnouncement = new StringBuffer();
        AudioFormat audioFormat = null;
        try {
            if (state != 'P') return;
            if (pluginAudioFileReader == null && !pluginTried) {
                try {
                    pluginTried = true;
                    Class codecClass = Class.forName("com.sun.media.codec.audio.mp3.JS_MP3FileReader");
                    Class[] nullClasses = new Class[0];
                    Constructor constructor = codecClass.getConstructor(nullClasses);
                    Object[] initargs = new Object[0];
                    pluginAudioFileReader = (AudioFileReader) constructor.newInstance(initargs);
                } catch (ClassNotFoundException cnf) {
                } catch (Exception ex) {
                    ex.printStackTrace();
                    pluginAudioFileReader = null;
                }
            }
            AudioInputStream audioInputStream;
            if (pluginAudioFileReader != null) audioInputStream = pluginAudioFileReader.getAudioInputStream(mp3Stream); else audioInputStream = AudioSystem.getAudioInputStream(mp3Stream);
            audioFormat = audioInputStream.getFormat();
            bytesPerSecond = (long) audioFormat.getSampleRate() * audioFormat.getChannels() * (nSampleSizeInBits / 8);
            playingTime = mp3Stream.mp3.getPlayingTime();
            playingTimeDisplay = formatTime(playingTime);
            startTime = playingTime * startPosition / 10000;
            nInternalBufferSize = (int) bytesPerSecond * bufferSecs;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, nInternalBufferSize);
            boolean bIsSupportedDirectly = AudioSystem.isLineSupported(info);
            if (!bIsSupportedDirectly) {
                AudioFormat sourceFormat = audioFormat;
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * (nSampleSizeInBits / 8), sourceFormat.getSampleRate(), false);
                audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                audioFormat = audioInputStream.getFormat();
            }
            line = null;
            waitFor = null;
            synchronized (lineSync) {
                if (lineDrain != null) {
                    if (lineDrain.audioFormat.matches(audioFormat)) {
                        line = lineDrain.line;
                        lineDrain.line = null;
                    } else waitFor = lineDrain;
                }
            }
            if (waitFor != null) {
                synchronized (lineSync) {
                    waitFor.interrupt();
                }
                waitFor.join();
            }
            strMixerName = Jampal.initialProperties.getProperty("mixer-name");
            if (line == null) {
                line = getSourceDataLine(strMixerName, audioFormat, nInternalBufferSize);
            }
            if (!line.isRunning()) {
                line.start();
                FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                String gainstr = Jampal.initialProperties.getProperty("mixer-gain-percent");
                float gain = 0;
                if (gainstr != null) {
                    try {
                        gain = Float.parseFloat(gainstr);
                        gain = (float) 10.0 * (float) Math.log10((double) gain * 0.01);
                    } catch (NumberFormatException ex) {
                        gain = 0;
                        System.err.println("Invalid gain value: " + gainstr + ", set to 0");
                    }
                    if (gain > volumeControl.getMaximum()) {
                        gain = volumeControl.getMaximum();
                        System.err.println("Gain value: " + gainstr + " too high, set to Maximum value: " + volumeControl.getMaximum());
                    }
                    if (gain < volumeControl.getMinimum()) {
                        gain = volumeControl.getMinimum();
                        System.err.println("Gain value: " + gainstr + " too low, set to Minimum value: " + volumeControl.getMinimum());
                    }
                }
                volumeControl.setValue(gain);
            }
            playedMicrosAtStart = line.getMicrosecondPosition();
            int nBytesRead = 0;
            prevSeconds = -1;
            mainFrame.timer.addActionListener(updateSlider);
            byte[] abData;
            abData = new byte[4608];
            byte[] abData2 = new byte[4608];
            int nBytesRead2;
            nBytesRead = audioInputStream.read(abData, 0, abData.length);
            nBytesRead = audioInputStream.read(abData, 0, abData.length);
            while (nBytesRead != -1 && state == 'P') {
                int nBytesWritten = 0;
                nBytesRead2 = audioInputStream.read(abData2, 0, abData2.length);
                if (nBytesRead2 == -1) nBytesRead = -1;
                if (nBytesRead >= 0) nBytesWritten = line.write(abData, 0, nBytesRead);
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead == -1) nBytesRead2 = -1;
                if (nBytesRead2 >= 0) nBytesWritten = line.write(abData2, 0, nBytesRead2);
            }
        } catch (LineUnavailableException li) {
            li.printStackTrace();
            state = 'S';
            ErrorMessage msg = new ErrorMessage();
            msg.errorMessage = li.toString();
            javax.swing.SwingUtilities.invokeLater(msg);
            stop();
            return;
        } catch (IOException io) {
            io.printStackTrace();
            state = 'N';
            ErrorMessage msg = new ErrorMessage();
            msg.errorMessage = io.toString();
            javax.swing.SwingUtilities.invokeLater(msg);
            if (++errorDialogCount >= 5) {
                stop();
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (state == 'P') {
                state = 'E';
                exceptionCount++;
                if (exceptionCount > 10) {
                    ErrorMessage msg = new ErrorMessage();
                    msg.errorMessage = ex.toString();
                    javax.swing.SwingUtilities.invokeLater(msg);
                    stop();
                    return;
                }
            }
        } finally {
            if (announce != null) announce.cancel = true;
            announce = null;
            if (line != null) {
                if (state == 'S' || state == 'A' || state == 'M') {
                    line.stop();
                    line.flush();
                }
                lineDrain = new LineDrain(line, audioFormat);
                lineDrain.start();
                line = null;
            }
            if (state == 'P' && mainFrame.library.attributes.libraryType != 'P') state = 'S';
            updateSlider.actionPerformed(null);
            if (state == 'S' || state == 'P' || state == 'N' || state == 'R') {
                updateSlider.position = 0;
                updateSlider.timeDisplay = "";
            }
            if (state == 'P' || state == 'N') {
                nextSong.increment = 1;
                javax.swing.SwingUtilities.invokeLater(nextSong);
            }
            if (state == 'R') {
                if (startTime + playedTime < 2) {
                    nextSong.increment = -1;
                    javax.swing.SwingUtilities.invokeLater(nextSong);
                } else {
                    nextSong.increment = -999;
                    updateSlider.position = 0;
                    javax.swing.SwingUtilities.invokeLater(nextSong);
                }
            }
            if (state == 'E' || state == 'M') {
                nextSong.increment = -999;
                javax.swing.SwingUtilities.invokeLater(nextSong);
            }
        }
    }

    Object lineSync = new Object();

    class LineDrain extends Thread {

        SourceDataLine line;

        AudioFormat audioFormat;

        LineDrain(SourceDataLine line, AudioFormat audioFormat) {
            super("audioplayer-linedrain");
            this.line = line;
            this.audioFormat = audioFormat;
        }

        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
            synchronized (lineSync) {
                interrupted();
                if (line != null) {
                    line.drain();
                    line.close();
                }
                lineDrain = null;
            }
        }
    }

    static Object syncSpeech = new Object();

    static SpeechInterface speaker;

    static String voice;

    static String engine;

    static int rate = 0;

    static int volume = 100;

    class Announce extends Thread {

        String announcement;

        boolean cancel;

        Announce(String announcement) {
            super("audioplayer-announce");
            this.announcement = announcement;
        }

        public void run() {
            synchronized (syncSpeech) {
                if (cancel) return;
                try {
                    String fixLanguage = mainFrame.library.attributes.langFixMap.get(announcementLanguage);
                    if (fixLanguage != null) announcementLanguage = fixLanguage;
                    String[] voiceSettings = Jampal.voiceSettings.get(announcementLanguage);
                    String newEngine = null;
                    String newVoice = null;
                    if (voiceSettings != null) {
                        newEngine = voiceSettings[0];
                        newVoice = voiceSettings[1];
                    }
                    if (newEngine == null) {
                        voiceSettings = Jampal.voiceSettings.get("other");
                        if (voiceSettings != null) {
                            newEngine = voiceSettings[0];
                            newVoice = voiceSettings[1];
                        }
                    }
                    if (newVoice.length() == 0 && "eSpeak".equals(newEngine)) {
                        if (announcementLanguage.length() == 3) {
                            newVoice = mainFrame.library.attributes.shortLangMap.get(announcementLanguage);
                            if (newVoice == null) newVoice = "";
                        }
                    }
                    int newRate = rate;
                    int newVolume = volume;
                    try {
                        newRate = Integer.parseInt(Jampal.initialProperties.getProperty("speech-rate"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        newVolume = Integer.parseInt(voiceSettings[2]);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (newEngine == null || "None".equals(newEngine)) return;
                    if (speaker != null) {
                        if (newEngine.equals(engine) && newVoice.equals(voice) && newRate == rate && newVolume == volume) {
                        } else {
                            speaker.close();
                            speaker = null;
                        }
                    }
                    engine = newEngine;
                    voice = newVoice;
                    rate = newRate;
                    volume = newVolume;
                    if (speaker == null) {
                        if ("FreeTTS".equals(engine)) speaker = new FreeTTSSpeaker(); else if ("Microsoft".equals(engine)) speaker = new Speaker(); else if ("Cepstral".equals(engine)) {
                            CepstralSpeaker cepstralSpeaker = new CepstralSpeaker();
                            speaker = cepstralSpeaker;
                            cepstralSpeaker.setPath(Jampal.initialProperties.getProperty("cepstral-path"));
                        } else if ("eSpeak".equals(engine)) {
                            ESpeakSpeaker espeakSpeaker = new ESpeakSpeaker();
                            speaker = espeakSpeaker;
                            espeakSpeaker.setPaths(Jampal.initialProperties.getProperty("espeak-prog"), Jampal.initialProperties.getProperty("espeak-data"), Jampal.initialProperties.getProperty("mbrola-prog"), Jampal.initialProperties.getProperty("mbrola-path"), Jampal.initialProperties.getProperty("mixer-name"), Jampal.initialProperties.getProperty("mixer-gain-percent"));
                        }
                        if (speaker != null) {
                            if (!"default voice".equals(voice) && !"".equals(voice)) speaker.setVoice(voice);
                            speaker.setRate(rate);
                            speaker.setVolume(volume);
                            speaker.init();
                            if (!"default voice".equals(voice)) speaker.setVoice(voice);
                            speaker.setRate(rate);
                            speaker.setVolume(volume);
                        }
                    }
                    if (speaker != null) {
                        speaker.speak(announcement);
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    static String formatTime(long seconds) {
        int minutes = (int) seconds / 60;
        int secs = (int) seconds % 60;
        String ret = String.valueOf(minutes) + ":";
        if (secs < 10) ret = ret + "0";
        ret = ret + String.valueOf(secs);
        return ret;
    }

    class UpdateSlider implements ActionListener {

        String timeDisplay;

        int position;

        int prevPosition;

        boolean adjusting;

        int loopCounter;

        public void actionPerformed(ActionEvent e) {
            if (state != 'P') {
                if (line != null) line.flush();
            }
            if (state == 'A' || state == 'M' || state == 'E') {
                mainFrame.timer.removeActionListener(updateSlider);
                return;
            }
            if (state != 'P' || line == null) {
                mainFrame.playLabel.setText(null);
                mainFrame.frame.setTitle(mainFrame.shortName + " - jampal");
                mainFrame.timeLabel.setText(null);
                mainFrame.playSlider.setValue(0);
                mainFrame.timer.removeActionListener(updateSlider);
                return;
            }
            long prevPlayedMicros = playedMicros;
            playedMicros = line.getMicrosecondPosition() - playedMicrosAtStart;
            playedTime = playedMicros / 1000000;
            if (playedMicros == prevPlayedMicros) {
                loopCounter++;
                System.err.println("Played:" + playedMicros + " prev:" + prevPlayedMicros);
                if (loopCounter >= 2) {
                    mp3Stream.throwException = "Decoder in a Loop";
                }
            } else loopCounter = 0;
            int newPosition = (int) ((startTime + playedTime) * 10000 / playingTime);
            position = newPosition;
            long seconds = startTime + playedTime;
            if (seconds > prevSeconds) {
                prevSeconds = seconds;
                timeDisplay = formatTime(seconds) + " / " + playingTimeDisplay;
            }
            mainFrame.timeLabel.setText(timeDisplay);
            if (mainFrame.playSlider.getValueIsAdjusting()) {
                adjusting = true;
                return;
            }
            if (prevPosition == -1) prevPosition = mainFrame.playSlider.getValue();
            if (position > 10000) position = 10000;
            int sliderPosition = mainFrame.playSlider.getValue();
            if (adjusting || sliderPosition != prevPosition) {
                state = 'M';
                return;
            }
            mainFrame.playSlider.setValue(position);
            prevPosition = position;
        }
    }

    class NextSong implements Runnable {

        int increment;

        public void run() {
            if (increment != -999) {
                selectedRow = mainFrame.library.trackVec.indexOf(mainFrame.trackLastPlaying);
                selectedRow += increment;
                int rowCount = mainFrame.mainTable.getRowCount();
                if (selectedRow < 0) selectedRow = 0;
                if (selectedRow >= rowCount) return;
                mainFrame.editorUpdateCount = 2;
                mainFrame.timer.addActionListener(mainFrame.editorUpdateActionListener);
                mainFrame.editorUpdateActionListening = true;
                mainFrame.mainTable.clearSelection();
                mainFrame.mainTable.addRowSelectionInterval(selectedRow, selectedRow);
                Rectangle cell = mainFrame.mainTable.getCellRect(selectedRow, 0, true);
                mainFrame.mainTable.scrollRectToVisible(cell);
            }
            play();
        }
    }

    static int errorDialogCount;

    class ErrorMessage implements Runnable {

        String errorMessage;

        public void run() {
            JOptionPane.showMessageDialog(mainFrame.frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            errorDialogCount--;
        }
    }

    public static SourceDataLine getSourceDataLine(String strMixerName, AudioFormat audioFormat, int nBufferSize) throws Exception {
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, nBufferSize);
        if (strMixerName != null && strMixerName.length() > 0) {
            Mixer.Info mixerInfo = AudioCommon.getMixerInfo(strMixerName);
            if (mixerInfo == null) {
                System.err.println("AudioPlayer: mixer not found: " + strMixerName);
                line = null;
            } else {
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                line = (SourceDataLine) mixer.getLine(info);
            }
        }
        if (line == null) {
            line = (SourceDataLine) AudioSystem.getLine(info);
        }
        line.open(audioFormat, nBufferSize);
        return line;
    }
}

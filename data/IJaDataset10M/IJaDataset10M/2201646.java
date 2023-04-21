package core;

import gui.AuroraGUI;
import gui.SongTable;
import gui.VisualOverlay;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.swing.SwingUtilities;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import kj.dsp.KJDigitalSignalProcessingAudioDataConsumer;

public class AuroraPlayer extends BasicPlayer {

    private final AuroraGUI gui;

    private final QueueRunner runner;

    protected VisualOverlay overlay;

    protected final KJDigitalSignalProcessingAudioDataConsumer dsp = new KJDigitalSignalProcessingAudioDataConsumer(2000, 30);

    public AuroraPlayer() {
        gui = new AuroraGUI();
        gui.setVisible(true);
        SongTable queue = gui.getQueueTable();
        runner = new QueueRunner(this, queue);
        overlay = new VisualOverlay(gui);
        dsp.add(overlay);
    }

    @Override
    protected void informListeners(int nEncodedBytes, byte[] pcm) {
        super.informListeners(nEncodedBytes, pcm);
        dsp.writeAudioData(pcm);
    }

    public void play(Song song) {
        System.out.println("Playing song: " + song.getTitle());
        MusicLibrary library = song.getOwner();
        InputStream is = new BufferedInputStream(library.getDataFor(song));
        try {
            super.open(is);
            super.play();
            dsp.start(m_line);
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new AuroraPlayer();
            }
        });
    }
}

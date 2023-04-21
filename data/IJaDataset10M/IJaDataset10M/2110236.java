package padrao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.RecordControl;

/**
 *
 * @author Thiago
 */
public class HomerSoundCollector extends CustomItem {

    private Player player;

    private Player recorder;

    private RecordControl ctrl;

    private ByteArrayOutputStream bos = new ByteArrayOutputStream();

    private ByteArrayInputStream bis = null;

    private SoundCounter sound;

    private Counter Counter;

    private boolean recording = false;

    private boolean playing = false;

    private int selected = 0;

    public HomerSoundCollector() {
        super("");
    }

    private void InitializeRecorder() {
        String url = "capture://audio";
        try {
            recorder = null;
            recorder = Manager.createPlayer(url);
            recorder.realize();
            ctrl = null;
            ctrl = (RecordControl) recorder.getControl("javax.microedition.media.control.RecordControl");
            if (ctrl == null) {
                try {
                    throw new Exception("Este aparelho não suporta gravação de áudio.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    public void record() {
        try {
            reset();
            InitializeRecorder();
            ctrl.setRecordStream(bos);
            recorder.start();
            ctrl.startRecord();
            Counter = new Counter();
            Counter.start();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    public void play() {
        sound = new SoundCounter(Counter.getSeconds());
        sound.start();
    }

    public void stopPlaying() {
        try {
            player.stop();
            playing = false;
            this.repaint();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    public void stopRecording() {
        try {
            ctrl.stopRecord();
            ctrl.commit();
            recorder.stop();
            bos.flush();
            bis = new ByteArrayInputStream(bos.toByteArray());
            recording = false;
            this.repaint();
        } catch (MediaException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean save() {
        return false;
    }

    public void reset() {
        recorder = null;
        player = null;
        bis = null;
        bos = null;
        bos = new ByteArrayOutputStream(1048576);
        Runtime.getRuntime().freeMemory();
        System.gc();
    }

    protected int getMinContentHeight() {
        return 50;
    }

    protected int getMinContentWidth() {
        return 150;
    }

    protected int getPrefContentHeight(int width) {
        return 50;
    }

    protected int getPrefContentWidth(int height) {
        return 200;
    }

    protected void paint(Graphics g, int w, int h) {
        String titulo = "Gravador de Som";
        String btn1 = "Gravar";
        String btn2 = "Tocar";
        g.setColor(0, 0, 0);
        g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
        g.drawString(titulo, 10, 0, 0);
        g.setColor(200, 200, 200);
        g.fillRect(10, 20, 50, 20);
        g.fillRect(80, 20, 50, 20);
        g.setColor(255, 0, 0);
        int spacing = 0;
        if (selected == 1) spacing = 70;
        g.drawRect(spacing + 7, 17, 56, 26);
        if (recording) {
            btn1 = "Parar";
            g.setColor(0, 0, 0);
            g.drawString(btn1, 17, 22, 0);
            g.setColor(150, 150, 150);
        } else {
            btn1 = "Gravar";
            g.setColor(0, 0, 0);
            g.drawString(btn1, 17, 22, 0);
        }
        if (playing) {
            btn2 = "Parar";
            g.drawString(btn2, 92, 22, 0);
            g.setColor(150, 150, 150);
            g.drawString(btn1, 17, 22, 0);
        } else {
            btn2 = "Tocar";
            if (bis == null) g.setColor(150, 150, 150);
            g.drawString(btn2, 92, 22, 0);
        }
    }

    protected void keyPressed(int keyCode) {
        int key = getGameAction(keyCode);
        if (key == 0) key = keyCode;
        switch(key) {
            case Canvas.KEY_STAR:
                if (selected == 1 && !recording && !playing) selected = 0;
                break;
            case Canvas.KEY_POUND:
                if (selected == 0 && !recording && !playing) selected = 1;
                break;
            case Canvas.FIRE:
                if (recording) {
                    Counter.stop();
                } else if (selected == 0) {
                    recording = true;
                    record();
                }
                if (playing) {
                    if (sound != null) sound.stop();
                } else if (selected == 1) {
                    playing = true;
                    play();
                }
                break;
            default:
                break;
        }
        this.repaint();
    }

    private class SoundCounter extends Thread {

        private long totalSeconds;

        private long seconds;

        private boolean ativo = true;

        public SoundCounter(long totalSeconds) {
            seconds = 0;
            this.totalSeconds = totalSeconds;
        }

        public void run() {
            try {
                if (player == null) player = Manager.createPlayer(bis, ctrl.getContentType());
                player.start();
                while (seconds < totalSeconds && ativo) {
                    Thread.sleep(1000);
                    seconds++;
                    System.out.println("aguardando..." + seconds);
                }
                seconds = 0;
                stopPlaying();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (MediaException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        public void stop() {
            ativo = false;
        }
    }

    private class Counter extends Thread {

        private long seconds;

        private long totalSeconds;

        private boolean ativo = true;

        public Counter() {
            seconds = 0;
        }

        public void run() {
            try {
                while (seconds < 60 && ativo) {
                    Thread.sleep(1000);
                    seconds++;
                    System.out.println("aguardando..." + seconds);
                }
                if (ativo) totalSeconds = seconds;
                seconds = 0;
                stopRecording();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        public void stop() {
            totalSeconds = seconds;
            System.out.println("Total: " + totalSeconds);
            ativo = false;
        }

        public long getSeconds() {
            return totalSeconds;
        }
    }
}

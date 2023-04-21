package player;

import gui.mediacontroller.MediaControllerInterno;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import javazoom.jlgui.player.amp.tag.TagInfo;
import javazoom.jlgui.player.amp.tag.TagInfoFactory;

public class MP3PlayerInterno extends BasicPlayer implements ChangeListener, MouseListener {

    MediaControllerInterno controller = null;

    int duracaoDaMedia = -1;

    private static int framesProgresso = 10000;

    private static int frameMin = 0;

    private static int frameAtual = 0;

    private static int processarProgressoFlag = 0;

    File arquivo = null;

    TagInfo taginfo;

    Map properties = null;

    boolean mute = false;

    int volume = -1;

    public MP3PlayerInterno() throws JavaLayerException {
        super();
        addBasicPlayerListener(new BasicPlayerListener() {

            public void stateUpdated(BasicPlayerEvent bpe) {
                System.out.println(bpe);
                if (bpe.getCode() == BasicPlayerEvent.EOM) {
                    controller.finalizouMusica();
                }
            }

            public void setController(BasicController arg0) {
            }

            public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
                if (processarProgressoFlag++ >= 20) {
                    int ftemp = (int) (long) microseconds / 1000000;
                    ftemp = (ftemp * framesProgresso) / duracaoDaMedia;
                    if (ftemp != frameAtual) {
                        frameAtual = ftemp;
                        controller.setPosition(frameMin + ftemp);
                    }
                }
            }

            public void opened(Object arg0, Map properties) {
                MP3PlayerInterno.this.properties = properties;
            }
        });
        controller = new MediaControllerInterno(this, framesProgresso);
        controller.getVolumeSlider().addChangeListener(this);
        controller.getMuteLabel().addMouseListener(this);
    }

    public void setMute(boolean m) {
        System.out.println("Volume Atual para Mute: " + volume);
        try {
            if (m) {
                setGain(getMinimumGain());
                controller.setSoundOffIcon();
            } else {
                setGain((double) volume / MediaControllerInterno.VOLUME_MAX);
                controller.setSoundOnIcon();
            }
            mute = m;
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void open(File arquivo) throws BasicPlayerException {
        super.open(arquivo);
        this.arquivo = arquivo;
        TagInfoFactory factory = TagInfoFactory.getInstance();
        taginfo = factory.getTagInfo(arquivo);
        duracaoDaMedia = (int) taginfo.getPlayTime();
        System.out.println("Duracao: " + taginfo.getPlayTime());
        frameAtual = 0;
        frameMin = 0;
        controller.setPosition(frameAtual);
        controller.inicializar();
    }

    @Override
    public void play() throws BasicPlayerException {
        super.play();
        if (mute) setGain(getMinimumGain()); else if (volume != -1) {
            setGain((double) volume / MediaControllerInterno.VOLUME_MAX);
        } else {
            volume = MediaControllerInterno.VOLUME_MAX / 2;
            setGain((double) volume / MediaControllerInterno.VOLUME_MAX);
            controller.getVolumeSlider().setValue(volume);
            controller.setSoundOnIcon();
        }
    }

    public Component getController() {
        return controller;
    }

    public void irParaPosicao(int value) {
        long bytes = Long.parseLong(properties.get("audio.length.bytes").toString());
        double rate = (double) value / framesProgresso;
        bytes = Math.round(bytes * rate);
        frameMin = value;
        if (getStatus() == STOPPED) {
            try {
                super.open(arquivo);
                controller.inicializar();
                frameMin = value;
                seek(bytes);
                play();
                atualizarVolume();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        } else {
            try {
                seek(bytes);
                atualizarVolume();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == controller.getVolumeSlider()) {
            atualizarVolume();
        }
    }

    private void atualizarVolume() {
        volume = controller.getVolumeSlider().getValue();
        System.out.println("Volume Atual: " + volume);
        if (mute) return;
        double gain = (double) volume / controller.getVolumeSlider().getMaximum();
        try {
            setGain(gain);
        } catch (BasicPlayerException e1) {
            e1.printStackTrace();
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == controller.getMuteLabel()) {
            setMute(!mute);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}

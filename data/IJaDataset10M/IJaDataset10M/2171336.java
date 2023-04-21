package org.jdesktop.jdic.mpcontrol.gmb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.jdesktop.jdic.mpcontrol.IMediaPlayer;
import org.jdesktop.jdic.mpcontrol.ISongInfo;
import org.jdesktop.jdic.mpcontrol.MediaPlayerService;
import org.jdesktop.jdic.mpcontrol.impl.ProcessUtil;

/**
 * @author zsombor
 *
 */
public class GMusicBrowser implements IMediaPlayer {

    String fifoPath;

    /**
	 * 
	 */
    public GMusicBrowser() {
        super();
    }

    public boolean isAvailableMediaPlayer() {
        return new File(getFifoPath()).getParentFile().exists();
    }

    public void init() {
    }

    public String getName() {
        return "gmusicbrowser";
    }

    public String getDescription() {
        return "GMusicBrowser (http://squentin.free.fr/gmusicbrowser/gmusicbrowser.html)";
    }

    public void destroy() {
    }

    public boolean isRunning() {
        return new File(getFifoPath()).canWrite();
    }

    public void setVolume(float volume) {
        throw new RuntimeException(this.getClass() + ".setVolume not supported!");
    }

    public float getVolume() {
        throw new RuntimeException(this.getClass() + ".getVolume not supported!");
    }

    public void play() {
        sendCommand("PlayPause\n");
    }

    public void pause() {
        sendCommand("PlayPause\n");
    }

    public boolean isPlaying() {
        return isRunning();
    }

    public void next() {
        sendCommand("NextSong\n");
    }

    public void previous() {
        sendCommand("PrevSong\n");
    }

    public ISongInfo getCurrentSong() {
        throw new RuntimeException(this.getClass() + ".getCurrentSong not supported!");
    }

    public void setMediaLocation(URL location) {
        throw new RuntimeException(this.getClass() + ".setMediaLocation not supported!");
    }

    public boolean startPlayerProcess() {
        return ProcessUtil.execute("gmusicbrowser") || ProcessUtil.execute("/usr/bin/gmusicbrowser");
    }

    protected String getFifoPath() {
        String path = System.getProperty("org.jdesktop.jdic.mpcontrol.gmusicbrowser.path");
        if (path == null) path = (String) MediaPlayerService.getProperties().get("org.jdesktop.jdic.mpcontrol.gmusicbrowser.path");
        if (path == null) {
            path = System.getProperty("user.home", ".") + "/.gmusicbrowser/gmusicbrowser.fifo";
        }
        return path;
    }

    protected void sendCommand(String line) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(getFifoPath());
            fos.write(line.getBytes("ASCII"));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        IMediaPlayer player = new GMusicBrowser();
        if (player.isAvailableMediaPlayer()) {
            player.init();
            if (player.isRunning()) {
                System.out.println("pause...");
                player.pause();
                Thread.sleep(2000);
                System.out.println("next...");
                player.next();
                System.out.println("play..");
                player.play();
                Thread.sleep(5000);
                System.out.println("prev...");
                player.previous();
            } else {
                System.out.println("GMusicBrowser not running!");
            }
            player.destroy();
        } else {
            System.out.println("GMusicBrowser not available!");
        }
    }
}

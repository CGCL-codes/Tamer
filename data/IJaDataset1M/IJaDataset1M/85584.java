package org.quelea.video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Sits out of process so as not to crash the primary VM.
 * @author Michael
 */
public abstract class OutOfProcessPlayer {

    /**
     * Start the main loop reading from the standard input stream and writing
     * to sout.
     * @param mediaPlayer the media player to control via the commands 
     * received.
     * @throws IOException if something goes wrong.
     */
    public void read(MediaPlayer mediaPlayer) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.startsWith("open ")) {
                inputLine = inputLine.substring("open ".length());
                mediaPlayer.prepareMedia(inputLine, getPrepareOptions());
            } else if (inputLine.equalsIgnoreCase("play")) {
                mediaPlayer.play();
            } else if (inputLine.equalsIgnoreCase("pause")) {
                mediaPlayer.pause();
            } else if (inputLine.equalsIgnoreCase("stop")) {
                mediaPlayer.stop();
            } else if (inputLine.equalsIgnoreCase("playable?")) {
                System.out.println(mediaPlayer.isPlayable());
            } else if (inputLine.startsWith("setTime ")) {
                inputLine = inputLine.substring("setTime ".length());
                mediaPlayer.setTime(Long.parseLong(inputLine));
            } else if (inputLine.startsWith("setVolume ")) {
                inputLine = inputLine.substring("setVolume ".length());
                mediaPlayer.setVolume(Integer.parseInt(inputLine));
            } else if (inputLine.startsWith("setMute ")) {
                inputLine = inputLine.substring("setMute ".length());
                mediaPlayer.mute(Boolean.parseBoolean(inputLine));
            } else if (inputLine.equalsIgnoreCase("mute?")) {
                boolean mute = mediaPlayer.isMute();
                System.out.println(mute);
            } else if (inputLine.equalsIgnoreCase("length?")) {
                long length = mediaPlayer.getLength();
                System.out.println(length);
            } else if (inputLine.equalsIgnoreCase("time?")) {
                long time = mediaPlayer.getTime();
                System.out.println(time);
            } else if (inputLine.equalsIgnoreCase("close")) {
                System.exit(0);
            } else {
                System.out.println("unknown command: ." + inputLine + ".");
            }
        }
    }

    /**
     * This method should return an array of any options that need to be passed 
     * onto VLCJ and in turn libvlc. If no options are required, an empty array
     * should be returned rather than null.
     * @return the options required by libvlc.
     */
    public abstract String[] getPrepareOptions();
}

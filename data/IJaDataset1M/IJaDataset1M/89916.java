package org.exmaralda.partitureditor.deprecated;

/**
 *
 * @author  thomas
 */
public class JavaSoundTimeThread extends Thread {

    JavaSoundPlayer player;

    /** Creates a new instance of JavaSoundTimeThread */
    public JavaSoundTimeThread(JavaSoundPlayer p) {
        player = p;
    }

    public void run() {
        player.playbackStartTime = System.currentTimeMillis();
        while (player.timeThread != null) {
            if (player.timeThread.isInterrupted()) break;
            try {
                player.timeThread.sleep(player.UPDATE_INTERVAL);
            } catch (InterruptedException ie) {
                player.timeThread.interrupt();
            }
        }
    }
}

package chrriis.dj.sweet.components.win32;

import chrriis.dj.sweet.components.OleAccess;

/**
 * A Media Player object responsible for controls-related actions.
 * @author Christopher Deckers
 */
public class WMPControls {

    private OleAccess oleAccess;

    WMPControls(JWMediaPlayer wMediaPlayer) {
        oleAccess = wMediaPlayer.getOleAccess();
    }

    /**
   * Indicate if the play functionality is enabled.
   * @return true if the play functionality is enabled.
   */
    public boolean isPlayEnabled() {
        return Boolean.TRUE.equals(oleAccess.getOleProperty(new String[] { "controls", "isAvailable" }, "Play"));
    }

    /**
   * Start playing the loaded media.
   */
    public void play() {
        oleAccess.invokeOleFunction(new String[] { "controls", "Play" });
    }

    /**
   * Indicate if the stop functionality is enabled.
   * @return true if the stop functionality is enabled.
   */
    public boolean isStopEnabled() {
        return Boolean.TRUE.equals(oleAccess.getOleProperty(new String[] { "controls", "isAvailable" }, "Stop"));
    }

    /**
   * Stop the currently playing media.
   */
    public void stop() {
        oleAccess.invokeOleFunction(new String[] { "controls", "Stop" });
    }

    /**
   * Indicate if the pause functionality is enabled.
   * @return true if the pause functionality is enabled.
   */
    public boolean isPauseEnabled() {
        return Boolean.TRUE.equals(oleAccess.getOleProperty(new String[] { "controls", "isAvailable" }, "Pause"));
    }

    /**
   * Pause the currently playing media.
   */
    public void pause() {
        oleAccess.invokeOleFunction(new String[] { "controls", "Pause" });
    }

    /**
   * Set the current position on the timeline.
   * @param time The current position in milliseconds.
   */
    public void setAbsolutePosition(int time) {
        oleAccess.setOleProperty(new String[] { "controls", "currentPosition" }, time / 1000d);
    }

    /**
   * Get the current position on the timeline.
   * @return the current position in milliseconds, or -1 in case of failure.
   */
    public int getAbsolutePosition() {
        try {
            return (int) Math.round((Double) oleAccess.getOleProperty(new String[] { "controls", "currentPosition" }) * 1000);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            return -1;
        }
    }
}

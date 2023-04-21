package jasel.io;

/**
 * Anyone interested in when the game pauses or unpauses
 * implements this and registers with KeyInput.
 */
public interface PauseListener {

    public void pausedSet(boolean paused);
}

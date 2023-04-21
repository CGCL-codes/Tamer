package skycastle.gamerenderer.renderer;

/**
 * @author Hans H�ggstr�m
 */
public interface GameRenderer {

    /**
     * Opens the 3D window and starts the main rendering loop.
     */
    void startRenderer();

    /**
     * Adds a new GameRenderingTask.  The rendering task may provide a part of the visible 3D geometry, and is called
     * every frame to allow it to update itself.  It doesn't have to provide 3D geometry though, it can be used for other
     * things too.
     * <p/>
     * The game rendering tasks are called each iteration in the order they were added.
     *
     * @param gameRenderingTask the GameRenderingTask to add.  Not added if it is null or already added.
     */
    void addGameRenderingTask(GameRenderingTask gameRenderingTask);

    /**
     * Removes a GameRenderingTask.
     *
     * @param gameRenderingTask the GameRenderingTask to remove.  Ignored if it is null or not found.
     */
    void removeGameRenderingTask(GameRenderingTask gameRenderingTask);

    /**
     * @return the title of the window the game renderer runs in.
     */
    String getTitle();

    /**
     * @param title the title of the window the game renderer runs in.
     */
    void setTitle(String title);
}

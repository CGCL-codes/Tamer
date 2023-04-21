package ngamejava2;

public interface iGameLoopListener {

    public void gameLoopUpdate(long currentTick);

    public void gameLoopRender(long currentFrame);

    public void gameLoopLoad();

    public void gameLoopUnload();
}

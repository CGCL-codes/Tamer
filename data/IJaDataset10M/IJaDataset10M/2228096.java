package ac.hiu.j314.elmve;

public interface Engine {

    public void take(LocalElm e);

    public void resume();

    public void suspend();

    public void dispose();
}

package logic.nodes;

public interface DestructionListener {

    public static final String DESTROYED = "destroyed";

    public void destroyed(DestructionEvent event);
}

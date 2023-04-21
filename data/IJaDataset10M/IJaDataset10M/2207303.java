package sdl4java.event;

import java.util.EventListener;

public interface KeyboardListener extends EventListener {

    public abstract void keyPressed(KeyboardEvent event);

    public abstract void keyReleased(KeyboardEvent event);
}

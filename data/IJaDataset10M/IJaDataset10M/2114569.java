package it.diamonds.engine.input;

public interface KeyboardInterface {

    void setListener(KeyboardListener listener);

    void update();

    void shutDown();
}

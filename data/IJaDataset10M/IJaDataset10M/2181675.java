package it.diamonds.engine.input;

import java.util.BitSet;

public interface AbstractKeyboard {

    void shutDown();

    void update(BitSet keys);
}

package br.org.databasetools.core.view.window;

import java.awt.AWTKeyStroke;
import java.util.Set;

public interface ITabulable extends IContainer {

    public Set<? extends AWTKeyStroke> getKeysTabular(int id);

    public void setKeysTabular(int id, Set<? extends AWTKeyStroke> keystrokes);
}

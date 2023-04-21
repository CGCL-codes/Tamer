package org.jumpmind.symmetric.admin;

import javax.swing.JPanel;

public abstract class AbstractScreen extends JPanel {

    public abstract void setup(SymmetricDatabase c);

    public abstract ScreenName getScreenName();
}

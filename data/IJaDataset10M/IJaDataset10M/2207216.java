package net.sf.matrixappender.ui.keylistener;

import java.awt.event.KeyEvent;

public interface PrimaryKeyListener {

    int getPrimaryKey();

    void onEvent(KeyEvent event);
}

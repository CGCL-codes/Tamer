package com.mytdev.swing.actions;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

/**
 *
 * @author Yann D'Isanto
 */
public interface R {

    long THREAD_WAIT_TIMEOUT = 10000L;

    String ACTION_1 = "action1";

    KeyStroke ACTION_1_KEYSTROKE = KeyStroke.getKeyStroke("ctrl A");

    String ACTION_1_NAME = "Action 1";

    String ACTION_1_NAME_FR = "Action 1 FR";

    String ACTION_1_DESCRIPTION = "A description";

    String ACTION_1_SHORT_DESCRIPTION = "A short description";

    String ACTION_1_LONG_DESCRIPTION = "A long description";

    String ACTION_1_ICON_PATH = "com/mytdev/swing/actions/icon.png";

    String ACTION_1_SMALL_ICON_PATH = "com/mytdev/swing/actions/test/small-icon.png";

    String ACTION_1_LARGE_ICON_PATH = "com/mytdev/swing/actions/large-icon.png";

    int ACTION_1_MNEMONIC = KeyEvent.VK_K;

    boolean ACTION_1_ENABLED = false;

    boolean ACTION_1_SELECTED = false;

    String ACTION_BACKGROUND = "actionBackground";

    String ACTION_EDT = "actionEDT";
}

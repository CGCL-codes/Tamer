package com.google.code.appengine.awt.im.spi;

import java.text.AttributedCharacterIterator;
import javax.swing.JFrame;
import com.google.code.appengine.awt.Window;
import com.google.code.appengine.awt.font.TextHitInfo;
import com.google.code.appengine.awt.im.InputMethodRequests;
import com.google.code.appengine.awt.im.spi.InputMethod;

public interface InputMethodContext extends InputMethodRequests {

    public JFrame createInputMethodJFrame(String title, boolean attachToInputContext);

    public Window createInputMethodWindow(String title, boolean attachToInputContext);

    public void dispatchInputMethodEvent(int id, AttributedCharacterIterator text, int committedCharacterCount, TextHitInfo caret, TextHitInfo visiblePosition);

    public void enableClientWindowNotification(InputMethod inputMethod, boolean enable);
}

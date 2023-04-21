package net.sourceforge.entrainer.notification;

import java.util.EventListener;

public interface PauseListener extends EventListener {

    void pauseEventPerformed(PauseEvent e);
}

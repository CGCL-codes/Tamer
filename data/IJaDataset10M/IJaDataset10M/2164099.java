package com.peterhi.player.events;

import java.util.EventListener;

public interface ClassroomDataListener extends EventListener {

    void talkingChanged(ClassroomDataEvent e);
}

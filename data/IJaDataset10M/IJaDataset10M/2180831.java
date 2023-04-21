package com.peterhi.client.ui.widgets.whiteboard.editors;

import java.util.EventListener;

public interface EditorListener extends EventListener {

    void update(EditorData[] data);
}

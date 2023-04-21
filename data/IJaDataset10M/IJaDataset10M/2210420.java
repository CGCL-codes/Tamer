package com.peterhi.classroom;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;

public interface Command {

    String getId();

    String getName();

    String getDescription();

    Image getImage();

    char getMnemonic();

    int getAccelerator();

    void execute(Event e);
}

package com.google.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.IncrementalCommand;
import com.google.gwt.user.client.ui.Label;

public class IncrementalCommandExample implements EntryPoint {

    public void onModuleLoad() {
        final Label label = new Label();
        DeferredCommand.addCommand(new IncrementalCommand() {

            private int index = 0;

            protected static final int COUNT = 10;

            public boolean execute() {
                label.setText("IncrementalCommand - index " + Integer.toString(index));
                return ++index < COUNT;
            }
        });
    }
}

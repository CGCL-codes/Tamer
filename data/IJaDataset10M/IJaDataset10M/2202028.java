package com.the_eventhorizon.todo.commons.editor;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Empty page for now.
 * 
 * @author <a href="mailto:prj-todo@the-eventhorizon.com">Pavel Krupets</a>
 */
public class RootView extends Composite {

    public RootView(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setSize(new Point(300, 200));
        setLayout(new GridLayout());
    }
}

package org.nakedobjects.nos.client.dnd.example.tree;

import org.nakedobjects.noa.adapter.ResolveState;
import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.ViewAxis;
import org.nakedobjects.nos.client.dnd.Workspace;
import org.nakedobjects.nos.client.dnd.basic.TreeBrowserSpecification;
import org.nakedobjects.nos.client.dnd.content.RootObject;
import org.nakedobjects.nos.client.dnd.drawing.Location;
import org.nakedobjects.nos.client.dnd.drawing.Size;
import org.nakedobjects.nos.client.dnd.example.view.TestViews;
import org.nakedobjects.nos.client.dnd.tree.TreeBrowserFrame;
import org.nakedobjects.nos.client.dnd.view.form.FormSpecification;
import org.nakedobjects.testing.DummyNakedObject;
import org.nakedobjects.testing.TestSpecification;

public class TreeExample extends TestViews {

    public static void main(final String[] args) {
        new TreeExample();
    }

    protected void views(final Workspace workspace) {
        DummyNakedObject object = new DummyNakedObject();
        object.setupSpecification(new TestSpecification());
        object.setupResolveState(ResolveState.TRANSIENT);
        ViewAxis axis = new TreeBrowserFrame(null, null);
        Content content = new RootObject(object);
        View view = new TreeBrowserSpecification().createView(content, axis);
        view.setLocation(new Location(100, 50));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
        view = new FormSpecification().createView(content, axis);
        view.setLocation(new Location(100, 200));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
    }
}

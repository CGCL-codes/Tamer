package org.nakedobjects.nos.client.dnd.example.table;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.ViewAxis;
import org.nakedobjects.nos.client.dnd.ViewSpecification;
import org.nakedobjects.nos.client.dnd.Workspace;
import org.nakedobjects.nos.client.dnd.border.ScrollBorder;
import org.nakedobjects.nos.client.dnd.content.RootObject;
import org.nakedobjects.nos.client.dnd.drawing.Location;
import org.nakedobjects.nos.client.dnd.drawing.Size;
import org.nakedobjects.nos.client.dnd.example.ExampleViewSpecification;
import org.nakedobjects.nos.client.dnd.example.view.TestObjectViewWithDragging;
import org.nakedobjects.nos.client.dnd.example.view.TestViews;

public class ScrollableTableBorderExample extends TestViews {

    public static void main(final String[] args) {
        new ScrollableTableBorderExample();
    }

    protected void views(final Workspace workspace) {
        NakedObject object = createExampleObjectForView();
        Content content = new RootObject(object);
        ViewSpecification specification = new ExampleViewSpecification();
        ViewAxis axis = null;
        TestHeaderView leftHeader = new TestHeaderView(axis, 40, 800);
        TestHeaderView topHeader = new TestHeaderView(axis, 800, 20);
        View view = new ScrollBorder(new TestObjectViewWithDragging(content, specification, axis, 800, 800, "both"), leftHeader, topHeader);
        view.setLocation(new Location(50, 60));
        view.setSize(new Size(216, 216));
        view.setParent(workspace);
        workspace.addView(view);
        view = new ScrollBorder(new TestObjectViewWithDragging(content, specification, axis, 200, 800, "vertical"));
        view.setLocation(new Location(300, 60));
        view.setSize(new Size(216, 216));
        view.setParent(workspace);
        workspace.addView(view);
        view = new ScrollBorder(new TestObjectViewWithDragging(content, specification, axis, 800, 200, "horizontal"));
        view.setLocation(new Location(550, 60));
        view.setSize(new Size(216, 216));
        view.setParent(workspace);
        workspace.addView(view);
    }
}

package org.nakedobjects.nos.client.dnd.table;

import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.ViewAxis;
import org.nakedobjects.nos.client.dnd.basic.TableFocusManager;
import org.nakedobjects.nos.client.dnd.border.ScrollBorder;
import org.nakedobjects.nos.client.dnd.border.WindowBorder;
import org.nakedobjects.nos.client.dnd.builder.CollectionElementBuilder;
import org.nakedobjects.nos.client.dnd.builder.StackLayout;

public class WindowTableSpecification extends org.nakedobjects.nos.client.dnd.table.AbstractTableSpecification {

    public WindowTableSpecification() {
        builder = new StackLayout(new CollectionElementBuilder(this));
    }

    public View doCreateView(final View view, final Content content, final ViewAxis axis) {
        ScrollBorder scrollingView = new ScrollBorder(view);
        WindowBorder viewWithWindowBorder = new WindowBorder(scrollingView, false);
        scrollingView.setTopHeader(new TableHeader(content, view.getViewAxis()));
        viewWithWindowBorder.setFocusManager(new TableFocusManager(viewWithWindowBorder));
        return viewWithWindowBorder;
    }

    public String getName() {
        return "Table";
    }
}

package org.nakedobjects.viewer.skylark.table;

import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.basic.CollectionElementBuilder;
import org.nakedobjects.viewer.skylark.basic.ScrollBorder;
import org.nakedobjects.viewer.skylark.basic.StackLayout;
import org.nakedobjects.viewer.skylark.basic.TableFocusManager;
import org.nakedobjects.viewer.skylark.basic.WindowBorder;

public class WindowTableSpecification extends org.nakedobjects.viewer.skylark.table.AbstractTableSpecification {

    public WindowTableSpecification() {
        builder = new StackLayout(new CollectionElementBuilder(this, true));
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

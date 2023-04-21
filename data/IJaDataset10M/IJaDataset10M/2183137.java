package com.tll.client.view;

import com.google.gwt.user.client.ui.Label;
import com.tll.client.view.AbstractView;
import com.tll.client.view.StaticViewInitializer;
import com.tll.client.view.ViewClass;

/**
 * ViewB
 * @author jpk
 */
public class ViewC extends AbstractView<StaticViewInitializer> {

    public static final Class klas = new Class();

    public static final class Class extends ViewClass {

        @Override
        public String getName() {
            return "ViewC";
        }

        @Override
        public ViewC newView() {
            return new ViewC();
        }
    }

    @Override
    protected void doInitialization(StaticViewInitializer initializer) {
        addWidget(new Label("This is view C"));
    }

    @Override
    protected void doDestroy() {
    }

    @Override
    public ViewClass getViewClass() {
        return klas;
    }

    @Override
    public String getLongViewName() {
        return "View C";
    }
}

package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.tll.common.model.SmbizEntityType;

/**
 * AspMain - ASP root view.
 * @author jpk
 */
public class AspMain extends MainView {

    public static final Class klas = new Class();

    public static final class Class extends MainViewClass {

        public Class() {
            super(SmbizEntityType.ASP);
        }

        @Override
        public AspMain newView() {
            return new AspMain();
        }
    }

    private final Label lbl;

    /**
	 * Constructor
	 */
    public AspMain() {
        super();
        lbl = new Label("This is the ASP main view.");
        addWidget(lbl);
    }

    public String getLongViewName() {
        return "ASP Main";
    }

    protected Widget getViewWidgetInternal() {
        return lbl;
    }

    @Override
    public ViewClass getViewClass() {
        return klas;
    }
}

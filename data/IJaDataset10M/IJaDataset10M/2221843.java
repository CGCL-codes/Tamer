package org.gwanted.gwt.widget.datawidget.client.ui;

import org.gwanted.gwt.core.client.nativeutil.ExtDOM;
import com.google.gwt.user.client.ui.Widget;

/**
 * Wrapper for BODY element.
 *
 * @author Joshua Hewitt aka Sposh
 */
public class Body extends Widget {

    public Body() {
        super();
        setElement(ExtDOM.createBody());
    }
}

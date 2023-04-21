package daam.ui.gwt.client.component.misc;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import daam.ui.gwt.client.component.IControl;

public class IJsEval extends IControl {

    @Override
    protected Widget initWidget() {
        return new Label("");
    }

    @Override
    public void receiveUpdateEvent(String propertyName, Object newValue) {
        super.receiveUpdateEvent(propertyName, newValue);
        if ("js".equals(propertyName)) {
            eval((String) newValue);
        }
    }

    public static native void eval(String js);
}

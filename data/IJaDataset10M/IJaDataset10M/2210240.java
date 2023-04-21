package custom.client;

import custom.client.control.IMyLabel;
import daam.ui.gwt.client.ComponentFactory;
import daam.ui.gwt.client.component.IControl;

public class CustomComponentFactory extends ComponentFactory {

    static {
        componentFactories.add(new CustomComponentFactory());
    }

    public IControl getControl(String controlName) {
        if ("custom.mylabel".equals(controlName)) return new IMyLabel();
        return null;
    }
}

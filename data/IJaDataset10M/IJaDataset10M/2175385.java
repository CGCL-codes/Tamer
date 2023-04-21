package test.showcase;

import daam.ui.Button;
import daam.ui.Container;
import daam.ui.Label;
import daam.ui.RichTextField;

public class RichText extends Container {

    RichTextField field;

    Label label;

    public RichText() {
        field = new RichTextField();
        field.setWidth("600px");
        addControl(field);
        addControl(new Button("Process", this, "process"));
        label = new Label("");
        addControl(label);
    }

    public void process() {
        label.setText(field.getText());
    }
}

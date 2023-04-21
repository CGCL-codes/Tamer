package ch.persi.ant.tasks;

import java.awt.Component;

public class RadioButtonGroupComponent extends Component {

    private DialogRadioButtonGroup group;

    public RadioButtonGroupComponent(DialogRadioButtonGroup group) {
        super();
        this.group = group;
    }

    public DialogRadioButtonGroup getGroup() {
        return group;
    }
}

package ontool.app.modelview;

import ontool.model.*;

public class KObjectProps extends ModelPropertySheet {

    private NetworkModel net;

    private KObjectModel model;

    private TextFieldEditor jName;

    public void init() {
        model = (KObjectModel) getModel();
        net = (NetworkModel) model.getParent().getParent().getParent();
        jName = new TextFieldEditor("", this);
        addProperty("Name", jName);
    }

    public void refresh() {
        jName.setText(model.getName());
    }

    public void apply() {
        model.setName(jName.getText());
    }
}

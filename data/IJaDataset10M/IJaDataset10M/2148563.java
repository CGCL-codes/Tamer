package ontool.app.modelview;

import java.util.*;
import javax.swing.*;
import ontool.model.*;

/**
 * Description of the Class
 *
 * @author     asrgomes
 * @created    23 de Julho de 2001
 */
public class LinkProps extends ModelPropertySheet {

    /**
     *  Description of the Field
     */
    protected NetworkModel net;

    private LinkModel model;

    private TextFieldEditor jName;

    private ComboBoxEditor jPortRef;

    private TextFieldEditor jWeight;

    public void init() {
        model = (LinkModel) getModel();
        net = (NetworkModel) model.getParent().getParent();
        jName = new TextFieldEditor("", this);
        addProperty("Name", jName);
        jPortRef = new ComboBoxEditor(this);
        addProperty("Port Ref", jPortRef);
        jWeight = new TextFieldEditor("", this);
        addProperty("Weight", jWeight);
    }

    public void refresh() {
        jName.setDefaultText(model.getName());
        jName.setText(model.getName());
        PrivatePort pp = getPrivPort();
        jPortRef.setModel(new DefaultComboBoxModel(getPortRefs(pp)));
        jPortRef.setDefaultSelectedItem(pp.getPortRef());
        jWeight.setText("1");
    }

    private PrivatePort getPrivPort() {
        PrivatePort privPort;
        if (model.getSource() instanceof PrivatePort) privPort = (PrivatePort) model.getSource(); else privPort = (PrivatePort) model.getDest();
        return privPort;
    }

    private Vector getPortRefs(PrivatePort pm) {
        PortHolderModel phm = (PortHolderModel) ((Model) pm).getParent();
        java.util.List v;
        Model genType = phm.getGenericType();
        if (genType == null) return new Vector();
        if (phm instanceof PlaceModel || phm instanceof InterfaceModel) {
            if (pm instanceof InPortModel) {
                v = genType.getChildren(SensorFieldModel.class);
            } else {
                v = genType.getChildren(EffectorFieldModel.class);
            }
        } else {
            v = genType.getChildren(InterfaceModel.class);
        }
        return new Vector(v);
    }

    public void apply() {
        if (jName.hasChanged()) model.setName(jName.getText());
        if (jPortRef.hasChanged()) getPrivPort().setPortRef((Model) jPortRef.getSelectedItem());
    }
}

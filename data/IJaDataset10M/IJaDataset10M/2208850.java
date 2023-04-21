package pedro.configurationTool;

import pedro.system.*;
import java.awt.*;
import javax.swing.*;

public class OKButtonPanel extends JPanel {

    private JButton ok;

    private JButton cancel;

    public OKButtonPanel(PedroUIFactory pedroUIFactory) {
        setLayout(new GridBagLayout());
        pedroUIFactory.setBasicProperties(this);
        JPanel panel = pedroUIFactory.createPanel();
        GridBagConstraints panelGC = new GridBagConstraints();
        panelGC.anchor = GridBagConstraints.SOUTHEAST;
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.gridx = 0;
        panelGC.gridy = 0;
        panelGC.weightx = 0;
        panelGC.weighty = 0;
        String okText = PedroResources.getMessage("buttons.ok");
        ok = pedroUIFactory.createButton(okText);
        add(ok, panelGC);
        panelGC.gridx++;
        String cancelText = PedroResources.getMessage("buttons.cancel");
        cancel = pedroUIFactory.createButton(cancelText);
        add(cancel, panelGC);
    }

    JButton getOKButton() {
        return ok;
    }

    JButton getCancelButton() {
        return cancel;
    }
}

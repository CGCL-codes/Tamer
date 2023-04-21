package net.sourceforge.omov.guicore;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class CheckedComponent extends JPanel {

    private static final long serialVersionUID = 2330272071370131517L;

    public CheckedComponent(final Component component, final JCheckBox checkBox) {
        super(new BorderLayout());
        this.add(checkBox, BorderLayout.WEST);
        this.add(component, BorderLayout.CENTER);
    }
}

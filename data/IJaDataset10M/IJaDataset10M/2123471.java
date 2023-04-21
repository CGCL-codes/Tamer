package org.modss.facilitator.port.ui.option.comp;

import org.modss.facilitator.shared.singleton.*;
import org.modss.facilitator.shared.resource.*;
import org.swzoo.log2.core.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

/**
 * Vertical stacker container.
 */
public class VerticalStackerContainer extends JPanel {

    int index;

    GridBagLayout gbl;

    GridBagConstraints gbc;

    public VerticalStackerContainer() {
        index = 0;
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        setLayout(gbl);
    }

    public Component add(Component c) {
        int i = index;
        LogTools.trace(logger, 25, "VerticalStackerContainer.add() - i=" + i);
        gbc.gridx = 0;
        gbc.gridy = i;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbl.setConstraints(c, gbc);
        index++;
        return super.add(c);
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource provider. */
    private static final ResourceProvider resources = Singleton.Factory.getInstance().getResourceProvider();
}

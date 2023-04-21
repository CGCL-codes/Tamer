package uk.ac.lkl.migen.system.cdst.ui.timeline;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import uk.ac.lkl.migen.system.server.*;

/**
 * A panel to put the name of the user.
 * 
 * The name adapts to the panel's width, e.g. if the panel shrinks, the 
 * name is replaced by just initials.  
 * 
 * @author sergut
 *
 */
public class NamePanel extends JPanel {

    /**
     * The UserSet for this NamePanel.
     */
    private UserSet userSet;

    /**
     * A label to display the name or the initials of the user(s)
     */
    private JLabel userNameLabel;

    /**
     * The width of the name of the user(s)
     */
    private Integer fullNameWidth;

    /**
     * The width of the initials of the user(s)
     */
    private Integer initialsWidth;

    public NamePanel(UserSet userSet) {
        setLayout(new BorderLayout(0, 0));
        this.userSet = userSet;
        setMinimumSize(new Dimension(0, 30));
        setPreferredSize(new Dimension(0, 30));
        userNameLabel = new JLabel(userSet.getNames(), JLabel.CENTER);
        userNameLabel.setText(userSet.getInitialsAsString());
        initialsWidth = userNameLabel.getPreferredSize().width;
        userNameLabel.setText(userSet.getNames());
        fullNameWidth = userNameLabel.getPreferredSize().width;
        add(userNameLabel, BorderLayout.CENTER);
        addComponentListener();
        setToolTipText(userSet.getNames());
    }

    private void addComponentListener() {
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                processComponentResized();
            }
        });
    }

    /**
     * If the component is resized, change the name displayed (e.g. name, initials, nothing).
     */
    private void processComponentResized() {
        Dimension size = getSize();
        if (size.width >= fullNameWidth) {
            userNameLabel.setText(userSet.getNames());
        } else if (size.width >= initialsWidth) userNameLabel.setText(userSet.getInitialsAsString()); else userNameLabel.setText("");
    }

    /**
     * Returns this name panel's UserSet.
     * 
     * @return this name panel's UserSet.
     */
    public UserSet getUserSet() {
        return userSet;
    }
}

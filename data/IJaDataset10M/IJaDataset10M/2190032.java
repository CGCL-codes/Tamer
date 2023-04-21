package foa.properties.bricks;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import foa.properties.groups.*;
import foa.properties.groups.sets.*;
import foa.properties.groups.sets.types.*;
import foa.properties.GenericDialog;
import foa.bricks.Brick;
import foa.bricks.simple.Paragraph;
import foa.templates.BrickDirector;

/**
 * @author Fabio Giannetti
 * @version 0.0.1
 */
public class PageNumberDialog extends BrickDialog {

    public PageNumberDialog(Container c, BrickDirector brickDirector, Hashtable brickAttributes) {
        super(c, brickDirector, "Page Number Brick", brickAttributes);
        initComponents();
    }

    protected void initComponents() {
        tabbedPane = new JTabbedPane();
        addGeneralElementsToPane();
        addContentElementsToPane();
        JPanel buttonPanel = new JPanel();
        okButton = new JButton(new UpdatePageNumberAction((Container) this, this.brickDirector));
        okButton.setText("Update");
        okButton.setToolTipText("Save changes");
        buttonPanel.add(okButton);
        cancelButton = new JButton(new DiscardAction((Container) this, this.brickDirector));
        cancelButton.setText("Discard");
        cancelButton.setToolTipText("Discard changes");
        buttonPanel.add(cancelButton);
        buttonPanel.setMaximumSize(new Dimension(80, 50));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(panel);
        setResizable(true);
    }
}

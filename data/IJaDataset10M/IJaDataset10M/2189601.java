package net.sf.pged.gui.gfactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sf.pged.gui.graph.VisualEdge;

/**
 * Dialog for setting edge property's.
 * 
 * @author ogi_dogi
 * @author dude03
 *
 */
class EdgePropertyDialog extends JDialog {

    private static final String DIALOG_TITLE = "Edge Property Dialog";

    private JFormattedTextField weightField;

    private JButton colorChooser;

    private Color workColor = null;

    private double workWeight = 0;

    private VisualEdge edge = null;

    private boolean isEdgeChanged = false;

    /**
	 * @param owner owner of this dialog.
	 */
    public EdgePropertyDialog(JFrame owner) {
        super(owner, DIALOG_TITLE, true);
        weightField = new JFormattedTextField(NumberFormat.getNumberInstance());
        weightField.setColumns(6);
        colorChooser = new JButton();
        colorChooser.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Color selected = JColorChooser.showDialog(colorChooser, "Set Edge Color", workColor);
                if (selected != null) {
                    workColor = selected;
                    colorChooser.setBackground(workColor);
                }
            }
        });
        JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                edge.setWeight(((Number) weightField.getValue()).doubleValue());
                edge.setColor(workColor);
                isEdgeChanged = true;
                setVisible(false);
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                isEdgeChanged = false;
                setVisible(false);
            }
        });
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(new JLabel("Weight: "), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        mainPanel.add(weightField, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 0, 0));
        mainPanel.add(new JLabel("Color: "), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        mainPanel.add(colorChooser, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 0, 0));
        JPanel panel = new JPanel();
        panel.add(ok);
        panel.add(cancel);
        add(mainPanel, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        setSize(250, 150);
    }

    /**
	 * Sets edge for property dialog.
	 * 
	 * @param edge the edge object.
	 */
    public void setEdge(VisualEdge edge) {
        this.edge = edge;
    }

    /**
	 * 
	 * @return
	 */
    public boolean isEdgeChanged() {
        return isEdgeChanged;
    }

    @Override
    public void setVisible(boolean b) {
        if (edge == null) new IllegalStateException("Edge for property's setting must be not null");
        if (b) {
            workColor = edge.getColor();
            workWeight = edge.getWeight();
            weightField.setValue(workWeight);
            colorChooser.setBackground(workColor);
        } else {
            edge = null;
        }
        super.setVisible(b);
    }
}

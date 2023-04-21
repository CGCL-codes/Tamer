package de.mse.mogwai.formmaker.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Visual class BeanIntrospectorPanel.
 *
 * Created with Mogwai FormMaker 0.7.
 */
public class BeanIntrospectorView extends JPanel {

    private javax.swing.JTabbedPane m_component_1;

    private javax.swing.JPanel m_component_2;

    private javax.swing.JButton m_resetpropertiesbutton;

    private javax.swing.JTable m_component_4;

    /**
	 * Constructor.
	 */
    public BeanIntrospectorView() {
        this.initialize();
    }

    /**
	 * Initialize method.
	 */
    private void initialize() {
        String rowDef = "fill:280dlu:grow";
        String colDef = "fill:140dlu:grow";
        FormLayout layout = new FormLayout(colDef, rowDef);
        this.setLayout(layout);
        CellConstraints cons = new CellConstraints();
        this.add(this.getComponent_1(), cons.xywh(1, 1, 1, 1));
        this.buildGroups();
    }

    /**
	 * Getter method for component Component_1.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JTabbedPane getComponent_1() {
        if (this.m_component_1 == null) {
            this.m_component_1 = new javax.swing.JTabbedPane();
            this.m_component_1.addTab("Properties", this.getComponent_2());
            this.m_component_1.setName("Component_1");
            this.m_component_1.setSelectedIndex(0);
            this.m_component_1.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    handleComponent_1StateChanged();
                }
            });
        }
        return this.m_component_1;
    }

    /**
	 * Getter method for component Component_2.
	 *
	 * @return the initialized component
	 */
    public JPanel getComponent_2() {
        if (this.m_component_2 == null) {
            this.m_component_2 = new JPanel();
            String rowDef = "fill:2dlu:grow,p,2dlu";
            String colDef = "fill:40dlu:grow";
            FormLayout layout = new FormLayout(colDef, rowDef);
            this.m_component_2.setLayout(layout);
            CellConstraints cons = new CellConstraints();
            this.m_component_2.add(this.getResetPropertiesButton(), cons.xywh(1, 2, 1, 1));
            this.m_component_2.add(new JScrollPane(this.getComponent_4()), cons.xywh(1, 1, 1, 1));
            this.m_component_2.setName("Component_2");
        }
        return this.m_component_2;
    }

    /**
	 * Getter method for component ResetPropertiesButton.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JButton getResetPropertiesButton() {
        if (this.m_resetpropertiesbutton == null) {
            this.m_resetpropertiesbutton = new javax.swing.JButton();
            this.m_resetpropertiesbutton.setActionCommand("Reset to default");
            this.m_resetpropertiesbutton.setIcon(new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/general/Undo16.gif")));
            this.m_resetpropertiesbutton.setName("ResetPropertiesButton");
            this.m_resetpropertiesbutton.setText("Reset to default");
            this.m_resetpropertiesbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleResetPropertiesButtonActionPerformed(e.getActionCommand());
                }
            });
            this.m_resetpropertiesbutton.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    handleResetPropertiesButtonStateChanged();
                }
            });
        }
        return this.m_resetpropertiesbutton;
    }

    /**
	 * Getter method for component Component_4.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JTable getComponent_4() {
        if (this.m_component_4 == null) {
            this.m_component_4 = new javax.swing.JTable();
            this.m_component_4.setName("Component_4");
        }
        return this.m_component_4;
    }

    /**
	 * Initialize method.
	 */
    private void buildGroups() {
    }

    /**
     * Change listener implementation for Component_1.
	 *
	 * @param item the selected item
	 */
    public void handleComponent_1StateChanged() {
    }

    /**
     * Action listener implementation for ResetPropertiesButton.
	 *
	 * @param actionCommand the spanned action command
	 */
    public void handleResetPropertiesButtonActionPerformed(String actionCommand) {
    }

    /**
     * Change listener implementation for ResetPropertiesButton.
	 *
	 * @param item the selected item
	 */
    public void handleResetPropertiesButtonStateChanged() {
    }

    /**
	 * Test main method.
	 */
    public static void main(String args[]) {
        JFrame test = new JFrame("Test for BeanIntrospectorPanel");
        test.setContentPane(new BeanIntrospectorView());
        test.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        test.pack();
        test.show();
    }
}

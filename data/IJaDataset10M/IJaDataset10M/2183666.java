package axis2swing.ui.content;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.axis2.description.AxisServiceGroup;
import axis2swing.ui.Axis2SwingUIController;

public class EngageModuleGroupPanel extends PanelContent {

    private static final long serialVersionUID = 1L;

    public EngageModuleGroupPanel(Axis2SwingUIController controller) {
        super(controller);
    }

    @Override
    protected void initGUI() {
        setHeader("Engage Module for a Service Group");
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        String message = "<html><p>To engage a module for a set of services grouped as " + "an axis service group,</p><ol>" + "<li>select the module you want to engage </li>" + "<li>select the axis service group you like the module to be engaged.</li>" + "<li>click \"Engage\".</li></ol></html>";
        JLabel newLabel = new JLabel(message);
        add(newLabel);
        JPanel newPanel = new JPanel();
        newPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        newPanel.setBackground(Color.white);
        newPanel.setLayout(new GridBagLayout());
        newPanel.setMaximumSize(new Dimension(300, 80));
        add(newPanel);
        Collection lstModule = controller.getAvailableModules();
        Iterator lstGroup = controller.getAvailableServiceGroups();
        GridBagConstraints gbc = new GridBagConstraints();
        if (lstModule != null && !lstModule.isEmpty() && lstGroup != null) {
            final JComboBox cmbModule;
            final JComboBox cmbGroup;
            newLabel = new JLabel("Select Module");
            newLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            newPanel.add(newLabel, gbc);
            String[] moduleNames = getModuleNames(lstModule);
            ComboBoxModel cmbModuleModel = new DefaultComboBoxModel(moduleNames);
            cmbModule = new JComboBox();
            cmbModule.setAlignmentX(Component.LEFT_ALIGNMENT);
            cmbModule.setPreferredSize(new Dimension(300, 40));
            cmbModule.setModel(cmbModuleModel);
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.gridx = 1;
            gbc.gridy = 0;
            newPanel.add(cmbModule, gbc);
            newLabel = new JLabel("Select Service Group");
            newLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.gridx = 0;
            gbc.gridy = 1;
            newPanel.add(newLabel, gbc);
            ArrayList<String> groupNames = new ArrayList<String>();
            while (lstGroup.hasNext()) {
                AxisServiceGroup group = (AxisServiceGroup) lstGroup.next();
                groupNames.add(group.getServiceGroupName());
            }
            ComboBoxModel cmbGroupModel = new DefaultComboBoxModel(groupNames.toArray());
            cmbGroup = new JComboBox();
            cmbGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
            cmbGroup.setPreferredSize(new Dimension(300, 40));
            cmbGroup.setModel(cmbGroupModel);
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.gridx = 1;
            gbc.gridy = 1;
            newPanel.add(cmbGroup, gbc);
            JButton newButton = new JButton("Engage");
            newButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    if (controller.engageModuleGroup(cmbModule.getSelectedItem().toString(), cmbGroup.getSelectedItem().toString())) {
                        JOptionPane.showMessageDialog(null, "Module successfully engaged.", "Engage Module", JOptionPane.INFORMATION_MESSAGE);
                        cmbModule.setSelectedIndex(0);
                        cmbGroup.setSelectedIndex(0);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error engaging module", "Engage Module", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            add(newButton);
        } else {
            newLabel = new JLabel("No available module or service group to be engaged.");
            gbc.gridx = 0;
            gbc.gridy = 0;
            newPanel.add(newLabel, gbc);
        }
    }
}

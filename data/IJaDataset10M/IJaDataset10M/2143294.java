package de.iph.arbeitsgruppenassistent.client.resourcemanagement.general;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import de.iph.arbeitsgruppenassistent.server.properties.entity.GroupOrientation;
import de.iph.arbeitsgruppenassistent.server.properties.session.PropertiesAdminRemote;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;

public class GroupSettingPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private PropertiesAdminRemote propsSrv;

    private JRadioButton radVerticalGroupOrientation;

    private JRadioButton radHorizontalGroupOrientation;

    private ButtonGroup grpOrientation;

    public GroupSettingPanel(PropertiesAdminRemote propsSrv) {
        this.propsSrv = propsSrv;
        initGUI();
        setGroupOrientation();
    }

    private void setGroupOrientation() {
        if (propsSrv.getGroupOrientation() == GroupOrientation.VERTICAL) {
            grpOrientation.setSelected(radVerticalGroupOrientation.getModel(), true);
        } else {
            grpOrientation.setSelected(radHorizontalGroupOrientation.getModel(), true);
        }
    }

    private void initGUI() {
        GroupLayout thisLayout = new GroupLayout((JComponent) this);
        this.setLayout(thisLayout);
        this.setPreferredSize(new java.awt.Dimension(170, 63));
        this.setBorder(BorderFactory.createTitledBorder("Gruppenausrichtung"));
        radHorizontalGroupOrientation = new JRadioButton();
        radHorizontalGroupOrientation.setText("Horizontal (per Auftrag)");
        radVerticalGroupOrientation = new JRadioButton();
        radVerticalGroupOrientation.setText("Vertikal (per Arbeitsschritt)");
        grpOrientation = new ButtonGroup();
        grpOrientation.add(radHorizontalGroupOrientation);
        radHorizontalGroupOrientation.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                radHorizontalGroupOrientationActionPerformed(evt);
            }
        });
        grpOrientation.add(radVerticalGroupOrientation);
        radVerticalGroupOrientation.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                radVerticalGroupOrientationActionPerformed(evt);
            }
        });
        thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().add(radHorizontalGroupOrientation, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(radVerticalGroupOrientation, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED));
        thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap().add(thisLayout.createParallelGroup().add(GroupLayout.LEADING, radHorizontalGroupOrientation, 0, 204, Short.MAX_VALUE).add(GroupLayout.LEADING, radVerticalGroupOrientation, 0, 204, Short.MAX_VALUE)).addContainerGap());
    }

    private void radHorizontalGroupOrientationActionPerformed(ActionEvent evt) {
        propsSrv.setGroupOrientation(GroupOrientation.HORIZONTAL);
    }

    private void radVerticalGroupOrientationActionPerformed(ActionEvent evt) {
        propsSrv.setGroupOrientation(GroupOrientation.VERTICAL);
    }
}

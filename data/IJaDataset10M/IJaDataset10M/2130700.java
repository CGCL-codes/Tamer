package VoltIJ.graphics;

import VoltIJ.tools.core.GUITool;
import VoltIJ.tools.core.VoltIJTool;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUICamera extends GUITool implements ActionListener {

    private JSpinner zoomSpinner;

    private JSpinner rotationSpinner;

    private JSpinner moveSpinner;

    private VoltIJTool cam;

    private JSlider frontSlider;

    private JSlider backSlider;

    public GUICamera() {
        createGUI();
    }

    @Override
    public void createGUI() {
        optPanel = new JPanel();
        optPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        GroupLayout camLayout = new GroupLayout(optPanel);
        optPanel.setLayout(camLayout);
        camLayout.setAutoCreateGaps(true);
        camLayout.setAutoCreateContainerGaps(true);
        JButton snapShot = new JButton("Take a snapshot");
        snapShot.addActionListener(this);
        snapShot.setActionCommand("SNAP_COMMAND");
        JLabel frontLabel = new JLabel("Front");
        JLabel backLabel = new JLabel("Back");
        frontSlider = new JSlider(1, 1000);
        frontSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                VP.setProperty("Front", (float) frontSlider.getValue());
                VP.setDirty(true);
            }
        });
        backSlider = new JSlider(1, 1000);
        backSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                VP.setProperty("Back", (float) backSlider.getValue());
                VP.setDirty(true);
            }
        });
        camLayout.setHorizontalGroup(camLayout.createSequentialGroup().addGroup(camLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(frontLabel).addComponent(backLabel)).addGroup(camLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(frontSlider).addComponent(backSlider).addComponent(snapShot)));
        camLayout.setVerticalGroup(camLayout.createSequentialGroup().addGroup(camLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(frontLabel).addComponent(frontSlider)).addGroup(camLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(backLabel).addComponent(backSlider)).addGroup(camLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(snapShot)));
        optPanel.setVisible(false);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("SNAP_COMMAND".equals(command)) {
            VP.setProperty("Snapshot", "true");
            VP.setDirty(true);
        }
    }
}

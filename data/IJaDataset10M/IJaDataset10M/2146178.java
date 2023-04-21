package de.hpi.eworld.scenarios;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import de.hpi.eworld.model.db.data.Area;
import de.hpi.eworld.model.db.data.Area.AreaType;

public class AreaConfigurationDialog extends JDialog {

    private static final long serialVersionUID = -8772525301621489881L;

    private static final String SOURCE_CONFIGURATION_CARD = "SourceConfiguration";

    private static final String DESTINATION_CONFIGURATION_CARD = "DestinationConfiguration";

    private static final String NO_SELECTION_CARD = "NoSelection";

    private CardLayout areaSelectionCardLayout;

    private JPanel areaConfigurationPanel;

    private AreaConfigurationPanel destinationConfigurationPanel, sourceConfigurationPanel;

    public AreaConfigurationDialog(Frame frame) {
        super(frame, "Area Configuration");
        initializeGUI();
        showAreaSelectionCard(NO_SELECTION_CARD);
    }

    private void initializeGUI() {
        setResizable(false);
        areaConfigurationPanel = new JPanel();
        Container contentPane = this.getContentPane();
        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        destinationConfigurationPanel = new DestinationAreaConfigurationPanel();
        sourceConfigurationPanel = new SourceAreaConfigurationPanel();
        areaSelectionCardLayout = new CardLayout();
        areaConfigurationPanel.setLayout(areaSelectionCardLayout);
        areaConfigurationPanel.add(sourceConfigurationPanel, SOURCE_CONFIGURATION_CARD);
        areaConfigurationPanel.add(destinationConfigurationPanel, DESTINATION_CONFIGURATION_CARD);
        areaConfigurationPanel.add(prepareUnselectedPanelCard(), NO_SELECTION_CARD);
        areaConfigurationPanel.setAlignmentX(CENTER_ALIGNMENT);
        JButton confirmationButton = new JButton("Ok");
        confirmationButton.setAlignmentX(RIGHT_ALIGNMENT);
        confirmationButton.setPreferredSize(new Dimension(150, confirmationButton.getPreferredSize().height));
        confirmationButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }
        });
        Component buttonBarGlue = Box.createHorizontalGlue();
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(areaConfigurationPanel).addGroup(layout.createSequentialGroup().addComponent(buttonBarGlue).addComponent(confirmationButton)));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(areaConfigurationPanel).addGroup(layout.createParallelGroup().addComponent(buttonBarGlue).addComponent(confirmationButton)));
        contentPane.setPreferredSize(new Dimension(300, contentPane.getPreferredSize().height));
    }

    private JPanel prepareUnselectedPanelCard() {
        JPanel noSelectionPanel = new JPanel();
        noSelectionPanel.setLayout(new GridBagLayout());
        noSelectionPanel.add(new JLabel("No Area Selected"), new GridBagConstraints());
        return noSelectionPanel;
    }

    public void loadArea(Area area) {
        if (area == null) {
            showAreaSelectionCard(NO_SELECTION_CARD);
        } else if (area.getAreaType() == AreaType.DESTINATION) {
            destinationConfigurationPanel.loadArea(area);
            showAreaSelectionCard(DESTINATION_CONFIGURATION_CARD);
        } else if (area.getAreaType() == AreaType.START) {
            sourceConfigurationPanel.loadArea(area);
            showAreaSelectionCard(SOURCE_CONFIGURATION_CARD);
        } else {
            showAreaSelectionCard(NO_SELECTION_CARD);
        }
        pack();
        centerDialog();
    }

    private void centerDialog() {
        Dimension desktopDimension = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogDimension = getSize();
        setLocation(desktopDimension.width / 2 - dialogDimension.width / 2, desktopDimension.height / 2 - dialogDimension.height / 2);
    }

    private void showAreaSelectionCard(String card) {
        areaSelectionCardLayout.show(areaConfigurationPanel, card);
    }
}

package edu.asu.commons.irrigation.client;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import edu.asu.commons.irrigation.server.ClientData;

/**
 * $Id: MiddleWindowPanel.java 362 2009-11-13 15:14:42Z alllee $
 * 
 * 
 * @author Allen Lee, Sanket Joshi
 */
public class MiddleWindowPanel extends JPanel {

    private static final long serialVersionUID = 2892921110280857458L;

    private List<MiddleScorePanel> middleScorePanels = new ArrayList<MiddleScorePanel>();

    private JLabel positionLabel;

    private JLabel availableWaterLabel;

    private JLabel waterCollectedLabel;

    private JLabel tokensEarnedLabel;

    private JLabel totalTokensEarnedLabel;

    public MiddleWindowPanel() {
        this.setLayout(null);
        this.setBounds(new Rectangle(13, 100 + 100 - 50, 1093, 100 + 50 + 50));
        this.setSize(new Dimension(1093, 200));
        positionLabel = new JLabel("Position");
        positionLabel.setBounds(new Rectangle(30, 10 + 32 + 10, 200, 20));
        positionLabel.setText("Position");
        availableWaterLabel = new JLabel("Available water per second");
        availableWaterLabel.setBounds(new Rectangle(30, 10 + 32 + 10 + 20 + 10, 200, 20));
        waterCollectedLabel = new JLabel("Water collected");
        waterCollectedLabel.setBounds(new Rectangle(30, 10 + 32 + 10 + 20 + 10 + 20 + 10, 150, 20));
        tokensEarnedLabel = new JLabel("Tokens earned");
        tokensEarnedLabel.setBounds(new Rectangle(30, 10 + 32 + 10 + 20 + 10 + 20 + 10 + 20 + 10, 200, 20));
        totalTokensEarnedLabel = new JLabel("Total tokens earned");
        totalTokensEarnedLabel.setBounds(new Rectangle(30, 10 + 32 + 10 + 20 + 10 + 20 + 10 + 20 + 10 + 30, 200, 20));
        this.add(positionLabel, null);
        this.add(availableWaterLabel, null);
        this.add(waterCollectedLabel, null);
        this.add(tokensEarnedLabel, null);
    }

    public void initialize(ClientDataModel clientDataModel) {
        for (MiddleScorePanel panel : middleScorePanels) {
            remove(panel);
        }
        middleScorePanels.clear();
        for (ClientData clientData : clientDataModel.getClientDataSortedByPriority()) {
            int priority = clientData.getPriority();
            MiddleScorePanel middleScorePanel = new MiddleScorePanel(clientDataModel.getPriority(), clientData);
            middleScorePanel.setBounds(new Rectangle((258 + 20 + priority * 198) - 20, 0, 60, 100 + 50 + 50));
            middleScorePanels.add(middleScorePanel);
            add(middleScorePanel, null);
        }
    }

    public void update(ClientDataModel clientDataModel) {
        for (ClientData clientData : clientDataModel.getClientDataSortedByPriority()) {
            middleScorePanels.get(clientData.getPriority()).update(clientData);
        }
    }
}

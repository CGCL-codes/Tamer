package edu.asu.csid.irrigation.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import edu.asu.csid.irrigation.server.ClientData;

/**
 * @author Sanket
 *
 */
public class ActivitySummaryPanelNew extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2393688647176774993L;

    Map<Integer, JProgressBar> availableBandwidthMap = new LinkedHashMap<Integer, JProgressBar>();

    Map<Integer, JLabel> filesDownLoadedMap = new LinkedHashMap<Integer, JLabel>();

    Map<Integer, JLabel> tokensCollectedMap = new LinkedHashMap<Integer, JLabel>();

    JLabel labelA;

    JLabel labelB;

    JLabel labelC;

    JLabel labelD;

    JLabel labelE;

    JLabel titleLabel[] = new JLabel[3];

    private int parameterIndex;

    IrrigationClient client;

    public ActivitySummaryPanelNew(int parameterIndex, IrrigationClient client) {
        super();
        initialize(parameterIndex, client);
    }

    private void initialize(int parameterIndex, IrrigationClient client) {
        this.parameterIndex = parameterIndex;
        this.client = client;
        this.setLayout(null);
        for (int i = 0; i < client.getRoundConfiguration().getClientsPerGroup(); i++) {
            System.out.println("I reach here");
            switch(i) {
                case 0:
                    labelA = new JLabel();
                    labelA.setBounds(new Rectangle(0, 30 + 15 * (i + i), 25, 15));
                    if (client.getClientGameState().getPriority() == i) {
                        labelA.setText("YOU");
                    } else labelA.setText("A");
                    this.add(labelA);
                    break;
                case 1:
                    labelB = new JLabel();
                    labelB.setBounds(new Rectangle(0, 30 + 15 * (i + i), 25, 15));
                    if (client.getClientGameState().getPriority() == i) {
                        labelB.setText("YOU");
                    } else labelB.setText("B");
                    this.add(labelB);
                    break;
                case 2:
                    labelC = new JLabel();
                    labelC.setBounds(new Rectangle(0, 30 + 15 * (i + i), 25, 15));
                    if (client.getClientGameState().getPriority() == i) {
                        labelC.setText("YOU");
                    } else labelC.setText("C");
                    this.add(labelC);
                    break;
                case 3:
                    labelD = new JLabel();
                    labelD.setBounds(new Rectangle(0, 30 + 15 * (i + i), 25, 15));
                    if (client.getClientGameState().getPriority() == i) {
                        labelD.setText("YOU");
                    } else labelD.setText("D");
                    this.add(labelD);
                    break;
                case 4:
                    labelE = new JLabel();
                    labelE.setBounds(new Rectangle(0, 30 + 15 * (i + i), 25, 15));
                    if (client.getClientGameState().getPriority() == i) {
                        labelE.setText("YOU");
                    } else labelE.setText("E");
                    this.add(labelE);
                    break;
            }
        }
        switch(parameterIndex) {
            case 0:
                this.setBackground(Color.PINK);
                titleLabel[0] = new JLabel();
                titleLabel[0].setText("Available Flow Capacity");
                titleLabel[0].setBounds(new Rectangle(60, 5, 200, 20));
                this.add(titleLabel[0], null);
                for (int i = 0; i < client.getRoundConfiguration().getClientsPerGroup(); i++) {
                    JProgressBar availableBandwidthProgressBar = new JProgressBar();
                    availableBandwidthProgressBar.setMaximum(25);
                    availableBandwidthProgressBar.setBounds(new Rectangle(40, 30 + 15 * (i + i), 150, 15));
                    availableBandwidthProgressBar.setStringPainted(true);
                    if (i == client.getClientGameState().getPriority()) {
                        availableBandwidthProgressBar.setForeground(Color.ORANGE);
                    } else availableBandwidthProgressBar.setForeground(new Color(51, 153, 255));
                    availableBandwidthProgressBar.setBackground(Color.white);
                    availableBandwidthProgressBar.setValue(25);
                    this.add(availableBandwidthProgressBar);
                    availableBandwidthMap.put(new Integer(i), availableBandwidthProgressBar);
                }
                break;
            case 1:
                this.setBackground(Color.WHITE);
                titleLabel[1] = new JLabel();
                titleLabel[1].setText("Crops Grown");
                titleLabel[1].setBounds(new Rectangle(60, 5, 200, 20));
                this.add(titleLabel[1], null);
                for (int i = 0; i < client.getRoundConfiguration().getClientsPerGroup(); i++) {
                    JLabel filesDownLoaded = new JLabel();
                    filesDownLoaded.setFont(new Font("Serif", Font.BOLD, 14));
                    if (i == client.getClientGameState().getPriority()) {
                        filesDownLoaded.setForeground(Color.ORANGE);
                    } else filesDownLoaded.setForeground(new Color(51, 153, 255));
                    filesDownLoaded.setBounds(new Rectangle(132, 30 + 15 * (i + i), 100, 15));
                    this.add(filesDownLoaded);
                    filesDownLoaded.setText("");
                    filesDownLoadedMap.put(new Integer(i), filesDownLoaded);
                }
                break;
            case 2:
                this.setBackground(Color.WHITE);
                titleLabel[2] = new JLabel();
                titleLabel[2].setText("Tokens Collected");
                titleLabel[2].setBounds(new Rectangle(60, 5, 200, 20));
                this.add(titleLabel[2], null);
                for (int i = 0; i < client.getRoundConfiguration().getClientsPerGroup(); i++) {
                    JLabel tokensCollected = new JLabel();
                    tokensCollected.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
                    if (i == client.getClientGameState().getPriority()) {
                        tokensCollected.setForeground(Color.ORANGE);
                    } else tokensCollected.setForeground(new Color(51, 153, 255));
                    tokensCollected.setBounds(new Rectangle(132, 30 + 15 * (i + i), 100, 15));
                    this.add(tokensCollected);
                    tokensCollected.setText("");
                    tokensCollectedMap.put(new Integer(i), tokensCollected);
                }
                break;
        }
    }

    public void update(ClientData clientData) {
        switch(parameterIndex) {
            case 0:
                availableBandwidthMap.get(new Integer(clientData.getPriority())).setValue((int) clientData.getAvailableFlowCapacity());
                break;
            case 1:
                filesDownLoadedMap.get(new Integer(clientData.getPriority())).setText(new Integer(clientData.getCropsGrown()).toString());
                break;
            case 2:
                tokensCollectedMap.get(new Integer(clientData.getPriority())).setText(new Integer(clientData.getAward()).toString());
                break;
        }
    }

    public void endRound() {
    }
}

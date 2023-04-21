package settlers.game.gui;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.JFrame;
import settlers.game.GameState;
import settlers.game.*;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class BottomPanel extends javax.swing.JPanel {

    public SettlersGUI parent;

    private ButtonPanel buttonPanel;

    private JPanel turnStartPanel;

    private TabbedPanel tabbedPanel;

    public JButton startButton;

    private JLabel startLabel;

    public BottomPanel(SettlersGUI _parent) {
        super();
        parent = _parent;
        initGUI();
    }

    private void initGUI() {
        try {
            this.setPreferredSize(new java.awt.Dimension(795, 150));
            AnchorLayout thisLayout = new AnchorLayout();
            this.setLayout(thisLayout);
            this.setSize(795, 150);
            {
                tabbedPanel = new TabbedPanel();
                this.add(tabbedPanel, new AnchorConstraint(3, 985, 990, 439, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
                tabbedPanel.setPreferredSize(new java.awt.Dimension(434, 148));
            }
            {
                buttonPanel = new ButtonPanel(this, parent.getSettlersEvent());
                this.add(buttonPanel, new AnchorConstraint(0, 440, 0, -1, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
            }
            {
                turnStartPanel = new JPanel();
                turnStartPanel.setVisible(false);
                turnStartPanel.setSize(this.getWidth(), this.getHeight());
                {
                    startLabel = new JLabel();
                    turnStartPanel.add(startLabel);
                }
                {
                    startButton = new JButton("Press to start");
                    startButton.addActionListener(ContainerGUI.gameButtons);
                    turnStartPanel.add(startButton);
                }
                this.add(turnStartPanel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ButtonPanel getButtonPanel() {
        return buttonPanel;
    }

    public TabbedPanel getTabbedPanel() {
        return tabbedPanel;
    }

    public void turnStart() {
        startLabel.setText(GameState.getCurPlayer().getName() + ": BEGIN YOUR TURN");
        this.setBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2));
        buttonPanel.setVisible(false);
        tabbedPanel.setVisible(false);
        turnStartPanel.setVisible(true);
        this.validate();
    }

    public void hideTurnStart() {
        turnStartPanel.setVisible(false);
        buttonPanel.setVisible(true);
        tabbedPanel.setVisible(true);
        this.validate();
    }

    public SettlersGUI getGUI() {
        return parent;
    }
}

package jskat.gui.main;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.JPanel;
import java.net.URL;
import java.util.Observer;
import java.util.Observable;
import jskat.control.JSkatMaster;
import jskat.data.JSkatDataModel;
import jskat.share.JSkatStates;
import jskat.gui.JSkatGraphicRepository;

/**
 *
 * @author  Jan Sch�fer <jan.schaefer@b0n541.net>
 */
public class JSkatPlayArea extends JPanel implements Observer {

    /** Creates a new instance of JSkatPlayArea */
    public JSkatPlayArea(JSkatMaster jskatMaster, JSkatDataModel dataModel, JSkatGraphicRepository jskatBitmaps) {
        this.jskatMaster = jskatMaster;
        jskatMaster.addObserver(this);
        this.dataModel = dataModel;
        this.jskatBitmaps = jskatBitmaps;
        initComponents();
    }

    private void initComponents() {
        setLayout(new java.awt.GridLayout(3, 1));
        setPreferredSize(new Dimension(800, 500));
        opponentPanel = new JPanel();
        opponentPanel.setLayout(new GridLayout(1, 2));
        opponentPanel.setOpaque(false);
        opponentPanel.add(new CardHoldingPanel(jskatMaster, dataModel, 0, CardHoldingPanel.OPPONENT_PANEL, 10, jskatBitmaps));
        opponentPanel.add(new CardHoldingPanel(jskatMaster, dataModel, 1, CardHoldingPanel.OPPONENT_PANEL, 10, jskatBitmaps));
        add(opponentPanel);
        skatTrickHoldingPanel = new JPanel();
        skatTrickHoldingPanel.setOpaque(false);
        skatTrickHoldingPanel.setLayout(new CardLayout());
        trickPanel = new JPanel();
        trickPanel.setOpaque(false);
        trickPanel.add(new CardHoldingPanel(jskatMaster, dataModel, -1, CardHoldingPanel.TRICK_PANEL, 3, jskatBitmaps));
        skatTrickHoldingPanel.add(trickPanel, "trick");
        skatPanel = new JPanel();
        skatPanel.setOpaque(false);
        skatPanel.add(new CardHoldingPanel(jskatMaster, dataModel, -1, CardHoldingPanel.SKAT_PANEL, 4, jskatBitmaps));
        skatTrickHoldingPanel.add(skatPanel, "skat");
        bidPanel = new BiddingPanel(jskatMaster);
        jskatMaster.getBidStatus().addObserver(bidPanel);
        skatTrickHoldingPanel.add(bidPanel, "bidding");
        add(skatTrickHoldingPanel);
        playerPanel = new JPanel();
        playerPanel.setOpaque(false);
        playerPanel.add(new CardHoldingPanel(jskatMaster, dataModel, 2, CardHoldingPanel.PLAYER_PANEL, 10, jskatBitmaps));
        add(playerPanel);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(jskatBitmaps.getSkatTableImage(), 0, 0, this);
    }

    public void update(Observable o, Object arg) {
        if (o instanceof JSkatMaster) {
            if (arg instanceof Integer) {
                int newState = ((Integer) arg).intValue();
                switch(newState) {
                    case (JSkatStates.DEALING):
                    case (JSkatStates.PLAYING):
                        ((CardLayout) skatTrickHoldingPanel.getLayout()).show(skatTrickHoldingPanel, "trick");
                        break;
                    case (JSkatStates.BIDDING):
                        ((CardLayout) skatTrickHoldingPanel.getLayout()).show(skatTrickHoldingPanel, "bidding");
                        break;
                    case (JSkatStates.SHOWING_SKAT):
                        ((CardLayout) skatTrickHoldingPanel.getLayout()).show(skatTrickHoldingPanel, "skat");
                        break;
                }
            }
        }
    }

    private JSkatMaster jskatMaster;

    private JSkatDataModel dataModel;

    private JSkatGraphicRepository jskatBitmaps;

    private JPanel opponentPanel;

    private JPanel skatTrickHoldingPanel;

    private JPanel skatPanel;

    private JPanel trickPanel;

    private BiddingPanel bidPanel;

    private JPanel playerPanel;
}

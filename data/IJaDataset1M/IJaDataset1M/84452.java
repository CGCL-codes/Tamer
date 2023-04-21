package org.icehockeymanager.ihm.clients.ihmgui.gui.lineup;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import org.icehockeymanager.ihm.clients.ihmgui.controller.ClientController;
import org.icehockeymanager.ihm.clients.ihmgui.gui.lib.JIhmLabel;
import org.icehockeymanager.ihm.clients.ihmgui.gui.lib.JIhmPanel;
import org.icehockeymanager.ihm.clients.ihmgui.gui.lib.JIhmTable;
import org.icehockeymanager.ihm.clients.ihmgui.ihm.player.PlayerTools;
import org.icehockeymanager.ihm.clients.ihmgui.ihm.player.TMPlayerAttributes;
import org.icehockeymanager.ihm.game.player.Player;
import org.icehockeymanager.ihm.game.player.fieldplayer.FieldPlayer;
import org.icehockeymanager.ihm.game.player.goalie.Goalie;
import org.icehockeymanager.ihm.game.tactics.Block;

/**
 * The Class PanelBlock.
 */
public class PanelBlock extends JIhmPanel {

    /** The Constant serialVersionUID. */
    static final long serialVersionUID = IHM_SWING_COMPONENT_VERSION;

    /** The lbl background. */
    private JIhmLabel lblBackground;

    /** The lbl goalie. */
    PlayerLabel lblGoalie;

    /** The lbl center. */
    PlayerLabel lblCenter;

    /** The lbl left wing. */
    PlayerLabel lblLeftWing;

    /** The lbl right defense. */
    PlayerLabel lblRightDefense;

    /** The lbl left defense. */
    PlayerLabel lblLeftDefense;

    /** The lbl right wing. */
    PlayerLabel lblRightWing;

    /** The block. */
    Block block = null;

    /** The player table. */
    JIhmTable playerTable = null;

    /**
   * Instantiates a new panel block.
   */
    public PanelBlock() {
        try {
            initGUI();
            ihmInit();
        } catch (Exception e) {
            String msg = "GUI Exception!";
            logger.log(Level.SEVERE, msg, e);
            ClientController.getInstance().showError(msg, e);
        }
    }

    /**
   * Inits the GUI.
   */
    private void initGUI() {
        {
            this.setLayout(null);
            this.setPreferredSize(new java.awt.Dimension(330, 360));
            {
                lblBackground = new JIhmLabel();
                this.add(lblBackground);
                lblBackground.setPreferredSize(new java.awt.Dimension(400, 355));
            }
            lblBackground.setIcon(new ImageIcon(ClientController.getInstance().getGuiResource("BlockSetup.png")));
            lblBackground.setBounds(0, 0, 330, 360);
            {
                lblRightDefense = new PlayerLabel();
                lblBackground.add(lblRightDefense);
                lblRightDefense.setText("RD");
                lblRightDefense.setBounds(170, 245, 140, 20);
                lblRightDefense.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            {
                lblRightWing = new PlayerLabel();
                lblBackground.add(lblRightWing);
                lblRightWing.setText("RW");
                lblRightWing.setBounds(180, 90, 140, 20);
                lblRightWing.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            {
                lblGoalie = new PlayerLabel();
                lblBackground.add(lblGoalie);
                lblGoalie.setText("GOALIE");
                lblGoalie.setBounds(95, 300, 140, 20);
                lblGoalie.setHorizontalAlignment(SwingConstants.CENTER);
            }
            {
                lblLeftWing = new PlayerLabel();
                lblBackground.add(lblLeftWing);
                lblLeftWing.setText("LW");
                lblLeftWing.setBounds(10, 90, 140, 20);
            }
            {
                lblLeftDefense = new PlayerLabel();
                lblBackground.add(lblLeftDefense);
                lblLeftDefense.setText("LD");
                lblLeftDefense.setBounds(20, 245, 140, 20);
            }
            {
                lblCenter = new PlayerLabel();
                lblBackground.add(lblCenter);
                lblCenter.setText("CENTER");
                lblCenter.setHorizontalAlignment(SwingConstants.CENTER);
                lblCenter.setBounds(95, 40, 140, 20);
            }
        }
    }

    /**
   * Ihm init.
   */
    private void ihmInit() {
    }

    /**
   * Sets the block.
   * 
   * @param blockToSet the block to set
   * @param playerTable the player table
   */
    public void setBlock(Block blockToSet, JIhmTable playerTable) {
        this.block = blockToSet;
        this.playerTable = playerTable;
        if (block.getBlockType() == Block.BLOCK_NORMAL || block.getBlockType() == Block.BLOCK_POWERPLAY) {
            this.lblCenter.setEnabled(true);
            this.lblCenter.setVisible(true);
            this.lblCenter.setText(PlayerTools.getShortName(block.getCenter()));
            this.lblCenter.setTransferHandler(new LineUpTransferHelper());
            this.lblCenter.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    block.setCenter(getSelectedFieldPlayer());
                    lblCenter.setText(PlayerTools.getShortName(block.getCenter()));
                }
            });
        } else {
            this.lblCenter.setEnabled(false);
            this.lblCenter.setVisible(false);
            this.lblCenter.setTransferHandler(null);
        }
        this.lblLeftWing.setText(PlayerTools.getShortName(block.getLeftWing()));
        this.lblLeftWing.setTransferHandler(new LineUpTransferHelper());
        this.lblLeftWing.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                block.setLeftWing(getSelectedFieldPlayer());
                lblLeftWing.setText(PlayerTools.getShortName(block.getLeftWing()));
            }
        });
        this.lblRightWing.setText(PlayerTools.getShortName(block.getRightWing()));
        this.lblRightWing.setTransferHandler(new LineUpTransferHelper());
        this.lblRightWing.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                block.setRightWing(getSelectedFieldPlayer());
                lblRightWing.setText(PlayerTools.getShortName(block.getRightWing()));
            }
        });
        this.lblLeftDefense.setText(PlayerTools.getShortName(block.getLeftDefender()));
        this.lblLeftDefense.setTransferHandler(new LineUpTransferHelper());
        this.lblLeftDefense.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                block.setLeftDefender(getSelectedFieldPlayer());
                lblLeftDefense.setText(PlayerTools.getShortName(block.getLeftDefender()));
            }
        });
        this.lblRightDefense.setText(PlayerTools.getShortName(block.getRightDefender()));
        this.lblRightDefense.setTransferHandler(new LineUpTransferHelper());
        this.lblRightDefense.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                block.setRightDefender(getSelectedFieldPlayer());
                lblRightDefense.setText(PlayerTools.getShortName(block.getRightDefender()));
            }
        });
        this.lblGoalie.setText(PlayerTools.getShortName(block.getGoalie()));
        this.lblGoalie.setTransferHandler(new LineUpTransferHelper());
        this.lblGoalie.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                block.setGoalie(getSelectedGoalie());
                lblGoalie.setText(PlayerTools.getShortName(block.getGoalie()));
            }
        });
    }

    /**
   * Gets the selected field player.
   * 
   * @return the selected field player
   */
    FieldPlayer getSelectedFieldPlayer() {
        TMPlayerAttributes ta = (TMPlayerAttributes) playerTable.getModel();
        int row = playerTable.getSelectedRow();
        Player player = ta.getPlayer(row);
        if (player instanceof FieldPlayer) {
            return (FieldPlayer) player;
        }
        return null;
    }

    /**
   * Gets the selected goalie.
   * 
   * @return the selected goalie
   */
    Goalie getSelectedGoalie() {
        TMPlayerAttributes ta = (TMPlayerAttributes) playerTable.getModel();
        int row = playerTable.getSelectedRow();
        Player player = ta.getPlayer(row);
        if (player instanceof Goalie) {
            return (Goalie) player;
        }
        return null;
    }
}

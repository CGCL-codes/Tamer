package src.gui;

import java.awt.*;
import javax.swing.*;
import src.Constants;
import src.game.Messages;
import src.game.Player;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HospitalShop extends JFrame {

    private static final long serialVersionUID = src.Constants.serialVersionUID;

    JLabel lblHealth = new JLabel();

    JLabel lblHealthNum = new JLabel();

    JLabel lblPrice = new JLabel();

    JLabel lblPriceNum = new JLabel();

    JButton btnMore = new JButton();

    JButton btnLess = new JButton();

    JButton btnOK = new JButton();

    JButton btnCancel = new JButton();

    GridLayout gridLayout1 = new GridLayout();

    private Controller controller;

    public HospitalShop(Controller controller) {
        try {
            this.controller = controller;
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        getContentPane().setLayout(gridLayout1);
        lblHealth.setHorizontalAlignment(SwingConstants.RIGHT);
        lblHealth.setHorizontalTextPosition(SwingConstants.CENTER);
        lblHealth.setText("Hit Points:");
        lblHealthNum.setText("0");
        lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPrice.setText("Price:");
        lblPriceNum.setText("0");
        btnMore.setText("More");
        btnMore.addActionListener(new HospitalShop_btnMore_actionAdapter(this));
        btnLess.setText("Less");
        btnLess.addActionListener(new HospitalShop_btnLess_actionAdapter(this));
        btnOK.setText("OK");
        btnOK.addActionListener(new HospitalShop_btnOK_actionAdapter(this));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new HospitalShop_btnCancel_actionAdapter(this));
        this.getContentPane().add(lblHealth, null);
        this.getContentPane().add(lblHealthNum, null);
        this.getContentPane().add(lblPrice, null);
        this.getContentPane().add(lblPriceNum, null);
        this.getContentPane().add(btnMore, null);
        gridLayout1.setColumns(2);
        gridLayout1.setHgap(5);
        gridLayout1.setRows(4);
        gridLayout1.setVgap(5);
        this.getContentPane().add(btnLess, null);
        this.getContentPane().add(btnOK, null);
        this.getContentPane().add(btnCancel, null);
        setPriceLabels();
        setSize(170, 140);
        setTitle("Hospital");
        src.Constants.centerFrame(this);
        setVisible(true);
    }

    /**
	 * Closes the window.
	 */
    private void closeWindow() {
        this.dispose();
    }

    /**
	 * Sets the price of the labels on the Hospital GUI window.
	 */
    private void setPriceLabels() {
        Player player = controller.getLocalPlayer();
        int neededHealth = player.getMaxHitPoints() - player.getHitPoints();
        if (player.getMoney() >= neededHealth / src.Constants.HP_PER_DOLLAR) {
            lblHealthNum.setText(String.valueOf(neededHealth));
            lblPriceNum.setText(String.valueOf(neededHealth / Constants.HP_PER_DOLLAR + 1));
        } else {
            lblPriceNum.setText(String.valueOf(player.getMoney()));
            lblHealthNum.setText(String.valueOf(player.getMoney() * Constants.HP_PER_DOLLAR));
        }
    }

    /**
	 * Action performed for the more button.
	 * @param e
	 */
    public void btnMore_actionPerformed(ActionEvent e) {
        Player player = controller.getLocalPlayer();
        if (player.getHitPoints() + Integer.parseInt(lblHealthNum.getText()) >= player.getMaxHitPoints() || Integer.parseInt(lblPriceNum.getText()) == player.getMoney()) {
            return;
        } else {
            if (Integer.parseInt(lblHealthNum.getText()) + 3 >= player.getMaxHitPoints()) {
                lblHealthNum.setText(String.valueOf(player.getMaxHitPoints()));
                lblPriceNum.setText(String.valueOf(player.getMaxHitPoints() / Constants.HP_PER_DOLLAR + 1));
            } else {
                lblHealthNum.setText(String.valueOf(Integer.parseInt(lblHealthNum.getText()) + 3));
                lblPriceNum.setText(String.valueOf(Integer.parseInt(lblHealthNum.getText()) / Constants.HP_PER_DOLLAR + 1));
            }
        }
    }

    /**
	 * Action performed for the less button.
	 * @param e
	 */
    public void btnLess_actionPerformed(ActionEvent e) {
        if (Integer.parseInt(lblHealthNum.getText()) - 3 <= 0) return; else {
            lblHealthNum.setText(String.valueOf(Integer.parseInt(lblHealthNum.getText()) - 3));
            lblPriceNum.setText(String.valueOf(Integer.parseInt(lblHealthNum.getText()) / Constants.HP_PER_DOLLAR + 1));
        }
    }

    /**
	 * Action performed for the OK button.
	 * @param e
	 */
    public void btnOK_actionPerformed(ActionEvent e) {
        Player player = controller.getLocalPlayer();
        player.removeMoney(Integer.parseInt(lblPriceNum.getText()));
        player.heal(Integer.parseInt(lblHealthNum.getText()));
        if (Integer.parseInt(lblHealthNum.getText()) > 0) {
            Messages.getInstance().addMessage("You just purchased " + Integer.parseInt(lblHealthNum.getText()) + " hit points.");
            Messages.getInstance().addMessage("You feel much better.");
        }
        controller.activateEnemyAI();
        closeWindow();
    }

    /**
	 * Action performed for the cancel button.
	 * @param e
	 */
    public void btnCancel_actionPerformed(ActionEvent e) {
        closeWindow();
    }
}

class HospitalShop_btnCancel_actionAdapter implements ActionListener {

    private HospitalShop adaptee;

    HospitalShop_btnCancel_actionAdapter(HospitalShop adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnCancel_actionPerformed(e);
    }
}

class HospitalShop_btnOK_actionAdapter implements ActionListener {

    private HospitalShop adaptee;

    HospitalShop_btnOK_actionAdapter(HospitalShop adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnOK_actionPerformed(e);
    }
}

class HospitalShop_btnLess_actionAdapter implements ActionListener {

    private HospitalShop adaptee;

    HospitalShop_btnLess_actionAdapter(HospitalShop adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnLess_actionPerformed(e);
    }
}

class HospitalShop_btnMore_actionAdapter implements ActionListener {

    private HospitalShop adaptee;

    HospitalShop_btnMore_actionAdapter(HospitalShop adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnMore_actionPerformed(e);
    }
}

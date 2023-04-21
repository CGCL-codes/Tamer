package edu.bsu.monopoly.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.bsu.monopoly.game.GameOptions;
import edu.bsu.monopoly.game.MonopdGame;
import edu.bsu.monopoly.game.player.Player;

public class InitializeGameDialog extends JDialog {

    /**
	 * A dialog for config game rules, it will pop up then user create a new
	 * game
	 * 
	 * @author Jipeng Tan
	 */
    private final Logger log = LoggerFactory.getLogger(getClass());

    private JCheckBox a;

    private JCheckBox b;

    private JCheckBox c;

    private JCheckBox d;

    private JCheckBox e;

    private JCheckBox f;

    private JCheckBox g;

    private JCheckBox h;

    private JCheckBox i;

    private JButton bStart;

    private JButton bCancel;

    private JPanel checkPanel;

    private JPanel choosePanel;

    private MonopdGame player;

    private GuiFrame gframe;

    private ResourceBundle resStrings = ResourceBundle.getBundle("test");

    public InitializeGameDialog(GuiFrame gframe, MonopdGame player) {
        this.player = player;
        this.gframe = gframe;
        initalize();
    }

    public InitializeGameDialog(MonopdGame player) {
        this.player = player;
        initalize();
    }

    private void initalize() {
        setTitle("Initialize Game");
        setLayout(new BorderLayout());
        setSize(new Dimension(370, 300));
        TitledBorder gameRules = BorderFactory.createTitledBorder("Game Rules");
        checkPanel = new JPanel();
        checkPanel.setBorder(gameRules);
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
        a = new JCheckBox(resStrings.getString("Free_Parking_collects_fines"));
        a.setMnemonic(KeyEvent.VK_F);
        a.addItemListener(itemListener);
        b = new JCheckBox(resStrings.getString("Always_shuffle_decks_before_taking_a_card"));
        b.setMnemonic(KeyEvent.VK_A);
        b.addItemListener(itemListener);
        c = new JCheckBox(resStrings.getString("Enable_auctions"));
        c.setMnemonic(KeyEvent.VK_E);
        c.addItemListener(itemListener);
        c.setSelected(true);
        d = new JCheckBox(resStrings.getString("Double_pass_money_on_exact_landings"));
        d.setMnemonic(KeyEvent.VK_D);
        d.addItemListener(itemListener);
        e = new JCheckBox(resStrings.getString("Bank_provides_unlimited_amount_of_houses/hotels"));
        e.setMnemonic(KeyEvent.VK_B);
        e.addItemListener(itemListener);
        f = new JCheckBox(resStrings.getString("Players_in_Jail_get_no_rent"));
        f.setMnemonic(KeyEvent.VK_P);
        f.addItemListener(itemListener);
        g = new JCheckBox(resStrings.getString("Allow_estates_to_be_sold_back_to_Bank"));
        g.setMnemonic(KeyEvent.VK_O);
        g.addItemListener(itemListener);
        h = new JCheckBox(resStrings.getString("Automate_tax_decisions"));
        h.setMnemonic(KeyEvent.VK_T);
        h.addItemListener(itemListener);
        i = new JCheckBox(resStrings.getString("Allow_spectators"));
        i.setMnemonic(KeyEvent.VK_W);
        i.addItemListener(itemListener);
        checkPanel.add(a);
        checkPanel.add(b);
        checkPanel.add(c);
        checkPanel.add(d);
        checkPanel.add(e);
        checkPanel.add(f);
        checkPanel.add(g);
        checkPanel.add(h);
        checkPanel.add(i);
        choosePanel = new JPanel(new GridLayout(0, 2));
        bStart = new JButton(resStrings.getString("OK"));
        bStart.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bCancel = new JButton(resStrings.getString("Cancel"));
        bCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                player.leaveGame();
                gframe.bJoinGame.setText((resStrings.getString("JoinGame")));
                dispose();
            }
        });
        choosePanel.add(bStart);
        choosePanel.add(bCancel);
        add(checkPanel, BorderLayout.CENTER);
        add(choosePanel, BorderLayout.SOUTH);
    }

    /** Listens to the check boxes. */
    private ItemListener itemListener = new ItemListener() {

        public void itemStateChanged(ItemEvent evt) {
            Object source = evt.getItemSelectable();
            if (source == a && evt.getStateChange() == ItemEvent.SELECTED) {
                player.sendOptionUpdate(GameOptions.Enable.FREEPARKING_COLLECT_FINES);
                log.info("Select Free Parking collects fines");
            } else if (source == b && evt.getStateChange() == ItemEvent.SELECTED) {
                player.sendOptionUpdate(GameOptions.Enable.ALWAYS_SHUFFLE_DECKS);
                log.info("Select Always shuffle decks before taking a card");
            } else if (source == c && evt.getStateChange() == ItemEvent.SELECTED) {
                player.sendOptionUpdate(GameOptions.Enable.AUCTIONS);
                System.out.println("Select Enable auctions");
            } else if (source == d && evt.getStateChange() == ItemEvent.SELECTED) {
                player.sendOptionUpdate(GameOptions.Enable.DOUBLE_PASS_MONEY);
                System.out.println("Select Double pass money on exact landings");
            } else if (source == e && evt.getStateChange() == ItemEvent.SELECTED) {
                player.sendOptionUpdate(GameOptions.Enable.UNLIMITED_HOUSES);
                System.out.println("Select Bank provides unlimited amount of houses/hotels");
            } else if (source == f && evt.getStateChange() == ItemEvent.SELECTED) {
                player.sendOptionUpdate(GameOptions.Enable.JAILED_GET_RENT);
                System.out.println("Select Players in Jail get no rent");
            } else if (source == g && evt.getStateChange() == ItemEvent.SELECTED) {
                player.sendOptionUpdate(GameOptions.Enable.SELL_TO_BANK);
                System.out.println("Select Allow estates to be sold back to Bank");
            } else if (source == h && evt.getStateChange() == ItemEvent.SELECTED) {
                player.sendOptionUpdate(GameOptions.Enable.AUTOMATE_TAX);
                System.out.println("Select Automate tax decisions");
            } else if (source == i && evt.getStateChange() == ItemEvent.SELECTED) {
                player.sendOptionUpdate(GameOptions.Enable.ALLOW_SPECTATORS);
                System.out.println("Select Allow spectators");
            }
            if (source == a && evt.getStateChange() == ItemEvent.DESELECTED) {
                player.sendOptionUpdate(GameOptions.Disable.FREEPARKING_COLLECT_FINES);
                System.out.println("Dis Select Free Parking collects fines");
            } else if (source == b && evt.getStateChange() == ItemEvent.DESELECTED) {
                player.sendOptionUpdate(GameOptions.Disable.ALWAYS_SHUFFLE_DECKS);
                System.out.println("DeSelect Always shuffle decks before taking a card");
            } else if (source == c && evt.getStateChange() == ItemEvent.DESELECTED) {
                player.sendOptionUpdate(GameOptions.Disable.AUCTIONS);
                System.out.println("DeSelect Enable auctions");
            } else if (source == d && evt.getStateChange() == ItemEvent.DESELECTED) {
                player.sendOptionUpdate(GameOptions.Disable.DOUBLE_PASS_MONEY);
                System.out.println("DeSelect Double pass money on exact landings");
            } else if (source == e && evt.getStateChange() == ItemEvent.DESELECTED) {
                player.sendOptionUpdate(GameOptions.Disable.UNLIMITED_HOUSES);
                System.out.println("DeSelect Bank provides unlimited amount of houses/hotels");
            } else if (source == f && evt.getStateChange() == ItemEvent.DESELECTED) {
                player.sendOptionUpdate(GameOptions.Disable.JAILED_GET_RENT);
                System.out.println("DeSelect Players in Jail get no rent");
            } else if (source == g && evt.getStateChange() == ItemEvent.DESELECTED) {
                player.sendOptionUpdate(GameOptions.Disable.SELL_TO_BANK);
                System.out.println("DeSelect Allow estates to be sold back to Bank");
            } else if (source == h && evt.getStateChange() == ItemEvent.DESELECTED) {
                player.sendOptionUpdate(GameOptions.Disable.AUTOMATE_TAX);
                System.out.println("DeSelect Automate tax decisions");
            } else if (source == i && evt.getStateChange() == ItemEvent.DESELECTED) {
                player.sendOptionUpdate(GameOptions.Disable.ALLOW_SPECTATORS);
                System.out.println("DeSelect Allow spectators");
            }
        }
    };
}

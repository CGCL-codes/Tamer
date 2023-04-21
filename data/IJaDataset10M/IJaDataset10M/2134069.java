package net.sf.evemsp.swa.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.sf.evemsp.api.CharRecord;

public class CharMainPanel extends JPanel {

    private CharRecord charRecord;

    private CharImageCmp characterImage;

    private CharSkillsPnl skillsPnl;

    private CharDetailsPnl charDetailsPnl;

    public CharMainPanel(CharRecord charRecord) {
        super(new GridBagLayout());
        setCharRecord(charRecord);
        setAlignmentX(0.0f);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        characterImage = new CharImageCmp(charRecord);
        add(characterImage, c);
        skillsPnl = new CharSkillsPnl(charRecord);
        c.gridy++;
        charDetailsPnl = new CharDetailsPnl(charRecord, skillsPnl);
        add(charDetailsPnl, c);
        JMouseTabbedPane tabbedPane = new JMouseTabbedPane();
        tabbedPane.addTab("Skills", new JScrollPane(skillsPnl));
        JMouseTabbedPane charWallet = new JMouseTabbedPane();
        charWallet.addTab("Transactions", new JLabel("TODO: Transactions"));
        charWallet.addTab("Journal", new JLabel("TODO: Journal"));
        tabbedPane.addTab("Personal Wallet", charWallet);
        JMouseTabbedPane corpWallets = new JMouseTabbedPane();
        corpWallets.add("Accounts", new JLabel("TODO: Account list"));
        JMouseTabbedPane corpWallet = new JMouseTabbedPane();
        corpWallet.addTab("Transactions", new JLabel("TODO: Transactions"));
        corpWallet.addTab("Journal", new JLabel("TODO: Journal"));
        corpWallets.add("Account 1", corpWallet);
        corpWallet = new JMouseTabbedPane();
        corpWallet.addTab("Transactions", new JLabel("TODO: Transactions"));
        corpWallet.addTab("Journal", new JLabel("TODO: Journal"));
        corpWallets.add("Account 2", corpWallet);
        corpWallet = new JMouseTabbedPane();
        corpWallet.addTab("Transactions", new JLabel("TODO: Transactions"));
        corpWallet.addTab("Journal", new JLabel("TODO: Journal"));
        corpWallets.add("Account 3", corpWallet);
        corpWallet = new JMouseTabbedPane();
        corpWallet.addTab("Transactions", new JLabel("TODO: Transactions"));
        corpWallet.addTab("Journal", new JLabel("TODO: Journal"));
        corpWallets.add("Account 4", corpWallet);
        corpWallet = new JMouseTabbedPane();
        corpWallet.addTab("Transactions", new JLabel("TODO: Transactions"));
        corpWallet.addTab("Journal", new JLabel("TODO: Journal"));
        corpWallets.add("Account 5", corpWallet);
        corpWallet = new JMouseTabbedPane();
        corpWallet.addTab("Transactions", new JLabel("TODO: Transactions"));
        corpWallet.addTab("Journal", new JLabel("TODO: Journal"));
        corpWallets.add("Account 6", corpWallet);
        corpWallet = new JMouseTabbedPane();
        corpWallet.addTab("Transactions", new JLabel("TODO: Transactions"));
        corpWallet.addTab("Journal", new JLabel("TODO: Journal"));
        corpWallets.add("Account 7", corpWallet);
        tabbedPane.addTab("Corporate Wallets", corpWallets);
        tabbedPane.addTab("Member tracking", new JLabel("TODO: Tracking"));
        tabbedPane.addTab("Alliances", new JLabel("TODO: Alliances"));
        c.gridx++;
        c.gridheight = 1 + c.gridy;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(tabbedPane, c);
    }

    /**
	 * @return the charRecord
	 */
    public CharRecord getCharRecord() {
        return charRecord;
    }

    /**
	 * @param charRecord the charRecord to set
	 */
    public void setCharRecord(CharRecord charRecord) {
        this.charRecord = charRecord;
        if (characterImage != null) {
            characterImage.setCharRecord(charRecord);
        }
        if (skillsPnl != null) {
            skillsPnl.setCharRecord(charRecord);
        }
        if (charDetailsPnl != null) {
            charDetailsPnl.setCharRecord(charRecord);
        }
    }
}

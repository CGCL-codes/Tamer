package org.homeunix.thecave.plugins.report.select;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.PluginReportDateRangeChoices;
import org.homeunix.thecave.buddi.plugin.api.BuddiReportPlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableDocument;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransaction;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlHelper;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlPage;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.swing.MossDialog;
import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

/**
 * Built-in plugin.  Feel free to use this as an example on how to make
 * report plugins (although this one is kind of ugly, so you may not 
 * want to use it..)
 * 
 * @author wyatt
 *
 */
public class ReportTransactionsBySelectedAccount extends BuddiReportPlugin {

    public static final long serialVersionUID = 0;

    public HtmlPage getReport(ImmutableDocument model, MossDocumentFrame frame, Date startDate, Date endDate) throws PluginException {
        StringBuilder sb = HtmlHelper.getHtmlHeader(getName(), null, startDate, endDate);
        List<ImmutableAccount> accounts = new LinkedList<ImmutableAccount>(model.getImmutableAccounts());
        Collections.sort(accounts, new Comparator<ImmutableAccount>() {

            public int compare(ImmutableAccount o1, ImmutableAccount o2) {
                if (o1.getAccountType().isCredit() != o2.getAccountType().isCredit()) {
                    if (!o1.getAccountType().isCredit()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
                return o1.toString().compareTo(o2.toString());
            }
        });
        AccountSelectionDialog dialog = new AccountSelectionDialog(frame, accounts);
        try {
            dialog.openWindow(new Dimension(400, 300), null);
        } catch (WindowOpenException woe) {
            throw new PluginException(woe);
        }
        if (dialog.clickedOK()) {
            accounts = dialog.getSelectedAccounts();
        } else {
            Logger.getLogger(this.getClass().getName()).info("Canceled account selection operation");
            return null;
        }
        sb.append("<h1>").append(TextFormatter.getTranslation("REPORT_DETAILS_TRANSACTIONS_BY_SELECTED_ACCOUNT")).append("</h1>\n");
        for (ImmutableAccount a : accounts) {
            List<ImmutableTransaction> transactions = new LinkedList<ImmutableTransaction>(model.getImmutableTransactions(a, startDate, endDate));
            Collections.sort(transactions);
            if (transactions.size() > 0) {
                sb.append(a.getAccountType().isCredit() ? "<h2>" : "<h2 class='red'>");
                sb.append(TextFormatter.getTranslation(a.toString()));
                sb.append("</h2>\n");
                sb.append(HtmlHelper.getHtmlTransactionHeader());
                for (ImmutableTransaction t : transactions) {
                    sb.append(HtmlHelper.getHtmlTransactionRow(t, a));
                }
                sb.append(HtmlHelper.getHtmlTransactionFooter());
            }
        }
        sb.append(HtmlHelper.getHtmlFooter());
        return new HtmlPage(sb.toString(), null);
    }

    @Override
    public PluginReportDateRangeChoices getDateRangeChoice() {
        return PluginReportDateRangeChoices.INTERVAL;
    }

    public String getName() {
        return "REPORT_TITLE_TRANSACTIONS_BY_SELECTED_ACCOUNT";
    }

    public String getDescription() {
        return "REPORT_DESCRIPTION_TRANSACTIONS_BY_SELECTED_ACCOUNT";
    }

    public boolean isPluginActive() {
        return true;
    }

    public Version getMaximumVersion() {
        return null;
    }

    public Version getMinimumVersion() {
        return null;
    }

    public class AccountSelectionDialog extends MossDialog {

        public static final long serialVersionUID = 0;

        private final Map<JCheckBox, ImmutableAccount> checkMap;

        private final List<ImmutableAccount> accounts;

        private boolean clickedOK = false;

        public AccountSelectionDialog(MossDocumentFrame frame, List<ImmutableAccount> accounts) {
            super(frame, true);
            this.setTitle(TextFormatter.getTranslation("REPORT_TITLE_TRANSACTIONS_BY_SELECTED_ACCOUNT_SELECT_ACCOUNTS"));
            this.accounts = accounts;
            this.checkMap = new HashMap<JCheckBox, ImmutableAccount>();
        }

        public void init() {
            JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1));
            for (ImmutableAccount account : accounts) {
                JCheckBox checkBox = new JCheckBox(account.toString(), true);
                checkMap.put(checkBox, account);
                checkBoxPanel.add(checkBox);
            }
            JButton ok = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_OK));
            JButton cancel = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL));
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            cancel.setPreferredSize(new Dimension(Math.max(100, cancel.getPreferredSize().width), cancel.getPreferredSize().height));
            ok.setPreferredSize(cancel.getPreferredSize());
            buttons.add(cancel);
            buttons.add(ok);
            this.setLayout(new BorderLayout());
            JScrollPane checkBoxPanelScroller = new JScrollPane(checkBoxPanel);
            this.add(checkBoxPanelScroller, BorderLayout.CENTER);
            this.add(buttons, BorderLayout.SOUTH);
            this.getRootPane().setDefaultButton(ok);
            ok.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    AccountSelectionDialog.this.clickedOK = true;
                    AccountSelectionDialog.this.closeWindow();
                }
            });
            cancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    AccountSelectionDialog.this.closeWindow();
                }
            });
        }

        public List<ImmutableAccount> getSelectedAccounts() {
            List<ImmutableAccount> returnAccounts = new LinkedList<ImmutableAccount>();
            for (JCheckBox checkBox : checkMap.keySet()) {
                if (checkBox.isSelected()) returnAccounts.add(checkMap.get(checkBox));
            }
            return returnAccounts;
        }

        public boolean clickedOK() {
            return clickedOK;
        }
    }
}

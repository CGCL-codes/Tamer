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
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.PluginReportDateRangeChoices;
import org.homeunix.thecave.buddi.plugin.api.BuddiReportPlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableBudgetCategory;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableDocument;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransaction;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlHelper;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlPage;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.swing.MossDialog;
import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

/**
 * Built-in plugin.  Feel free to use this as an example on how to make
 * report plugins (although this one is kind of ugly, so you may not 
 * want to use it..)
 * 
 * @author wyatt
 *
 */
public class ReportIncomeExpenseBySelectedCategory extends BuddiReportPlugin {

    public static final long serialVersionUID = 0;

    public HtmlPage getReport(ImmutableDocument model, MossDocumentFrame callingFrame, Date startDate, Date endDate) throws PluginException {
        StringBuilder sb = HtmlHelper.getHtmlHeader(getName(), null, startDate, endDate);
        List<ImmutableBudgetCategory> categories = model.getImmutableBudgetCategories();
        Collections.sort(categories, new Comparator<ImmutableBudgetCategory>() {

            public int compare(ImmutableBudgetCategory o1, ImmutableBudgetCategory o2) {
                if (o1.isIncome() != o2.isIncome()) {
                    if (o1.isIncome()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
                return o1.toString().compareTo(o2.toString());
            }
        });
        CategorySelectionDialog dialog = new CategorySelectionDialog(callingFrame, categories);
        try {
            dialog.openWindow(new Dimension(400, 300), null);
        } catch (WindowOpenException woe) {
        }
        if (dialog.clickedOK()) {
            categories = dialog.getSelectedCategories();
        } else {
            Logger.getLogger(this.getClass().getName()).info("Canceled category selection operation");
            return null;
        }
        sb.append("<h1>").append(TextFormatter.getTranslation(BuddiKeys.REPORT_SUMMARY)).append("</h1>\n");
        sb.append("<table class='main'>\n");
        sb.append("<tr><th>");
        sb.append(TextFormatter.getTranslation(BuddiKeys.NAME));
        sb.append("</th><th>");
        sb.append(TextFormatter.getTranslation(BuddiKeys.ACTUAL));
        sb.append("</th><th>");
        sb.append(TextFormatter.getTranslation(BuddiKeys.BUDGETED));
        sb.append("</th><th>");
        sb.append(TextFormatter.getTranslation(BuddiKeys.DIFFERENCE));
        sb.append("</th></tr>\n");
        long totalActual = 0, totalBudgeted = 0;
        for (ImmutableBudgetCategory c : categories) {
            List<ImmutableTransaction> transactions = model.getImmutableTransactions(c, startDate, endDate);
            long actual = 0;
            for (ImmutableTransaction transaction : transactions) {
                actual += transaction.getAmount();
                if (transaction.getTo() instanceof ImmutableBudgetCategory) {
                    totalActual -= transaction.getAmount();
                } else if (transaction.getFrom() instanceof ImmutableBudgetCategory) {
                    totalActual += transaction.getAmount();
                }
            }
            long budgeted = c.getAmount(startDate, endDate);
            if (c.isIncome()) {
                totalBudgeted += budgeted;
            } else {
                totalBudgeted -= budgeted;
            }
            if (budgeted != 0 || transactions.size() > 0) {
                sb.append("<tr>");
                sb.append("<td>");
                sb.append(TextFormatter.getTranslation(c.toString()));
                sb.append("</td><td class='right" + (TextFormatter.isRed(c, actual) ? " red'>" : "'>"));
                sb.append(TextFormatter.getFormattedCurrency(actual));
                sb.append("</td><td class='right" + (TextFormatter.isRed(c, budgeted) ? " red'>" : "'>"));
                sb.append(TextFormatter.getFormattedCurrency(budgeted));
                long difference = actual - budgeted;
                sb.append("</td><td class='right" + (difference > 0 ^ c.isIncome() ? " red'>" : "'>"));
                sb.append(TextFormatter.getFormattedCurrency(difference, difference < 0));
                sb.append("</td></tr>\n");
            }
        }
        sb.append("<tr><th>");
        sb.append(TextFormatter.getTranslation(BuddiKeys.TOTAL));
        sb.append("</th><th class='right" + (totalActual < 0 ? " red'>" : "'>"));
        sb.append(TextFormatter.getFormattedCurrency(totalActual));
        sb.append("</th><th class='right" + (totalBudgeted < 0 ? " red'>" : "'>"));
        sb.append(TextFormatter.getFormattedCurrency(totalBudgeted));
        long totalDifference = totalActual - totalBudgeted;
        sb.append("</th><th class='right" + (totalDifference < 0 ? " red'>" : "'>"));
        sb.append(TextFormatter.getFormattedCurrency(totalDifference));
        sb.append("</th></tr>\n");
        sb.append("</table>\n\n");
        sb.append("<hr>\n");
        sb.append("<h1>").append(TextFormatter.getTranslation(BuddiKeys.REPORT_DETAILS)).append("</h1>\n");
        for (ImmutableBudgetCategory c : categories) {
            List<ImmutableTransaction> transactions = model.getImmutableTransactions(c, startDate, endDate);
            if (transactions.size() > 0) {
                sb.append(c.isIncome() ? "<h2>" : "<h2 class='red'>");
                sb.append(TextFormatter.getTranslation(c.toString()));
                sb.append("</h2>\n");
                sb.append(HtmlHelper.getHtmlTransactionHeader());
                for (ImmutableTransaction t : transactions) {
                    sb.append(HtmlHelper.getHtmlTransactionRow(t, c));
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
        return "REPORT_TITLE_INCOME_AND_EXPENSES_BY_SELECTED_CATEGORY";
    }

    public String getDescription() {
        return "REPORT_DESCRIPTION_INCOME_EXPENSES_BY_SELECTED_CATEGORY";
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

    public class CategorySelectionDialog extends MossDialog {

        public static final long serialVersionUID = 0;

        private final Map<String, ImmutableBudgetCategory> catMap;

        private final Map<String, JCheckBox> checkMap;

        private final List<ImmutableBudgetCategory> categories;

        private boolean clickedOK = false;

        public CategorySelectionDialog(MossFrame parent, List<ImmutableBudgetCategory> categories) {
            super(parent, true);
            this.setTitle(TextFormatter.getTranslation("REPORT_TITLE_INCOME_AND_EXPENSES_BY_SELECTED_CATEGORY_SELECT_CATEGORIES"));
            this.categories = categories;
            this.catMap = new HashMap<String, ImmutableBudgetCategory>();
            this.checkMap = new HashMap<String, JCheckBox>();
        }

        public void init() {
            JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1));
            for (ImmutableBudgetCategory bc : categories) {
                JCheckBox checkBox = new JCheckBox(bc.toString(), true);
                catMap.put(bc.toString(), bc);
                checkMap.put(bc.toString(), checkBox);
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
                    CategorySelectionDialog.this.clickedOK = true;
                    CategorySelectionDialog.this.closeWindow();
                }
            });
            cancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    CategorySelectionDialog.this.closeWindow();
                }
            });
        }

        @Override
        public Object closeWindow() {
            this.setVisible(false);
            return this;
        }

        public Vector<ImmutableBudgetCategory> getSelectedCategories() {
            Vector<ImmutableBudgetCategory> returnCategories = new Vector<ImmutableBudgetCategory>();
            for (ImmutableBudgetCategory category : categories) {
                if (checkMap.get(category.toString()).isSelected() && catMap.get(category.toString()) != null) {
                    returnCategories.add(catMap.get(category.toString()));
                }
            }
            return returnCategories;
        }

        public boolean clickedOK() {
            return clickedOK;
        }
    }
}

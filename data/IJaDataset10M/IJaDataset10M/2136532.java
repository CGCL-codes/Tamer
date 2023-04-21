package org.homeunix.drummer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.homeunix.drummer.Const;
import org.homeunix.drummer.model.Category;
import org.homeunix.drummer.model.DataInstance;
import org.homeunix.drummer.view.AbstractDialog;
import org.homeunix.drummer.view.GraphFrameLayout;
import org.homeunix.drummer.view.ModifyDialogLayout;
import org.homeunix.drummer.view.ReportFrameLayout;
import org.homeunix.thecave.moss.util.Log;

public class CategoryModifyDialog extends ModifyDialogLayout<Category> {

    public static final long serialVersionUID = 0;

    public CategoryModifyDialog() {
        super(MainBuddiFrame.getInstance());
        amountLabel.setText(Translate.getInstance().get(TranslateKeys.BUDGETED_AMOUNT));
        pulldownLabel.setText(Translate.getInstance().get(TranslateKeys.PARENT_CATEGORY));
        creditLimit.setVisible(false);
        creditLimitLabel.setVisible(false);
        interestRate.setVisible(false);
        interestRateLabel.setVisible(false);
    }

    protected String getType() {
        return Translate.getInstance().get(TranslateKeys.CATEGORY);
    }

    @Override
    protected AbstractDialog initActions() {
        okButton.addActionListener(new ActionListener() {

            @SuppressWarnings("unchecked")
            public void actionPerformed(ActionEvent arg0) {
                if (name.getText().length() == 0) {
                    JOptionPane.showMessageDialog(CategoryModifyDialog.this, Translate.getInstance().get(TranslateKeys.ENTER_CATEGORY_NAME), Translate.getInstance().get(TranslateKeys.MORE_INFO_NEEDED), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    final Category c;
                    if (source == null) c = DataInstance.getInstance().getDataModelFactory().createCategory(); else c = source;
                    if (pulldown.getSelectedItem() instanceof Category) {
                        Category temp = (Category) pulldown.getSelectedItem();
                        if (temp.equals(c)) {
                            if (c.getParent() != null) {
                                if (Const.DEVEL) Log.debug("A Category cannot be it's own parent.  Where is your knowledge of Bio?!?");
                                pulldown.setSelectedItem(c.getParent());
                            } else pulldown.setSelectedItem(Translate.getInstance().get(TranslateKeys.NO_PARENT));
                            return;
                        }
                        while (temp.getParent() != null) {
                            if (temp.getParent().equals(c)) {
                                Log.error("A Category cannot be it's own grandpa (or other ancestor), although it seems as though you would like that.");
                                if (c.getParent() != null) pulldown.setSelectedItem(c.getParent()); else pulldown.setSelectedItem(Translate.getInstance().get(TranslateKeys.NO_PARENT));
                                return;
                            }
                            temp = temp.getParent();
                        }
                    }
                    c.setName(name.getText());
                    c.setBudgetedAmount(amount.getValue());
                    if (Const.DEVEL) Log.debug("No parent selected.");
                    if (c.getParent() != null) {
                        c.getParent().getChildren().remove(c);
                        if (Const.DEVEL) Log.debug("Removing " + c + " from " + c.getParent());
                    }
                    if (pulldown.getSelectedItem() instanceof Category) {
                        if (Const.DEVEL) Log.debug("Setting parent of " + c + " to " + pulldown.getSelectedItem());
                        ((Category) pulldown.getSelectedItem()).getChildren().add(c);
                        c.setParent((Category) pulldown.getSelectedItem());
                    } else c.setParent(null);
                    if (check.isSelected()) c.setIncome(true); else c.setIncome(false);
                    if (source == null) DataInstance.getInstance().addCategory(c); else DataInstance.getInstance().saveDataModel();
                    CategoryModifyDialog.this.setVisible(false);
                    MainBuddiFrame.getInstance().getCategoryListPanel().updateContent();
                }
                TransactionsFrame.updateAllTransactionWindows();
                ReportFrameLayout.updateAllReportWindows();
                GraphFrameLayout.updateAllGraphWindows();
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                CategoryModifyDialog.this.setVisible(false);
                MainBuddiFrame.getInstance().getCategoryListPanel().updateContent();
            }
        });
        pulldown.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (pulldown.getSelectedItem() instanceof Category) {
                    check.setEnabled(false);
                    check.setSelected(((Category) pulldown.getSelectedItem()).isIncome());
                } else {
                    check.setEnabled(true);
                }
            }
        });
        check.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                updateContent();
            }
        });
        return this;
    }

    @Override
    protected AbstractDialog initContent() {
        if (source == null) {
            updateContent();
            name.setText("");
            amount.setValue(0);
            pulldown.setSelectedItem(Translate.getInstance().get(TranslateKeys.NO_PARENT));
            check.setSelected(false);
        } else {
            name.setText(Translate.getInstance().get(source.getName()));
            amount.setValue(source.getBudgetedAmount());
            if (source.isIncome()) check.setSelected(true); else check.setSelected(false);
            updateContent();
            if (source.getParent() != null) {
                if (Const.DEVEL) Log.debug(source.getParent());
                pulldown.setSelectedItem(source.getParent());
            } else pulldown.setSelectedItem(Translate.getInstance().get(TranslateKeys.NO_PARENT));
        }
        return this;
    }

    public AbstractDialog updateContent() {
        pulldownModel.removeAllElements();
        pulldownModel.addElement(Translate.getInstance().get(TranslateKeys.NO_PARENT));
        for (Category c : DataInstance.getInstance().getCategories()) {
            if (source == null || c.isIncome() == check.isSelected()) pulldownModel.addElement(c);
        }
        return this;
    }
}

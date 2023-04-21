package org.mov.analyser;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.mov.main.CommandManager;
import org.mov.main.Module;
import org.mov.main.ModuleFrame;
import org.mov.ui.AbstractTable;
import org.mov.ui.AbstractTableModel;
import org.mov.ui.Column;
import org.mov.ui.ChangeFormat;
import org.mov.ui.EditorDialog;
import org.mov.ui.ExpressionEditorDialog;
import org.mov.ui.MenuHelper;
import org.mov.util.Locale;
import org.mov.util.Money;
import org.mov.util.TradingDate;

public class PaperTradeResultModule extends AbstractTable implements Module {

    private PropertyChangeSupport propertySupport;

    private static final int START_DATE_COLUMN = 0;

    private static final int END_DATE_COLUMN = 1;

    private static final int SYMBOLS_COLUMN = 2;

    private static final int BUY_RULE_COLUMN = 3;

    private static final int SELL_RULE_COLUMN = 4;

    private static final int A_COLUMN = 5;

    private static final int B_COLUMN = 6;

    private static final int C_COLUMN = 7;

    private static final int TRADE_COST_COLUMN = 8;

    private static final int NUMBER_OF_TRADES_COLUMN = 9;

    private static final int INITIAL_CAPITAL_COLUMN = 10;

    private static final int FINAL_CAPITAL_COLUMN = 11;

    private static final int PERCENT_RETURN_COLUMN = 12;

    private Model model;

    private JMenuBar menuBar;

    private JMenuItem openMenuItem;

    private JMenuItem graphMenuItem;

    private JMenuItem transactionsMenuItem;

    private JMenuItem viewBuyRuleMenuItem;

    private JMenuItem viewSellRuleMenuItem;

    private JMenuItem storeBuyRuleMenuItem;

    private JMenuItem storeSellRuleMenuItem;

    private JMenuItem getTipMenuItem;

    private JMenuItem removeMenuItem;

    private JMenuItem removeAllMenuItem;

    private JDesktopPane desktop;

    private class Model extends AbstractTableModel {

        private List results;

        public Model(List columns) {
            super(columns);
            results = new ArrayList();
        }

        public PaperTradeResult getResult(int row) {
            return (PaperTradeResult) results.get(row);
        }

        public void removeAllResults() {
            results.clear();
            fireTableDataChanged();
        }

        public List getResults() {
            return results;
        }

        public void setResults(List results) {
            this.results = results;
            fireTableDataChanged();
        }

        public void addResults(List results) {
            this.results.addAll(results);
            fireTableDataChanged();
        }

        public int getRowCount() {
            return results.size();
        }

        public Object getValueAt(int row, int column) {
            if (row >= getRowCount()) return "";
            PaperTradeResult result = (PaperTradeResult) results.get(row);
            if (column == START_DATE_COLUMN) return result.getStartDate(); else if (column == END_DATE_COLUMN) return result.getEndDate(); else if (column == SYMBOLS_COLUMN) return result.getSymbols(); else if (column == BUY_RULE_COLUMN) return result.getBuyRule(); else if (column == SELL_RULE_COLUMN) return result.getSellRule(); else if (column == A_COLUMN) return new Integer(result.getA()); else if (column == B_COLUMN) return new Integer(result.getB()); else if (column == C_COLUMN) return new Integer(result.getC()); else if (column == TRADE_COST_COLUMN) return result.getTradeCost(); else if (column == NUMBER_OF_TRADES_COLUMN) return new Integer(result.getNumberTrades()); else if (column == FINAL_CAPITAL_COLUMN) return result.getFinalCapital(); else if (column == INITIAL_CAPITAL_COLUMN) return result.getInitialCapital(); else if (column == PERCENT_RETURN_COLUMN) return new ChangeFormat(result.getInitialCapital(), result.getFinalCapital()); else {
                assert false;
                return "";
            }
        }
    }

    public PaperTradeResultModule() {
        List columns = new ArrayList();
        columns.add(new Column(START_DATE_COLUMN, Locale.getString("START_DATE"), Locale.getString("START_DATE_COLUMN_HEADER"), TradingDate.class, Column.VISIBLE));
        columns.add(new Column(END_DATE_COLUMN, Locale.getString("END_DATE"), Locale.getString("END_DATE_COLUMN_HEADER"), TradingDate.class, Column.VISIBLE));
        columns.add(new Column(SYMBOLS_COLUMN, Locale.getString("SYMBOLS"), Locale.getString("SYMBOLS_COLUMN_HEADER"), String.class, Column.VISIBLE));
        columns.add(new Column(BUY_RULE_COLUMN, Locale.getString("BUY_RULE"), Locale.getString("BUY_RULE_COLUMN_HEADER"), String.class, Column.VISIBLE));
        columns.add(new Column(SELL_RULE_COLUMN, Locale.getString("SELL_RULE"), Locale.getString("SELL_RULE_COLUMN_HEADER"), String.class, Column.VISIBLE));
        columns.add(new Column(A_COLUMN, "A", "A", Integer.class, Column.HIDDEN));
        columns.add(new Column(B_COLUMN, "B", "B", Integer.class, Column.HIDDEN));
        columns.add(new Column(C_COLUMN, "C", "C", Integer.class, Column.HIDDEN));
        columns.add(new Column(TRADE_COST_COLUMN, Locale.getString("TRADE_COST"), Locale.getString("TRADE_COST_COLUMN_HEADER"), Money.class, Column.HIDDEN));
        columns.add(new Column(NUMBER_OF_TRADES_COLUMN, Locale.getString("NUMBER_TRADES"), Locale.getString("NUMBER_TRADES_COLUMN_HEADER"), Integer.class, Column.HIDDEN));
        columns.add(new Column(INITIAL_CAPITAL_COLUMN, Locale.getString("INITIAL_CAPITAL"), Locale.getString("INITIAL_CAPITAL_COLUMN_HEADER"), Money.class, Column.VISIBLE));
        columns.add(new Column(FINAL_CAPITAL_COLUMN, Locale.getString("FINAL_CAPITAL"), Locale.getString("FINAL_CAPITAL_COLUMN_HEADER"), Money.class, Column.HIDDEN));
        columns.add(new Column(PERCENT_RETURN_COLUMN, Locale.getString("PERCENT_RETURN"), Locale.getString("PERCENT_RETURN_COLUMN_HEADER"), ChangeFormat.class, Column.VISIBLE));
        model = new Model(columns);
        setModel(model);
        model.addTableModelListener(this);
        propertySupport = new PropertyChangeSupport(this);
        addMenu();
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                handleMouseClicked(evt);
            }
        });
        showColumns(model);
    }

    public void setDesktop(JDesktopPane desktop) {
        this.desktop = desktop;
    }

    private void handleMouseClicked(MouseEvent event) {
        Point point = event.getPoint();
        if (event.getButton() == MouseEvent.BUTTON3) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem popupOpenMenuItem = new JMenuItem(Locale.getString("OPEN"));
            popupOpenMenuItem.setEnabled(getSelectedRowCount() == 1);
            popupOpenMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    openSelectedResult();
                }
            });
            menu.add(popupOpenMenuItem);
            JMenuItem popupGraphMenuItem = new JMenuItem(Locale.getString("GRAPH"));
            popupGraphMenuItem.setEnabled(getSelectedRowCount() == 1);
            popupGraphMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    graphSelectedResult();
                }
            });
            menu.add(popupGraphMenuItem);
            JMenuItem popupTransactionsMenuItem = new JMenuItem(Locale.getString("TRANSACTIONS"));
            popupTransactionsMenuItem.setEnabled(getSelectedRowCount() == 1);
            popupTransactionsMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    transactionsSelectedResult();
                }
            });
            menu.add(popupTransactionsMenuItem);
            menu.addSeparator();
            JMenuItem popupViewBuyRuleMenuItem = new JMenuItem(Locale.getString("VIEW_BUY_RULE"));
            popupViewBuyRuleMenuItem.setEnabled(getSelectedRowCount() == 1);
            popupViewBuyRuleMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewBuyRule();
                }
            });
            menu.add(popupViewBuyRuleMenuItem);
            JMenuItem popupViewSellRuleMenuItem = new JMenuItem(Locale.getString("VIEW_SELL_RULE"));
            popupViewSellRuleMenuItem.setEnabled(getSelectedRowCount() == 1);
            popupViewSellRuleMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewSellRule();
                }
            });
            menu.add(popupViewSellRuleMenuItem);
            JMenuItem popupStoreBuyRuleMenuItem = new JMenuItem(Locale.getString("STORE_BUY_RULE"));
            popupStoreBuyRuleMenuItem.setEnabled(getSelectedRowCount() == 1);
            popupStoreBuyRuleMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    storeBuyRule();
                }
            });
            menu.add(popupStoreBuyRuleMenuItem);
            JMenuItem popupStoreSellRuleMenuItem = new JMenuItem(Locale.getString("STORE_SELL_RULE"));
            popupStoreSellRuleMenuItem.setEnabled(getSelectedRowCount() == 1);
            popupStoreSellRuleMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    storeSellRule();
                }
            });
            menu.add(popupStoreSellRuleMenuItem);
            menu.addSeparator();
            JMenuItem popupGetTipMenuItem = new JMenuItem(Locale.getString("GET_TIP"));
            popupGetTipMenuItem.setEnabled(getSelectedRowCount() == 1);
            popupGetTipMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showTip();
                }
            });
            menu.add(popupGetTipMenuItem);
            menu.addSeparator();
            JMenuItem popupRemoveMenuItem = new JMenuItem(Locale.getString("REMOVE"));
            popupRemoveMenuItem.setEnabled(getSelectedRowCount() >= 1);
            popupRemoveMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    removeSelectedResults();
                    checkMenuDisabledStatus();
                }
            });
            menu.add(popupRemoveMenuItem);
            menu.show(this, point.x, point.y);
        } else if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 2) {
            graphSelectedResult();
        }
    }

    private void graphSelectedResult() {
        int row = getSelectedRow();
        if (row != -1) {
            PaperTradeResult result = model.getResult(row);
            CommandManager.getInstance().graphPortfolio(result.getPortfolio(), result.getQuoteBundle(), result.getStartDate(), result.getEndDate());
        }
    }

    private void transactionsSelectedResult() {
        int row = getSelectedRow();
        if (row != -1) {
            PaperTradeResult result = model.getResult(row);
            CommandManager.getInstance().tableTransactions(result.getPortfolio(), result.getQuoteBundle());
        }
    }

    private void viewBuyRule() {
        int row = getSelectedRow();
        if (row != -1) {
            final PaperTradeResult result = model.getResult(row);
            Thread thread = new Thread(new Runnable() {

                public void run() {
                    ExpressionEditorDialog.showViewDialog(Locale.getString("VIEW_BUY_RULE"), result.getBuyRule());
                }
            });
            thread.start();
        }
    }

    private void viewSellRule() {
        int row = getSelectedRow();
        if (row != -1) {
            final PaperTradeResult result = model.getResult(row);
            Thread thread = new Thread(new Runnable() {

                public void run() {
                    ExpressionEditorDialog.showViewDialog(Locale.getString("VIEW_SELL_RULE"), result.getSellRule());
                }
            });
            thread.start();
        }
    }

    private void storeBuyRule() {
        int row = getSelectedRow();
        if (row != -1) {
            final PaperTradeResult result = model.getResult(row);
            Thread thread = new Thread(new Runnable() {

                public void run() {
                    ExpressionEditorDialog.showAddDialog(Locale.getString("STORE_BUY_RULE"), result.getBuyRule());
                }
            });
            thread.start();
        }
    }

    private void storeSellRule() {
        int row = getSelectedRow();
        if (row != -1) {
            final PaperTradeResult result = model.getResult(row);
            Thread thread = new Thread(new Runnable() {

                public void run() {
                    ExpressionEditorDialog.showAddDialog(Locale.getString("STORE_SELL_RULE"), result.getSellRule());
                }
            });
            thread.start();
        }
    }

    private void showTip() {
        int row = getSelectedRow();
        if (row != -1) {
            final PaperTradeResult result = model.getResult(row);
            Thread thread = new Thread(new Runnable() {

                public void run() {
                    showTipWindow();
                }
            });
            thread.start();
        }
    }

    private void showTipWindow() {
        int row = getSelectedRow();
        if (row != -1) {
            PaperTradeResult result = model.getResult(row);
            EditorDialog.showViewDialog(Locale.getString("GET_TIP"), Locale.getString("SHORT_GET_TIP"), new String(result.getTip()));
        }
    }

    private void openSelectedResult() {
        int row = getSelectedRow();
        if (row != -1) {
            PaperTradeResult result = model.getResult(row);
            CommandManager.getInstance().openPortfolio(result.getPortfolio(), result.getQuoteBundle());
        }
    }

    private void removeSelectedResults() {
        int[] rows = getSelectedRows();
        List rowIntegers = new ArrayList();
        for (int i = 0; i < rows.length; i++) rowIntegers.add(new Integer(rows[i]));
        List sortedRows = new ArrayList(rowIntegers);
        Collections.sort(sortedRows);
        Collections.reverse(sortedRows);
        List results = model.getResults();
        Iterator iterator = sortedRows.iterator();
        while (iterator.hasNext()) {
            Integer rowToRemove = (Integer) iterator.next();
            results.remove(rowToRemove.intValue());
        }
        model.setResults(results);
    }

    private void checkMenuDisabledStatus() {
        int numberOfSelectedRows = getSelectedRowCount();
        openMenuItem.setEnabled(numberOfSelectedRows == 1);
        graphMenuItem.setEnabled(numberOfSelectedRows == 1);
        transactionsMenuItem.setEnabled(numberOfSelectedRows == 1);
        viewBuyRuleMenuItem.setEnabled(numberOfSelectedRows == 1);
        viewSellRuleMenuItem.setEnabled(numberOfSelectedRows == 1);
        storeBuyRuleMenuItem.setEnabled(numberOfSelectedRows == 1);
        storeSellRuleMenuItem.setEnabled(numberOfSelectedRows == 1);
        getTipMenuItem.setEnabled(numberOfSelectedRows == 1);
        removeMenuItem.setEnabled(numberOfSelectedRows >= 1);
        removeAllMenuItem.setEnabled(model.getRowCount() > 0);
    }

    private void addMenu() {
        menuBar = new JMenuBar();
        JMenu resultMenu = MenuHelper.addMenu(menuBar, Locale.getString("RESULT"));
        openMenuItem = new JMenuItem(Locale.getString("OPEN"));
        openMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openSelectedResult();
            }
        });
        resultMenu.add(openMenuItem);
        graphMenuItem = new JMenuItem(Locale.getString("GRAPH"));
        graphMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphSelectedResult();
            }
        });
        resultMenu.add(graphMenuItem);
        transactionsMenuItem = new JMenuItem(Locale.getString("TRANSACTIONS"));
        transactionsMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                transactionsSelectedResult();
            }
        });
        resultMenu.add(transactionsMenuItem);
        resultMenu.addSeparator();
        viewBuyRuleMenuItem = new JMenuItem(Locale.getString("VIEW_BUY_RULE"));
        viewBuyRuleMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                viewBuyRule();
            }
        });
        resultMenu.add(viewBuyRuleMenuItem);
        viewSellRuleMenuItem = new JMenuItem(Locale.getString("VIEW_SELL_RULE"));
        viewSellRuleMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                viewSellRule();
            }
        });
        resultMenu.add(viewSellRuleMenuItem);
        storeBuyRuleMenuItem = new JMenuItem(Locale.getString("STORE_BUY_RULE"));
        storeBuyRuleMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                storeBuyRule();
            }
        });
        resultMenu.add(storeBuyRuleMenuItem);
        storeSellRuleMenuItem = new JMenuItem(Locale.getString("STORE_SELL_RULE"));
        storeSellRuleMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                storeSellRule();
            }
        });
        resultMenu.add(storeSellRuleMenuItem);
        resultMenu.addSeparator();
        getTipMenuItem = new JMenuItem(Locale.getString("GET_TIP"));
        getTipMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showTip();
            }
        });
        resultMenu.add(getTipMenuItem);
        resultMenu.addSeparator();
        removeMenuItem = new JMenuItem(Locale.getString("REMOVE"));
        removeMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeSelectedResults();
                checkMenuDisabledStatus();
            }
        });
        resultMenu.add(removeMenuItem);
        removeAllMenuItem = new JMenuItem(Locale.getString("REMOVE_ALL"));
        removeAllMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                model.removeAllResults();
                checkMenuDisabledStatus();
            }
        });
        resultMenu.add(removeAllMenuItem);
        resultMenu.addSeparator();
        JMenu columnMenu = createShowColumnMenu(model);
        resultMenu.add(columnMenu);
        resultMenu.addSeparator();
        JMenuItem resultCloseMenuItem = new JMenuItem(Locale.getString("CLOSE"));
        openMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                model.removeAllResults();
                propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
            }
        });
        resultMenu.add(resultCloseMenuItem);
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                checkMenuDisabledStatus();
            }
        });
        checkMenuDisabledStatus();
    }

    public void addResults(List results) {
        model.addResults(results);
        checkMenuDisabledStatus();
        validate();
        repaint();
    }

    public void save() {
        model.removeAllResults();
    }

    public String getTitle() {
        return Locale.getString("PAPER_TRADE_RESULTS_TITLE");
    }

    public void addModuleChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removeModuleChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public ImageIcon getFrameIcon() {
        return null;
    }

    public JComponent getComponent() {
        return this;
    }

    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    public boolean encloseInScrollPane() {
        return true;
    }
}

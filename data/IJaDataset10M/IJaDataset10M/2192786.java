package org.mov.table;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.mov.main.CommandManager;
import org.mov.main.Module;
import org.mov.main.ModuleFrame;
import org.mov.util.Locale;
import org.mov.util.TradingDate;
import org.mov.prefs.PreferencesManager;
import org.mov.quote.MissingQuoteException;
import org.mov.quote.Quote;
import org.mov.quote.ScriptQuoteBundle;
import org.mov.quote.Symbol;
import org.mov.ui.AbstractTable;
import org.mov.ui.Column;
import org.mov.ui.DesktopManager;
import org.mov.ui.MainMenu;
import org.mov.ui.MenuHelper;
import org.mov.ui.QuoteModel;
import org.mov.ui.SymbolListDialog;
import org.mov.ui.TextDialog;

/**
 * Venice module for displaying a watch screen to the user. This module allows a
 * user to build and modify a watch screen which can be used to monior a group
 * of stocks.
 *
 * @see WatchScreen
 */
public class WatchScreenModule extends AbstractTable implements Module, ActionListener {

    private JMenuBar menuBar;

    private JMenuItem addSymbols;

    private JMenuItem removeSymbols;

    private JMenuItem graphSymbols;

    private JMenuItem graphIndexSymbols;

    private JMenuItem tableSymbols;

    private JMenuItem tableClose;

    private JMenuItem renameWatchScreen;

    private JMenuItem deleteWatchScreen;

    private JMenuItem applyEquationsMenuItem;

    private JMenuItem popupRemoveSymbols = null;

    private JMenuItem popupGraphSymbols = null;

    private JMenuItem popupTableSymbols = null;

    private PropertyChangeSupport propertySupport;

    private ScriptQuoteBundle quoteBundle;

    private WatchScreen watchScreen;

    private QuoteModel model;

    private String frameIcon = "org/mov/images/TableIcon.gif";

    private boolean isDeleted = false;

    /**
     * Create a watch screen module.
     *
     * @param watchScreen the watch screen object
     * @param quoteBundle watch screen quotes
     */
    public WatchScreenModule(WatchScreen watchScreen, ScriptQuoteBundle quoteBundle) {
        this.watchScreen = watchScreen;
        this.quoteBundle = quoteBundle;
        propertySupport = new PropertyChangeSupport(this);
        model = new QuoteModel(quoteBundle, getQuotes(), Column.HIDDEN, Column.VISIBLE);
        setModel(model, QuoteModel.SYMBOL_COLUMN, SORT_UP);
        showColumns(model);
        addMenu();
        model.addTableModelListener(this);
        resort();
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                handleMouseClicked(evt);
            }
        });
    }

    private void checkMenuDisabledStatus() {
        int numberOfSelectedRows = getSelectedRowCount();
        removeSymbols.setEnabled(numberOfSelectedRows > 0 ? true : false);
        graphSymbols.setEnabled(numberOfSelectedRows > 0 ? true : false);
        graphIndexSymbols.setEnabled(numberOfSelectedRows > 0 ? true : false);
        tableSymbols.setEnabled(numberOfSelectedRows > 0 ? true : false);
    }

    private void handleMouseClicked(MouseEvent event) {
        Point point = event.getPoint();
        if (event.getButton() == MouseEvent.BUTTON3) {
            JPopupMenu menu = new JPopupMenu();
            popupGraphSymbols = MenuHelper.addMenuItem(this, menu, Locale.getString("GRAPH"));
            popupGraphSymbols.setEnabled(getSelectedRowCount() > 0);
            popupTableSymbols = MenuHelper.addMenuItem(this, menu, Locale.getString("TABLE"));
            popupTableSymbols.setEnabled(getSelectedRowCount() > 0);
            menu.addSeparator();
            popupRemoveSymbols = MenuHelper.addMenuItem(this, menu, Locale.getString("REMOVE"));
            popupRemoveSymbols.setEnabled(getSelectedRowCount() > 0);
            menu.show(this, point.x, point.y);
        } else if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 2) {
            int[] selectedRows = getSelectedRows();
            List symbols = new ArrayList();
            for (int i = 0; i < selectedRows.length; i++) {
                Symbol symbol = (Symbol) model.getValueAt(selectedRows[i], QuoteModel.SYMBOL_COLUMN);
                symbols.add(symbol);
            }
            CommandManager.getInstance().graphStockBySymbol(symbols);
        }
    }

    private void addMenu() {
        menuBar = new JMenuBar();
        {
            JMenu tableMenu = MenuHelper.addMenu(menuBar, Locale.getString("WATCH_SCREEN"));
            tableMenu.add(createShowColumnMenu(model));
            tableMenu.addSeparator();
            applyEquationsMenuItem = MenuHelper.addMenuItem(this, tableMenu, Locale.getString("APPLY_EQUATIONS"));
            tableMenu.addSeparator();
            deleteWatchScreen = MenuHelper.addMenuItem(this, tableMenu, Locale.getString("DELETE"));
            renameWatchScreen = MenuHelper.addMenuItem(this, tableMenu, Locale.getString("RENAME"));
            tableMenu.addSeparator();
            tableClose = MenuHelper.addMenuItem(this, tableMenu, Locale.getString("CLOSE"));
        }
        {
            JMenu symbolsMenu = MenuHelper.addMenu(menuBar, Locale.getString("SYMBOLS"));
            addSymbols = MenuHelper.addMenuItem(this, symbolsMenu, Locale.getString("ADD"));
            removeSymbols = MenuHelper.addMenuItem(this, symbolsMenu, Locale.getString("REMOVE"));
            symbolsMenu.addSeparator();
            graphSymbols = MenuHelper.addMenuItem(this, symbolsMenu, Locale.getString("GRAPH"));
            graphIndexSymbols = MenuHelper.addMenuItem(this, symbolsMenu, Locale.getString("GRAPH_INDEX"));
            tableSymbols = MenuHelper.addMenuItem(this, symbolsMenu, Locale.getString("TABLE"));
        }
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                checkMenuDisabledStatus();
            }
        });
        checkMenuDisabledStatus();
    }

    /**
     * Tell module to save any current state data / preferences data because
     * the window is being closed.
     */
    public void save() {
        if (!isDeleted) PreferencesManager.saveWatchScreen(watchScreen);
    }

    /**
     * Return the window title.
     *
     * @return	the window title
     */
    public String getTitle() {
        return watchScreen.getName();
    }

    /**
     * Add a property change listener for module change events.
     *
     * @param	listener	listener
     */
    public void addModuleChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove a property change listener for module change events.
     *
     * @param	listener	listener
     */
    public void removeModuleChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * Return frame icon for table module.
     *
     * @return	the frame icon.
     */
    public ImageIcon getFrameIcon() {
        return new ImageIcon(ClassLoader.getSystemClassLoader().getResource(frameIcon));
    }

    /**
     * Return displayed component for this module.
     *
     * @return the component to display.
     */
    public JComponent getComponent() {
        return this;
    }

    /**
     * Return menu bar for chart module.
     *
     * @return	the menu bar.
     */
    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    /**
     * Return whether the module should be enclosed in a scroll pane.
     *
     * @return	enclose module in scroll bar
     */
    public boolean encloseInScrollPane() {
        return true;
    }

    /**
     * Handle widget events.
     *
     * @param	e	action event
     */
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == tableClose) {
            propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
        } else if ((popupGraphSymbols != null && e.getSource() == popupGraphSymbols) || e.getSource() == graphSymbols || e.getSource() == graphIndexSymbols) {
            int[] selectedRows = getSelectedRows();
            List symbols = new ArrayList();
            for (int i = 0; i < selectedRows.length; i++) {
                Symbol symbol = (Symbol) model.getValueAt(selectedRows[i], QuoteModel.SYMBOL_COLUMN);
                symbols.add(symbol);
            }
            if (e.getSource() == graphSymbols) {
                CommandManager.getInstance().graphStockBySymbol(symbols);
            }
            if (e.getSource() == graphIndexSymbols) {
                CommandManager.getInstance().graphIndexBySymbol(symbols);
            }
        } else if ((popupRemoveSymbols != null && e.getSource() == popupRemoveSymbols) || e.getSource() == removeSymbols) {
            int[] selectedRows = getSelectedRows();
            List symbols = new ArrayList();
            for (int i = 0; i < selectedRows.length; i++) {
                Symbol symbol = (Symbol) model.getValueAt(selectedRows[i], QuoteModel.SYMBOL_COLUMN);
                symbols.add(symbol);
            }
            watchScreen.removeAllSymbols(symbols);
            model.setQuotes(getQuotes());
            model.fireTableDataChanged();
        } else if (e.getSource() == addSymbols) addSymbols(); else if (e.getSource() == applyEquationsMenuItem) applyEquations(quoteBundle, model); else if (e.getSource() == deleteWatchScreen) deleteWatchScreen(); else if (e.getSource() == renameWatchScreen) renameWatchScreen(); else if ((popupTableSymbols != null && e.getSource() == popupTableSymbols) || e.getSource() == tableSymbols) {
            int[] selectedRows = getSelectedRows();
            List symbols = new ArrayList();
            for (int i = 0; i < selectedRows.length; i++) {
                Symbol symbol = (Symbol) model.getValueAt(selectedRows[i], QuoteModel.SYMBOL_COLUMN);
                symbols.add(symbol);
            }
            CommandManager.getInstance().tableStocks(symbols);
        } else assert false;
    }

    private void deleteWatchScreen() {
        int option = JOptionPane.showInternalConfirmDialog(DesktopManager.getDesktop(), Locale.getString("SURE_DELETE_WATCH_SCREEN"), Locale.getString("DELETE_WATCH_SCREEN"), JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            PreferencesManager.deleteWatchScreen(watchScreen.getName());
            MainMenu.getInstance().updateWatchScreenMenu();
            isDeleted = true;
            propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
        }
    }

    private void renameWatchScreen() {
        Thread thread = new Thread() {

            public void run() {
                String oldWatchScreenName = watchScreen.getName();
                TextDialog dialog = new TextDialog(DesktopManager.getDesktop(), Locale.getString("ENTER_NEW_WATCH_SCREEN_NAME"), Locale.getString("RENAME_WATCH_SCREEN"), oldWatchScreenName);
                String newWatchScreenName = dialog.showDialog();
                if (newWatchScreenName != null && newWatchScreenName.length() > 0 && !newWatchScreenName.equals(oldWatchScreenName)) {
                    watchScreen.setName(newWatchScreenName);
                    PreferencesManager.saveWatchScreen(watchScreen);
                    PreferencesManager.deleteWatchScreen(oldWatchScreenName);
                    MainMenu.getInstance().updateWatchScreenMenu();
                    propertySupport.firePropertyChange(ModuleFrame.TITLEBAR_CHANGED_PROPERTY, 0, 1);
                }
            }
        };
        thread.start();
    }

    private void addSymbols() {
        Thread thread = new Thread() {

            public void run() {
                Set symbols = SymbolListDialog.getSymbols(DesktopManager.getDesktop(), Locale.getString("ADD_SYMBOLS"));
                if (symbols != null) {
                    for (Iterator iterator = symbols.iterator(); iterator.hasNext(); ) watchScreen.addSymbol((Symbol) iterator.next());
                    model.setQuotes(getQuotes());
                }
            }
        };
        thread.start();
    }

    private List getQuotes() {
        List quotes = new ArrayList();
        TradingDate lastDate = quoteBundle.getLastDate();
        for (Iterator iterator = watchScreen.getSymbols().iterator(); iterator.hasNext(); ) {
            Symbol symbol = (Symbol) iterator.next();
            Quote quote;
            try {
                quote = new Quote(symbol, lastDate, (int) quoteBundle.getQuote(symbol, Quote.DAY_VOLUME, lastDate), quoteBundle.getQuote(symbol, Quote.DAY_LOW, lastDate), quoteBundle.getQuote(symbol, Quote.DAY_HIGH, lastDate), quoteBundle.getQuote(symbol, Quote.DAY_OPEN, lastDate), quoteBundle.getQuote(symbol, Quote.DAY_CLOSE, lastDate));
            } catch (MissingQuoteException e) {
                quote = new Quote(symbol, lastDate, 0, 0.0D, 0.0D, 0.0D, 0.0D);
            }
            quotes.add(quote);
        }
        return quotes;
    }
}

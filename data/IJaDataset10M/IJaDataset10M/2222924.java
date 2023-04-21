package ontorama.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import ontorama.OntoramaConfig;
import ontorama.backends.Backend;
import ontorama.ontotools.query.Query;
import ontorama.ui.action.BackHistoryAction;
import ontorama.ui.action.ForwardHistoryAction;
import org.tockit.events.EventBroker;

/**
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: DSTC</p>
 */
@SuppressWarnings("serial")
public class HistoryMenu extends JMenu {

    private JMenuItem _historyBackMenuItem;

    private JMenuItem _historyForwardMenuItem;

    private static Map<JRadioButtonMenuItem, HistoryElement> _menuItemHistoryMapping;

    private final int _maxHistoryItems = 20;

    /**
     * hold all history menu items for operating
     * back and forward buttons
     */
    private static List<JRadioButtonMenuItem> _historyItems;

    /**
     * event broker capable of processing queries
     */
    private final EventBroker _eventBroker;

    public Action _backAction;

    public Action _forwardAction;

    private final ButtonGroup _buttonGroup;

    private final Backend _backend = OntoramaConfig.getBackend();

    public HistoryMenu(EventBroker eventBroker) {
        super("History");
        _eventBroker = eventBroker;
        _menuItemHistoryMapping = new HashMap<JRadioButtonMenuItem, HistoryElement>();
        _historyItems = new ArrayList<JRadioButtonMenuItem>();
        _backAction = new BackHistoryAction(this);
        _forwardAction = new ForwardHistoryAction(this);
        _buttonGroup = new ButtonGroup();
        setMnemonic(KeyEvent.VK_H);
        buildHistoryMenu();
    }

    public Action getBackAction() {
        return _backAction;
    }

    public Action getForwardAction() {
        return _forwardAction;
    }

    /**
     * create History Menu
     */
    private void buildHistoryMenu() {
        _historyBackMenuItem = add(_backAction);
        _historyForwardMenuItem = add(_forwardAction);
        _historyBackMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.ALT_MASK));
        _historyForwardMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.ALT_MASK));
        enableBackForwardButtons();
        addSeparator();
    }

    /**
     * Update history menu: if given query is a query not stored in history
     * items list - append history list. Otherwise - jump to the history
     * item corresponding to this query.
     */
    public void updateHistory(Query query) {
        JRadioButtonMenuItem historyItem;
        if ((historyItem = findIfQueryAlreadyExists(query)) != null) {
            historyItem.setSelected(true);
        } else {
            appendHistory(query);
        }
        enableBackForwardButtons();
    }

    /**
	 * Append history with given query
	 */
    public void appendHistory(Query query) {
        if ((_historyItems.size() > 0) && (_historyItems.size() > _maxHistoryItems)) {
            JRadioButtonMenuItem firstMenuItem = _historyItems.get(0);
            _historyItems.remove(0);
            _menuItemHistoryMapping.remove(firstMenuItem);
            remove(firstMenuItem);
        }
        final HistoryElement historyElement = _backend.createHistoryElement(query, _eventBroker);
        if (historyElement == null) {
            return;
        }
        JRadioButtonMenuItem historyItem = new JRadioButtonMenuItem(historyElement.getMenuDisplayName());
        _buttonGroup.add(historyItem);
        historyItem.setToolTipText(historyElement.getToolTipText());
        historyItem.setSelected(true);
        _menuItemHistoryMapping.put(historyItem, historyElement);
        _historyItems.add(historyItem);
        historyItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayHistoryItem(historyElement);
            }
        });
        add(historyItem);
    }

    public JRadioButtonMenuItem findIfQueryAlreadyExists(Query query) {
        Iterator<JRadioButtonMenuItem> it = _historyItems.iterator();
        while (it.hasNext()) {
            JRadioButtonMenuItem historyItem = it.next();
            HistoryElement historyElement = _menuItemHistoryMapping.get(historyItem);
            Query historyQuery = historyElement.getQuery();
            if (query.equals(historyQuery)) {
                return historyItem;
            }
        }
        return null;
    }

    public JRadioButtonMenuItem getMenuItem(int index) {
        return _historyItems.get(index);
    }

    /**
     * Return currently selected menu item from History menu.
     * @return  selected menu item,
     *          if there is no item selected - return null
     * NOTE: Assuming that there is only one item selected at any time,
     *        if there is more then one items selected - return
     *        the first one.
     */
    private JRadioButtonMenuItem getSelectedHistoryMenuItem() {
        for (JRadioButtonMenuItem cur : _menuItemHistoryMapping.keySet()) {
            if (cur.isSelected()) {
                return cur;
            }
        }
        return null;
    }

    public int getIndexOfSelectedHistoryMenuItem() {
        JRadioButtonMenuItem curSelectedItem = getSelectedHistoryMenuItem();
        if (curSelectedItem == null) {
            return (-1);
        }
        return (_historyItems.indexOf(curSelectedItem));
    }

    protected void enableBackForwardButtons() {
        int curSelectedHistoryIndex = getIndexOfSelectedHistoryMenuItem();
        if (curSelectedHistoryIndex <= 0) {
            _backAction.setEnabled(false);
        } else {
            _backAction.setEnabled(true);
        }
        if (curSelectedHistoryIndex >= (_menuItemHistoryMapping.size() - 1)) {
            _forwardAction.setEnabled(false);
        } else {
            _forwardAction.setEnabled(true);
        }
    }

    public void displayPreviousHistoryItem() {
        int indexOfCur = getIndexOfSelectedHistoryMenuItem();
        JMenuItem menuItem = getMenuItem(indexOfCur - 1);
        displayHistoryItem(menuItem);
    }

    public void displayNextHistoryItem() {
        int indexOfCur = getIndexOfSelectedHistoryMenuItem();
        JMenuItem menuItem = getMenuItem(indexOfCur + 1);
        displayHistoryItem(menuItem);
    }

    private void displayHistoryItem(JMenuItem menuItem) {
        JRadioButtonMenuItem historyItem = (JRadioButtonMenuItem) menuItem;
        HistoryElement historyElement = _menuItemHistoryMapping.get(historyItem);
        displayHistoryItem(historyElement);
    }

    private void displayHistoryItem(HistoryElement historyElement) {
        historyElement.displayElement();
        enableBackForwardButtons();
    }
}

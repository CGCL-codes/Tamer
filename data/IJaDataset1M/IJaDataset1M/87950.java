package de.kout.wlFxp.view.table;

import de.kout.wlFxp.MyFile;
import de.kout.wlFxp.wlFxp;
import de.kout.wlFxp.view.MainPanel;
import de.kout.wlFxp.view.View;
import de.kout.wlFxp.view.ViewKeyListener;
import de.kout.wlFxp.view.ViewMouseListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

/**
 * table that displays the lists of files
 *
 * @author Alexander Kout
 *
 * 30. März 2002
 */
public class MainTable extends JTable implements View {

    MainTableModel tm;

    TableButtonRenderer renderer;

    /**
         * Constructor for the MainList object
         * 
         * @param panel
         *            the panel which ownz this list
         */
    public MainTable(MainPanel panel) {
        super();
        addFocusListener(panel);
        setShowGrid(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setFont(new Font("SansSerif", Font.PLAIN, 12));
        ViewMouseListener ml = new ViewMouseListener(panel);
        ViewKeyListener kl = new ViewKeyListener(panel);
        renderer = new TableButtonRenderer();
        tm = new MainTableModel(panel, wlFxp.getConfig().getShowHidden());
        MainTableColumnModel cm = new MainTableColumnModel(renderer, panel.mode);
        MainTableCellRenderer cr = new MainTableCellRenderer();
        setModel(tm);
        setColumnModel(cm);
        setDefaultRenderer(Object.class, cr);
        getTableHeader().addMouseListener(new TableButtonMouseListener(this, renderer));
        setRowHeight(20);
        setIntercellSpacing(new Dimension(0, 0));
        renderer.setSelectedColumn(0);
        tm.sortBy(0, true);
        ComponentKeyEventHelper.ignoreKeyEvent(this, KeyEvent.VK_ENTER);
        addKeyListener(kl);
        addMouseListener(ml);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param i DOCUMENT ME!
	 */
    public void removeElementAt(int i) {
        tm.removeElementAt(i);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param f DOCUMENT ME!
	 */
    public void removeElement(MyFile f) {
        tm.removeElement(f);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param file DOCUMENT ME!
	 */
    public void addElement(MyFile file) {
        tm.addElement(file);
    }

    /**
	 * Gets the selectedIndex attribute of the MainTable object
	 *
	 * @return The selectedIndex value
	 */
    public int getSelectedIndex() {
        return getSelectedRow();
    }

    /**
	 * Gets the selectedIndices attribute of the MainTable object
	 *
	 * @return The selectedIndices value
	 */
    public int[] getSelectedIndices() {
        return getSelectedRows();
    }

    /**
	 * Gets the elementAt attribute of the MainTable object
	 *
	 * @param row Description of the Parameter
	 *
	 * @return The elementAt value
	 */
    public Object getElementAt(int row) {
        return getModel().getValueAt(row, 0);
    }

    /**
	 * Adds a feature to the SelectionInterval attribute of the
	 * MainTable object
	 *
	 * @param a The feature to be added to the SelectionInterval attribute
	 * @param b The feature to be added to the SelectionInterval attribute
	 */
    public void addSelectionInterval(Point a, Point b) {
        addRowSelectionInterval(rowAtPoint(a), rowAtPoint(b));
    }

    /**
	 * Sets the selectedIndex attribute of the MainTable object
	 *
	 * @param index The new selectedIndex value
	 */
    public void setSelectedIndex(int index) {
        setRowSelectionInterval(index, index);
    }

    /**
	 * Sets the selectedIndex attribute of the MainTable object
	 *
	 * @param a The new selectedIndex value
	 */
    public void setSelectedIndex(Point a) {
        setRowSelectionInterval(rowAtPoint(a), rowAtPoint(a));
    }

    /**
	 * Gets the elementAt attribute of the MainTable object
	 *
	 * @param p Description of the Parameter
	 *
	 * @return The elementAt value
	 */
    public Object getElementAt(Point p) {
        return getElementAt(rowAtPoint(p));
    }

    /**
	 * Gets the oModel attribute of the MainTable object
	 *
	 * @return The oModel value
	 */
    public Object getOModel() {
        return super.getModel();
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public TableButtonRenderer renderer() {
        return renderer;
    }
}

/**
 * Description of the Class
 *
 * @author Alexander Kout
 *
 * 11. August 2003
 */
final class ComponentKeyEventHelper {

    /**
	 * Some components have keys mapped to actions. For example, a
	 * JTable consumes the ENTER key press and moves the cell selection down.
	 * The key events never reach the default button of a dialog. Use this
	 * class to override the mapped action and forward the event to the
	 * specified component's parent.
	 *
	 * @param component Description of the Parameter
	 * @param keyCode Description of the Parameter
	 */
    public static void ignoreKeyEvent(JComponent component, int keyCode) {
        InputMap inputMap = component.getInputMap();
        ActionMap actionMap = component.getActionMap();
        Object actionKey = new Object();
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), actionKey);
        actionMap.put(actionKey, new IgnoreKeyAction(component, keyCode));
    }

    /**
	 * Description of the Class
	 *
	 * @author Alexander Kout
	 *
	 * 11. August 2003
	 */
    private static final class IgnoreKeyAction extends AbstractAction {

        /**
                 * Constructor for the IgnoreKeyAction object
                 * 
                 * @param component
                 *            Description of the Parameter
                 * @param keyCode
                 *            Description of the Parameter
                 */
        public IgnoreKeyAction(JComponent component, int keyCode) {
        }

        /**
		 * Description of the Method
		 *
		 * @param e Description of the Parameter
		 */
        public void actionPerformed(ActionEvent e) {
        }
    }
}

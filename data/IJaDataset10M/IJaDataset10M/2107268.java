package net.sf.tmxeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author mgagne
 */
public class TmxListPanel extends JPanel implements ListSelectionListener {

    /**
     * List model for key display
     */
    private DefaultListModel listModel;

    /**
     * Stores the size of the list panel that displays the locale keys
     */
    private static Dimension listPanelSize;

    /**
     * List to hole listModel and display all key values
     */
    private JList itemList;

    /**
     * Stores the TmxData object that is in use by all GUI elements
     */
    private TmxData data;

    /**
     * The parent containing this view panel instance
     */
    private TmxEditorFrame parent;

    /**
     * Renders changed keys with yellow background
     */
    private TmxListCellRenderer cellRenderer;

    /**
     * Constructor
     * 
     * @param data
     *        the TmxData instance that will sync data between GUI components
     * @param parent
     *        the parent TmxEditorFrame that this panel is part of
     * @param width
     *        the desired width of the overall TmxEditor frame
     * @param height
     *        the desired height of the overall TmxEditor frame
     */
    public TmxListPanel(TmxData data, TmxEditorFrame parent, int width, int height) {
        this.data = data;
        this.parent = parent;
        cellRenderer = new TmxListCellRenderer();
        listPanelSize = new Dimension(230, height);
        listModel = new DefaultListModel();
        this.add(buildListPanel());
    }

    /**
     * Builds the list panel that displays all keys, the list model must be
     * built first
     * 
     * @return
     */
    private JPanel buildListPanel() {
        updateKeyModel();
        itemList = new JList();
        if (data.isShowModified()) {
            cellRenderer.setHighlightColor(Color.YELLOW);
            cellRenderer.setHighlights(data.getModifiedKeys());
        } else if (data.isShowIncomplete()) {
            cellRenderer.setHighlightColor(Color.RED);
            cellRenderer.setHighlights(data.getIncompleteKeys());
        } else {
            cellRenderer.setHighlights(null);
        }
        itemList.setCellRenderer(cellRenderer);
        itemList.addListSelectionListener(this);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemList.setVisibleRowCount(-1);
        itemList.setModel(listModel);
        JScrollPane listScroller = new JScrollPane(itemList);
        listScroller.setPreferredSize(listPanelSize);
        listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
        listPanel.setPreferredSize(listPanelSize);
        listPanel.add(listScroller);
        TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Language Keys");
        listPanel.setBorder(title);
        return listPanel;
    }

    /**
     * Update the key model (used by itemList component) with all keys in TMX
     * store. This means that if only one language has a key, the key will be
     * shown for all others as an empty value.
     */
    private void updateKeyModel() {
        listModel.removeAllElements();
        Iterator<String> itr = data.getAllKeys().iterator();
        while (itr.hasNext()) {
            listModel.addElement(itr.next());
        }
    }

    /**
     * Listens to the key list to change the value panel as the user selects
     * various keys to view/edit.
     * 
     * @param e
     *        ListSelectionEvent from the list panel
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == itemList && itemList.getSelectedIndex() > -1) {
            data.setCurrentKey((String) listModel.get(itemList.getSelectedIndex()));
            parent.refreshViewPanel();
        }
    }

    /**
     * Refresh the key list panel on our gui
     */
    public void refreshListPanel() {
        this.removeAll();
        this.add(buildListPanel());
        parent.pack();
        this.repaint();
    }
}

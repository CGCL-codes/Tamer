package com.sparkit.extracta.builder.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.w3c.dom.*;
import com.sparkit.extracta.*;
import com.sparkit.extracta.builder.*;
import com.sparkit.extracta.builder.tools.*;
import com.sparkit.extracta.edl.*;

/**
 * This class displays the result side of the VisualBuilder. It
 * contains the reuslt tree and the list of selected elements.
 *
 * @version 1.0
 * @author Bostjan Vester
 * @author Dominik Roblek
 * @author Dejan Pazin
 */
public class ResultPanel extends JPanel {

    /** A reference to the main panel */
    private VisualBuilder m_parent;

    /** The appropriate result context*/
    private ResultContext m_context;

    /** The result tree */
    private JTree m_outputTree;

    /** The list of selected elements */
    private JList m_selectedNodesJL;

    /** The document created from the tree, that is the subdocument of the original document */
    private Document m_subdocument;

    /** Temporary file */
    private File m_tempFile;

    /**
   * The constructor takes a VisualBuilder as a parameter to get a reference on
   * other needed objects with which the result panel comunicates.
   *
   * @param parent The main panel
   */
    public ResultPanel(VisualBuilder parent) {
        super(new BorderLayout());
        m_parent = parent;
        setFont(UIFactory.TITLE_FONT);
        setForeground(UIFactory.SELECTED_FOREGROUND);
        setOpaque(true);
        FocusListener fl = new FocusHandler();
        m_context = new ResultContext();
        JPanel titleResultPanel = new JPanel();
        JLabel resultLabel = new JLabel("RESULT");
        resultLabel.setFont(UIFactory.TITLE_FONT);
        resultLabel.setForeground(UIFactory.SELECTED_FOREGROUND);
        resultLabel.setOpaque(true);
        titleResultPanel.add(resultLabel);
        JPanel selectedNodesPanel = new JPanel();
        selectedNodesPanel.setLayout(new GridLayout(1, 2));
        JScrollPane scrollPane = new JScrollPane(m_selectedNodesJL = createSelectedNodesJList(fl, new ListPopupHandler()));
        selectedNodesPanel.add(scrollPane);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Selected elements"));
        JPanel resultTree = new JPanel(new BorderLayout());
        resultTree.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Result tree"));
        resultTree.add(createOutputTree(fl, new TreePopupHandler()));
        add(selectedNodesPanel, BorderLayout.SOUTH);
        add(titleResultPanel, BorderLayout.NORTH);
        add(resultTree, BorderLayout.CENTER);
    }

    /**
   * This creates the list of selected nodes.
   */
    private JList createSelectedNodesJList(FocusListener focusListener, MouseListener mouseListener) {
        final JList list = new JList(new ElementListModel(m_context.getEdl()));
        list.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                    int selected = list.locationToIndex(e.getPoint());
                    if (!list.isSelectedIndex(selected)) {
                        list.setSelectedIndex(selected);
                    }
                }
                if (e.getClickCount() >= 2) {
                    try {
                        m_parent.getEditAction().getCommand().execute();
                    } catch (Throwable t) {
                        UITools.handleThrowable(t);
                    }
                }
            }
        });
        list.setBorder(BorderFactory.createBevelBorder(1));
        list.addListSelectionListener(new ListSelectionHandler());
        list.addFocusListener(focusListener);
        list.addMouseListener(mouseListener);
        SelectedListCellRenderer renderer = new SelectedListCellRenderer();
        list.setCellRenderer(renderer);
        return list;
    }

    /**
   * This creates the result tree.
   */
    private Component createOutputTree(FocusListener focusListener, MouseListener mouseListener) {
        m_outputTree = new JTree();
        m_outputTree.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                m_outputTree.setSelectionPath(m_outputTree.getClosestPathForLocation(e.getX(), e.getY()));
            }
        });
        m_outputTree.setCellRenderer(UIFactory.getTreeCellRenderer());
        m_outputTree.putClientProperty("JTree.lineStyle", "Angled");
        m_outputTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        m_outputTree.addMouseListener(mouseListener);
        m_outputTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "SEARCH_NEXT_NODE");
        m_outputTree.getActionMap().put("SEARCH_NEXT_NODE", m_parent.getFindNextNodeAction());
        m_outputTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK), "SEARCH_NODE");
        m_outputTree.getActionMap().put("SEARCH_NODE", m_parent.getFindNodeAction());
        m_outputTree.addFocusListener(focusListener);
        JScrollPane sp = new JScrollPane(m_outputTree);
        sp.setPreferredSize(new Dimension(400, 400));
        return sp;
    }

    public void setContext(ResultContext context) throws ExtractaException {
        removeAllExportedElements(false);
        m_context = context;
        m_outputTree.setModel(new DomTreeModel(m_context.getDocument(), BuilderSettings.m_tagFilter));
        m_selectedNodesJL.setModel(new ElementListModel(m_context.getEdl()));
        Iterator iter = m_context.getEdl().getItemDescriptors().iterator();
        while (iter.hasNext()) {
            ItemDescriptor itemDescriptor = (ItemDescriptor) iter.next();
            Edl edl2 = new Edl();
            edl2.addItemDescriptor(itemDescriptor);
            UIFactory.getTreeCellRenderer().addMarkedElements(itemDescriptor, ExtractaHelper.filterDocument(m_context.getDocument(), edl2).getNodes());
        }
    }

    public ResultContext getContext() {
        return m_context;
    }

    public JTree getTree() {
        return m_outputTree;
    }

    public java.util.List getSelectedNodes() {
        return ((ElementListModel) m_selectedNodesJL.getModel()).getSelectedItems(m_selectedNodesJL);
    }

    /**
   * Deletes the selected item(s) in the list of exported elements. Item is deleted
   * from the m_selectedNodesList
   */
    public void deleteSelectedElement() throws Exception {
        java.util.List selectedNodes = getSelectedNodes();
        if (selectedNodes.size() > 0) {
            int nResult = JOptionPane.showConfirmDialog(m_parent, "Do you want to remove selected elements?", "Question", JOptionPane.YES_NO_OPTION);
            if (nResult == JOptionPane.YES_OPTION) {
                m_context.getEdl().getItemDescriptors().removeAll(selectedNodes);
                m_selectedNodesJL.setModel(new ElementListModel(m_context.getEdl()));
                UIFactory.getTreeCellRenderer().removeMarkedElements(selectedNodes);
                m_parent.setModified(true);
                m_parent.getRefreshAction().getCommand().execute();
            }
        } else {
            JOptionPane.showMessageDialog(m_parent, "No element selected");
        }
    }

    /**
   * Edit selected element in the list of exported elements
   */
    public void editSelectedElement() throws Exception {
        java.util.List list = ((ElementListModel) m_selectedNodesJL.getModel()).getSelectedItems(m_selectedNodesJL);
        if (list.size() > 0) {
            ItemDescriptor descriptor = (ItemDescriptor) list.get(0);
            ItemDescriptor resultDescriptor = ExportedElementDialog.handleDialog((ItemDescriptor) descriptor.clone());
            if (resultDescriptor != null) {
                UIFactory.getTreeCellRenderer().removeMarkedElements(descriptor);
                m_context.getEdl().exchangeItemDescriptors(descriptor, resultDescriptor);
                m_selectedNodesJL.setModel(new ElementListModel(m_context.getEdl()));
                Edl edl = new Edl();
                edl.addItemDescriptor(resultDescriptor);
                UIFactory.getTreeCellRenderer().addMarkedElements(resultDescriptor, ExtractaHelper.filterDocument(m_context.getDocument(), edl).getNodes());
                m_parent.setModified(true);
            }
        } else {
            JOptionPane.showMessageDialog(m_parent, "No element selected");
        }
    }

    /**
   * Removes all exported elements
   */
    public void removeAllExportedElements() {
        removeAllExportedElements(true);
    }

    public void removeAllExportedElements(boolean bConfirmationDialog) {
        Object[] options = { "OK", "Cancel" };
        int nResult = bConfirmationDialog ? JOptionPane.showConfirmDialog(m_parent, "Do you want to remove all selected elements?", "Question", JOptionPane.YES_NO_OPTION) : JOptionPane.YES_OPTION;
        if (nResult == JOptionPane.YES_OPTION) {
            m_context.getEdl().getItemDescriptors().clear();
            m_selectedNodesJL.setModel(new ElementListModel(m_context.getEdl()));
            UIFactory.getTreeCellRenderer().removeAllMarkedElements();
            m_parent.setModified(true);
        }
    }

    private File getTempFile() throws IOException {
        m_tempFile = File.createTempFile("result", ".html");
        m_tempFile.deleteOnExit();
        return m_tempFile;
    }

    /**
   * Shows the result tree in a browser.
   */
    public void showInBrowser() throws Exception {
        File result = getTempFile();
        if (m_subdocument == null) {
            m_subdocument = createSubdocument();
        }
        DomHelper.printHtml(m_subdocument, new FileOutputStream(result));
        BrowserControl.displayURL("file://" + result.getAbsolutePath());
    }

    public void addElement(ItemDescriptor itemDescriptor) throws Exception {
        if (!m_context.getEdl().getItemDescriptors().contains(itemDescriptor)) {
            m_context.getEdl().addItemDescriptor(itemDescriptor);
        }
        m_selectedNodesJL.setModel(new ElementListModel(m_context.getEdl()));
        Edl edl = new Edl();
        edl.addItemDescriptor(itemDescriptor);
        UIFactory.getTreeCellRenderer().addMarkedElements(itemDescriptor, ExtractaHelper.filterDocument(m_context.getDocument(), edl).getNodes());
    }

    /**
   * This method gets all the elements from the exported elements list
   * and shows the result in a jtree.
   */
    public void refresh() throws Exception {
        UITools.changeCursor(UITools.WAIT_CURSOR, m_parent);
        try {
            m_subdocument = createSubdocument();
            m_outputTree.setModel(new DomTreeModel(m_subdocument, BuilderSettings.m_tagFilter));
            m_outputTree.repaint();
            m_outputTree.setSelectionPath(new TreePath(((DomTreeModel) (m_outputTree.getModel())).getRoot()));
        } finally {
            UITools.changeCursor(UITools.DEFAULT_CURSOR, m_parent);
        }
    }

    private Document createSubdocument() throws Exception {
        return ExtractaHelper.createSubdocument(m_context.getEdl(), m_context.getDocument(), m_context.getContextUrl());
    }

    private class TreePopupHandler extends MouseAdapter {

        private JPopupMenu m_popupMenu;

        public TreePopupHandler() {
            m_popupMenu = createPopup();
        }

        private JPopupMenu createPopup() {
            JPopupMenu menu = new JPopupMenu();
            menu = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem(m_parent.getFindNodeAction());
            menu.add(menuItem);
            menuItem = new JMenuItem(m_parent.getFindNextNodeAction());
            menu.add(menuItem);
            menu.addSeparator();
            menuItem = new JMenuItem(m_parent.getExpandTreeAction());
            menu.add(menuItem);
            menuItem = new JMenuItem(m_parent.getCollapseTreeAction());
            menu.add(menuItem);
            return menu;
        }

        public void mousePressed(MouseEvent e) {
            doShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            doShowPopup(e);
        }

        private void doShowPopup(MouseEvent e) {
            try {
                if (e.isPopupTrigger()) {
                    UITools.showPopup(m_popupMenu, e.getComponent(), e.getX(), e.getY());
                }
            } catch (Throwable t) {
                UITools.handleThrowable(t);
            }
        }
    }

    private class ListPopupHandler extends MouseAdapter {

        private JPopupMenu m_popupMenu;

        public ListPopupHandler() {
            m_popupMenu = createPopup();
        }

        private JPopupMenu createPopup() {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem(m_parent.getEditAction());
            menu.add(menuItem);
            menu.addSeparator();
            menuItem = new JMenuItem(m_parent.getDeleteAction());
            menu.add(menuItem);
            menuItem = new JMenuItem(m_parent.getRemoveAllExportedElementsAction());
            menu.add(menuItem);
            return menu;
        }

        public void mousePressed(MouseEvent e) {
            doShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            doShowPopup(e);
        }

        private void doShowPopup(MouseEvent e) {
            try {
                if (e.isPopupTrigger()) {
                    UITools.showPopup(m_popupMenu, e.getComponent(), e.getX(), e.getY());
                }
            } catch (Throwable t) {
                UITools.handleThrowable(t);
            }
        }
    }

    /**
   * Controls the color of selected node in a tree and list. When the tree loses
   * focus the color of selected node turns gray.
   */
    private class FocusHandler implements FocusListener {

        public void focusGained(FocusEvent e) {
            try {
                Object source = e.getSource();
                if (source instanceof JTree) {
                    redrawTree(UIFactory.SELECTED_BACKGROUND);
                } else if (source instanceof JList) {
                    ((SelectedListCellRenderer) (m_selectedNodesJL.getCellRenderer())).hasFocus(true);
                }
            } catch (Throwable t) {
                UITools.handleThrowable(t);
            }
        }

        public void focusLost(FocusEvent e) {
            try {
                Object source = e.getSource();
                if (source instanceof JTree) {
                    redrawTree(Color.lightGray);
                } else if (source instanceof JList) {
                    ((SelectedListCellRenderer) (m_selectedNodesJL.getCellRenderer())).hasFocus(false);
                }
            } catch (Throwable t) {
                UITools.handleThrowable(t);
            }
        }

        private void redrawTree(Color color) {
            DomTreeCellRenderer renderer = (DomTreeCellRenderer) m_outputTree.getCellRenderer();
            renderer.m_selectedBackgroundColor = color;
            m_outputTree.repaint();
        }
    }

    /**
   * This class is used for setting the model for the list of selected nodes.
   */
    private class ElementListModel extends AbstractListModel {

        private Edl m_edl;

        public ElementListModel(Edl edl) {
            m_edl = edl;
        }

        public int getSize() {
            return m_edl.getItemDescriptors().size();
        }

        public Object getElementAt(int index) {
            return ((ItemDescriptor) m_edl.getItemDescriptors().get(index)).getName();
        }

        public java.util.List getSelectedItems(JList jList) {
            java.util.List list = new ArrayList();
            if (jList != null) {
                int selected[] = jList.getSelectedIndices();
                for (int i = 0; i < selected.length; i++) {
                    list.add(m_edl.getItemDescriptors().get(selected[i]));
                }
            }
            return list;
        }
    }

    /**
   * This class is used to control the repainting of items in the tree that
   * are referenced by the selected elements in the list.
   */
    private class ListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            try {
                UIFactory.getTreeCellRenderer().setReferencedItems(getSelectedNodes());
                m_parent.getRefreshInputAction().getCommand().execute();
            } catch (Throwable t) {
                UITools.handleThrowable(t);
            }
        }
    }
}

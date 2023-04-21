package org.tidelaget.gui.tree;

import org.tidelaget.gui.*;
import org.tidelaget.gui.tree.listener.*;
import org.tidelaget.gui.tree.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FileTreePanel extends JPanel {

    protected static final int BUFFER_EMPTY = 0;

    protected static final int BUFFER_CUT = 1;

    protected static final int BUFFER_COPY = 2;

    protected static final int NODES_SELECTED = 4;

    protected static final int DIR_NODE_SELECTED = 8;

    protected int m_state = BUFFER_EMPTY;

    protected TIDENode[] m_nodesToActUpon;

    protected JScrollPane m_scrollPane;

    protected FileTree m_fileTree;

    protected Vector m_listeners = new Vector();

    protected JComponent m_source;

    protected TButton m_cutBut;

    protected TButton m_copyBut;

    protected TButton m_pasteBut;

    protected TButton m_deleteBut;

    protected int m_width = 0;

    protected int m_height = 0;

    protected static ImageIcon m_iconCut = new ImageIcon(ClassLoader.getSystemResource("icons/icon_fileexplorer_cut.gif"));

    protected static ImageIcon m_iconCopy = new ImageIcon(ClassLoader.getSystemResource("icons/icon_fileexplorer_copy.gif"));

    protected static ImageIcon m_iconPaste = new ImageIcon(ClassLoader.getSystemResource("icons/icon_fileexplorer_paste.gif"));

    protected static ImageIcon m_iconDelete = new ImageIcon(ClassLoader.getSystemResource("icons/icon_fileexplorer_delete.gif"));

    protected static ImageIcon m_iconClose = new ImageIcon(ClassLoader.getSystemResource("icons/icon_fileexplorer_close.gif"));

    public FileTreePanel(FileTree fileTree) {
        m_source = this;
        m_fileTree = fileTree;
        m_scrollPane = new JScrollPane(fileTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setLayout(new BorderLayout());
        m_fileTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent event) {
                TreePath[] selPaths = m_fileTree.getSelectionPaths();
                if (selPaths != null) {
                    m_state |= NODES_SELECTED;
                    if (selPaths.length > 1) m_state &= (Integer.MAX_VALUE - DIR_NODE_SELECTED); else if (selPaths[0].getLastPathComponent() instanceof IfcDirectoryNode) m_state |= DIR_NODE_SELECTED; else m_state &= (Integer.MAX_VALUE - DIR_NODE_SELECTED);
                } else {
                    m_state &= (Integer.MAX_VALUE - NODES_SELECTED);
                    m_state &= (Integer.MAX_VALUE - DIR_NODE_SELECTED);
                }
                updateButtonState();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        m_width = 0;
        m_height = 0;
        m_cutBut = new TButton(m_iconCut);
        m_cutBut.setToolTipText("Cut files");
        actionPanel.add(m_cutBut);
        m_copyBut = new TButton(m_iconCopy);
        m_copyBut.setToolTipText("Copy files");
        actionPanel.add(m_copyBut);
        m_pasteBut = new TButton(m_iconPaste);
        m_pasteBut.setToolTipText("Paste files");
        actionPanel.add(m_pasteBut);
        m_deleteBut = new TButton(m_iconDelete);
        m_deleteBut.setToolTipText("Delete files");
        actionPanel.add(m_deleteBut);
        JPanel systemPanel = new JPanel();
        systemPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        TButton closeBut = new TButton(m_iconClose);
        closeBut.setToolTipText("Close file explorer");
        systemPanel.add(closeBut);
        buttonPanel.add(actionPanel, BorderLayout.WEST);
        buttonPanel.add(systemPanel, BorderLayout.EAST);
        m_cutBut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_nodesToActUpon = getSelectedNodes();
                m_state |= BUFFER_CUT;
                m_state &= (Integer.MAX_VALUE - BUFFER_COPY);
                updateButtonState();
            }
        });
        m_copyBut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_nodesToActUpon = getSelectedNodes();
                m_state &= (Integer.MAX_VALUE - BUFFER_CUT);
                m_state |= BUFFER_COPY;
                updateButtonState();
            }
        });
        m_pasteBut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < m_listeners.size(); i++) {
                    FileTreePanelEvent event = new FileTreePanelEvent(m_source, m_nodesToActUpon, getSelectedNode(), m_fileTree);
                    if ((m_state & BUFFER_CUT) == BUFFER_CUT) {
                        ((FileTreePanelListener) m_listeners.get(i)).pasteCut(event);
                    } else if ((m_state & BUFFER_COPY) == BUFFER_COPY) {
                        ((FileTreePanelListener) m_listeners.get(i)).pasteCopy(event);
                    }
                }
                if ((m_state & BUFFER_CUT) == BUFFER_CUT) {
                    m_state &= (Integer.MAX_VALUE - BUFFER_CUT);
                    m_state &= (Integer.MAX_VALUE - BUFFER_COPY);
                    updateButtonState();
                }
            }
        });
        m_deleteBut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < m_listeners.size(); i++) {
                    FileTreePanelEvent event = new FileTreePanelEvent(m_source, getSelectedNodes(), null, null);
                    ((FileTreePanelListener) m_listeners.get(i)).delete(event);
                }
            }
        });
        closeBut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < m_listeners.size(); i++) {
                    FileTreePanelEvent event = new FileTreePanelEvent(m_source, null, null, null);
                    ((FileTreePanelListener) m_listeners.get(i)).close(event);
                }
            }
        });
        add(buttonPanel, BorderLayout.NORTH);
        add(m_scrollPane, BorderLayout.CENTER);
        setMinimumSize(new Dimension(150, 100));
        updateButtonState();
    }

    public void addFileTreePanelListener(FileTreePanelListener pl) {
        if (!m_listeners.contains(pl)) m_listeners.add(pl);
    }

    public void removeFileTreePanelListener(FileTreePanelListener pl) {
        if (m_listeners.contains(pl)) m_listeners.remove(pl);
    }

    public EventListener[] getListeners() {
        EventListener[] eArray = new EventListener[m_listeners.size()];
        for (int i = 0; i < m_listeners.size(); i++) eArray[i] = (EventListener) m_listeners.get(i);
        return eArray;
    }

    public TIDENode[] getSelectedNodes() {
        TreePath[] paths = m_fileTree.getSelectionPaths();
        TIDENode[] res = null;
        if (paths != null) {
            res = new TIDENode[paths.length];
            for (int i = 0; i < paths.length; i++) {
                res[i] = (TIDENode) paths[i].getLastPathComponent();
            }
        }
        return res;
    }

    public TIDENode getSelectedNode() {
        TreePath path = m_fileTree.getSelectionPath();
        TIDENode res = null;
        if (path != null) res = (TIDENode) path.getLastPathComponent();
        return res;
    }

    public void updateButtonState() {
        m_cutBut.setEnabled((m_state & NODES_SELECTED) == NODES_SELECTED);
        m_copyBut.setEnabled((m_state & NODES_SELECTED) == NODES_SELECTED);
        m_deleteBut.setEnabled((m_state & NODES_SELECTED) == NODES_SELECTED);
        m_pasteBut.setEnabled(((m_state & BUFFER_CUT) == BUFFER_CUT || (m_state & BUFFER_COPY) == BUFFER_COPY) && (m_state & DIR_NODE_SELECTED) == DIR_NODE_SELECTED);
    }
}

package geovista.touchgraph.interaction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.AbstractAction;
import javax.swing.ComponentInputMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import geovista.touchgraph.Edge;
import geovista.touchgraph.LinkGraph;
import geovista.touchgraph.Node;
import geovista.touchgraph.SubspaceLinkGraph;
import geovista.touchgraph.TGException;
import geovista.touchgraph.TGPanel;

/** GLEditUI:  User Interface for editing the graph.
  *
  * @author   Alexander Shapiro
  * 
  */
public class GLEditUI extends TGUserInterface {

    /** True when the current UI is active. */
    transient TGPanel tgPanel;

    transient DragAddUI dragAddUI;

    transient DragNodeUI dragNodeUI;

    transient DragMultiselectUI dragMultiselectUI;

    transient TGAbstractClickUI switchSelectUI;

    transient TGAbstractDragUI hvDragUI;

    transient GLEditMouseListener ml;

    transient GLEditMouseMotionListener mml;

    transient JPopupMenu nodePopup;

    transient JPopupMenu edgePopup;

    transient JPopupMenu backPopup;

    transient Node popupNode;

    transient Edge popupEdge;

    transient AbstractAction deleteSelectAction;

    final transient KeyStroke deleteKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);

    /** Constructor with TGPanel <tt>tgp</tt>.
     */
    public GLEditUI(TGPanel tgp) {
        active = false;
        tgPanel = tgp;
        ml = new GLEditMouseListener();
        mml = new GLEditMouseMotionListener();
        deleteSelectAction = new AbstractAction("DeleteSelect") {

            public void actionPerformed(ActionEvent e) {
                Node select = tgPanel.getSelect();
                if (select != null) {
                    tgPanel.deleteNode(select);
                    tgPanel.repaint();
                }
            }
        };
        dragAddUI = new DragAddUI(tgPanel);
        dragNodeUI = new DragNodeUI(tgPanel);
        dragMultiselectUI = new DragMultiselectUI(tgPanel);
        switchSelectUI = tgPanel.getSwitchSelectUI();
        setUpNodePopup();
        setUpEdgePopup();
        setUpBackPopup();
    }

    public GLEditUI(LinkGraph glPanel) {
        this(glPanel.getTGPanel());
        hvDragUI = glPanel.hvScroll.getHVDragUI();
    }

    public GLEditUI(SubspaceLinkGraph glPanel) {
        this(glPanel.getTGPanel());
        hvDragUI = glPanel.hvScroll.getHVDragUI();
    }

    public void activate() {
        tgPanel.addMouseListener(ml);
        tgPanel.addMouseMotionListener(mml);
        tgPanel.getActionMap().put("DeleteSelect", deleteSelectAction);
        ComponentInputMap cim = new ComponentInputMap(tgPanel);
        cim.put(deleteKey, "DeleteSelect");
        tgPanel.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, cim);
        active = true;
    }

    public void deactivate() {
        if (!active) dragMultiselectUI.deactivate();
        tgPanel.removeMouseListener(ml);
        tgPanel.removeMouseMotionListener(mml);
        tgPanel.getInputMap().put(deleteKey, null);
        tgPanel.getActionMap().put("DeleteSelect", null);
        active = false;
    }

    class GLEditMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            Node mouseOverN = tgPanel.getMouseOverN();
            Node select = tgPanel.getSelect();
            if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
                if (mouseOverN != null) {
                    if (mouseOverN != select) dragNodeUI.activate(e); else dragAddUI.activate(e);
                } else if (hvDragUI != null) hvDragUI.activate(e);
            }
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getModifiers() == InputEvent.BUTTON1_MASK) switchSelectUI.activate(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popupNode = tgPanel.getMouseOverN();
                popupEdge = tgPanel.getMouseOverE();
                if (popupNode != null) {
                    tgPanel.setMaintainMouseOver(true);
                    nodePopup.show(e.getComponent(), e.getX(), e.getY());
                } else if (popupEdge != null) {
                    tgPanel.setMaintainMouseOver(true);
                    edgePopup.show(e.getComponent(), e.getX(), e.getY());
                } else {
                    backPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    class GLEditMouseMotionListener extends MouseMotionAdapter {

        public void mouseMoved(MouseEvent e) {
        }
    }

    private void setUpNodePopup() {
        nodePopup = new JPopupMenu();
        JMenuItem menuItem;
        JMenu navigateMenu = new JMenu("Navigate");
        menuItem = new JMenuItem("Delete Node");
        ActionListener deleteNodeAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    tgPanel.deleteNode(popupNode);
                }
            }
        };
        menuItem.addActionListener(deleteNodeAction);
        nodePopup.add(menuItem);
        menuItem = new JMenuItem("Expand Node");
        ActionListener expandAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    tgPanel.expandNode(popupNode);
                }
            }
        };
        menuItem.addActionListener(expandAction);
        navigateMenu.add(menuItem);
        menuItem = new JMenuItem("Hide Node");
        ActionListener hideAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Node select = tgPanel.getSelect();
                if (popupNode != null) {
                    tgPanel.hideNode(popupNode, select);
                }
            }
        };
        menuItem.addActionListener(hideAction);
        navigateMenu.add(menuItem);
        nodePopup.add(navigateMenu);
        nodePopup.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuCanceled(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverN(null);
                tgPanel.repaint();
            }

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }
        });
    }

    private void setUpEdgePopup() {
        edgePopup = new JPopupMenu();
        JMenuItem menuItem;
        menuItem = new JMenuItem("Relax Edge");
        ActionListener relaxEdgeAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (popupEdge != null) {
                    popupEdge.setLength(popupEdge.getLength() * 4);
                    tgPanel.resetDamper();
                }
            }
        };
        menuItem.addActionListener(relaxEdgeAction);
        edgePopup.add(menuItem);
        menuItem = new JMenuItem("Tighten Edge");
        ActionListener tightenEdgeAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (popupEdge != null) {
                    popupEdge.setLength(popupEdge.getLength() / 4);
                    tgPanel.resetDamper();
                }
            }
        };
        menuItem.addActionListener(tightenEdgeAction);
        edgePopup.add(menuItem);
        menuItem = new JMenuItem("Delete Edge");
        ActionListener deleteEdgeAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (popupEdge != null) {
                    tgPanel.deleteEdge(popupEdge);
                }
            }
        };
        menuItem.addActionListener(deleteEdgeAction);
        edgePopup.add(menuItem);
        edgePopup.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuCanceled(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverE(null);
                tgPanel.repaint();
            }

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }
        });
    }

    private void setUpBackPopup() {
        backPopup = new JPopupMenu();
        JMenuItem menuItem;
        menuItem = new JMenuItem("Multi-Select");
        ActionListener multiselectAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dragMultiselectUI.activate(GLEditUI.this);
            }
        };
        menuItem.addActionListener(multiselectAction);
        backPopup.add(menuItem);
        menuItem = new JMenuItem("Start Over");
        ActionListener startOverAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tgPanel.clearAll();
                tgPanel.clearSelect();
                try {
                    tgPanel.addNode();
                } catch (TGException tge) {
                    System.err.println(tge.getMessage());
                    tge.printStackTrace(System.err);
                }
                tgPanel.fireResetEvent();
                tgPanel.repaint();
            }
        };
        menuItem.addActionListener(startOverAction);
        backPopup.add(menuItem);
    }
}

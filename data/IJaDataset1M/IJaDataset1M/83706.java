package com.touchgraph.linkbrowser;

import com.touchgraph.graphlayout.graphelements.*;
import com.touchgraph.graphlayout.interaction.*;
import com.touchgraph.graphlayout.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class LBNavigateUI extends TGUserInterface {

    TGPanel tgPanel;

    TGLinkBrowser tgLinkBrowser;

    LBNavigateMouseListener ml;

    TGAbstractDragUI hvDragUI;

    DragNodeUI dragNodeUI;

    LocalityScroll localityScroll;

    JPopupMenu nodePopup;

    JPopupMenu edgePopup;

    Node popupNode;

    Edge popupEdge;

    public LBNavigateUI(TGLinkBrowser tglb) {
        tgLinkBrowser = tglb;
        tgPanel = tgLinkBrowser.getTGPanel();
        localityScroll = tgLinkBrowser.localityScroll;
        hvDragUI = tgLinkBrowser.hvScroll.getHVDragUI();
        dragNodeUI = new DragNodeUI(tgPanel);
        ml = new LBNavigateMouseListener();
        setUpNodePopup();
        setUpEdgePopup();
    }

    @Override
    public void activate() {
        tgPanel.addMouseListener(ml);
    }

    @Override
    public void deactivate() {
        tgPanel.removeMouseListener(ml);
    }

    class LBNavigateMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            Node mouseOverN = tgPanel.getMouseOverN();
            if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
                if (mouseOverN == null) hvDragUI.activate(e); else dragNodeUI.activate(e);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Node mouseOverN = tgPanel.getMouseOverN();
            Node select = tgPanel.getSelect();
            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
                if (mouseOverN != null) {
                    if (mouseOverN != select) {
                        tgPanel.setSelect(mouseOverN);
                        tgPanel.setLocale(mouseOverN, localityScroll.getRadius());
                    }
                }
            }
        }

        @Override
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
                }
            }
        }
    }

    private void setUpNodePopup() {
        nodePopup = new JPopupMenu();
        JMenuItem menuItem;
        menuItem = new JMenuItem("Expand Node");
        ActionListener expandAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    tgPanel.expandNode(popupNode);
                }
            }
        };
        menuItem.addActionListener(expandAction);
        nodePopup.add(menuItem);
        menuItem = new JMenuItem("Hide Node");
        ActionListener hideAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Node select = tgPanel.getSelect();
                if (popupNode != null) {
                    tgPanel.hideNode(popupNode, select);
                }
            }
        };
        menuItem.addActionListener(hideAction);
        nodePopup.add(menuItem);
        menuItem = new JMenuItem("Select Node");
        ActionListener selectAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    tgPanel.setSelect(popupNode);
                }
            }
        };
        menuItem.addActionListener(selectAction);
        nodePopup.add(menuItem);
        nodePopup.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverN(null);
                tgPanel.repaint();
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }
        });
    }

    private void setUpEdgePopup() {
        edgePopup = new JPopupMenu();
        JMenuItem menuItem;
        menuItem = new JMenuItem("Hide Edge");
        ActionListener hideAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (popupEdge != null) {
                    tgPanel.hideEdge(popupEdge);
                }
            }
        };
        menuItem.addActionListener(hideAction);
        edgePopup.add(menuItem);
        edgePopup.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverE(null);
                tgPanel.repaint();
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }
        });
    }
}

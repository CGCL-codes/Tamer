package org.fudaa.ebli.visuallibrary;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.fudaa.ebli.visuallibrary.tree.EbliWidgetJXTreeTableModel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;

/**
 * Classe qui gere la synchronisation entre la scene et les autres composants d affichage de la scene tel que l arbre.
 * 
 * @author Adrien Hadoux
 */
public class EbliWidgetSynchroniser implements ObjectSceneListener, TreeSelectionListener {

    /**
   * la scene a synchroniser.
   */
    EbliScene scene_;

    /**
   * l arbre representant la scene.
   */
    EbliWidgetJXTreeTableModel treeModel_;

    JXTreeTable arbre_;

    /**
   * booleen qui permet d eviter de realiser des cycles entre la selection des nodes sur le tree et sur la scene.
   */
    boolean noCycleScene = true;

    boolean noCycleTree = false;

    public EbliWidgetSynchroniser(final EbliWidgetJXTreeTableModel treeModel, final JXTreeTable arbre) {
        super();
        treeModel_ = treeModel;
        this.scene_ = treeModel_.getScene();
        arbre_ = arbre;
        arbre_.addTreeSelectionListener(this);
        arbre_.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.isPopupTrigger() && e.getClickCount() == 1) {
                    affichePopupNode(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (e.isPopupTrigger() && e.getClickCount() == 1) {
                    affichePopupNode(e.getX(), e.getY());
                }
            }
        });
    }

    /**
   * Methode qui affiche la popup du node selectionn�. entree: la position ou afficher la popup
   * 
   * @param x
   * @param y
   */
    public void affichePopupNode(final int x, final int y) {
        final TreePath clickedElement = arbre_.getPathForLocation(x, y);
        MutableTreeTableNode treeNode = null;
        if (clickedElement != null) treeNode = (MutableTreeTableNode) clickedElement.getLastPathComponent();
        if (treeNode != null) {
            final EbliNode node = (EbliNode) treeNode.getUserObject();
            if (node != null && node.hasWidget() && node.getWidget().isVisible()) {
                JPopupMenu poppup = null;
                if (node.getWidget() instanceof EbliWidgetBordureSingle) {
                    final EbliWidgetBordureSingle bordure = (EbliWidgetBordureSingle) node.getWidget();
                    poppup = bordure.intern_.getController().getPopup();
                } else poppup = node.getWidget().getController().getPopup();
                if (poppup != null) poppup.show(arbre_, x, y);
            }
        }
    }

    public void focusChanged(final ObjectSceneEvent event, final Object previousFocusedObject, final Object newFocusedObject) {
    }

    public void highlightingChanged(final ObjectSceneEvent event, final Set<Object> previousHighlighting, final Set<Object> newHighlighting) {
    }

    public void hoverChanged(final ObjectSceneEvent event, final Object previousHoveredObject, final Object newHoveredObject) {
    }

    public void objectAdded(final ObjectSceneEvent event, final Object addedObject) {
    }

    public void objectRemoved(final ObjectSceneEvent event, final Object removedObject) {
    }

    public void objectStateChanged(final ObjectSceneEvent event, final Object changedObject, final ObjectState previousState, final ObjectState newState) {
    }

    /**
   * Des que la selection change dans la scene, on selectionne le noeud correspondand dans le tree.
   */
    public void selectionChanged(final ObjectSceneEvent event, final Set<Object> previousSelection, final Set<Object> newSelection) {
        if (noCycleScene) {
            noCycleScene = false;
            if (newSelection != null && newSelection.size() != 0) {
                final DefaultListSelectionModel model = (DefaultListSelectionModel) arbre_.getSelectionModel();
                model.clearSelection();
                for (final Iterator<Object> it = newSelection.iterator(); it.hasNext(); ) {
                    final EbliNode nodeSelect = (EbliNode) it.next();
                    final MutableTreeTableNode treeNode = treeModel_.findTreeTableNode(nodeSelect);
                    model.addSelectionInterval(treeModel_.getIndexOfChild(treeModel_.getRoot(), treeNode), treeModel_.getIndexOfChild(treeModel_.getRoot(), treeNode));
                }
                arbre_.validate();
                scene_.refresh();
            } else {
                arbre_.clearSelection();
            }
            noCycleScene = true;
        }
    }

    /**
   * signal envoye par l arbre lors du changement de selection.
   */
    public void valueChanged(final TreeSelectionEvent e) {
        if (noCycleScene) {
            noCycleScene = false;
            if (arbre_.getSelectedRows() != null && arbre_.getSelectedRows().length != 0) {
                final Set<Object> listeNodeSelect = new HashSet<Object>();
                final TreePath[] paths = arbre_.getTreeSelectionModel().getSelectionPaths();
                for (int j = 0; j < paths.length; j++) {
                    final MutableTreeTableNode treeNode = ((MutableTreeTableNode) paths[j].getLastPathComponent());
                    final EbliNode nodeSelect = (EbliNode) treeNode.getUserObject();
                    listeNodeSelect.add(nodeSelect);
                }
                scene_.setSelectedObjects(listeNodeSelect);
                arbre_.validate();
                scene_.refresh();
            }
            noCycleScene = true;
        }
    }
}

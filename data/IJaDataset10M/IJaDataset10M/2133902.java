package net.sf.gridarta.gui.mapmenu;

import java.awt.Cursor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements a drag source for {@link JTree} instances.
 * @author Andreas Kirschbaum
 */
public class TreeDragSource {

    /**
     * The {@link JTree} being monitored.
     */
    @NotNull
    private final JTree tree;

    /**
     * The {@link DragSource} for {@link #tree}.
     */
    @NotNull
    private final DragSource dragSource = new DragSource();

    /**
     * The {@link MutableTreeNode} being dragged.
     */
    @Nullable
    private MutableTreeNode draggedTreeNode = null;

    /**
     * The {@link DragSourceListener} attached to {@link #dragSource}.
     */
    @NotNull
    private final DragSourceListener dragSourceListener = new DragSourceListener() {

        @Override
        public void dragEnter(@NotNull final DragSourceDragEvent dsde) {
        }

        @Override
        public void dragOver(@NotNull final DragSourceDragEvent dsde) {
        }

        @Override
        public void dropActionChanged(@NotNull final DragSourceDragEvent dsde) {
        }

        @Override
        public void dragExit(@NotNull final DragSourceEvent dse) {
        }

        @Override
        public void dragDropEnd(@NotNull final DragSourceDropEvent dsde) {
            if (draggedTreeNode == null) {
                return;
            }
            if (dsde.getDropSuccess()) {
                switch(dsde.getDropAction()) {
                    case DnDConstants.ACTION_COPY:
                        break;
                    case DnDConstants.ACTION_MOVE:
                        ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(draggedTreeNode);
                        break;
                }
            }
            draggedTreeNode = null;
        }
    };

    /**
     * Creates a new instance.
     * @param tree the tree to monitor
     * @param actions the actions to support
     */
    public TreeDragSource(@NotNull final JTree tree, final int actions) {
        this.tree = tree;
        final DragGestureListener dragGestureListener = new DragGestureListener() {

            @Override
            public void dragGestureRecognized(@NotNull final DragGestureEvent dge) {
                final TreePath selectionPath = tree.getSelectionPath();
                if (selectionPath == null || selectionPath.getPathCount() <= 1) {
                    return;
                }
                draggedTreeNode = (MutableTreeNode) selectionPath.getLastPathComponent();
                final Cursor cursor;
                switch(dge.getDragAction()) {
                    case DnDConstants.ACTION_COPY:
                        cursor = DragSource.DefaultCopyDrop;
                        break;
                    case DnDConstants.ACTION_MOVE:
                        cursor = DragSource.DefaultMoveDrop;
                        break;
                    default:
                        return;
                }
                dragSource.startDrag(dge, cursor, new TransferableTreeNode(selectionPath), dragSourceListener);
            }
        };
        dragSource.createDefaultDragGestureRecognizer(tree, actions, dragGestureListener);
    }
}

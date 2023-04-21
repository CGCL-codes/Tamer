package thoto.jamyda.gui;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import thoto.jamyda.controller.ExceptionHandler;
import thoto.jamyda.controller.MainController;
import thoto.jamyda.data.AppConstants;
import thoto.jamyda.data.AppData;
import thoto.jamyda.data.AppTransferable;
import thoto.jamyda.data.CategoryData;

public class CategoryTree extends JTree implements DropTargetListener, Autoscroll {

    private static final int AUTOSCROLL_MARGIN = 6;

    /**
	 * Creates a new instance of CategoryTree
	 * 
	 */
    public CategoryTree() {
        setDropTarget(new DropTarget(this, this));
    }

    /**
	 * Returns the currently selected category during drag action.
	 * 
	 * @param dtde
	 *            The dispatched event
	 * 
	 * @return A category or null.
	 */
    private CategoryData getCurrentDropCategory(DropTargetDragEvent dtde) {
        Point dropPoint = dtde.getLocation();
        TreePath dropPath = getPathForLocation(dropPoint.x, dropPoint.y);
        if (null != dropPath) {
            return (CategoryData) ((DefaultMutableTreeNode) dropPath.getLastPathComponent()).getUserObject();
        }
        return null;
    }

    /**
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
    public void dragOver(DropTargetDragEvent dtde) {
        Point dragPoint = dtde.getLocation();
        TreePath path = getPathForLocation(dragPoint.x, dragPoint.y);
        setSelectionPath(path);
        repaint();
        CategoryData curCat = getCurrentDropCategory(dtde);
        if (AppConstants.CATEGORY_ALL != curCat) {
            dtde.acceptDrag(dtde.getDropAction());
        } else {
            dtde.rejectDrag();
        }
    }

    /**
	 * 
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
    public void drop(DropTargetDropEvent dtde) {
        Point dropPoint = dtde.getLocation();
        TreePath dropPath = getPathForLocation(dropPoint.x, dropPoint.y);
        if (null == dropPath) {
            dtde.dropComplete(false);
            return;
        }
        CategoryData targetCategory = (CategoryData) ((DefaultMutableTreeNode) dropPath.getLastPathComponent()).getUserObject();
        String newCategoryPath = CategoryData.getPath(dropPath);
        boolean dropped = false;
        try {
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
            Object droppedObject = dtde.getTransferable().getTransferData(AppTransferable.LOCAL_OBJECT_FLAVOR);
            if (droppedObject instanceof List) {
                List<AppData> appDataList = (List<AppData>) droppedObject;
                for (int a = 0, z = appDataList.size(); a < z; a++) {
                    AppData appData = appDataList.get(a);
                    if (AppConstants.CATEGORY_FAVORITES == targetCategory) {
                        appData.setAddedToFavorites(true);
                    } else {
                        appData.setCategoryPath(newCategoryPath);
                    }
                    appData.save();
                }
            }
            MainController.getInstance().updateApplicationList();
            dropped = true;
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        dtde.dropComplete(dropped);
    }

    /**
	 * 
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    /**
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
    public void dragEnter(DropTargetDragEvent dtde) {
    }

    /**
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
    public void dragExit(DropTargetEvent dte) {
    }

    /**
	 *
	 * @see java.awt.dnd.Autoscroll#autoscroll(java.awt.Point)
	 */
    public void autoscroll(Point cursorLocn) {
        int currentRow = getRowForLocation(cursorLocn.x, cursorLocn.y);
        Rectangle outerBounds = getBounds();
        currentRow = (cursorLocn.y + outerBounds.y <= AUTOSCROLL_MARGIN ? currentRow < 1 ? 0 : currentRow - 1 : currentRow < getRowCount() - 1 ? currentRow + 1 : currentRow);
        scrollRowToVisible(currentRow);
    }

    /**
	 *
	 * @see java.awt.dnd.Autoscroll#getAutoscrollInsets()
	 */
    public Insets getAutoscrollInsets() {
        Rectangle outerBounds = getBounds();
        Rectangle innerBounds = getParent().getBounds();
        return new Insets(innerBounds.y - outerBounds.y + AUTOSCROLL_MARGIN, innerBounds.x - outerBounds.x + AUTOSCROLL_MARGIN, outerBounds.height - innerBounds.height - innerBounds.y + outerBounds.y + AUTOSCROLL_MARGIN, outerBounds.width - innerBounds.width - innerBounds.x + outerBounds.x + AUTOSCROLL_MARGIN);
    }
}

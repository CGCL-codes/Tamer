package com.anothergtdapp.controller;

import com.anothergtdapp.model.ActionTableModel;
import com.anothergtdapp.model.TransferableGTD;
import com.anothergtdapp.view.Messages;
import com.anothergtdapp.db.Action;
import com.anothergtdapp.db.DBController;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;

class IExecute {

    public Action orig;

    public List<Action> origList;

    public Action dest;

    public DropTargetDropEvent dtde;

    public void execute(boolean up) {
    }

    ;
}

/**
 *
 * @author adolfo
 */
public class ActionTableDragAndDrop implements DragGestureListener, DropTargetListener, DragSourceListener {

    private JXTable table;

    private ActionTableController controller;

    private static Logger logger = Logger.getLogger(ActionTableDragAndDrop.class.getName());

    public ActionTableDragAndDrop(JXTable table, ActionTableController controller) {
        this.table = table;
        this.controller = controller;
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(table, DnDConstants.ACTION_COPY_OR_MOVE, this);
        DropTarget dropTarget = new DropTarget(table, this);
    }

    public void dragGestureRecognized(DragGestureEvent dge) {
        JXTable ltable = (JXTable) dge.getComponent();
        if (ltable != table) {
            logger.error("no parece ser el objeto que estamos controlando");
            return;
        }
        int[] selectedrows = table.getSelectedRows();
        for (int i = 0; i < selectedrows.length; i++) selectedrows[i] = table.convertRowIndexToModel(selectedrows[i]);
        if (selectedrows.length == 0) {
            logger.warn("Nothing selected - Drag parado");
        } else {
            ArrayList<Action> al = new ArrayList<Action>(selectedrows.length);
            ActionTableModel m = (ActionTableModel) table.getModel();
            for (int i : selectedrows) {
                al.add(m.getAction(i));
            }
            TransferableGTD t = new TransferableGTD(al);
            try {
                dge.startDrag(DragSource.DefaultCopyDrop, t, this);
            } catch (InvalidDnDOperationException e) {
                logger.error("Operación drag inválida:" + e.getMessage());
            }
        }
    }

    public void dragEnter(DropTargetDragEvent dtde) {
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent dtde) {
        if (table.getSortedColumn() != null) {
            Messages.error("Error en la colocación", "Para colocar los elementos, la tabla no debe estar ordenada");
            dtde.rejectDrop();
            return;
        }
        Point location = dtde.getLocation();
        if (location == null) return;
        int row = table.convertRowIndexToModel(table.rowAtPoint(location));
        if (row < 0 || row > table.getRowCount() - 1) {
            Messages.error("Error en la colocación", "Fila fuera de rango : " + row);
            dtde.rejectDrop();
            return;
        }
        Action dest = ((ActionTableModel) table.getModel()).getAction(row);
        if (dest == null) {
            Messages.error("Error en la colocación", "Fila destino nula");
            dtde.rejectDrop();
            return;
        }
        try {
            Transferable tr = dtde.getTransferable();
            if (tr.isDataFlavorSupported(TransferableGTD.DEFAULT_ACTION_FLAVOR) && tr.getTransferData(TransferableGTD.DEFAULT_ACTION_FLAVOR) instanceof Action) {
                IExecute ex = dropAction(dtde, dest);
                if (ex != null) createPopup(ex, location.x, location.y);
            } else if (tr.isDataFlavorSupported(TransferableGTD.DEFAULT_ACTION_LIST_FLAVOR) && tr.getTransferData(TransferableGTD.DEFAULT_ACTION_LIST_FLAVOR) instanceof List) {
                IExecute ex = dropActionList(dtde, dest);
                if (ex != null) createPopup(ex, location.x, location.y);
            } else {
                logger.error("Rejected drop!!!");
                dtde.rejectDrop();
            }
        } catch (IOException io) {
            io.printStackTrace();
            dtde.rejectDrop();
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
            dtde.rejectDrop();
        }
    }

    private IExecute dropAction(DropTargetDropEvent dtde, Action dest) throws UnsupportedFlavorException, IOException {
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        Transferable tr = dtde.getTransferable();
        Action orig = (Action) tr.getTransferData(TransferableGTD.DEFAULT_ACTION_FLAVOR);
        if (orig == null) {
            Messages.error("Error en la colocación", "El origen no parece ser una tarea/accion");
            dtde.rejectDrop();
            return null;
        }
        IExecute ex = new IExecute() {

            public void execute(boolean up) {
                DBController db = DBController.getDefaultController();
                int po = orig.getPosition();
                int pd = dest.getPosition();
                db.incrementActionsFromPosition(orig.getProject(), po, -1);
                db.incrementActionsFromPosition(orig.getProject(), (up) ? pd - 1 : pd);
                orig.setPosition((up) ? pd : pd + 1);
                db.updateAction(orig);
                dtde.dropComplete(true);
                controller.structureChanged(orig.getProject());
            }
        };
        ex.dtde = dtde;
        ex.orig = orig;
        ex.dest = dest;
        return ex;
    }

    private IExecute dropActionList(DropTargetDropEvent dtde, Action dest) throws UnsupportedFlavorException, IOException {
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        Transferable tr = dtde.getTransferable();
        List<Action> orig = (List<Action>) tr.getTransferData(TransferableGTD.DEFAULT_ACTION_LIST_FLAVOR);
        if (orig == null) {
            Messages.error("Error en la colocación", "El origen no parece ser una tarea/accion");
            dtde.rejectDrop();
            return null;
        }
        if (orig.size() <= 0) {
            Messages.error("Error en la colocación", "El origen está vacío");
            dtde.rejectDrop();
            return null;
        }
        int pd = dest.getPosition();
        IExecute ex = new IExecute() {

            public void execute(boolean up) {
                DBController db = DBController.getDefaultController();
                int pd = dest.getPosition();
                pd = (up) ? pd : pd + 1;
                db.incrementActionsFromPosition(dest.getProject(), pd, origList.size());
                for (Action a : origList) {
                    a.setPosition(pd++);
                    db.updateAction(a);
                }
                dtde.dropComplete(true);
                controller.structureChanged(dest.getProject());
            }
        };
        ex.dtde = dtde;
        ex.origList = orig;
        ex.dest = dest;
        return ex;
    }

    private void createPopup(IExecute exec, int x, int y) {
        final IExecute ex = exec;
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuUp = new JMenuItem("Insertar antes");
        menuUp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ex.execute(true);
            }
        });
        JMenuItem menuDown = new JMenuItem("Insertar después");
        menuDown.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ex.execute(false);
            }
        });
        menu.add(menuUp);
        menu.add(menuDown);
        menu.show(table, x, y);
    }

    public void dragEnter(DragSourceDragEvent dsde) {
        DragSourceContext context = dsde.getDragSourceContext();
        int dropAction = dsde.getDropAction();
        if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
            context.setCursor(DragSource.DefaultCopyDrop);
        } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
            context.setCursor(DragSource.DefaultMoveDrop);
        } else {
            context.setCursor(DragSource.DefaultCopyNoDrop);
        }
    }

    public void dragOver(DragSourceDragEvent dsde) {
    }

    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    public void dragExit(DragSourceEvent dse) {
    }

    public void dragDropEnd(DragSourceDropEvent dsde) {
        if (dsde.getDropSuccess()) {
            int dropAction = dsde.getDropAction();
            if (dropAction == DnDConstants.ACTION_MOVE) {
                System.out.println("MOVE: remove node");
            }
        }
    }
}

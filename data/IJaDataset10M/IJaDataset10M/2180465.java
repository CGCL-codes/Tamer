package com.rapidminer.gui.dnd;

import java.awt.datatransfer.Transferable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.gui.operatortree.actions.CutCopyPasteAction;
import com.rapidminer.operator.Operator;
import com.rapidminer.tools.LogService;

/** Transfer handler that supports dragging operators.
 * 
 * @author Simon Fischer
 *
 */
public abstract class OperatorTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;

    /** Returns a list of operators selected for dragging out of this component. */
    protected abstract List<Operator> getDraggedOperators();

    @Override
    public Icon getVisualRepresentation(Transferable transferable) {
        if (transferable instanceof TransferableOperator) {
            Operator op;
            try {
                op = (Operator) transferable.getTransferData(transferable.getTransferDataFlavors()[0]);
                return op.getOperatorDescription().getIcon();
            } catch (Exception e) {
                LogService.getRoot().log(Level.WARNING, "Error while dragging: " + e, e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    public Transferable createTransferable(JComponent c) {
        List<Operator> operators = getDraggedOperators();
        if ((operators == null) || operators.isEmpty()) {
            return null;
        }
        Iterator<Operator> i = operators.iterator();
        while (i.hasNext()) {
            Operator op = i.next();
            Operator parent = op.getParent();
            while (parent != null) {
                if (operators.contains(parent)) {
                    i.remove();
                    continue;
                }
                parent = parent.getParent();
            }
        }
        return new TransferableOperator(operators.toArray(new Operator[operators.size()]));
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (data instanceof TransferableOperator) {
            TransferableOperator top = (TransferableOperator) data;
            switch(action) {
                case MOVE:
                    Operator parent = null;
                    for (Operator operator : top.getOperators()) {
                        if (parent == null) {
                            parent = operator.getParent();
                        }
                        operator.removeAndKeepConnections(Arrays.asList(top.getOperators()));
                    }
                    if (parent != null) {
                        RapidMinerGUI.getMainFrame().selectOperator(parent);
                    }
                    break;
                default:
            }
        }
        super.exportDone(source, data, action);
    }

    public static void installMenuItems(JPopupMenu editmenu) {
        editmenu.add(CutCopyPasteAction.CUT_ACTION);
        editmenu.add(CutCopyPasteAction.COPY_ACTION);
        editmenu.add(CutCopyPasteAction.PASTE_ACTION);
    }

    public static void installMenuItems(JMenu editmenu) {
        editmenu.add(CutCopyPasteAction.CUT_ACTION);
        editmenu.add(CutCopyPasteAction.COPY_ACTION);
        editmenu.add(CutCopyPasteAction.PASTE_ACTION);
    }

    public static void addToActionMap(JComponent component) {
        ActionMap actionMap = component.getActionMap();
        actionMap.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
        actionMap.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
        actionMap.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());
        InputMap inputMap = component.getInputMap();
        inputMap.put(KeyStroke.getKeyStroke("ctrl X"), TransferHandler.getCutAction().getValue(Action.NAME));
        inputMap.put(KeyStroke.getKeyStroke("ctrl C"), TransferHandler.getCopyAction().getValue(Action.NAME));
        inputMap.put(KeyStroke.getKeyStroke("ctrl V"), TransferHandler.getPasteAction().getValue(Action.NAME));
    }
}

package org.fudaa.fudaa.lido.editor;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JComponent;
import com.memoire.bu.BuCommonInterface;
import org.fudaa.ebli.dialog.BDialogContent;
import org.fudaa.fudaa.commun.projet.FudaaParamEvent;
import org.fudaa.fudaa.commun.projet.FudaaParamEventProxy;
import org.fudaa.fudaa.commun.projet.FudaaParamListener;
import org.fudaa.fudaa.lido.LidoApplication;
import org.fudaa.fudaa.lido.LidoResource;
import org.fudaa.fudaa.lido.dnd.LidoDnDTransferParam;

/**
 * @version      $Revision: 1.11 $ $Date: 2006-09-19 15:05:00 $ by $Author: deniger $
 * @author       Axel von Arnim
 */
public abstract class LidoCustomizer extends BDialogContent implements FudaaParamListener, DropTargetListener {

    protected FudaaParamEvent LIDO_MODIFY_EVENT;

    private boolean objectModified_;

    private boolean dndAcceptFlavor_;

    private DataFlavor dndFlavor_;

    private Point dndDropLocation_;

    private Component dndDropComponent_;

    private LidoDnDTransferParam dndDropParam_;

    protected LidoCustomizer(final String _title) {
        super((BuCommonInterface) LidoApplication.FRAME, _title);
    }

    protected LidoCustomizer(final String _title, final JComponent _message) {
        super((BuCommonInterface) LidoApplication.FRAME, _title, _message);
    }

    protected LidoCustomizer(final BDialogContent _parent, final String _title) {
        super((BuCommonInterface) LidoApplication.FRAME, _parent, _title);
    }

    protected LidoCustomizer(final BuCommonInterface app, final BDialogContent _parent, final String _title) {
        super(app, _parent, _title);
    }

    protected LidoCustomizer(final BDialogContent _parent, final String _title, final JComponent _message) {
        super((BuCommonInterface) LidoApplication.FRAME, _parent, _title, _message);
    }

    protected void baseInit() {
        super.baseInit();
        setClosable(false);
        LIDO_MODIFY_EVENT = new FudaaParamEvent(this, 0, LidoResource.PRO, null, "object ??");
        objectModified_ = false;
        setModal(false);
        FudaaParamEventProxy.FUDAA_PARAM.addFudaaParamListener(this);
    }

    public abstract boolean restore();

    public abstract void setObject(Object _n);

    public void fermer() {
        super.fermer();
        firePropertyChange("fermer", null, null);
        objectModified_ = false;
    }

    public void show() {
        setValeurs();
        super.show();
    }

    protected void objectModified() {
        if (objectModified_) {
            return;
        }
        FudaaParamEventProxy.FUDAA_PARAM.fireParamStructModified(LIDO_MODIFY_EVENT);
        objectModified_ = true;
    }

    protected void objectDeleted() {
        FudaaParamEventProxy.FUDAA_PARAM.fireParamStructDeleted(LIDO_MODIFY_EVENT);
    }

    protected void objectCreated() {
        FudaaParamEventProxy.FUDAA_PARAM.fireParamStructCreated(LIDO_MODIFY_EVENT);
    }

    public boolean isObjectModified() {
        return objectModified_;
    }

    protected void setObjectModified(final boolean b) {
        objectModified_ = b;
    }

    public void paramStructCreated(final FudaaParamEvent e) {
        if (e.getSource() == this) {
            return;
        }
    }

    public void paramStructDeleted(final FudaaParamEvent e) {
        if (e.getSource() == this) {
            return;
        }
        final Object struct = e.getStruct();
        if (struct == null) {
            return;
        }
        if (isObjectModificationImportant(struct)) {
            final String clazz = getClass().getName();
            final String structclazz = struct.getClass().getName();
            System.err.println(clazz.substring(clazz.lastIndexOf('.') + 1) + ": " + structclazz.substring(structclazz.lastIndexOf('.') + 1) + " deleted");
            fermer();
        }
    }

    public void paramStructModified(final FudaaParamEvent e) {
        if (e.getSource() == this) {
            return;
        }
        final Object struct = e.getStruct();
        if (struct == null) {
            return;
        }
        if (isObjectModificationImportant(struct)) {
            final String clazz = getClass().getName();
            final String structclazz = struct.getClass().getName();
            System.err.println(clazz.substring(clazz.lastIndexOf('.') + 1) + ": " + structclazz.substring(structclazz.lastIndexOf('.') + 1) + " modified");
            setValeurs();
            getValeurs();
        }
    }

    protected abstract boolean isObjectModificationImportant(Object o);

    protected abstract void setValeurs();

    protected abstract boolean getValeurs();

    protected void dndInitDropTarget(final Component c) {
        new DropTarget(c, DnDConstants.ACTION_COPY_OR_MOVE, this);
        dndAcceptFlavor_ = false;
        dndFlavor_ = new DataFlavor(dndGetSourceParamClass(), DataFlavor.javaJVMLocalObjectMimeType);
        dndDropParam_ = null;
    }

    protected Class dndGetSourceParamClass() {
        return null;
    }

    protected boolean dndIsDropAccepted(final Point p) {
        return false;
    }

    protected void dndDropSucceeded() {
    }

    public void dragEnter(final DropTargetDragEvent dtde) {
        dndAcceptFlavor_ = dtde.isDataFlavorSupported(dndFlavor_);
        dtde.rejectDrag();
    }

    public void dragExit(final DropTargetEvent dte) {
        dndAcceptFlavor_ = false;
    }

    public void dragOver(final DropTargetDragEvent dtde) {
        if (dndAcceptFlavor_ && dndIsDropAccepted(dtde.getLocation())) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        } else {
            dtde.rejectDrag();
        }
    }

    protected Point dndGetDropLocation() {
        return dndDropLocation_;
    }

    protected Component dndGetDropComponent() {
        return dndDropComponent_;
    }

    protected LidoDnDTransferParam dndGetDropObject() {
        return dndDropParam_;
    }

    public void drop(final DropTargetDropEvent dtde) {
        dndDropLocation_ = dtde.getLocation();
        dndDropComponent_ = dtde.getDropTargetContext().getComponent();
        final Transferable t = dtde.getTransferable();
        if ((!t.isDataFlavorSupported(dndFlavor_)) || !dndIsDropAccepted(dndDropLocation_)) {
            System.err.println("DND: drop rejected");
            dtde.rejectDrop();
        } else {
            try {
                System.err.println("DND: drop accepted");
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                dndDropParam_ = (LidoDnDTransferParam) t.getTransferData(dndFlavor_);
                dndDropSucceeded();
                dtde.getDropTargetContext().dropComplete(true);
            } catch (final java.io.IOException ioe) {
                System.err.println(ioe);
                dtde.rejectDrop();
            } catch (final UnsupportedFlavorException ufe) {
                System.err.println(ufe);
                dtde.rejectDrop();
            }
        }
    }

    public void dropActionChanged(final DropTargetDragEvent dtde) {
    }
}

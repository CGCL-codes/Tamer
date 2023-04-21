package org.xaware.ide.xadev.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.xaware.ide.xadev.datamodel.ListElementWrapper;
import org.xaware.ide.xadev.datamodel.LocalObjectDataFlavor;
import org.xaware.ide.xadev.datamodel.MapTreeNode;
import org.xaware.ide.xadev.datamodel.StoredProcedureParameter;
import org.xaware.ide.xadev.datamodel.Transferable;
import org.xaware.ide.xadev.datamodel.TransferableWrapper;
import org.xaware.ide.xadev.datamodel.XMLTreeNode;
import org.xaware.ide.xadev.gui.customcontrol.CList;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Contains utility methods for DNDList.
 *
 * @author Srinivas Ch
 * @version 1.0
*/
public class DNDListHandler extends DNDHandler implements DragSourceListener, DropTargetListener {

    /** Logger for DNDListHandler */
    public static XAwareLogger logger = XAwareLogger.getXAwareLogger(DNDListHandler.class.getName());

    /** XAware Namespace instance */
    public static final Namespace ns = XAwareConstants.xaNamespace;

    /** List instance. */
    public CList list;

    /** List viewer instance. */
    public TableViewer listViewer;

    /** holds original values for undo processing */
    public HashMap objectValueMap;

    /** inputparameters vector */
    public Vector inputParams;

    /** represents whether drag is enabled */
    public boolean dropEnabled = true;

    /** represents whether drop is enabled */
    public boolean dragEnabled = true;

    /** To allow popup menu or not */
    public boolean allowDatapathPopup;

    /** Allow Functoid or not */
    public boolean allowFunctoid;

    /** data flavor */
    protected ByteArrayTransfer myFlavor;

    /** holds the transfer data */
    protected Object transferData;

    /** holds drop target */
    public Object toDropOn;

    /** holds last drop target for undo operation */
    public Object lastDroppedOn;

    /** reference to hold the Image and Label provider instance*/
    private DNDListImageAndLabelProvider listImageAndLabelProvider;

    /**
	 * Creates a new DNDListHandler object.
	 *
	 * @param parent composite on which list viewer to be placed
	 * @param inFlavor Datapath popup boolean
	 * @param inVec elements to be populated in the list viewer
	 */
    public DNDListHandler(final Composite parent, final ByteArrayTransfer inFlavor, final Vector inVec) {
        this(parent, inFlavor, inVec, false, false);
    }

    /**
	 * Creates a new DNDListHandler object.
	 *
	 * @param parent Object which shows this list control
	 * @param inFlavor data flavor
	 * @param inVec values which should be shown in the list viewer
	 * @param inAllowDatapathPopup boolean allow data path popup
	 * @param functoid boolean allow functioid popup
	 */
    public DNDListHandler(final Composite parent, final ByteArrayTransfer inFlavor, final Vector inVec, final boolean inAllowDatapathPopup, final boolean functoid) {
        myFlavor = inFlavor;
        allowDatapathPopup = inAllowDatapathPopup;
        allowFunctoid = functoid;
        list = new CList(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        list.setLayoutData(new GridData(GridData.FILL_BOTH));
        listViewer = list.getListViewer();
        listViewer.setContentProvider(new DNDListContentProvider());
        listImageAndLabelProvider = new DNDListImageAndLabelProvider(inVec);
        listViewer.setLabelProvider(listImageAndLabelProvider);
        listViewer.setInput(inVec);
        final DragSource dragSource = new DragSource(listViewer.getTable(), DND.DROP_COPY);
        dragSource.setTransfer(new Transfer[] { LocalObjectDataFlavor.getLocalObjectDataFlavorInstance() });
        dragSource.addDragListener(this);
        dragEnabled = true;
        final DropTarget dropTarget = new DropTarget(listViewer.getTable(), DND.DROP_COPY);
        dropTarget.setTransfer(new Transfer[] { LocalObjectDataFlavor.getLocalObjectDataFlavorInstance() });
        dropTarget.addDropListener(this);
        dropEnabled = true;
        Object selListItem;
        String mappedData = "";
        objectValueMap = new HashMap();
        for (int i = 0; i < inVec.size(); i++) {
            selListItem = inVec.get(i);
            if (selListItem instanceof Element) {
                mappedData = ((Element) selListItem).getText().trim();
            } else if (selListItem instanceof StoredProcedureParameter) {
                mappedData = ((StoredProcedureParameter) selListItem).getMappedValue().trim();
            }
            objectValueMap.put(selListItem, mappedData);
        }
        addMouseListenerHandler(this, getListViewer().getTable());
        init();
    }

    /**
	 * initializes objects and popups
	 */
    private void init() {
        toDropOn = null;
        transferData = null;
        if (allowFunctoid) {
            final GenericTreePopupController popupFunctoid = new GenericTreePopupController(GenericTreePopupController.FUNCTOID_MENU);
            popupFunctoid.setList(this);
        } else {
            new GenericTreePopupController(GenericTreePopupController.GENERIC_MENU).setList(this);
        }
    }

    /**
	 * eneble/disable drag
	 *
	 * @param dragEnabled dragEnabled
	 */
    public void setDragEnabled(final boolean dragEnabled) {
        this.dragEnabled = dragEnabled;
    }

    /**
	 * eneble/disable drop
	 *
	 * @param inDropEnabled dropEnabled
	 */
    public void setDropEnabled(final boolean inDropEnabled) {
        dropEnabled = inDropEnabled;
    }

    /**
	 * performs undo
	 */
    public void undo() {
        logger.info("lastDroppedOn is a : " + lastDroppedOn.getClass().toString() + ", lastDroppedOn = " + lastDroppedOn);
        if (lastDroppedOn instanceof StoredProcedureParameter) {
            final int index = getModel().indexOf(lastDroppedOn);
            getModel().removeElementAt(index);
            ((StoredProcedureParameter) lastDroppedOn).setMappedValue((String) objectValueMap.get(lastDroppedOn));
            getModel().insertElementAt(lastDroppedOn, index);
        } else if (lastDroppedOn instanceof Element) {
            final int indx = getModel().indexOf(lastDroppedOn);
            getModel().removeElementAt(indx);
            ((Element) lastDroppedOn).setText((String) objectValueMap.get(lastDroppedOn));
            getModel().insertElementAt(lastDroppedOn, indx);
        }
    }

    /**
	 * Returns InputParamenters
	 *
	 * @return reference to Vector
	 */
    public Vector getInputParams() {
        return inputParams;
    }

    /**
	 * Sets value for Input Parameters
	 *
	 * @param params reference to Vector
	 */
    public void setInputParams(final Vector params) {
        inputParams = params;
    }

    /**
	 * Validates for InputParameter
	 *
	 * @param param reference to String
	 *
	 * @return boolean Variable
	 */
    public boolean isInputParam(final String param) {
        if (inputParams == null) {
            return false;
        }
        final Iterator itr = inputParams.iterator();
        while (itr.hasNext()) {
            final String ipParam = (String) itr.next();
            if (ipParam.equals(param)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Sets old Mapped Value
	 *
	 * @param toDropOn reference to Object
	 * @param value reference to String
	 */
    public void setOldMappedValue(final Object toDropOn, final String value) {
        objectValueMap.put(toDropOn, value);
    }

    /**
	 * Sets Last Dropped Data Object
	 *
	 * @param item reference to Object
	 */
    public void setLastDroppedOn(final Object item) {
        lastDroppedOn = item;
    }

    /**
	 * Interface method. Invokes when the user has begun the actions required
	 * to drag the widget.
	 *
	 * @param event the information associated with the drag set data event
	 */
    public void dragStart(final DragSourceEvent event) {
    }

    /**
	 * Interface method.Sets Drag data to Event Object.
	 *
	 * @param event the information associated with the drag set data event
	 */
    public void dragSetData(final DragSourceEvent event) {
        if (!dragEnabled) {
            return;
        }
        try {
            final Object dragNode = getSelectedValue();
            if (dragNode != null) {
                Transferable transferable = null;
                if (dragNode instanceof XMLTreeNode) {
                    transferable = ((XMLTreeNode) dragNode).getJDOMContent();
                } else {
                    transferable = new TransferableWrapper(dragNode);
                }
                event.data = transferable;
            }
        } catch (final Exception ex) {
            logger.info("Exception caught starting drag : " + ex);
            logger.printStackTrace(ex);
        }
    }

    /**
	 * Interface method. The drop has successfully completed(mouse up over a
	 * valid target) or has been terminated (such as hitting the ESC key).
	 *
	 * @param event the information associated with the drag finished event
	 */
    public void dragFinished(final DragSourceEvent event) {
    }

    /**
	 * Interface Method. Invokes when the cursor has entered the drop target
	 * boundaries.
	 *
	 * @param event the information associated with the drag enter event
	 */
    public void dragEnter(final DropTargetEvent event) {
        event.detail = DND.DROP_COPY;
    }

    /**
	 * Interface Method. Invokes when the cursor has left the drop target
	 * boundaries.
	 *
	 * @param event the information associated with the drag leave event
	 */
    public void dragLeave(final DropTargetEvent event) {
    }

    /**
	 * Interface Method. Invokes when the operation being performed has changed
	 * (usually due to the user changing the selected key while dragging).
	 *
	 * @param event the information associated with the drag operation changed
	 *        event
	 */
    public void dragOperationChanged(final DropTargetEvent event) {
    }

    /**
	 * Interface Method. Invokes when the cursor is moving over the drop target
	 *
	 * @param event the information associated with the drag over event
	 */
    public void dragOver(final DropTargetEvent event) {
        if (!dropEnabled) {
            event.detail = DND.DROP_NONE;
            event.feedback = DND.FEEDBACK_NONE;
            return;
        }
        event.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND | DND.FEEDBACK_SELECT;
    }

    /**
	 * Interface Method. Invokes when the data is being dropped.
	 *
	 * @param e the information associated with the drop event
	 */
    public void drop(final DropTargetEvent e) {
        if (!dropEnabled) {
            e.detail = DND.DROP_NONE;
            return;
        }
        try {
            final Transferable transObj = (Transferable) e.data;
            if (transObj == null) {
                e.detail = DND.DROP_NONE;
                return;
            }
            if (transObj.isDataFlavorSupported(myFlavor)) {
                logger.info(" using flavor : " + myFlavor);
                transferData = transObj.getTransferData(myFlavor);
            } else {
                logger.info("using string flavor...");
                transferData = transObj.getTransferData(TextTransfer.getInstance());
            }
            logger.finest("transferData is a : " + transferData.getClass() + ", = " + transferData);
            final int index = locationToIndex(list.toControl(e.x, e.y));
            if (index == -1) {
                e.detail = DND.DROP_NONE;
                return;
            }
            toDropOn = ((Vector) listViewer.getInput()).elementAt(index);
            lastDroppedOn = toDropOn;
            logger.info("toDropOn is a : " + toDropOn.getClass() + ", = " + toDropOn);
            if (toDropOn instanceof ListElementWrapper && transferData instanceof String) {
                getModel().removeElementAt(index);
                objectValueMap.put(toDropOn, ((ListElementWrapper) toDropOn).getXML());
                ((ListElementWrapper) toDropOn).setXML("%" + transferData + "%", false);
                getModel().insertElementAt(toDropOn, index);
                listViewer.refresh();
            } else if (toDropOn instanceof StoredProcedureParameter && transferData instanceof String) {
                getModel().removeElementAt(index);
                objectValueMap.put(toDropOn, ((StoredProcedureParameter) toDropOn).getMappedValue());
                ((StoredProcedureParameter) toDropOn).setMappedValue("%" + transferData + "%");
                getModel().insertElementAt(toDropOn, index);
                listViewer.refresh();
            } else if (toDropOn instanceof StoredProcedureParameter && transferData instanceof MapTreeNode) {
                getModel().removeElementAt(index);
                objectValueMap.put(toDropOn, ((StoredProcedureParameter) toDropOn).getMappedValue());
                ((StoredProcedureParameter) toDropOn).setMappedValue("%" + ((MapTreeNode) transferData).getXAPathString(false, false, true, false) + "%");
                getModel().insertElementAt(toDropOn, index);
                listViewer.refresh();
            } else if (transferData instanceof String) {
                getModel().removeElementAt(index);
                objectValueMap.put(toDropOn, ((Element) toDropOn).getTextTrim());
                ((Element) toDropOn).setText("%" + transferData.toString() + "%");
                getModel().insertElementAt(toDropOn, index);
                listViewer.getTable().setSelection(index);
                listViewer.refresh();
            } else if (((MapTreeNode) transferData).myContent instanceof Attribute) {
                getModel().removeElementAt(index);
                objectValueMap.put(toDropOn, ((Element) toDropOn).getTextTrim());
                ((Element) toDropOn).setText("%" + ((MapTreeNode) transferData).getXAPathString(false, false, true, false) + "%");
                ((Element) toDropOn).removeAttribute(XAwareConstants.BIZCOMPONENT_ATTR_COPY, ns);
                getModel().insertElementAt(toDropOn, index);
                listViewer.getTable().setSelection(index);
                listViewer.refresh();
            } else if (transferData instanceof MapTreeNode && ((MapTreeNode) transferData).myContent instanceof Element) {
                getModel().removeElementAt(index);
                objectValueMap.put(toDropOn, ((Element) toDropOn).getTextTrim());
                ((Element) toDropOn).setText("%" + ((MapTreeNode) transferData).getXAPathString(false, false, true, false) + "%");
                getModel().insertElementAt(toDropOn, index);
                listViewer.getTable().setSelection(index);
                listViewer.refresh();
            }
        } catch (final ClassCastException cce) {
            logger.info("Class cast exception ");
            logger.printStackTrace(cce);
            return;
        } catch (final Exception ex) {
            logger.info("Throwable caught : ");
            logger.printStackTrace(ex);
            return;
        }
    }

    /**
	 * Interface Method. Invokes when the drop target is given the chance to
	 * change the nature of the drop.
	 *
	 * @param event the information associated with the drop accept event
	 */
    public void dropAccept(final DropTargetEvent event) {
    }

    /**
	 * Returns List Index for specified location
	 *
	 * @param loc reference to Point
	 *
	 * @return List Index reference to Integer Variable
	 */
    public int locationToIndex(final Point loc) {
        int index;
        if ((list.getItemCount() * list.getItemHeight()) > loc.y) {
            index = (loc.y / list.getItemHeight());
        } else {
            index = -1;
        }
        return index;
    }

    /**
	 * Returns the TableViewer instance.
	 *
	 * @return Returns the table viewer instance.
	 */
    public TableViewer getListViewer() {
        return listViewer;
    }

    /**
	 * returns the model of the tree viewer
	 *
	 * @return returns the model of the tree viewer
	 */
    public Vector getModel() {
        return (Vector) this.listViewer.getInput();
    }

    /**
	 * returns first element in the selected objects in the list viewer
	 *
	 * @return selected object
	 */
    public Object getSelectedValue() {
        final IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
        return selection.getFirstElement();
    }

    @Override
    public Control getControl() {
        return getListViewer().getTable();
    }

    @Override
    public StructuredViewer getViewer() {
        return getListViewer();
    }

    @Override
    protected DNDFontProvider getImageLabelFontProvider() {
        return listImageAndLabelProvider;
    }
}

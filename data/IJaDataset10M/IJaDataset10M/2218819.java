package org.wings.sdnd;

import org.wings.*;
import org.wings.util.WeakArrayList;
import org.wings.plaf.SDragAndDropManagerCG;
import org.wings.session.SCursor;
import org.wings.event.SMouseEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Drag and Drop-Manager to provide a interface between client-side drag-and-drop support (in javascript) and the server-
 * side swing-like drag-and-drop
 */
public class SDragAndDropManager extends SComponent implements LowLevelEventListener {

    private Collection<SComponent> dragSources;

    private Collection<SComponent> dropTargets;

    private static final transient Log LOG = LogFactory.getLog(SDragAndDropManager.class);

    private STransferHandler.TransferSupport transferSupport;

    private SDragAndDropManagerCG dndCG;

    private Map<SComponent, String> dragCodes;

    private Map<SComponent, String> dropCodes;

    private static final SResourceIcon DEFAULT_DND_ICON_STOP = new SResourceIcon("org/wings/icons/dnd/dnd_stop.png");

    private static final SResourceIcon DEFAULT_DND_ICON_MOVE = new SResourceIcon("org/wings/icons/dnd/dnd_move.png");

    private static final SResourceIcon DEFAULT_DND_ICON_COPY = new SResourceIcon("org/wings/icons/dnd/dnd_copy.png");

    private static final SResourceIcon DEFAULT_DND_ICON_LINK = new SResourceIcon("org/wings/icons/dnd/dnd_link.png");

    private SIcon DND_ICON_STOP = DEFAULT_DND_ICON_STOP;

    private SIcon DND_ICON_MOVE = DEFAULT_DND_ICON_MOVE;

    private SIcon DND_ICON_COPY = DEFAULT_DND_ICON_COPY;

    private SIcon DND_ICON_LINK = DEFAULT_DND_ICON_LINK;

    private String valuesToProcess;

    public SDragAndDropManager() {
        this.dragSources = new WeakArrayList<SComponent>();
        this.dropTargets = new WeakArrayList<SComponent>();
        this.dndCG = (SDragAndDropManagerCG) getCG();
        getSession().getDispatcher().register(this);
    }

    /**
     * Sets a Drag-and-Drop Iconset to be used for drag-and-drop in this session
     *
     * @param iconSet
     */
    public void setDNDIcons(DNDIconSet iconSet) {
        DND_ICON_STOP = iconSet.getNoActionAllowedIcon();
        DND_ICON_MOVE = iconSet.getMoveIcon();
        DND_ICON_COPY = iconSet.getCopyIcon();
        DND_ICON_LINK = iconSet.getLinkIcon();
    }

    /**
     * Adds component as a Drag Source
     *
     * @param component
     */
    public void addDragSource(SComponent component) {
        if (!this.dragSources.contains(component)) {
            this.dragSources.add(component);
            update(dndCG.getRegistrationUpdate(this, new DragAndDropRegistrationEvent(DDEventType.ADD_DRAGSOURCE, component)));
        }
    }

    /**
     * Adds a Drag Source with custom dragcode
     *
     * @param component
     * @param dragCode
     */
    public void addDragSource(SComponent component, String dragCode) {
        if (this.dragCodes == null) {
            this.dragCodes = new WeakHashMap<SComponent, String>();
        }
        this.dragCodes.put(component, dragCode);
        addDragSource(component);
    }

    /**
     * Adds component as a Drop Target
     *
     * @param component
     */
    public void addDropTarget(SComponent component) {
        if (!this.dropTargets.contains(component)) {
            this.dropTargets.add(component);
            update(dndCG.getRegistrationUpdate(this, new DragAndDropRegistrationEvent(DDEventType.ADD_DROPTARGET, component)));
        }
    }

    /**
     * Adds a Drop Target with custom dropcode
     *
     * @param component
     * @param dropCode
     */
    public void addDropTarget(SComponent component, String dropCode) {
        if (this.dropCodes == null) {
            this.dropCodes = new WeakHashMap<SComponent, String>();
        }
        this.dropCodes.put(component, dropCode);
        addDropTarget(component);
    }

    /**
     * Removes a drag source from the list
     *
     * @param component
     */
    public void removeDragSource(SComponent component) {
        if (this.dragCodes != null) {
            this.dragCodes.remove(component);
        }
        if (this.dragSources.contains(component)) {
            this.dragSources.remove(component);
            update(dndCG.getRegistrationUpdate(this, new DragAndDropRegistrationEvent(DDEventType.REMOVE_DRAGSOURCE, component)));
        }
    }

    /**
     * Removes a drop target from the list
     *
     * @param component
     */
    public void removeDropTarget(SComponent component) {
        if (this.dropCodes != null) {
            this.dropCodes.remove(component);
        }
        if (this.dragSources.contains(component)) {
            this.dropTargets.remove(component);
            update(dndCG.getRegistrationUpdate(this, new DragAndDropRegistrationEvent(DDEventType.REMOVE_DROPTARGET, component)));
        }
    }

    /**
     * Returns a Collection of components, that are set as drag sources
     *
     * @return
     */
    public Collection<SComponent> getDragSources() {
        return this.dragSources;
    }

    /**
     * Returns a Collection of components, that are set as drop target
     *
     * @return
     */
    public Collection<SComponent> getDropTargets() {
        return this.dropTargets;
    }

    public boolean isRecursivelyVisible() {
        return true;
    }

    public boolean isVisible() {
        return true;
    }

    public SFrame getParentFrame() {
        return getSession().getRootFrame();
    }

    public void processLowLevelEvent(String name, String[] values) {
        this.valuesToProcess = values[0];
        SForm.addArmedComponent(this);
    }

    private void resetDndStatus() {
        SCursor cursor = getSession().getCursor();
        setTransferSupport(null);
        cursor.setLabel(null);
        cursor.removeIcons("dnd");
        cursor.hideCursorIfPossible();
    }

    private boolean dragStart(SComponent source, STransferHandler sourceTH, SComponent target, int action, SMouseEvent mouseEvent) {
        SCursor cursor = getSession().getCursor();
        setTransferSupport(null);
        if (sourceTH instanceof CustomDragHandler) {
            if (((CustomDragHandler) sourceTH).dragStart(source, target, action, mouseEvent)) {
                return true;
            }
        }
        sourceTH.exportAsDrag(source, mouseEvent, action);
        if (getTransferSupport() == null) {
            return false;
        } else {
            SLabel label = sourceTH.getVisualRepresentationLabel(getTransferSupport().getTransferable());
            if (label != null) {
                cursor.setLabel(label);
            }
            cursor.setShowCursor(true);
        }
        return true;
    }

    private void dragEnter(SComponent source, SComponent target, STransferHandler targetTH, int action, SMouseEvent mouseEvent) {
        SCursor cursor = getSession().getCursor();
        if (source == target && !dropTargets.contains(source)) {
            cursor.removeIcons("dnd");
            cursor.addIcon("dnd", DND_ICON_STOP, SCursor.HIGH_PRIORITY);
            return;
        }
        if (targetTH != null && (targetTH instanceof CustomDragOverHandler)) {
            if (((CustomDragOverHandler) targetTH).dragOverEnter(source, target, action, mouseEvent)) {
                return;
            }
        }
        transferSupport.setIsDrop(target, source, mouseEvent, action);
        if ((transferSupport.getSourceDropActions() & action) == 0) {
            cursor.removeIcons("dnd");
            cursor.addIcon("dnd", DND_ICON_STOP, SCursor.HIGH_PRIORITY);
            return;
        }
        if (targetTH != null && targetTH.canImport(getTransferSupport())) {
            cursor.removeIcons("dnd");
            switch(action) {
                case STransferHandler.MOVE:
                    cursor.addIcon("dnd", DND_ICON_MOVE, SCursor.HIGH_PRIORITY);
                    break;
                case STransferHandler.COPY:
                    cursor.addIcon("dnd", DND_ICON_COPY, SCursor.HIGH_PRIORITY);
                    break;
                case STransferHandler.LINK:
                    cursor.addIcon("dnd", DND_ICON_LINK, SCursor.HIGH_PRIORITY);
                    break;
                default:
                    LOG.error("Unknown action while dragenter: " + action);
            }
        } else {
            cursor.removeIcons("dnd");
            cursor.addIcon("dnd", DND_ICON_STOP, SCursor.HIGH_PRIORITY);
        }
    }

    private void dragLeave(SComponent source, SComponent target, STransferHandler targetTH, int action, SMouseEvent mouseEvent) {
        SCursor cursor = getSession().getCursor();
        if (targetTH instanceof CustomDragOverHandler) {
            if (((CustomDragOverHandler) targetTH).dragOverLeave(source, target, action, mouseEvent)) return;
        }
        cursor.removeIcons("dnd");
        cursor.addIcon("dnd", DND_ICON_STOP, SCursor.HIGH_PRIORITY);
    }

    private void dropStay(SComponent source, SComponent target, STransferHandler targetTH, int action, SMouseEvent mouseEvent) {
        if (targetTH instanceof CustomDropStayHandler) {
            ((CustomDropStayHandler) targetTH).dropStay(source, target, action, mouseEvent);
        }
    }

    private void drop(SComponent source, STransferHandler sourceTH, SComponent target, STransferHandler targetTH, int action, SMouseEvent mouseEvent) {
        if (targetTH instanceof CustomDropHandler) {
            if (((CustomDropHandler) targetTH).drop(source, target, action, mouseEvent)) {
                return;
            }
        }
        transferSupport.setIsDrop(target, source, mouseEvent, action);
        if ((transferSupport.getSourceDropActions() & action) == 0) {
            resetDndStatus();
            return;
        }
        if (targetTH != null && targetTH.canImport(transferSupport)) {
            targetTH.importData(transferSupport);
            sourceTH.exportDone(source, transferSupport.getTransferable(), action);
        }
        resetDndStatus();
    }

    private void doFireIntermediateEvents() {
        String[] params = valuesToProcess.split(";");
        String operation = params[0];
        String sourceId = params[1];
        String targetId = params[2];
        int action = Integer.parseInt(params[3]);
        String additionalParams = null;
        if (params.length > 4) additionalParams = params[4];
        SComponent source = getSession().getComponentByName(sourceId);
        if (source == null) {
            return;
        }
        STransferHandler sourceTH = source.getTransferHandler();
        if (sourceTH == null) {
            return;
        }
        SComponent target = getSession().getComponentByName(targetId);
        STransferHandler targetTH = null;
        if (target == null) {
            if (!("ds".equals(operation) || "ab".equals(operation))) {
                LOG.fatal("target not found: " + targetId + " " + valuesToProcess);
                return;
            }
        } else {
            targetTH = target.getTransferHandler();
        }
        SMouseEvent mouseEvent = new SMouseEvent(source, 0, new SPoint(additionalParams));
        if ("ds".equals(operation)) {
            if (dragStart(source, sourceTH, target, action, mouseEvent)) {
                target = source;
                targetTH = sourceTH;
                dragEnter(source, target, targetTH, action, mouseEvent);
            }
            return;
        }
        if ("de".equals(operation)) {
            dragEnter(source, target, targetTH, action, mouseEvent);
        } else if ("dl".equals(operation)) {
            dragLeave(source, target, targetTH, action, mouseEvent);
        } else if ("st".equals(operation)) {
            dropStay(source, target, targetTH, action, mouseEvent);
        } else if ("dr".equals(operation)) {
            drop(source, sourceTH, target, targetTH, action, mouseEvent);
        } else if ("ab".equals(operation)) {
            resetDndStatus();
        }
    }

    public void fireIntermediateEvents() {
        try {
            doFireIntermediateEvents();
        } catch (Exception e) {
            LOG.error("Error while processing Drag-and-Drop operation", e);
            resetDndStatus();
        }
    }

    /**
     * Returns the custom dragcode for component or null if there is none
     *
     * @param component
     * @return
     */
    public String getCustomDragCode(SComponent component) {
        return (this.dragCodes != null) ? this.dragCodes.get(component) : null;
    }

    /**
     * Returns the custom dropcode for component or null if there is none
     *
     * @param component
     * @return
     */
    public String getCustomDropCode(SComponent component) {
        return (this.dropCodes != null) ? this.dropCodes.get(component) : null;
    }

    /**
     * Returns the TransferSupport for the current transfer in this session
     *
     * @return
     */
    public STransferHandler.TransferSupport getTransferSupport() {
        return this.transferSupport;
    }

    /**
     * Sets the TransferSupport for this session
     *
     * @param transferSupport
     */
    public void setTransferSupport(STransferHandler.TransferSupport transferSupport) {
        this.transferSupport = transferSupport;
    }

    public boolean isEpochCheckEnabled() {
        return false;
    }

    public enum DDEventType {

        ADD_DRAGSOURCE, REMOVE_DRAGSOURCE, ADD_DROPTARGET, REMOVE_DROPTARGET
    }

    /**
     * Represents a Drag and Drop Registration (add/remove) event
     */
    public static class DragAndDropRegistrationEvent {

        private DDEventType eventType;

        private SComponent component;

        public DragAndDropRegistrationEvent(DDEventType type, SComponent component) {
            this.eventType = type;
            this.component = component;
        }

        public DDEventType getEventType() {
            return eventType;
        }

        public SComponent getComponent() {
            return component;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DragAndDropRegistrationEvent event = (DragAndDropRegistrationEvent) o;
            if (!component.equals(event.component)) return false;
            return eventType == event.eventType;
        }

        public int hashCode() {
            int result;
            result = eventType.hashCode();
            result = 31 * result + component.hashCode();
            return result;
        }
    }

    public static class DNDIconSet {

        private SIcon noActionAllowedIcon;

        private SIcon moveIcon;

        private SIcon copyIcon;

        private SIcon linkIcon;

        public DNDIconSet(SIcon noActionAllowedIcon, SIcon moveIcon, SIcon copyIcon, SIcon linkIcon) {
            this.noActionAllowedIcon = noActionAllowedIcon;
            this.moveIcon = moveIcon;
            this.copyIcon = copyIcon;
            this.linkIcon = linkIcon;
        }

        public SIcon getNoActionAllowedIcon() {
            return noActionAllowedIcon;
        }

        public void setNoActionAllowedIcon(SIcon noActionAllowedIcon) {
            this.noActionAllowedIcon = noActionAllowedIcon;
        }

        public SIcon getMoveIcon() {
            return moveIcon;
        }

        public void setMoveIcon(SIcon moveIcon) {
            this.moveIcon = moveIcon;
        }

        public SIcon getCopyIcon() {
            return copyIcon;
        }

        public void setCopyIcon(SIcon copyIcon) {
            this.copyIcon = copyIcon;
        }

        public SIcon getLinkIcon() {
            return linkIcon;
        }

        public void setLinkIcon(SIcon linkIcon) {
            this.linkIcon = linkIcon;
        }
    }
}

package com.trollworks.ttk.widgets.outline;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.util.List;

/** An outline which gets its data and selection from another {@link Outline}. */
public class OutlineProxy extends Outline {

    private Outline mOutlineToProxy;

    /**
	 * Creates a new proxy outline.
	 * 
	 * @param outlineToProxy The {@link Outline} to proxy.
	 */
    public OutlineProxy(Outline outlineToProxy) {
        super(outlineToProxy.getModel(), outlineToProxy.showIndent());
        mOutlineToProxy = outlineToProxy;
        mOutlineToProxy.addProxy(this);
    }

    /** @return The outline being proxied. */
    @Override
    public Outline getRealOutline() {
        return mOutlineToProxy;
    }

    @Override
    public boolean canDeleteSelection() {
        return getRealOutline().canDeleteSelection();
    }

    @Override
    public void deleteSelection() {
        getRealOutline().deleteSelection();
    }

    @Override
    protected void addProxy(OutlineProxy proxy) {
        assert false : "You cannot add a proxy to a proxy";
    }

    @Override
    public void convertDragRowsToSelf(List<Row> list) {
        mOutlineToProxy.convertDragRowsToSelf(list);
    }

    @Override
    protected boolean isDragAcceptable(DropTargetDragEvent dtde) {
        return mOutlineToProxy.isDragAcceptable(dtde);
    }

    @Override
    public Color getBackground(int rowIndex, boolean selected, boolean active) {
        return mOutlineToProxy.getBackground(rowIndex, selected, active);
    }

    @Override
    public void contentSizeMayHaveChanged() {
        mOutlineToProxy.contentSizeMayHaveChanged();
    }

    @Override
    public void notifyActionListeners(ActionEvent event) {
        if (mOutlineToProxy != null) {
            mOutlineToProxy.notifyActionListeners(event);
        } else {
            super.notifyActionListeners(event);
        }
    }

    @Override
    public String getActionCommand() {
        if (mOutlineToProxy != null) {
            return mOutlineToProxy.getActionCommand();
        }
        return super.getActionCommand();
    }

    @Override
    public String getSelectionChangedActionCommand() {
        if (mOutlineToProxy != null) {
            return mOutlineToProxy.getSelectionChangedActionCommand();
        }
        return super.getSelectionChangedActionCommand();
    }

    @Override
    public String getPotentialContentSizeChangeActionCommand() {
        if (mOutlineToProxy != null) {
            return mOutlineToProxy.getPotentialContentSizeChangeActionCommand();
        }
        return super.getPotentialContentSizeChangeActionCommand();
    }

    @Override
    public void repaint(Rectangle bounds) {
        if (mOutlineToProxy != null) {
            mOutlineToProxy.repaint(bounds);
        }
    }

    @Override
    void repaintHeader(Rectangle bounds) {
        if (mOutlineToProxy != null) {
            mOutlineToProxy.repaintHeader(bounds);
        }
    }

    @Override
    public void repaintSelection() {
        mOutlineToProxy.repaintSelection();
    }

    @Override
    protected void repaintProxyRow(Row row) {
        mOutlineToProxy.repaintProxyRow(row);
    }

    /**
	 * Repaint the specified area.
	 * 
	 * @param bounds The area to repaint.
	 */
    protected void repaintProxy(Rectangle bounds) {
        super.repaint(bounds);
    }

    @Override
    protected void rowsWereDropped() {
        mOutlineToProxy.rowsWereDropped();
    }

    @Override
    protected int dragEnterRow(DropTargetDragEvent dtde) {
        mOutlineToProxy.dragEnterRow(dtde, this);
        return super.dragEnterRow(dtde);
    }

    @Override
    protected void dragExitRow(DropTargetEvent dte) {
        mOutlineToProxy.dragExitRow(dte, this);
        super.dragExitRow(dte);
    }

    @Override
    protected void dropRow(DropTargetDropEvent dtde) {
        mOutlineToProxy.dropRow(dtde, this);
        super.dropRow(dtde);
    }
}

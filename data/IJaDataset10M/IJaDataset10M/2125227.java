package net.sf.rcpforms.widgetwrapper.gen;

import net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget2;
import java.lang.Object;
import java.lang.String;
import org.eclipse.swt.widgets.Item;
import net.sf.rcpforms.widgetwrapper.entity.ICellEditorType;

/**
 * <h3 style='color: #D22'><span style='font-size: 18pt'>NON-API</span>, not intended to user or subclassed!</h3> 
 * <p>
 * <ul><li><i>This class is generated, do not alter it manually!!!</i></li></ul>
 * <p>
 */
public abstract class RCPViewerColumn<COLUMN_TYPE extends Item> extends RCPWidget2 implements IRCPViewerColumnProperties, IRCPViewerColumn {

    private int cashedAlignment;

    private boolean cashedMoveable;

    private boolean cashedResizable;

    private int cashedWidth;

    private boolean grabHorizontal = true;

    private Object[] comboItems;

    private ICellEditorType cellEditorType;

    private String header;

    private int columnIndex;

    protected RCPViewerColumn(String labelText, int style) {
        super(labelText, style);
    }

    protected RCPViewerColumn(String labelText, int style, Object uid) {
        super(labelText, style, uid);
    }

    public abstract COLUMN_TYPE getSWTColumn();

    public void setAlignment(int alignment) {
        setAlignment_impl(alignment, true, true);
    }

    protected void setAlignment_impl(int alignment, boolean event, boolean update) {
        if (!hasSpawned()) {
            addSpawnHook4Property(PROP_ALIGNMENT, alignment);
            return;
        }
        int oldAlignment = cashedAlignment;
        if (update) setAlignment_ui(alignment);
        if (event) firePropertyChange(PROP_ALIGNMENT, oldAlignment, alignment);
        cashedAlignment = alignment;
    }

    public int getAlignment() {
        if (!hasSpawned()) {
            return (Integer) getCashedHookPropertyValue(PROP_ALIGNMENT, null);
        }
        return getAlignment_ui();
    }

    protected abstract int getAlignment_ui();

    protected abstract void setAlignment_ui(int alignment);

    public void setMoveable(boolean moveable) {
        setMoveable_impl(moveable, true, true);
    }

    protected void setMoveable_impl(boolean moveable, boolean event, boolean update) {
        if (!hasSpawned()) {
            addSpawnHook4Property(PROP_MOVEABLE, moveable);
            return;
        }
        boolean oldMoveable = cashedMoveable;
        if (update) setMoveable_ui(moveable);
        if (event) firePropertyChange(PROP_MOVEABLE, oldMoveable, moveable);
        cashedMoveable = moveable;
    }

    public boolean getMoveable() {
        if (!hasSpawned()) {
            return (Boolean) getCashedHookPropertyValue(PROP_MOVEABLE, null);
        }
        return getMoveable_ui();
    }

    protected abstract boolean getMoveable_ui();

    protected abstract void setMoveable_ui(boolean moveable);

    public void setResizable(boolean resizable) {
        setResizable_impl(resizable, true, true);
    }

    protected void setResizable_impl(boolean resizable, boolean event, boolean update) {
        if (!hasSpawned()) {
            addSpawnHook4Property(PROP_RESIZABLE, resizable);
            return;
        }
        boolean oldResizable = cashedResizable;
        if (update) setResizable_ui(resizable);
        if (event) firePropertyChange(PROP_RESIZABLE, oldResizable, resizable);
        cashedResizable = resizable;
    }

    public boolean getResizable() {
        if (!hasSpawned()) {
            return (Boolean) getCashedHookPropertyValue(PROP_RESIZABLE, null);
        }
        return getResizable_ui();
    }

    protected abstract boolean getResizable_ui();

    protected abstract void setResizable_ui(boolean resizable);

    public void setWidth(int width) {
        setWidth_impl(width, true, true);
    }

    protected void setWidth_impl(int width, boolean event, boolean update) {
        if (!hasSpawned()) {
            addSpawnHook4Property(PROP_WIDTH, width);
            return;
        }
        int oldWidth = cashedWidth;
        if (update) setWidth_ui(width);
        if (event) firePropertyChange(PROP_WIDTH, oldWidth, width);
        cashedWidth = width;
    }

    public int getWidth() {
        if (!hasSpawned()) {
            return (Integer) getCashedHookPropertyValue(PROP_WIDTH, null);
        }
        return getWidth_ui();
    }

    protected abstract int getWidth_ui();

    protected abstract void setWidth_ui(int width);

    /**  @see IRCPColumnConfiguration#setGrabHorizontal */
    @SuppressWarnings("hiding")
    public void setGrabHorizontal(boolean grabHorizontal) {
        this.grabHorizontal = grabHorizontal;
    }

    /**  @see IRCPColumnConfiguration#getGrabHorizontal */
    public boolean getGrabHorizontal() {
        return this.grabHorizontal;
    }

    /**  @see IRCPColumnConfiguration#setComboItems */
    @SuppressWarnings("hiding")
    public void setComboItems(Object[] comboItems) {
        this.comboItems = comboItems;
    }

    /**  @see IRCPColumnConfiguration#getComboItems */
    public Object[] getComboItems() {
        return this.comboItems;
    }

    /**  @see IRCPColumnConfiguration#setCellEditorType */
    @SuppressWarnings("hiding")
    public void setCellEditorType(ICellEditorType cellEditorType) {
        this.cellEditorType = cellEditorType;
    }

    /**  @see IRCPColumnConfiguration#getCellEditorType */
    public ICellEditorType getCellEditorType() {
        return this.cellEditorType;
    }

    /**  @see IRCPColumnConfiguration#setHeader */
    @SuppressWarnings("hiding")
    public void setHeader(String header) {
        this.header = header;
    }

    /**  @see IRCPColumnConfiguration#getHeader */
    public String getHeader() {
        return this.header;
    }

    /**  @see IRCPColumnConfiguration#setColumnIndex */
    @SuppressWarnings("hiding")
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    /**  @see IRCPColumnConfiguration#getColumnIndex */
    public int getColumnIndex() {
        return this.columnIndex;
    }
}

package net.sf.amemailchecker.gui.entrypoint;

public abstract class BaseEntryPoint implements AppEntryPoint {

    protected EntryPointPopupMenu popupMenu;

    protected EntryPointType entryPointType;

    public EntryPointPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void setPopupMenu(EntryPointPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public EntryPointType getType() {
        return entryPointType;
    }

    public void setType(EntryPointType entryPointType) {
        this.entryPointType = entryPointType;
    }
}

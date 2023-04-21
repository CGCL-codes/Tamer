package org.rubypeople.rdt.refactoring.ui.pages.extractmethod;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class MethodArgumentTableItem extends TableItem {

    private final boolean isMoveable;

    private final int originalPosition;

    private String name;

    public MethodArgumentTableItem(Table parent, String name, boolean isMoveable, int originalPosition, int position) {
        super(parent, SWT.NONE, position);
        this.name = name;
        this.isMoveable = isMoveable;
        setText(name);
        this.originalPosition = originalPosition;
    }

    @Override
    protected void checkSubclass() {
    }

    public boolean isMoveable() {
        return isMoveable;
    }

    public String getItemName() {
        return name;
    }

    public void setItemName(String name) {
        this.name = name;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }
}

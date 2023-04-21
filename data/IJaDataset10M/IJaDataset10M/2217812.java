package org.skyfree.ghyll.tcard.action;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;

/**
 * this class is ...
 */
public abstract class AbstractMenuItemAction implements MenuItemAction, SelectionListener {

    String text;

    int Accelerator;

    int style;

    TreeItem item;

    Image image;

    String ErrorMessage;

    TreeViewer treeViewer;

    public AbstractMenuItemAction(int style, String text, int Accelerator, Image image, TreeViewer treeViewer) {
        this.style = style;
        this.text = text;
        this.Accelerator = Accelerator;
        this.image = image;
        this.treeViewer = treeViewer;
    }

    public void createItem(Menu parent, TreeItem item) {
        this.item = item;
        MenuItem menuitem = new MenuItem(parent, this.style);
        menuitem.setEnabled(isEnable());
        menuitem.setText(this.text);
        menuitem.setAccelerator(this.Accelerator);
        menuitem.setImage(this.image);
        menuitem.addSelectionListener(this);
    }

    public final void widgetSelected(SelectionEvent e) {
        try {
            this.execute(e.getSource());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.refresh();
    }

    public final void widgetDefaultSelected(SelectionEvent e) {
    }

    public void refresh() {
        if (this.item.getParentItem() != null) this.treeViewer.refresh(this.item.getParentItem().getData(), true); else this.treeViewer.refresh();
        this.treeViewer.setSelection(this.treeViewer.getSelection());
    }
}

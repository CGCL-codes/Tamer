package org.zkoss.zul;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

/**
 * GroupFooter serves as a summary listitem of listgroup.
 * 
 * <p>Default {@link #getSclass}: listgroupfooter.
 *
 *<p>Note: All the {@link Label} child of this component are automatically applied
 * the group-cell CSS, if you don't want this CSS, you can invoke the {@link Label#setSclass(String)}
 * after the child added.
 * 
 * @author robbiecheng
 * @since 3.5.0
 */
public class Listgroupfooter extends Listitem {

    public Listgroupfooter() {
        setSclass("listgroupfooter");
    }

    public Listgroupfooter(String label) {
        this();
        setLabel(label);
    }

    public Listgroupfooter(String label, Object value) {
        this();
        setLabel(label);
        setValue(value);
    }

    /** Returns the value of the {@link Label} it contains, or null
	 * if no such cell.
	 */
    public String getLabel() {
        final Component cell = (Component) getFirstChild();
        return cell != null && cell instanceof Label ? ((Label) cell).getValue() : null;
    }

    /** Sets the value of the {@link Label} it contains.
	 *
	 * <p>If it is not created, we automatically create it.
	 */
    public void setLabel(String label) {
        autoFirstCell().setLabel(label);
    }

    private Listcell autoFirstCell() {
        Listcell cell = (Listcell) getFirstChild();
        if (cell == null) {
            cell = new Listcell();
            cell.applyProperties();
            cell.setParent(this);
        }
        return cell;
    }

    public void onChildAdded(Component child) {
        final HtmlBasedComponent cmp = (HtmlBasedComponent) child;
        final String clx = cmp.getSclass();
        cmp.setSclass(clx != null && clx.length() > 0 ? clx + " listgroupfooter-cell" : "listgroupfooter-cell");
    }

    public void onChildRemoved(Component child) {
        final HtmlBasedComponent cmp = (HtmlBasedComponent) child;
        final String cls = cmp.getSclass();
        cmp.setSclass(cls != null && cls.indexOf("listgroupfooter-cell") > -1 ? cls.replaceAll("(?:^|\\s+)" + "listgroupfooter-cell" + "(?:\\s+|$)", " ").trim() : cls);
    }
}

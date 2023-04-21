package mapEditor.map;

import mapEditor.MapEditor;
import mapEditor.obj.DrawObject;
import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import org.w3c.dom.svg.SVGElement;

public class ObjectTableModel extends AbstractTableModel {

    static final long serialVersionUID = 0;

    private Vector<DrawObject> attachedObjects = new Vector<DrawObject>();

    private Hashtable<SVGElement, DrawObject> map = new Hashtable<SVGElement, DrawObject>();

    public void add(DrawObject obj) {
        map.put(obj.getSVGElement(), obj);
        attachedObjects.add(obj);
    }

    public void remove(DrawObject obj) {
        map.remove(obj);
        attachedObjects.remove(obj);
    }

    public Enumeration<DrawObject> elements() {
        return (attachedObjects.elements());
    }

    public void edit(int row) {
        attachedObjects.elementAt(row).edit();
    }

    public void select(int row) {
        MapEditor.getCurrentMapCanvas().setSelected(attachedObjects.elementAt(row));
    }

    public int indexOf(DrawObject obj) {
        return (attachedObjects.indexOf(obj));
    }

    public ObjectTableModel() {
    }

    public int getColumnCount() {
        return (DrawObject.getFieldNames().length);
    }

    public String getColumnName(int column) {
        return (DrawObject.getFieldNames()[column]);
    }

    public int getRowCount() {
        return (attachedObjects.size());
    }

    public Object getValueAt(int row, int col) {
        return (attachedObjects.elementAt(row).getField(col));
    }

    public void setValueAt(Object value, int row, int col) {
        attachedObjects.elementAt(row).setField(col, value);
        MapEditor.repaint();
    }

    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return (getColumnClass(col) == String.class);
    }
}

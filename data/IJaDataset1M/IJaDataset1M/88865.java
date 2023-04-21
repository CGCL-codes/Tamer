package org.gwanted.gwt.widget.grid.client.view;

import org.gwanted.gwt.widget.grid.client.view.cells.ControlCell;
import org.gwanted.gwt.widget.grid.client.view.cells.TableCell;
import org.gwanted.gwt.widget.grid.client.view.controls.Control;
import org.gwanted.gwt.widget.grid.client.view.disclosure.AbstractDisclosure;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Miguel A. Rager
 */
public class RowView extends BasePanel {

    private String id;

    private AbstractDisclosure detailRow;

    private int index;

    /**
     * @param element
     */
    public RowView() {
        this(DOM.createTR());
    }

    public RowView(Element elem) {
        super(elem);
    }

    /**
     * @param element
     */
    public RowView(String id) {
        this(DOM.getElementById(id));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addCell(TableCell cell) {
        super.add(cell);
    }

    /**
     * @param view
     */
    public void addCell(TableCell cell, int index) {
        super.add(cell, index);
    }

    public void addControlCell(Control control) {
        addCell(new ControlCell(control, this.index));
    }

    public TableCell getCell(int index) {
        return (TableCell) super.getChild(index);
    }

    public void onAttach() {
        super.onAttach();
        if (detailRow != null) {
            detailRow.show();
        }
    }

    public void onDetach() {
        super.onDetach();
        if (detailRow != null) {
            detailRow.hide();
        }
    }

    /**
     * @param detail
     */
    public void setDetailWidget(AbstractDisclosure detail) {
        this.detailRow = detail;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

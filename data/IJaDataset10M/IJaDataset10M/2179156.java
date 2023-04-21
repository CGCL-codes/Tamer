package uy.gub.imm.sae.web.common;

import java.io.Serializable;

public class Row<T> implements Serializable {

    private static final long serialVersionUID = 3345492499383513579L;

    private T data;

    private RowList<T> rowListRef;

    public Row(T data, RowList<T> rowList) {
        this.data = data;
        this.rowListRef = rowList;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSelected() {
        return (this.equals(rowListRef.getSelectedRow()));
    }

    public void setSelected(Boolean selected) {
        if (selected) {
            rowListRef.setSelectedRow(this);
        } else if (getSelected()) {
            rowListRef.setSelectedRow(null);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        return (o == null ? false : data.equals(((Row<T>) o).getData()));
    }
}

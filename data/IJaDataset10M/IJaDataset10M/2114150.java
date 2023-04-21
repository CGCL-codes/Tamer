package net.sf.pim;

import net.sf.component.table.BindedTableViewer;

/**
 * 当前的tableeditor信息
 * @author levin
 */
public interface ITableEditor {

    public String getName();

    public Class getEntityClass();

    public BindedTableViewer getViewer();
}

package org.itsnat.comp.table;

import org.itsnat.comp.*;

/**
 * Is the interface of the free table components.
 *
 * <p>ItsNat provides a default implementation of this interface.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see org.itsnat.comp.ItsNatComponentManager#createItsNatFreeTable(org.w3c.dom.Element element,ItsNatTableStructure,org.itsnat.core.NameValue[] artifacts)
 */
public interface ItsNatFreeTable extends ItsNatTable, ItsNatFreeComponent {

    /**
     * Returns the table header sub-component.
     *
     * @return the table header. Null if this table does not have a header.
     */
    public ItsNatFreeTableHeader getItsNatFreeTableHeader();
}

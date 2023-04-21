package com.ivis.xprocess.ui.datawrappers;

import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.datawrappers.record.IRecordWrapper;
import com.ivis.xprocess.ui.tables.columns.definition.XProcessColumn;

public interface IWrapper {

    /**
     * @return the IWrappers UI displayable name
     */
    public String getLabel();

    /**
     * @return the wrapped Xelement
     */
    public Xelement getElement();

    /**
     * @param column - the column or null
     * @return true if the IWrapper can be inplace edited for
     * specific column, or if no column is given can it
     * be inplaced edited
     */
    public boolean canInplaceEdit(XProcessColumn column);

    /**
     * @return the wrappers parent Wrapper, or null if the
     * IWrapper is the Root Portfolio (as it is the top most
     * element)
     */
    public IWrapper getParent();

    /**
     * A more specific version of getParent if you want
     * to deal with a IElementWrapper object.
     *
     * @return  the wrappers parent IElementWrapper, or null
     * if the IWrapper is the Root Portfolio (as it is the
     * top most element)
     */
    public IElementWrapper getParentWrapper();

    /**
     * Remove this IRecordWrapper from the IWrapper.
     * @param recordWrapper
     */
    public void remove(IRecordWrapper recordWrapper);
}

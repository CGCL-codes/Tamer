package org.apache.myfaces.trinidadinternal.ui.data.bean;

import org.apache.myfaces.trinidadinternal.ui.data.DataObject;

/**
 * Generic interface supported by Bean data object adapter.
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 * @deprecated This class comes from the old Java 1.2 UIX codebase and should not be used anymore.
 */
@Deprecated
public interface BeanDOAdapter extends DataObject {

    /**
   * Attaches an instance of the bean class to the adapter.
   */
    public void setInstance(Object instance);
}

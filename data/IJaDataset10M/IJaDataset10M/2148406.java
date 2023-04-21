package de.ios.framework.remote.cl;

import java.util.*;
import de.ios.framework.basic.*;

/**
 * Interface for DirectClientDescriptions-Data-Classes, will be only used, if there are problems occuring with the new i18n.
 * <PRE>
 * to be implemented the following way:
 *
 * - define the descriptions as static constants:
 *
 *  (descriptions:)
 *
 *  long[] dClassIds = new long[] {
 *  };
 *  String[] keys = new String[] {
 *  };
 *  String[] descr = new String[] {
 *  };
 *
 *  (descriptionClasses:)
 *
 *  long[] classIds = new long[] {
 *  };
 *  String[] classNames = new String[] {
 *  };
 *
 * - implement setData(...):
 *
 *  public void setData() {
 *    int i;
 *    for ( i=0; i<dClassIds.length; i++ )
 *      DirectClinetDescriptions.descriptions.put( ""+dClassIds[i]+":"+keys[i], descr[i] );
 *    for ( i=0; i<classIds.length; i++ )
 *      DirectClientDescriptions.classes.put( classNames[i], new Long( classIds[i] ) );
 *  }
 *
 * - return the name of your class on the Server's getDirectClientDescriptionsDataClasses()
 *   (WITHOUT INSTANCIATING OR LOADING THE CLASS!!! NOT AVAILABLE AT THE SERVER!!!)
 *
 * </PRE>
 */
public interface DirectClientDescriptionsData {

    /**
   * Method for addind the Data to the DirectClientDescriptions (called automatically by DirectClientDescriptions).
   * Fill your data into the DirectClientDescriptions' Hashtables (see this interface-description).
   */
    public void setData();
}

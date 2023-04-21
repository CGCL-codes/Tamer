package org.vikamine.swing.subgroup.event.listener;

import java.util.EventListener;
import javax.swing.event.ListDataEvent;

public interface BackgroundKnowledgeSubgroupListener extends EventListener {

    /** 
   * Sent when the contents of the list has changed in a way 
   * that's too complex to characterize with the previous 
   * methods. For example, this is sent when an item has been
   * replaced. Index0 and index1 bracket the change.
   *
   * @param e  a <code>ListDataEvent</code> encapsulating the
   *    event information
   */
    void contentsChanged(ListDataEvent e);
}

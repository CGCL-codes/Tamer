package com.croftsoft.core.util.slot;

/***********************************************************************
    * Mail slot for receiving messages to be processed, stored, or relayed. 
    *
    * Please see the online tutorial <a target="_blank"
    * href="http://www.CroftSoft.com/library/tutorials/slot/">
    * Interface Slot</a>.
    * 
    * @version
    *   $Id: Slot.java 98 2011-10-06 19:21:54Z croft $
    * @since
    *   2007-01-28
    * @author
    *   <a href="http://www.CroftSoft.com/">David Wallace Croft</a>
    ***********************************************************************/
public interface Slot<E> {

    public boolean offer(E e);
}

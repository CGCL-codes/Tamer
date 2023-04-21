package org.plazmaforge.studio.core.storage;

/** 
 * @author Oleh Hapon
 * $Id: Storage.java,v 1.4 2010/04/28 06:40:53 ohapon Exp $
 */
public interface Storage extends Container {

    String getDescription();

    boolean supportsHierarchial();

    boolean supportsCreateTopContainer();
}

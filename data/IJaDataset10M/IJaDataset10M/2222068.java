package org.likken.core.command;

import javax.naming.directory.ModificationItem;

/**
 * @author Stephane Boisson
 * @version $Revision: 1.1 $ $Date: 2000/12/07 22:49:37 $
 */
public interface Command {

    public ModificationItem getModificationItem();
}

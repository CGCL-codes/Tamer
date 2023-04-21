package com.organic.maynard.outliner.event;

import com.organic.maynard.outliner.*;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.2 $, $Date: 2002/07/16 21:25:28 $
 */
public interface UndoQueueListener {

    public void undo(UndoQueueEvent e);
}

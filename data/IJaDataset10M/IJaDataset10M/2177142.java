package org.sgmiller.formicidae.editor;

import java.awt.Cursor;
import org.sgmiller.formicidae.World;

/**
 * DOCUMENT ME!
 * 
 * @author scgmille
 * @since 1.3, Sep-6-2004
 */
public abstract class CellSetter extends Tool {

    /**
   * DOCUMENT ME!
   * 
   * @param cursor
   */
    public CellSetter(Cursor cursor) {
        super(cursor);
    }

    /**
   * DOCUMENT ME!
   * 
   * @param world
   * @param cellpos
   */
    public void fire(World world, int cellpos) {
        setCell(world, cellpos);
    }

    /**
   * DOCUMENT ME!
   * 
   * @param world
   * @param cellpos
   */
    public abstract void setCell(World world, int cellpos);
}

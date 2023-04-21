package org.formaria.svg;

import java.util.Vector;

/**
 *
 *
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p> $Revision: 1.2 $</p>
 */
public interface SvgRolloverFinder {

    /**
   * Is the block with the specified name a block that has a rollover?
   * @param blockName the basic block name
   * @return true if this block participates in the rollover process
   */
    public boolean isRolloverName(String blockName);

    /**
   * Get the corresponding rollover name for the named block
   * @param blockName the basic blockname
   * @return the rollover name
   */
    public String getRolloverName(String blockName);

    /**
   * Setup the mapping of the base names to the rollover names. The method is
   * called whenever a svg image is loaded and initialized.
   * @param elements the svg elements
   * @param baseShapes the names of the basic svg elements
   */
    public void setup(Vector elements, String[] baseShapes);
}

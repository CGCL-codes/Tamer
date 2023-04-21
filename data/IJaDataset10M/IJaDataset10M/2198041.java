package net.taylor.portal.entity.menu.editor;

import net.taylor.portal.entity.menu.Menu;
import net.taylor.seam.AbstractFinder;
import net.taylor.seam.PieChart;

/**
 * The local interface for a Finder user interface.
 *
 * @author jgilbert
 * @generated
 */
public interface MenuFinder extends AbstractFinder<Menu> {

    /** @generated */
    PieChart getParentPieChart();
}

package net.sf.myway.edit.ui.views;

import java.util.List;
import net.sf.myway.calibrator.da.entities.Folder;
import net.sf.myway.calibrator.da.entities.ScannedMap;
import net.sf.myway.map.da.entities.Region;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;

/**
 * @author Andreas Beckers
 * @version $Revision: 1.1 $
 */
public class MapsAndRegionStructureAdvisor extends TreeStructureAdvisor {

    /**
	 * @see org.eclipse.jface.databinding.viewers.TreeStructureAdvisor#getParent(java.lang.Object)
	 */
    @Override
    public Object getParent(final Object element) {
        if (element instanceof Region) return ((Region) element).getParent();
        if (element instanceof Folder) return ((Folder) element).getParent();
        if (element instanceof ScannedMap) return ((ScannedMap) element).getFolder();
        return super.getParent(element);
    }

    /**
	 * @see org.eclipse.jface.databinding.viewers.TreeStructureAdvisor#hasChildren(java.lang.Object)
	 */
    @Override
    public Boolean hasChildren(final Object element) {
        if (element instanceof Region) {
            final List<Region> children = ((Region) element).getChildren();
            return children != null && !children.isEmpty();
        }
        if (element instanceof Folder) return ((Folder) element).hasChildren();
        return super.hasChildren(element);
    }
}

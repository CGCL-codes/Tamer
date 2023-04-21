package org.xith3d.render.preprocessing.sorting;

import org.openmali.vecmath2.Point3f;
import org.xith3d.render.preprocessing.RenderAtom;
import org.xith3d.render.preprocessing.RenderBin;
import org.xith3d.render.preprocessing.RenderBin.DynamicAtomArray;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.utility.comparator.Sorter;

/**
 * This sorter sorts RenderAtoms back-to-front.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 */
public class BackToFrontByBoundingSphereAndEyeRayIntersectionRenderBinSorter extends BackToFrontRenderBinSorter {

    private Point3f viewPosition = new Point3f();

    private Point3f tmpPos = new Point3f();

    /**
     * {@inheritDoc}
     */
    @Override
    public void sortRenderBin(RenderBin renderBin, Transform3D viewTransform) {
        viewTransform.getTranslation(viewPosition);
        final DynamicAtomArray atoms = renderBin.getAtoms();
        for (int i = 0; i < atoms.size(); i++) {
            final RenderAtom<?> atom = atoms.get(i);
            atom.getPosition(tmpPos);
            atom.setCompareIndicators(tmpPos.distanceSquared(viewPosition) - atom.getNode().getWorldBounds().getMaxCenterDistanceSquared(), tmpPos.getZ(), null);
        }
        Sorter.quickSort(atoms.getRawArray(), 0, atoms.size() - 1, this);
    }

    public BackToFrontByBoundingSphereAndEyeRayIntersectionRenderBinSorter() {
    }
}

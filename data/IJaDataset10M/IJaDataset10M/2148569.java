package maltcms.datastructures.constraint;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import maltcms.datastructures.alignment.AnchorPairSet;
import cross.Logging;
import cross.datastructures.tuple.Tuple2D;
import cross.tools.EvalTools;

/**
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 */
public class ConstraintFactory {

    private static final ConstraintFactory cf = new ConstraintFactory();

    public static ConstraintFactory getInstance() {
        return ConstraintFactory.cf;
    }

    public Area calculateLayout(final int rows, final int cols, final int neighborhood1, final AnchorPairSet aps, final double band, final int rowoverlap, final int coloverlap) {
        final List<Tuple2D<Integer, Integer>> ris = aps.getCorrespondingScans();
        final Tuple2D<Integer, Integer> tbegin = ris.remove(0);
        int x = tbegin.getFirst();
        int y = tbegin.getSecond();
        EvalTools.eqI(x, 0, ConstraintFactory.class);
        EvalTools.eqI(y, 0, ConstraintFactory.class);
        int cnt = 0;
        final int roverlap = rowoverlap;
        final int coverlap = coloverlap;
        int j0 = 0, i0 = 0, j1 = 0, i1 = 0;
        final int npartitions = ris.size();
        final Area bounds = new Area();
        final ArrayList<Rectangle> partitions = new ArrayList<Rectangle>();
        final ArrayList<Rectangle> distinctRects = new ArrayList<Rectangle>();
        for (final Tuple2D<Integer, Integer> t : ris) {
            j1 = t.getSecond();
            i1 = t.getFirst();
            j0 = x;
            i0 = y;
            if (cnt == npartitions - 1) {
                j1 = cols;
                i1 = rows;
            }
            final Rectangle r = new Rectangle(j0, i0, j1 - j0 + coverlap, i1 - i0 + roverlap);
            if ((band > 0.0d) && (band < 1.0d)) {
                final Area bandArea = createBandConstraint(j0, i0, r.height, r.width, band);
                final Area b = new Area(r);
                b.intersect(bandArea);
                bounds.add(b);
            } else {
                bounds.add(new Area(r));
            }
            final Area dr = new Area(r);
            dr.intersect(bounds);
            distinctRects.add(dr.getBounds());
            x = j1;
            y = i1;
            final int ax = x - neighborhood1;
            final int ay = y - neighborhood1;
            if (cnt < npartitions - 1) {
                final Rectangle anchor = new Rectangle(ax, ay, (neighborhood1 * 2) + 1, (neighborhood1 * 2) + 1);
                bounds.add(new Area(anchor));
                final Area dr2 = new Area(anchor);
                dr2.intersect(bounds);
                distinctRects.add(dr2.getBounds());
                Logging.getLogger(ConstraintFactory.class).debug("Anchor # " + cnt + " at " + x + "," + y);
            }
            x = j1 + 1;
            y = i1 + 1;
            cnt++;
            partitions.add(r);
        }
        for (final Rectangle r : distinctRects) {
            Logging.getLogger(ConstraintFactory.class).debug(r.toString());
        }
        return bounds;
    }

    public Area createBandConstraint(final int x, final int y, final int rows, final int cols, final double r) {
        final double maxdev = Math.ceil(Math.max(rows, cols) * r);
        final double ascent = (double) cols / (double) rows;
        Logging.getLogger(ConstraintFactory.class).info("Using band constraint with width {}, |REF| = {}, |QUERY| = {}", new Object[] { maxdev, rows, cols });
        Logging.getLogger(ConstraintFactory.class).info("Ascent of diagonal is {}", ascent);
        final GeneralPath band = new GeneralPath();
        band.moveTo(x, y);
        band.lineTo(x + (int) maxdev, y);
        band.lineTo(x + cols, y + rows - (int) maxdev);
        band.lineTo(x + cols, y + rows);
        band.lineTo(x + cols - (int) maxdev, y + rows);
        band.lineTo(x, y + (int) maxdev);
        band.closePath();
        return new Area(band);
    }
}

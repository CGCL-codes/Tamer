package mapgen;

import objects.Race;
import objects.geom.MapGeometry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public final class OctagonMapGenerator extends RegularMapGenerator {

    private static final double sq2 = Math.sqrt(2.0);

    @Override
    public final void figure(Race[] races, MapGeometry geometry) {
        int columns = (int) Math.ceil(Math.sqrt((double) races.length) / 2.0);
        double dx = homePlanetTypes.get(0).minDistance * (1.0 + sq2);
        geometry.setSize(Math.ceil(dx * (double) columns));
        dx = geometry.getSize() / (double) columns;
        List<Race> races2 = Arrays.asList(Arrays.copyOf(races, columns * columns * 4));
        Collections.shuffle(races2);
        ListIterator<Race> iter = races2.listIterator();
        double[][] pattern = { { dx / (2.0 + sq2), 0.0 }, { 0.0, dx / (2.0 + sq2) }, { dx / sq2, 0.0 }, { 0.0, dx / sq2 } };
        for (int i = 0; i < columns; ++i) for (int j = 0; j < columns; ++j) {
            double x0 = (double) i * dx + dx / (2.0 + sq2) / 2.0;
            double y0 = (double) j * dx + dx / (2.0 + sq2) / 2.0;
            for (int k = 0; k < 4; ++k) {
                if (!iter.hasNext()) break;
                Race race = iter.next();
                if (race != null) putNode(geometry, x0 + pattern[k][0], y0 + pattern[k][1], race);
            }
        }
    }
}

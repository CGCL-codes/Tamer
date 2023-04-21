package work.analysis;

import geo.Coordinate;
import geo.draw.MapImage;
import geometry.drawing.PolygonDrawer;
import geometry.drawing.PolygonDrawerAwt;
import geometry.serialization.util.PolygonLoader;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.osm.EntityNotFoundException;
import model.osm.OsmNode;
import model.osm.OsmWay;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import osmosis.FileFormat;
import osmosis.SourceIterator;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import db.nodedb.NodeDB;
import db.nodedb.osmmodel.WayImpl;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class WayPlotter {

    /**
	 * @param args
	 *            boundary, input, nodes, output, key
	 * @throws IOException
	 *             on failure.
	 * @throws FileNotFoundException
	 *             on failure.
	 */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Options options = new Options();
        Option optionInput = new Option("input", true, "an osm input file containing ways");
        Option optionOutput = new Option("output", true, "an image");
        Option optionNodes = new Option("nodes_index", true, "a node database");
        Option optionNodesData = new Option("nodes_data", true, "a node database");
        Option optionKey = new Option("key", true, "a key");
        Option optionBoundary = new Option("boundary", true, "a boundary to plot and to define the area");
        optionInput.setRequired(true);
        optionOutput.setRequired(true);
        optionNodes.setRequired(true);
        optionNodesData.setRequired(true);
        optionKey.setRequired(true);
        optionBoundary.setRequired(true);
        options.addOption(optionInput);
        options.addOption(optionOutput);
        options.addOption(optionNodes);
        options.addOption(optionNodesData);
        options.addOption(optionKey);
        options.addOption(optionBoundary);
        CommandLine line = null;
        try {
            line = new GnuParser().parse(options, args);
        } catch (ParseException e) {
            System.out.println("unable to parse command line: " + e.getMessage());
            System.exit(1);
        }
        if (line == null) return;
        String input = line.getOptionValue("input");
        String output = line.getOptionValue("output");
        String nodesIndex = line.getOptionValue("nodes_index");
        String nodesData = line.getOptionValue("nodes_data");
        String key = line.getOptionValue("key");
        String boundary = line.getOptionValue("boundary");
        NodeDB nodeDB = new NodeDB(nodesData, nodesIndex);
        int width = 1600;
        int height = 1600;
        Geometry bound = PolygonLoader.readPolygon(boundary);
        Envelope envelope = bound.getEnvelopeInternal();
        MapImage image = new MapImage(envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(), envelope.getMaxY(), width, height);
        PolygonDrawer pd = new PolygonDrawerAwt(image, output, width, height);
        pd.setColorForeground(new Color(255, 255, 255, 255));
        pd.setColorBackground(new Color(255, 255, 255, 255));
        pd.drawRectangle(0, 0, width, height, true);
        pd.setColorForeground(new Color(0, 0, 0, 255));
        pd.setLineWidth(1);
        pd.drawGeometry(bound, false);
        pd.setColorForeground(new Color(0, 0, 0, 0));
        pd.setColorBackground(new Color(255, 127, 0, 127));
        SourceIterator sourceIterator = new SourceIterator(FileFormat.PBF, input);
        for (EntityContainer ec : sourceIterator) {
            if (!(ec instanceof WayContainer)) continue;
            WayContainer wc = (WayContainer) ec;
            Way way = wc.getEntity();
            Collection<Tag> tags = way.getTags();
            for (Tag t : tags) {
                if (t.getKey().equals(key)) {
                    WayImpl wayImpl = new WayImpl(nodeDB, way);
                    Coordinate mean = materalize(wayImpl);
                    pd.drawCircle(mean.getLongitude(), mean.getLatitude(), 2, true);
                }
            }
        }
        pd.close();
    }

    private static Coordinate materalize(OsmWay way) {
        List<Coordinate> coords = new ArrayList<Coordinate>();
        for (int i = 0; i < way.getNumberOfNodes(); i++) {
            OsmNode node = null;
            try {
                node = way.getNode(i);
            } catch (EntityNotFoundException e) {
            }
            if (node != null) {
                Coordinate c = new Coordinate(node.getLongitude(), node.getLatitude());
                coords.add(c);
            }
        }
        Coordinate mean = Coordinate.mean(coords);
        return mean;
    }
}

package us.jonesrychtar.gispatialnet.Writer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import us.jonesrychtar.gispatialnet.DataSet;

/**
 *
 * @author cfbevan
 * @version 0.0.1
 */
public class ShapefileNodeWriter {

    private ShapefileWriter outN;

    private DataSet ds;

    private String schemeNodes;

    /**
     *Constructor
     * @param filename Name of output file
     * @param xin Vector matrix of x coordinate values
     * @param yin Vector matrix of y coordinate values
     * @param attbin Matrix containing attribute data for nodes
     * @throws IllegalArgumentException
     * @throws SchemaException
     * @throws IOException
     * @throws MalformedURLException
     */
    public ShapefileNodeWriter(String filename, Matrix xin, Matrix yin, Matrix attbin) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException {
        ds = new DataSet(xin, yin);
        ds.setAttb(attbin);
        schemeNodes = _analyzeScheme(attbin);
        outN = new ShapefileWriter(filename, schemeNodes);
    }

    /**
     * Constructor (no attributes)
     * @param filename Name of output file
     * @param xin Vector matrix of x coordinate values
     * @param yin Vector matrix of y coordinate values
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws SchemaException
     * @throws MalformedURLException
     */
    public ShapefileNodeWriter(String filename, Matrix xin, Matrix yin) throws IllegalArgumentException, IOException, MalformedURLException, SchemaException {
        ds = new DataSet(xin, yin);
        schemeNodes = "*geom:Point";
        outN = new ShapefileWriter(filename, schemeNodes);
    }

    public ShapefileNodeWriter(String nodefilename, DataSet ds) {
        this.ds = ds;
    }

    /**
     * Writes node data to shapefile
     * @throws IOException
     */
    public void write() throws IOException {
        GeometryFactory gfact = new GeometryFactory();
        for (int r = 0; r < ds.getX().getRowCount(); r++) {
            Object[] data;
            if (ds.getAttb() != null) {
                data = new Object[(int) ds.getAttb().getColumnCount() + 1];
            } else data = new Object[1];
            Coordinate coord = new Coordinate(ds.getX().getAsDouble(r, 0), ds.getY().getAsDouble(r, 0));
            Point geo1 = gfact.createPoint(coord);
            data[0] = geo1;
            if (ds.getAttb() != null) {
                for (int j = 1; j < ds.getAttb().getColumnCount(); j++) {
                    data[j] = ds.getAttb().getAsString(r, j);
                }
            }
            outN.addData(data);
        }
    }

    /**
     * Analyzes scheme of attribute data
     * @param in MAtrix to analyze
     * @return String containing the scheme
     */
    private String _analyzeScheme(Matrix in) {
        String sch = "*geom:Point";
        for (int i = 0; i < in.getColumnCount(); i++) {
            if (in.getColumnObject(i).getClass().isInstance(java.lang.Number.class)) {
                sch += ", " + in.getColumnLabel(i) + ":Float";
            } else {
                sch += ", " + in.getColumnLabel(i) + ":String";
            }
        }
        return sch;
    }
}

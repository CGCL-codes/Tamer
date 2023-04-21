package mil.army.usace.ehlschlaeger.rgik.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mil.army.usace.ehlschlaeger.digitalpopulations.tabletools.MapList;
import mil.army.usace.ehlschlaeger.rgik.core.DataException;

/**
 * Captures metadata from a CSV file. CSV tables by default have no metadata, so
 * we've defined a special marker that can be used to identify metadata rows in
 * the table. "Metadata" is data that describes the data, i.e. column data
 * types, descriptive titles, help text, etc. The exact meaning of the metadata
 * is application-dependent; this class only defines the format for the rows.
 * <P>
 * A metadata row is a normal table row where the first column flags the row as
 * metadata. The first field must start with a special marker sequence in the
 * first character, and the marker must be followed by a single keyword. The
 * keyword may be followed by whitespace and regular content for the first
 * column of the table. Each column (including the cleaned first column) defines
 * a value for each column in the data table, and is stored internally in a way
 * that can be accessed by column number, or by the column's regular name. These
 * values generally describe the column their column, but this is not necessary.
 * <P>
 * For example:
 * 
 * <PRE>
 *     Name          Easting Northing Size Age Color
 *     ##type string double  double   int  int string
 *     Bill          45      35        12  90  blonde
 *     Willhelm      37      22         8  72  alabaster
 *     Billiard      18      89         1   3  ivory
 * </PRE>
 * 
 * Data column 1 will contain {45, 37, 18}, while metadata column 1 contains
 * "double", which can be used to define the type of structure that should hold
 * the data. The metadata field can be accessed as metadata.get(1).get("type")
 * or metadata.get("Easting").get("type").
 * 
 * @author William R. Zwicky
 */
public class CSVMetadata {

    /** Default prefix for metadata rows. */
    public static final String DEFAULT_MARKER = "##";

    /** Name of metadata field that holds column index. */
    public static final String INDEX_NAME = "csvIndex";

    protected String marker;

    protected Pattern matcher;

    /**
     * colName -> (#key -> val)
     */
    protected MapList<String, Map<String, Object>> metadata;

    /**
     * Construct with default marker "##".
     */
    public CSVMetadata() {
        this.metadata = new MapList<String, Map<String, Object>>();
        this.metadata.setValues(new ExtendableList<Map<String, Object>>());
        setMarker(DEFAULT_MARKER);
    }

    /**
     * Construct with give metadata container. Container.getValues() should be
     * an ExtendableList, as values will be inserted at random.
     * 
     * @param container
     *            object to receive collected metadata, or null to detect
     *            metadata but not store it
     */
    public CSVMetadata(MapList<String, Map<String, Object>> container) {
        metadata = container;
        if (metadata != null) if (metadata.getValues() == null) metadata.setValues(new ExtendableList<Map<String, Object>>());
        setMarker(DEFAULT_MARKER);
    }

    /**
     * Change the marker string that identifies rows of metadata.
     * 
     * @param marker
     *            character string that marks a metadata line. Marker must
     *            appear at the first character of line, and must be immediately
     *            followed by a keyword.
     */
    public void setMarker(String marker) {
        this.marker = marker;
        this.matcher = Pattern.compile(String.format("^%s([A-Za-z0-9][^\\s]*)(\\s+(.*))?$", Pattern.quote(marker)));
    }

    /**
     * @return the metadata container, with all data collected so far. NOT A
     *         CLONE, changes to returned might affect this class. Object is a
     *         map of maps; object takes a column descriptor (either number or
     *         name), and returns a map of keywords to values. So a field must
     *         be accessed as getMetadata().get("column").get("keyword").
     */
    public MapList<String, Map<String, Object>> getMetadata() {
        return metadata;
    }

    /**
     * Add given names as column names.
     * 
     * @param header
     *            list of names for data columns. Nulls and blank strings are
     *            skipped.
     * @param metaVar
     *            if not null, this name will be defined in each columns
     *            metadata holding the corresponding name
     */
    public void addNames(List<String> header, String metaVar) {
        List<String> names = new ArrayList<String>();
        for (int c = 0; c < header.size(); c++) {
            String title = header.get(c);
            if (ObjectUtil.isBlank(title)) {
                names.add(null);
            } else {
                names.add(title);
                if (metaVar != null) {
                    Map<String, Object> map = metadata.get(c);
                    if (map == null) {
                        map = new HashMap<String, Object>();
                        metadata.set(c, map);
                        map.put(INDEX_NAME, c);
                    }
                    map.put(metaVar, title);
                }
            }
        }
        metadata.addAliases(names);
    }

    /**
     * Test line and extract metdata if found.
     * 
     * @param line one row of a table
     * @return true if given line is metadata; false if regular data
     */
    public boolean readRow(List<String> line) {
        Matcher mat = matcher.matcher(line.get(0));
        if (mat.matches()) {
            if (metadata != null) {
                String key = mat.group(1);
                String col0 = mat.group(3);
                for (int c = 0; c < line.size(); c++) {
                    Map<String, Object> map = metadata.get(c);
                    if (map == null) {
                        map = new HashMap<String, Object>();
                        metadata.set(c, map);
                        map.put(INDEX_NAME, c);
                    }
                    if (map.containsKey(key)) throw new DataException(String.format("Metadata key \"%s\" has been defined twice.", key));
                    if (c == 0) map.put(key, col0); else map.put(key, line.get(c));
                }
            }
            return true;
        } else {
            return false;
        }
    }
}

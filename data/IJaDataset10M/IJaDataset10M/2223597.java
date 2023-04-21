package ArianneEditor;

import java.util.Vector;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class QueryPlot {

    private int id;

    private Vector columns;

    private String query;

    QueryPlot(int i, Vector cols, String q) {
        id = i;
        columns = cols;
        query = q;
    }

    public int getId() {
        return id;
    }

    public Vector getColumns() {
        return columns;
    }

    public String getQuery() {
        return query;
    }
}

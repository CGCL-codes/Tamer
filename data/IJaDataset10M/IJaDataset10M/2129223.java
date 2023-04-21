package org.proclos.etlcore.source.filter;

import org.palo.api.Dimension;
import org.palo.api.Cube;
import org.palo.api.Element;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class CubeFilter {

    private HashMap<String, Dimension> dimensionLookup = new HashMap<String, Dimension>();

    private Vector<Dimension> dimensions = new Vector<Dimension>();

    private HashMap<String, DimensionFilter> filters = new HashMap<String, DimensionFilter>();

    public CubeFilter(Cube cube) {
        Dimension[] dims = cube.getDimensions();
        for (Dimension dim : dims) {
            dimensionLookup.put(dim.getName(), dim);
            dimensions.add(dim);
        }
    }

    public DimensionFilter addDimensionFilter(String dimension) {
        Dimension dim = dimensionLookup.get(dimension);
        DimensionFilter filter = new DimensionFilter(dim);
        filters.put(filter.getName(), filter);
        return filter;
    }

    private ArrayList<String[]> cartesian(String[][] arrays) {
        ArrayList<String[]> lines = new ArrayList<String[]>();
        long idx = 1;
        for (int i = 0; i < arrays.length; idx *= arrays[i].length, i++) ;
        while (idx-- > 0) {
            long j = 1;
            String[] line = new String[arrays.length];
            int lineindex = 0;
            for (String[] a : arrays) {
                line[lineindex] = a[(int) ((idx / j) % a.length)];
                j *= a.length;
                lineindex++;
            }
            lines.add(0, line);
        }
        return lines;
    }

    public String[][] getBasis() {
        String[][] basis = new String[dimensions.size()][];
        int index = 0;
        for (Dimension dim : dimensions) {
            DimensionFilter filter = filters.get(dim.getName());
            if (filter == null) {
                filter = new DimensionFilter(dim);
                filter.acceptRootElements();
            }
            String[] coordinates = filter.process();
            if (coordinates.length == 0) {
                filter.acceptRootElements();
                coordinates = filter.process();
            }
            basis[index] = coordinates;
            index++;
        }
        return basis;
    }

    public Element[][] getBasisElements() {
        Element[][] basis = new Element[dimensions.size()][];
        int index = 0;
        for (Dimension dim : dimensions) {
            DimensionFilter filter = filters.get(dim.getName());
            if (filter == null) {
                filter = new DimensionFilter(dim);
                filter.acceptRootElements();
            }
            String[] coordinates = filter.process();
            if (coordinates.length == 0) {
                filter.acceptRootElements();
                coordinates = filter.process();
            }
            basis[index] = filter.getElements(coordinates);
            index++;
        }
        return basis;
    }

    public String[][] buildCartesian() {
        ArrayList<String[]> coordinates = cartesian(getBasis());
        return coordinates.toArray(new String[coordinates.size()][]);
    }

    public String[][] buildCartesian(String[][] basis) {
        ArrayList<String[]> coordinates = cartesian(basis);
        return coordinates.toArray(new String[coordinates.size()][]);
    }
}

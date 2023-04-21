package org.supercsv.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;

/**
 * Useful utility methods.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public final class Util {

    private Util() {
    }

    /**
	 * Convert a map to a list
	 * 
	 * @param source
	 *            the map to create a list from
	 * @param nameMapping
	 *            the keys of the map whose values will constitute the list
	 * @return a list
	 */
    public static List<? extends Object> map2List(final Map<String, ? extends Object> source, final String[] nameMapping) {
        final List<? super Object> result = new ArrayList<Object>(nameMapping.length);
        for (final String element : nameMapping) {
            result.add(source.get(element));
        }
        return result;
    }

    /**
	 * A function to convert a list to a map using a namemapper for defining the keys of the map.
	 * 
	 * @param destination
	 *            the resulting map instance. The map is cleared before populated
	 * @param nameMapper
	 *            cannot contain duplicate names
	 * @param values
	 *            A list of values TODO: see if using an iterator for the values is more efficient
	 */
    public static <T> void mapStringList(final Map<String, T> destination, final String[] nameMapper, final List<T> values) {
        if (nameMapper.length != values.size()) {
            throw new SuperCSVException("The namemapper array and the value list must match in size. Number of columns mismatch number of entries for your map.");
        }
        destination.clear();
        for (int i = 0; i < nameMapper.length; i++) {
            final String key = nameMapper[i];
            if (key == null) {
                continue;
            }
            if (destination.containsKey(key)) {
                throw new SuperCSVException("nameMapper array contains duplicate key \"" + key + "\" cannot map the list");
            }
            destination.put(key, values.get(i));
        }
    }

    /**
	 * A function which given a list of strings, process each cell using its corresponding processor-chain from the
	 * processor array and return the result as an array. Can be extended so the safety check is cached in case the same
	 * two arrays are used on several requests
	 * 
	 * @param destination
	 *            This list is emptied and then populated with the result from reading a line
	 * @param source
	 *            Is an array of Strings/null's representing the soure elements
	 * @param processors
	 *            an array of CellProcessors/null's enabling custom processing of each cellvalue specified in the
	 *            cellValue array. The number of non-null entries in the cellValues array must match the number of
	 *            processors/null given.
	 * @param lineNo
	 *            the line number of the CSV source the processing is taking place on
	 */
    public static void processStringList(final List<? super Object> destination, final List<? extends Object> source, final CellProcessor[] processors, final int lineNo) throws SuperCSVException {
        final CSVContext context = new CSVContext();
        context.lineSource = source;
        context.lineNumber = lineNo;
        if (source.size() != processors.length) {
            throw new SuperCSVException("The value array (size " + source.size() + ")  must match the processors array (size " + processors.length + "):" + " You are probably reading a CSV line with a different number of columns" + " than the number of cellprocessors specified", context);
        }
        destination.clear();
        for (int i = 0; i < source.size(); i++) {
            if (processors[i] == null) {
                destination.add(source.get(i));
            } else {
                context.columnNumber = i;
                destination.add(processors[i].execute(source.get(i), context));
            }
        }
    }

    /**
	 * Convert a map to an array of objects
	 * 
	 * @param values
	 *            the values
	 * @param nameMapping
	 *            the mapping defining the order of the value extract of the map
	 * @return the map unfolded as an array based on the nameMapping
	 */
    public static Object[] stringMap(final Map<String, ? extends Object> values, final String[] nameMapping) {
        final Object[] res = new Object[nameMapping.length];
        int i = 0;
        for (final String name : nameMapping) {
            res[i++] = values.get(name);
        }
        return res;
    }
}

package org.jcube.util;

import org.jcube.Dimension;
import org.jcube.Value;
import java.util.List;

/**
 * User: liujidong
 * Date: 2007-7-3
 * Time: 15:59:01
 * To change this template use File | Settings | File Templates.
 */
public class TextView {

    public static void output(List dimensionsArray, int[] dimCol, int currentCol, List prevIntersection, int valueCol) {
        List dimensions = (List) dimensionsArray.get(dimCol[currentCol]);
        if (currentCol == dimCol.length - 1) {
            for (int j = 0; j < dimensions.size(); j++) {
                Dimension dim = (Dimension) dimensions.get(j);
                System.err.print(dim.getName() + "\t");
                List intersection = dim.intersect(valueCol, prevIntersection);
                System.err.println(Value.sum(intersection));
            }
            return;
        }
        for (int i = 0; i < dimensions.size(); i++) {
            Dimension dim = (Dimension) dimensions.get(i);
            System.err.print(dim.getName() + "\t");
            output(dimensionsArray, dimCol, currentCol + 1, dim.intersect(valueCol, prevIntersection), valueCol);
        }
    }
}

package org.mili.jmibs.examples;

import java.util.*;
import org.mili.jmibs.impl.*;

/**
 * This class defines a benchmark to test performance while traversing an ArrayList of strings
 * with Java embedded for-each.
 *
 * @author Michael Lieshoff
 * @version 1.0 12.04.2010
 * @since 1.0
 */
public class TraverseForEachArrayListStringBenchmark extends AbstractObjectLoadBenchmark<List<String>> {

    /**
     * creates a new traverse for each array list string benchmark.
     */
    public TraverseForEachArrayListStringBenchmark() {
        super();
        this.setName("Traverse: For(embedded for each) ArrayList<String>");
    }

    @Override
    public void execute() {
        for (String s : this.getModel()) {
        }
    }

    @Override
    public void prepare() {
        this.setModel(new ArrayList<String>());
        for (int i = 0, n = this.getObjectLoad(); i < n; i++) {
            this.getModel().add(String.valueOf(i));
        }
    }
}

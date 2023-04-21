package com.rapidminer.example.set;

import java.util.Iterator;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

/**
 * This reader simply uses all examples from the parent and all available attributes.
 * 
 * @author Ingo Mierswa
 */
public class AttributesExampleReader extends AbstractExampleReader {

    /** The parent example reader. */
    private Iterator<Example> parent;

    /** The used attributes are described in this example set. */
    private ExampleSet exampleSet;

    /** Creates a simple example reader. */
    public AttributesExampleReader(Iterator<Example> parent, ExampleSet exampleSet) {
        this.parent = parent;
        this.exampleSet = exampleSet;
    }

    /** Returns true if there are more data rows. */
    public boolean hasNext() {
        return this.parent.hasNext();
    }

    /** Returns a new example based on the current data row. */
    public Example next() {
        if (!hasNext()) return null;
        Example example = this.parent.next();
        if (example == null) return null;
        return new Example(example.getDataRow(), exampleSet);
    }
}

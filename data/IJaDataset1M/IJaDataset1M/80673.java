package de.ui.sushi.fs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** 
 * Mainly used to Node.readLines and testing; applications should use LineProcessor instead, 
 * because it's more efficient.
 */
public class LineCollector extends LineProcessor {

    private final List<String> result;

    public LineCollector(int size, boolean trim, boolean empty, String comment) {
        super(size, trim, empty, comment);
        result = new ArrayList<String>();
    }

    public List<String> collect(Node node) throws IOException {
        run(node);
        return result;
    }

    @Override
    public void line(String line) {
        result.add(line);
    }
}

package org.jalgo.module.am0c0.model.am0;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a non specific stream for an abstract machine.
 * 
 * @author Max Leuth&auml;user
 */
public abstract class Stream {

    protected List<MemoryCell> stream;

    /**
	 * Creates a new empty stream.
	 */
    public Stream() {
        stream = new LinkedList<MemoryCell>();
    }

    /**
	 * @return the stream as String
	 */
    @Override
    public String toString() {
        if (stream.isEmpty()) {
            return "Ɛ";
        }
        StringBuilder result = new StringBuilder();
        for (MemoryCell i : stream) {
            result.append("," + i.get());
        }
        return result.toString().replaceFirst(",", "");
    }
}

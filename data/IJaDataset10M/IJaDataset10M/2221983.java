package org.gbt2.measurement;

/**
 * @author Christoph Müller
 * @version 1.0 - 21.03.2007
 *
 */
public class LongContainer {

    public long content;

    public LongContainer() {
        this.content = 0L;
    }

    public LongContainer(long content) {
        this.content = content;
    }

    public void increment() {
        this.content++;
    }
}

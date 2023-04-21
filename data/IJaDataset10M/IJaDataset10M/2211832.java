package net.sf.parc.pipeline.support;

import net.sf.parc.pipeline.Consumer;
import net.sf.parc.pipeline.InputAdapter;

public abstract class AbstractInputAdapter implements InputAdapter {

    private Consumer validConsumer;

    private Consumer errorConsumer;

    public void init() {
    }

    public void close() {
    }

    public Consumer getValidConsumer() {
        return this.validConsumer;
    }

    public void setValidConsumer(Consumer validConsumer) {
        this.validConsumer = validConsumer;
    }

    public Consumer getErrorConsumer() {
        return this.errorConsumer;
    }

    public void setErrorConsumer(Consumer errorConsumer) {
        this.errorConsumer = errorConsumer;
    }
}

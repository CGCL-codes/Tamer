package ctags.sidekick;

import java.util.Vector;

public abstract class ListObjectProcessor extends AbstractObjectProcessor {

    Vector<IObjectProcessor> processors;

    public ListObjectProcessor(String name, String description) {
        super(name, description);
        processors = new Vector<IObjectProcessor>();
    }

    public void add(IObjectProcessor processor) {
        processors.add(processor);
    }

    public Vector<IObjectProcessor> getProcessors() {
        return processors;
    }

    public AbstractObjectEditor getEditor() {
        return null;
    }
}

package serene.internal;

import serene.bind.ElementTask;

class ExceptNameClassTaskFactory extends RNGParseEndElementTaskFactory {

    public ExceptNameClassTaskFactory() {
        super();
    }

    public ExceptNameClassTask getTask() {
        return new ExceptNameClassTask();
    }
}

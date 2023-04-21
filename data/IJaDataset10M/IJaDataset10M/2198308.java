package serene.internal;

import serene.bind.ElementTaskFactory;

abstract class RNGParseElementTaskFactory implements ElementTaskFactory {

    RNGParseElementTaskFactory() {
    }

    public abstract RNGParseElementTask getTask();
}

package serene.internal;

import serene.bind.ElementTask;

class GrammarTaskFactory extends RNGParseEndElementTaskFactory {

    public GrammarTaskFactory() {
        super();
    }

    public GrammarTask getTask() {
        return new GrammarTask();
    }
}

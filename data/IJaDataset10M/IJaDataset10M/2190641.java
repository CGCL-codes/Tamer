package serene.internal;

import serene.bind.ElementTask;

class DivGrammarContentTaskFactory extends RNGParseEndElementTaskFactory {

    public DivGrammarContentTaskFactory() {
        super();
    }

    public DivGrammarContentTask getTask() {
        return new DivGrammarContentTask();
    }
}

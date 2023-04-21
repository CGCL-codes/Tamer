package org.metatemplate;

public class PlainProcessorInvocationContent extends ProcessorInvocationContent {

    public PlainProcessorInvocationContent(ProcessorRegistry registry) {
        super(registry);
    }

    @Override
    public Content invoke() {
        PlainProcessor processor = (PlainProcessor) getProcessor();
        return processor.invoke(args);
    }
}

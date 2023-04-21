package uk.co.cocking.getinline2.pipeline;

import java.util.List;
import uk.co.cocking.getinline2.exceptions.EnvironmentException;
import uk.co.cocking.getinline2.exceptions.ExceptionHandler;
import uk.co.cocking.getinline2.pipeline.io.CouldNotProcessRecordException;
import uk.co.cocking.getinline2.pipeline.transformers.Transformer;

public class GenericPipe<IncomingType, OutgoingType> implements Consumer<IncomingType> {

    private final Transformer<IncomingType, OutgoingType> transformer;

    private final Consumer<OutgoingType> sink;

    private final ExceptionHandler exceptionHandler;

    public GenericPipe(Transformer<IncomingType, OutgoingType> transformer, Consumer<OutgoingType> sink, ExceptionHandler exceptionHandler) {
        this.transformer = transformer;
        this.sink = sink;
        this.exceptionHandler = exceptionHandler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void open() throws EnvironmentException {
        sink.open();
        List<OutgoingType> openingItems = transformer.open();
        for (OutgoingType item : openingItems) {
            sink.process(item);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(IncomingType... items) throws EnvironmentException {
        List<OutgoingType> transformedItems;
        for (IncomingType item : items) {
            try {
                transformedItems = transformer.transform(item);
                for (OutgoingType transformedItem : transformedItems) {
                    sink.process(transformedItem);
                }
            } catch (CouldNotProcessRecordException e) {
                exceptionHandler.warn("could not transform item " + item, e);
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void close() throws EnvironmentException {
        List<OutgoingType> closingItems = transformer.close();
        for (OutgoingType item : closingItems) {
            sink.process(item);
        }
        sink.close();
    }
}

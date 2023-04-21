package edu.cmu.ece.agora.pipeline.messages;

public abstract class ResponseMessage<R extends RequestMessage> implements PipelineMessage {

    private final R request;

    private final Throwable failure_cause;

    public ResponseMessage(R request) {
        this(request, null);
    }

    public ResponseMessage(R request, Throwable failure_cause) {
        this.request = request;
        this.failure_cause = failure_cause;
    }

    public final R getRequest() {
        return request;
    }

    public final boolean isFailure() {
        return failure_cause != null;
    }

    public final Throwable getFailureCause() {
        return failure_cause;
    }
}

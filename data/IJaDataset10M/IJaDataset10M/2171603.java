package net.viens.numenor;

import java.io.Serializable;

public class Response implements Serializable {

    public static final Response EMPTY_RESPONSE = new Response();

    public static final long RESULT_SUCCESS = 0;

    public static final long RESULT_FAIL = -1;

    public static final long REASON_SUCCESS = 0;

    public static final long REASON_INVALID_XML = -1;

    private long result = RESULT_SUCCESS;

    private long reason = REASON_SUCCESS;

    private String message = "";

    private Queue queue;

    public Response() {
        super();
        this.queue = Queue.EMPTY_QUEUE;
    }

    public long getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }

    public long getReason() {
        return reason;
    }

    public void setReason(long reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Queue getQueue() {
        return queue;
    }
}

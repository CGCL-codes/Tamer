package tms.client.services.result;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Abstract class representing the result of any data operation.
 * 
 * @author Werner Liebenberg
 * @author Wildrich Fourie
 */
public abstract class DataOperationResult implements IsSerializable {

    private boolean successful;

    private boolean failed;

    private boolean partiallyFailed;

    private boolean cancelled;

    private String message;

    protected Exception exception;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
        this.failed = !this.successful;
        this.partiallyFailed = !this.successful;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
        this.successful = !this.failed;
    }

    public boolean isPartiallyFailed() {
        return partiallyFailed;
    }

    public void setPartiallyFailed(boolean partiallyFailed) {
        this.partiallyFailed = partiallyFailed;
        this.successful = !this.partiallyFailed;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getMessage() {
        return message != null && !message.isEmpty() ? message : exception != null && !exception.getMessage().isEmpty() ? exception.getMessage() : null;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public String toString() {
        return this.getMessage() + "; " + (successful ? "successful" : failed ? "failed" : partiallyFailed ? "failed partially" : cancelled ? "cancelled" : "unknown") + (this.exception != null ? "; " + this.exception : "");
    }
}

package jpfm.util;

import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import jpfm.JPfmError;
import jpfm.operations.readwrite.Completer;
import jpfm.operations.readwrite.ReadRequest;

/**
 *
 * @author Shashank Tulsyan
 */
public final class SplittedRequestCompletionHandler implements CompletionHandler<Integer, Integer>, Completer<ReadRequest> {

    private final int[] expectedReadSizes;

    private final int[] actualReadSizes;

    private final ReadRequest read;

    private final AtomicBoolean completed = new AtomicBoolean(false);

    SplittedRequestCompletionHandler(final int[] expectedReadSizes, final ReadRequest read) {
        this.expectedReadSizes = expectedReadSizes;
        this.read = read;
        actualReadSizes = new int[expectedReadSizes.length];
        for (int i = 0; i < actualReadSizes.length; i++) {
            actualReadSizes[i] = Integer.MAX_VALUE;
        }
    }

    @Override
    public final void completed(final Integer result, final Integer indexOfRequestWrtStartIndex) {
        if (completed.get()) return;
        actualReadSizes[indexOfRequestWrtStartIndex] = result;
        check();
    }

    @Override
    public final void failed(final Throwable exc, final Integer indexOfRequestWrtStartIndex) {
        if (completed.get()) return;
        actualReadSizes[indexOfRequestWrtStartIndex] = -2;
        check();
    }

    private void check() {
        int bytesFilledTillNow = 0;
        for (int i = 0; i < actualReadSizes.length; i++) {
            if (actualReadSizes[i] == expectedReadSizes[i]) {
                bytesFilledTillNow += expectedReadSizes[i];
            } else if (actualReadSizes[i] <= 0) {
                if (completed.compareAndSet(false, true)) {
                    return;
                } else {
                    if (bytesFilledTillNow == 0) read.complete(JPfmError.getJPfmErrorForNothingRead(), bytesFilledTillNow, this);
                    read.complete(JPfmError.SUCCESS, bytesFilledTillNow, this);
                    return;
                }
            } else {
                return;
            }
        }
        if (!completed.compareAndSet(false, true)) {
            if (read.isCompleted()) {
                return;
            }
        }
        byte[] n = new byte[read.getByteBuffer().capacity()];
        for (int i = 0; i < expectedReadSizes.length; i++) {
            n[i] = read.getByteBuffer().get(i);
        }
        read.complete(JPfmError.SUCCESS, read.getByteBuffer().capacity(), this);
    }

    public final ReadRequest getActualReadRequest() {
        return read;
    }

    @Override
    public final int getBytesFilledTillNow(ReadRequest pendingRequest) {
        if (pendingRequest != this.read) {
            throw new IllegalArgumentException("This SplitRequestCompletionHandler handles only requests that it" + "was associated with during it\'s creation. Attempting to get info on some other pendingRequest");
        }
        int bytesFilledTillNow = 0;
        for (int i = 0; i < actualReadSizes.length; i++) {
            if (actualReadSizes[i] == expectedReadSizes[i]) {
                bytesFilledTillNow += expectedReadSizes[i];
            } else {
                break;
            }
        }
        return bytesFilledTillNow;
    }

    @Override
    public final void completeNow(ReadRequest pendingRequest) {
        if (pendingRequest != this.read) {
            throw new IllegalArgumentException("This SplitRequestCompletionHandler handles only requests that it" + "was associated with during it\'s creation. Attempting to complete some other pendingRequest");
        }
        if (!completed.compareAndSet(false, true)) {
        }
        int bytesFilledTillNow = getBytesFilledTillNow(pendingRequest);
        if (bytesFilledTillNow == 0) read.complete(JPfmError.getJPfmErrorForNothingRead(), bytesFilledTillNow, this);
        read.complete(JPfmError.SUCCESS, read.getByteBuffer().capacity(), this);
    }

    @Override
    public final String toString() {
        return read.toString() + " " + getExpectedReadSize();
    }

    private String getExpectedReadSize() {
        String l = "";
        for (int i = 0; i < expectedReadSizes.length; i++) {
            l = l + " " + expectedReadSizes[i];
        }
        return l;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

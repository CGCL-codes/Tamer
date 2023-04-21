package maze.server_reactor;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;
import maze.commons.shared.base.BasicBase;
import maze.commons.shared.util.SharedUtils;
import maze.server_reactor.AbstractServerReactor.CloseNotify;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public abstract class AbstractHandler extends BasicBase implements Runnable, Closeable {

    private static final Logger logger = Logger.getLogger(AbstractHandler.class.getName());

    public static final int MAX_INPUT = 1024 * 100;

    private ByteBuffer inputBuffer;

    private ByteBuffer outputBuffer = null;

    protected final SocketChannel socketChannel;

    private final Selector selector;

    protected final SelectionKey selectionKey;

    private HandlerWorkerState currentWorkerState = null;

    protected final CloseNotify closeNotify;

    protected abstract boolean isReadingCompleted();

    protected abstract void process();

    protected int getMaxInputSize() {
        return MAX_INPUT;
    }

    protected boolean isSendingCompleted() {
        return outputBuffer == null || !outputBuffer.hasRemaining();
    }

    protected ByteBuffer newInputBuffer() {
        return inputBuffer = ByteBuffer.allocate(getMaxInputSize());
    }

    protected ByteBuffer wrapOutputBuffer(final byte[] array) {
        if (array == null) {
            return outputBuffer = null;
        }
        return outputBuffer = ByteBuffer.wrap(array);
    }

    protected ByteBuffer getInputBuffer() {
        return inputBuffer;
    }

    protected int getReadyForReading() {
        final ByteBuffer inputBuffer = getInputBuffer();
        return inputBuffer == null ? 0 : getMaxInputSize() - inputBuffer.remaining();
    }

    protected ByteBuffer getOutputBuffer() {
        return outputBuffer;
    }

    protected Selector selectorWakeup() {
        return selector.wakeup();
    }

    protected void interestOps(final int ops) {
        selectionKey.interestOps(ops);
        if (ops != 0) {
            selectorWakeup();
        }
    }

    protected int socketChannelRead() throws IOException {
        final int wasRemaining = getInputBuffer().remaining();
        try {
            return socketChannel.read(getInputBuffer());
        } finally {
            if (wasRemaining == getInputBuffer().remaining()) {
                throw new RuntimeException("No data read!");
            }
        }
    }

    protected void cancelSelectionKey() {
        selectionKey.cancel();
    }

    @Override
    public void close() throws IOException {
        try {
            cancelSelectionKey();
        } finally {
            try {
                socketChannel.close();
            } finally {
                if (closeNotify != null) {
                    closeNotify.notifyClose();
                }
            }
        }
    }

    protected void innerClose() {
        try {
            close();
        } catch (IOException ioe) {
            logger.severe(SharedUtils.printStackTrace(ioe));
        }
    }

    protected void innerProcessExceptionHandlig(final RuntimeException e) {
        try {
            close();
        } catch (Exception closeExc) {
            logger.severe(SharedUtils.printStackTrace(closeExc));
        }
    }

    protected void innerProcess() {
        try {
            process();
        } catch (RuntimeException e) {
            logger.severe(SharedUtils.printStackTrace(e));
            innerProcessExceptionHandlig(e);
        }
    }

    protected HandlerWorkerState getCurrentWorkerState() {
        return currentWorkerState;
    }

    protected HandlerWorkerState changeCurrentWorkerState(final HandlerWorkerState currentWorkerState) {
        return this.currentWorkerState = currentWorkerState;
    }

    protected class SendWorkerState implements HandlerWorkerState {

        @Override
        public HandlerWorkerState nextWorkerState() throws IOException {
            return null;
        }

        @Override
        public HandlerWorkerState init() throws IOException {
            if (outputBuffer != null) {
                outputBuffer.rewind();
                interestOps(SelectionKey.OP_WRITE);
                return this;
            }
            return nextWorkerState();
        }

        @Override
        public boolean isWorkCompleted() {
            return isSendingCompleted();
        }

        @Override
        public boolean contWork() {
            return false;
        }

        @Override
        public void work() throws IOException {
            if (outputBuffer != null) {
                socketChannel.write(outputBuffer);
            }
        }
    }

    protected HandlerWorkerState createSendWorkerState() {
        return new SendWorkerState();
    }

    protected class ReadWorkerState implements HandlerWorkerState {

        @Override
        public HandlerWorkerState init() {
            interestOps(SelectionKey.OP_READ);
            return this;
        }

        protected void process() {
            inputBuffer.rewind();
            AbstractHandler.this.innerProcess();
        }

        @Override
        public boolean isWorkCompleted() {
            return isReadingCompleted();
        }

        @Override
        public boolean contWork() {
            return false;
        }

        @Override
        public void work() throws IOException {
            socketChannelRead();
        }

        @Override
        public HandlerWorkerState nextWorkerState() {
            process();
            return createSendWorkerState();
        }
    }

    protected HandlerWorkerState createReadWorkerState() {
        return new ReadWorkerState();
    }

    protected HandlerWorkerState createStartWorkerState() {
        return createReadWorkerState();
    }

    protected HandlerWorkerState switchWorkerState() throws IOException {
        HandlerWorkerState currentWorkerState;
        if (getCurrentWorkerState() == null) {
            currentWorkerState = changeCurrentWorkerState(createStartWorkerState());
        } else {
            currentWorkerState = changeCurrentWorkerState(getCurrentWorkerState().nextWorkerState());
        }
        if (currentWorkerState != null) {
            currentWorkerState = changeCurrentWorkerState(currentWorkerState.init());
        }
        if (currentWorkerState == null) {
            close();
        }
        return currentWorkerState;
    }

    protected AbstractHandler(final SocketChannel socketChannel, final Selector selector, final AbstractServerReactor.CloseNotify closeNotify) throws IOException {
        assert socketChannel != null;
        assert selector != null;
        newInputBuffer();
        this.selector = selector;
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        this.selectionKey = socketChannel.register(selector, 0);
        this.selectionKey.attach(this);
        this.closeNotify = closeNotify;
        switchWorkerState();
    }

    protected void contWork() throws IOException {
        if (getCurrentWorkerState() == null) {
            return;
        }
        final boolean mustWorkContFlag = getCurrentWorkerState().contWork();
        if (getCurrentWorkerState().isWorkCompleted()) {
            switchWorkerState();
            if (mustWorkContFlag) {
                contWork();
            }
        }
    }

    protected void work() throws IOException {
        if (getCurrentWorkerState() == null) {
            return;
        }
        getCurrentWorkerState().work();
        contWork();
    }

    protected void innerRunExceptionHandling(final Exception e) {
    }

    protected void runExceptionHandling(final Exception e) {
        innerClose();
        innerRunExceptionHandling(e);
    }

    @Override
    public void run() {
        try {
            work();
        } catch (Exception e) {
            logger.info(SharedUtils.printStackTrace(e));
            runExceptionHandling(e);
        }
    }
}

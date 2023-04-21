package net.kano.joustsim.oscar.oscar.service.icbm.dim;

import net.kano.joscar.ByteBlock;
import net.kano.joscar.DynAsciiCharSequence;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.controllers.AbstractTransferrer;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.controllers.PauseHelper;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.events.EventPost;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.state.StreamInfo;
import org.jetbrains.annotations.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectimReceiver extends AbstractTransferrer {

    private static final Logger LOGGER = Logger.getLogger(DirectimReceiver.class.getName());

    /**
   * Matches ID=abc123 case-insensitively with any combination of single or
   * double quotes around the value. I've made a learning computer.
   */
    private static final Pattern PATTERN_ID = Pattern.compile("ID=['\"]*(\\w+)[\"']*", Pattern.CASE_INSENSITIVE);

    /**
   * Matches SIZE=abc123 case-insensitively with any combination of single or
   * double quotes around the value.
   */
    private static final Pattern PATTERN_SIZE = Pattern.compile("SIZE=['\"]*(\\w+)[\"']*", Pattern.CASE_INSENSITIVE);

    private boolean autoResponse;

    private final String charset;

    private final AttachmentSaver saver;

    private final EventPost eventPost;

    @Nullable
    private final PauseHelper pauseHelper;

    @Nullable
    private final Cancellable cancellable;

    private ByteBuffer buffer;

    private DynAsciiCharSequence chars;

    {
        resizeBuffer(1024);
    }

    private ByteArrayOutputStream msgBuffer = new ByteArrayOutputStream();

    @Nullable
    private Selector selector;

    private Mode mode = Mode.MESSAGE;

    private String lastid = null;

    private Attachment last = null;

    private long lastAttachmentReceived = 0;

    private Long lastAttachmentSize = null;

    private boolean checkbuffer = false;

    private WritableByteChannel destchannel = null;

    public DirectimReceiver(StreamInfo stream, EventPost eventPost, @Nullable PauseHelper pauseHelper, AttachmentSaver saver, @Nullable Cancellable cancellable, String charset, long datalen, boolean autoResponse) {
        this(eventPost, pauseHelper, saver, cancellable, charset, datalen, stream.getReadableChannel(), stream.getSelectableChannel(), autoResponse);
    }

    public DirectimReceiver(EventPost eventPost, AttachmentSaver saver, String charset, long datalen, ReadableByteChannel readable, boolean autoResponse) {
        this(eventPost, null, saver, null, charset, datalen, readable, null, autoResponse);
    }

    public DirectimReceiver(EventPost eventPost, @Nullable PauseHelper pauseHelper, AttachmentSaver saver, @Nullable Cancellable cancellable, String charset, long datalen, ReadableByteChannel readable, @Nullable SelectableChannel selectable, boolean autoResponse) {
        super(readable, null, selectable, 0, datalen);
        this.cancellable = cancellable;
        this.charset = charset;
        this.saver = saver;
        this.eventPost = eventPost;
        this.pauseHelper = pauseHelper;
        this.autoResponse = autoResponse;
    }

    public void resizeBuffer(int size) {
        buffer = ByteBuffer.allocate(size);
        chars = new DynAsciiCharSequence(ByteBlock.wrap(buffer.array()));
    }

    protected boolean isCancelled() {
        return cancellable != null && cancellable.isCancelled();
    }

    protected boolean waitIfPaused() {
        return pauseHelper != null && pauseHelper.waitUntilUnpause();
    }

    protected void waitUntilReady() throws IOException {
        if (checkbuffer) return;
        if (mode == Mode.DATA && selector != null) {
            selector.select(50);
        }
        if (buffer.position() == 0) {
            super.waitUntilReady();
        }
    }

    protected long transferChunk(ReadableByteChannel readable, WritableByteChannel writable, long transferred, long remaining) throws IOException {
        int origpos = buffer.position();
        if (!checkbuffer && ((mode == Mode.MESSAGE || mode == Mode.TAG) && buffer.remaining() == 0)) {
            LOGGER.warning("DIM buffer full; entering drain mode from " + mode);
            eventPost.fireEvent(new EnteringDrainModeEvent(remaining));
            mode = Mode.DRAIN;
            checkbuffer = false;
            buffer.rewind();
            buffer.limit(buffer.capacity());
            return origpos;
        }
        buffer.limit((int) Math.min(buffer.capacity(), buffer.position() + remaining));
        int read = readable.read(buffer);
        int actuallyRead = Math.max(read, 0);
        if (!checkbuffer && (read == -1 || mode == Mode.DRAIN)) {
            int skipped = buffer.position();
            buffer.rewind();
            buffer.limit(buffer.capacity());
            long progress = transferred + actuallyRead;
            long total = transferred + remaining;
            eventPost.fireEvent(new DrainingEvent(progress, total));
            checkbuffer = false;
            return skipped;
        }
        checkbuffer = (read > 0);
        chars.setLength(buffer.position());
        if (mode == Mode.MESSAGE || mode == Mode.TAG || mode == Mode.DRAIN) {
            if (mode == Mode.MESSAGE) {
                int binaryPos = chars.indexOf("<BINARY>");
                if (binaryPos != -1) {
                    int firstDataTag = binaryPos + "<BINARY>".length();
                    msgBuffer.write(buffer.array(), 0, binaryPos);
                    buffer.position(firstDataTag);
                    buffer.compact();
                    String message = new String(msgBuffer.toByteArray(), charset);
                    mode = Mode.TAG;
                    checkbuffer = true;
                    msgBuffer = null;
                    eventPost.fireEvent(new ReceivedMessageEvent(message, autoResponse));
                    return firstDataTag;
                } else {
                    int writelen;
                    if (remaining - actuallyRead <= 7) {
                        writelen = buffer.position();
                    } else {
                        writelen = Math.max(0, buffer.position() - 7);
                    }
                    if (writelen > 0) {
                        msgBuffer.write(buffer.array(), 0, writelen);
                        String message = new String(msgBuffer.toByteArray(), charset);
                        eventPost.fireEvent(new ReceivingMessageEvent(msgBuffer.size(), transferred + remaining, message));
                    }
                    int endpos = buffer.position();
                    buffer.position(writelen);
                    buffer.limit(endpos);
                    buffer.compact();
                    return writelen;
                }
            } else if (mode == Mode.TAG) {
                int closeBracket = chars.indexOf(">");
                if (closeBracket != -1) {
                    CharSequence tag = chars.subSequence(0, closeBracket + 1);
                    Matcher sizem = PATTERN_SIZE.matcher(tag);
                    if (sizem.find()) {
                        try {
                            lastAttachmentSize = Long.parseLong(sizem.group(1));
                        } catch (NumberFormatException e) {
                        }
                    }
                    Matcher idm = PATTERN_ID.matcher(tag);
                    if (idm.find()) {
                        lastid = idm.group(1);
                    }
                    if (lastAttachmentSize != null && lastid != null) {
                        mode = Mode.DATA;
                        last = saver.createChannel(lastid, lastAttachmentSize);
                        destchannel = last.openForWriting();
                        SelectableChannel destinationSel = last.getSelectableForWriting();
                        if (destinationSel != null) {
                            selector = Selector.open();
                            destinationSel.register(selector, SelectionKey.OP_WRITE);
                        } else {
                            selector = null;
                        }
                        checkbuffer = true;
                    }
                    buffer.limit(buffer.position());
                    buffer.position(closeBracket + 1);
                    buffer.compact();
                }
            }
            if (!checkbuffer && actuallyRead == 0 && origpos >= remaining) {
                return remaining;
            }
            return actuallyRead + origpos - buffer.position();
        } else if (mode == Mode.DATA) {
            buffer.flip();
            int origLimit = buffer.limit();
            if (lastAttachmentReceived + buffer.remaining() > lastAttachmentSize) {
                buffer.limit((int) (lastAttachmentSize - lastAttachmentReceived));
            }
            int wrote = destchannel.write(buffer);
            if (wrote == -1) {
                return -1;
            }
            lastAttachmentReceived += wrote;
            eventPost.fireEvent(new ReceivingAttachmentEvent(transferred + wrote, transferred + remaining, lastAttachmentReceived, last));
            if (lastAttachmentReceived >= lastAttachmentSize) {
                eventPost.fireEvent(new ReceivedAttachmentEvent(lastid, lastAttachmentSize, last));
                mode = Mode.TAG;
                lastid = null;
                lastAttachmentReceived = 0;
                lastAttachmentSize = null;
                last = null;
                try {
                    destchannel.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Error closing attachment saver", e);
                }
                destchannel = null;
                if (selector != null) {
                    try {
                        selector.close();
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Error closing attachment NIO selector", e);
                    }
                }
                checkbuffer = true;
            }
            if (wrote != read) checkbuffer = true;
            buffer.limit(origLimit);
            buffer.compact();
            return wrote;
        } else {
            throw new IllegalStateException("Unknown mode " + mode);
        }
    }

    protected void cleanUp() throws IOException {
        if (mode == Mode.MESSAGE) {
            msgBuffer.write(buffer.array(), 0, buffer.position());
            String msg = new String(msgBuffer.toByteArray(), charset);
            eventPost.fireEvent(new ReceivedMessageEvent(msg, autoResponse));
        } else if (mode == Mode.DATA) {
        }
        if (selector != null) {
            selector.close();
        }
    }

    protected int getSelectionKey() {
        return SelectionKey.OP_READ;
    }

    private static enum Mode {

        MESSAGE, TAG, DATA, DRAIN
    }
}

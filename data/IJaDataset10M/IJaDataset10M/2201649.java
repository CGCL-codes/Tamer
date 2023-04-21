package org.pz.net;

import org.pz.net.shared.Command;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import static org.pz.net.util.BufferHelper.*;

/**
 *
 * @author Jannek
 */
public class Other {

    private int id;

    private int key;

    private SocketAddress address;

    private ByteBuffer data;

    private short sendId;

    private Message outbox;

    private Message outboxTail;

    private Message inbox;

    private Message inboxTail;

    private Message pending;

    private long receivedTick;

    private long confirmedTick;

    private int memorySize = 256;

    private long[] received;

    private long[] confirmed;

    protected Other(int otherId, int myId, int key, SocketAddress address) {
        this.id = otherId;
        this.key = key;
        this.address = address;
        sendId = 1;
        data = ByteBuffer.allocate(512);
        data.clear();
        put(Command.HELLO, data);
        data.putInt(myId);
        data.putInt(key);
        data.putShort(sendId);
        received = new long[memorySize];
        confirmed = new long[memorySize];
        receivedTick = memorySize;
        confirmedTick = memorySize;
        outbox = Message.get();
        outboxTail = outbox;
        inbox = Message.get();
        inboxTail = inbox;
        pending = Message.get();
    }

    public int getId() {
        return id;
    }

    public boolean isKey(int key) {
        return this.key == key;
    }

    protected void destroy() {
        id = -1;
    }

    public void enqueue(Message message) {
        message.finalize(sendId, ++sendId);
        message.next = outbox.next;
        outboxTail.next = message;
        outboxTail = message;
    }

    public Message getMessage() {
        if (inbox == inboxTail) return null;
        Message message = inbox.next;
        inbox.next = message.next;
        message.next = null;
        if (message == inboxTail) inbox = inboxTail;
        return message;
    }

    private void confirmSendPackage(short packageId) {
        for (Message toDelete, current = outbox; current.next != null; current = current.next) {
            if (current.next.id == packageId) {
                toDelete = current.next;
                current.next = toDelete.next;
                if (current.next == outboxTail) outboxTail = current;
                toDelete.dispose();
                break;
            }
        }
    }

    private void clearData(Command command) {
        data.clear();
        put(command, data);
        data.position(9);
    }

    public void send(DatagramChannel channel) throws IOException {
        clearData(Command.FROM_USER_MULTI);
        for (Message send = outbox.next; send != null; send = send.next) {
            data.putShort(send.id);
            data.putShort(send.required);
            data.putShort((short) send.data.remaining());
            data.put(send.data);
            send.data.flip();
            if (send.next == null || data.remaining() < send.next.data.remaining() + 6) {
                data.flip();
                channel.send(data, address);
                clearData(Command.FROM_USER_MULTI);
            }
        }
    }

    private boolean hasReceivedPackage(short packageId) {
        int index = packageId % memorySize;
        if (index < 0) index += memorySize;
        if (received[packageId % memorySize] - receivedTick < memorySize / 2) return true;
        received[packageId % memorySize] = ++receivedTick;
        return false;
    }

    private boolean hasConfirmed(short packageId) {
        int index = packageId % memorySize;
        if (index < 0) index += memorySize;
        return confirmed[packageId % memorySize] - confirmedTick < memorySize / 2;
    }

    private void confirmReceivedPackage(short packageId) {
        int index = packageId % memorySize;
        if (index < 0) index += memorySize;
        confirmed[packageId % memorySize] = ++confirmedTick;
        updatePending(packageId);
    }

    private void updatePending(short confirmedId) {
        for (Message toConfirm, current = pending; current.next != null; current = current.next) {
            if (current.next.required == confirmedId) {
                toConfirm = current.next;
                current.next = toConfirm.next;
                toConfirm.next = null;
                confirmedId = toConfirm.id;
                inboxTail.next = toConfirm;
                inboxTail = toConfirm;
                confirmReceivedPackage(confirmedId);
            }
        }
    }

    public void receive(ByteBuffer input, DatagramChannel channel) throws IOException {
        input.rewind();
        Command command = getCommand(input);
        if (input.getInt() != id || input.getInt() != key) return;
        switch(command) {
            case FROM_USER_MULTI:
                short packageId;
                short requiredId;
                short size;
                Message message;
                int limit = input.limit();
                clearData(Command.CONFIRM);
                while (input.remaining() >= 6) {
                    packageId = input.getShort();
                    requiredId = input.getShort();
                    size = input.getShort();
                    if (!hasReceivedPackage(packageId)) {
                        input.limit(input.position() + size);
                        message = Message.get();
                        message.data.put(input);
                        message.finalize(requiredId, packageId);
                        input.limit(limit);
                        if (hasConfirmed(requiredId)) {
                            inboxTail.next = message;
                            inboxTail = message;
                            confirmReceivedPackage(packageId);
                        } else {
                            message.next = pending.next;
                            pending.next = message;
                        }
                    }
                    data.putShort(packageId);
                }
                if (data.position() > 9) {
                    data.flip();
                    channel.send(data, address);
                }
                break;
            case CONFIRM:
                while (input.remaining() >= 2) confirmSendPackage(input.getShort());
                break;
        }
    }
}

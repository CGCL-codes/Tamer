package org.simpleframework.util.packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueCompressor implements Compressor {

    private final BlockingQueue<Packet> queue;

    public QueueCompressor() {
        this.queue = new LinkedBlockingQueue<Packet>();
    }

    public Packet compress() throws Exception {
        Packet packet = queue.peek();
        if (packet == null) {
            return packet;
        }
        int length = packet.length();
        if (length == 0) {
            packet.close();
            queue.take();
            return null;
        }
        return new Closer(packet);
    }

    public Packet compress(Packet packet) throws Exception {
        boolean update = queue.offer(packet);
        if (!update) {
            throw new PacketException("Could not compress packet");
        }
        return compress();
    }

    public int length() throws Exception {
        int count = 0;
        for (Packet packet : queue) {
            count += packet.length();
        }
        return count;
    }

    private class Closer extends FilterPacket {

        public Closer(Packet packet) {
            super(packet);
        }

        @Override
        public void close() throws Exception {
            Packet top = queue.peek();
            if (top != packet) {
                throw new PacketException("Close out of sequence");
            }
            packet.close();
            queue.take();
        }
    }
}

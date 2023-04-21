package net.sf.ij_plugins.filters;

import java.util.Arrays;

/**
 * @author Jarek Sacha
 * @version $Revision: 1.1 $
 */
public class RunningMedianRBTOperator implements IRunningMedianFloatOperator {

    private Packet[] packets;

    private int updatablePacket = 0;

    private final RedBlackTreeFloat rankTree = new RedBlackTreeFloat();

    public RunningMedianRBTOperator() {
    }

    @Override
    public void reset(final int maxPackets, final int maxElementsPerPacket) {
        packets = new Packet[maxPackets];
        for (int i = 0; i < packets.length; ++i) {
            packets[i] = new Packet(maxElementsPerPacket);
        }
        clear();
    }

    @Override
    public void push(final int length, final float[] data) {
        final Packet packet = packets[updatablePacket];
        if (length < 0 || length > packet.data.length) {
            throw new IllegalArgumentException("Argument 'length' out of range, got " + length + ", the range is [" + 0 + "," + packet.data.length + "].");
        }
        if (data == null && length != 0) {
            throw new IllegalArgumentException("Argument 'data' cannot be 'null' when argument " + "'length' is non zero.");
        }
        if (data.length < length) {
            throw new IllegalArgumentException("Size of argument 'data' cannot be less than " + "value of argument 'length'.");
        }
        for (int i = 0; i < packet.size; ++i) {
            final float v = packet.data[i];
            if (!rankTree.remove(v)) {
                throw new RuntimeException("Algorithm bug: internal data inconsistency.");
            }
        }
        for (int i = 0; i < length; ++i) {
            final float v = data[i];
            packet.data[i] = v;
            rankTree.insert(v);
        }
        packet.size = length;
        updatablePacket = (updatablePacket + 1) % packets.length;
    }

    @Override
    public float evaluate() {
        final int medianRank = rankTree.size() / 2 + 1;
        return rankTree.select(medianRank);
    }

    @Override
    public void clear() {
        updatablePacket = 0;
        rankTree.clear();
        rankTree.verify();
        for (final Packet packet : packets) {
            packet.size = 0;
            Arrays.fill(packet.data, 0);
        }
    }

    private static class Packet {

        int size;

        final float[] data;

        Packet(final int maxSize) {
            data = new float[maxSize];
        }
    }
}

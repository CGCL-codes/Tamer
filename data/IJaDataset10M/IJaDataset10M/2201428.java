package jpar2.files;

import java.util.Iterator;
import jpar2.packets.Packet;

public class PacketIterator implements Iterator<Packet> {

    private ParityFile parityFile;

    private long position = 0;

    public PacketIterator(ParityFile parityFile) {
        setParityFile(parityFile);
    }

    private void setParityFile(ParityFile parityFile) {
        if (parityFile == null) throw new NullPointerException("ParityFile cannot be null");
        this.parityFile = parityFile;
    }

    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Packet next() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove packet is not supported.");
    }
}

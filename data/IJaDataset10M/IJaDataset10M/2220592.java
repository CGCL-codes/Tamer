package net.kano.joustsim.oscar.oscar.service.icbm.dim;

import net.kano.joscar.ByteBlock;
import org.jetbrains.annotations.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.WritableByteChannel;

public class OutgoingMemoryAttachment extends MemoryAttachment {

    private final ByteBlock readData;

    public OutgoingMemoryAttachment(String id, ByteBlock readData) {
        super(id, readData.getLength());
        this.readData = readData;
    }

    public WritableByteChannel openForWriting() throws IOException {
        throw new IllegalStateException("Cannot be opened for writing");
    }

    @Nullable
    public SelectableChannel getSelectableForWriting() {
        return null;
    }

    public ReadableByteChannel openForReading() throws FileNotFoundException {
        return Channels.newChannel(ByteBlock.createInputStream(readData));
    }

    public ByteBlock getBuffer() {
        return readData;
    }
}

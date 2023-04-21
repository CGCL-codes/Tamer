package net.sf.joafip.heapfile.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.heapfile.record.entity.DataRecordIdentifier;
import net.sf.joafip.heapfile.service.HeapException;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@DoNotTransform
public class BlockDataManagerHeader implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8579792598621073099L;

    private int blockLength;

    private DataRecordIdentifier nextIdentifier;

    private long dataLength;

    private int numberOfDataRecord;

    public int getBlockLength() {
        return blockLength;
    }

    public void setBlockLength(final int blockLength) {
        this.blockLength = blockLength;
    }

    public DataRecordIdentifier getNextIdentifier() {
        return nextIdentifier;
    }

    public void setNextIdentifier(final DataRecordIdentifier nextIdentifier) {
        this.nextIdentifier = nextIdentifier;
    }

    public long getDataLength() {
        return dataLength;
    }

    public void setDataLength(final long dataLength) {
        this.dataLength = dataLength;
    }

    public int getNumberOfDataRecord() {
        return numberOfDataRecord;
    }

    public void setNumberOfDataRecord(final int numberOfDataRecord) {
        this.numberOfDataRecord = numberOfDataRecord;
    }

    public void decrementNumberOfDataRecord() {
        numberOfDataRecord--;
    }

    public void incrementNumberOfDataRecord() {
        numberOfDataRecord++;
    }

    public void set(final byte[] data) throws HeapException {
        try {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            final BlockDataManagerHeader header = (BlockDataManagerHeader) objectInputStream.readObject();
            objectInputStream.close();
            set(header);
        } catch (IOException exception) {
            throw new HeapException(exception);
        } catch (ClassNotFoundException exception) {
            throw new HeapException(exception);
        }
    }

    private void set(final BlockDataManagerHeader header) {
        this.dataLength = header.dataLength;
        this.nextIdentifier = header.nextIdentifier;
        this.blockLength = header.blockLength;
        this.numberOfDataRecord = header.numberOfDataRecord;
    }

    public byte[] get() throws HeapException {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException exception) {
            throw new HeapException(exception);
        }
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeInt(blockLength);
        out.writeLong(dataLength);
        out.writeLong(nextIdentifier.value);
        out.writeInt(numberOfDataRecord);
    }

    private void readObject(final ObjectInputStream input) throws IOException, ClassNotFoundException {
        blockLength = input.readInt();
        dataLength = input.readLong();
        final long value = input.readLong();
        nextIdentifier = new DataRecordIdentifier(value);
        numberOfDataRecord = input.readInt();
    }
}

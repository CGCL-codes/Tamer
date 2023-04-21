package net.sf.joafip.heapfile.entity;

import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class DataAndDataRecordIdentifier {

    private final DataRecordIdentifier dataRecordIdentifier;

    private final byte[] data;

    public DataAndDataRecordIdentifier(final DataRecordIdentifier dataRecordIdentifier, final byte[] data) {
        super();
        this.dataRecordIdentifier = dataRecordIdentifier;
        this.data = data;
    }

    public DataRecordIdentifier getDataRecordIdentifier() {
        return dataRecordIdentifier;
    }

    public byte[] getData() {
        return data;
    }
}

package org.jcvi.common.core.seq.read.trace.sanger.chromat.ab1.tag;

import org.jcvi.common.core.io.IOUtil;
import org.jcvi.common.core.seq.read.trace.sanger.chromat.ab1.AbiUtil;

public abstract class AbstractTaggedDataRecord<T, D> implements TaggedDataRecord<T, D> {

    private final TaggedDataName name;

    private final long tagNumber;

    private final TaggedDataType dataType;

    private final int elementLength;

    private final long numberOfElements;

    private final long recordLength;

    private final long dataRecord;

    private final long crypticValue;

    public AbstractTaggedDataRecord(TaggedDataName name, long number, TaggedDataType dataType, int elementLength, long numberOfElements, long recordLength, long dataRecord, long crypticValue) {
        this.name = name;
        this.tagNumber = number;
        this.dataType = dataType;
        this.elementLength = elementLength;
        this.numberOfElements = numberOfElements;
        this.recordLength = recordLength;
        this.dataRecord = dataRecord;
        this.crypticValue = crypticValue;
    }

    @Override
    public long getCrypticValue() {
        return crypticValue;
    }

    @Override
    public long getDataRecord() {
        return dataRecord;
    }

    @Override
    public TaggedDataType getDataType() {
        return dataType;
    }

    @Override
    public int getElementLength() {
        return elementLength;
    }

    @Override
    public TaggedDataName getTagName() {
        return name;
    }

    @Override
    public long getTagNumber() {
        return tagNumber;
    }

    @Override
    public long getNumberOfElements() {
        return numberOfElements;
    }

    @Override
    public long getRecordLength() {
        return recordLength;
    }

    @Override
    public D parseDataRecordFrom(byte[] ab1DataBlock) {
        if (recordLength < 5) {
            return parseDataFrom(IOUtil.convertUnsignedIntToByteArray(dataRecord));
        }
        byte[] data = new byte[(int) recordLength];
        System.arraycopy(ab1DataBlock, (int) (dataRecord - AbiUtil.HEADER_SIZE), data, 0, data.length);
        return parseDataFrom(data);
    }

    protected abstract D parseDataFrom(byte[] data);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (crypticValue ^ (crypticValue >>> 32));
        result = prime * result + (int) (dataRecord ^ (dataRecord >>> 32));
        result = prime * result + dataType.hashCode();
        result = prime * result + elementLength;
        result = prime * result + name.hashCode();
        result = prime * result + (int) (tagNumber ^ (tagNumber >>> 32));
        result = prime * result + (int) (numberOfElements ^ (numberOfElements >>> 32));
        result = prime * result + (int) (recordLength ^ (recordLength >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TaggedDataRecord)) {
            return false;
        }
        TaggedDataRecord other = (TaggedDataRecord) obj;
        if (!name.equals(other.getTagName())) {
            return false;
        }
        if (tagNumber != other.getTagNumber()) {
            return false;
        }
        if (!dataType.equals(other.getDataType())) {
            return false;
        }
        if (elementLength != other.getElementLength()) {
            return false;
        }
        if (numberOfElements != other.getNumberOfElements()) {
            return false;
        }
        if (recordLength != other.getRecordLength()) {
            return false;
        }
        if (dataRecord != other.getDataRecord()) {
            return false;
        }
        if (crypticValue != other.getCrypticValue()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s[%s(%d) %d]", this.getClass().getName(), name, tagNumber, dataRecord);
    }
}

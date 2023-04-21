package ar.com.oddie.persistence.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RandomAccessRecord implements Record<Long> {

    private RandomAccessRecordHeader header;

    private Serializable data;

    public RandomAccessRecord() {
    }

    public RandomAccessRecord(Long id, int size, Serializable data) {
        this.header = new RandomAccessRecordHeader(id, size);
        this.data = data;
    }

    public RandomAccessRecord(RandomAccessRecordHeader header, Serializable data) {
        this.header = header;
        this.data = data;
    }

    @Override
    public Long getId() {
        return this.header.getId();
    }

    @Override
    public void setId(Long id) {
        this.header.setId(id);
    }

    public RandomAccessRecordHeader getHeader() {
        return header;
    }

    public void setHeader(RandomAccessRecordHeader header) {
        this.header = header;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    @Override
    public boolean equals(Record<Long> record) {
        if (record instanceof RandomAccessRecord) {
            RandomAccessRecord randomAccessRecord = (RandomAccessRecord) record;
            return this.header.getId().equals(randomAccessRecord.header.getId());
        }
        return super.equals(record);
    }
}

package org.epoline.phoenix.indexing.shared;

import java.io.Serializable;
import java.sql.Date;
import org.epoline.phoenix.common.shared.AbstractTableDescriptor;
import org.epoline.phoenix.common.shared.Item;
import org.epoline.phoenix.common.shared.ItemLocation;
import org.epoline.phoenix.common.shared.TableDescriptor;
import org.epoline.phoenix.common.shared.Util;

public class ItemBoxBatch extends Item {

    public static class Id implements Serializable {

        private Date date;

        private int sequenceNumber;

        private String location;

        private String id;

        private static final int ID_LENGTH = 14;

        public Id(String location, Date date, int sequenceNumber) {
            if (date == null) {
                throw new NullPointerException();
            }
            if (!isValidLocation(location) || !isValidSequenceNumber(sequenceNumber)) {
                throw new IllegalArgumentException();
            }
            this.location = location;
            this.date = date;
            this.sequenceNumber = sequenceNumber;
            id = formatId();
        }

        public Id(String id) {
            parseId(id);
            if (!isValidLocation(getLocation())) {
                throw new IllegalArgumentException();
            }
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getLocation() {
            return location;
        }

        public Date getDate() {
            return (Date) date.clone();
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }

        public void parseId(String id) {
            if (id.length() != ID_LENGTH && id.trim().length() != ID_LENGTH) {
                throw new IllegalArgumentException();
            }
            location = id.substring(0, 2);
            date = Util.parseDmsDate(id.substring(2, 10));
            sequenceNumber = Integer.parseInt(id.substring(10, 14));
        }

        public String formatId() {
            String numberString = "0000" + String.valueOf(sequenceNumber);
            numberString = numberString.substring(numberString.length() - 4);
            return location + Util.formatDmsDate(date) + numberString;
        }

        private static boolean isValidLocation(String location) {
            if (location == null) {
                throw new NullPointerException();
            }
            return location.length() == 2 && location.trim().length() == 2;
        }

        private static boolean isValidSequenceNumber(int sequenceNumber) {
            return sequenceNumber >= 0 && sequenceNumber < 10000;
        }

        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!obj.getClass().equals(this.getClass())) {
                return false;
            }
            Id other = (Id) obj;
            return other.getSequenceNumber() == this.getSequenceNumber() && other.getLocation().equals(this.getLocation()) && other.getDate().equals(this.getDate());
        }

        public Object clone() throws CloneNotSupportedException {
            return new Id(getLocation(), getDate(), getSequenceNumber());
        }

        public static final int getIdLength() {
            return ID_LENGTH;
        }
    }

    private final Id id;

    private ItemLocation location;

    private final ItemBoxDetails boxDetails;

    public ItemBoxBatch(String id) {
        this(new Id(id), null, null);
    }

    public ItemBoxBatch(Id id, ItemLocation location, ItemBoxDetails boxDetails) {
        super();
        if (id == null) {
            throw new NullPointerException();
        }
        this.id = id;
        this.location = location;
        this.boxDetails = boxDetails;
    }

    public Object clone() throws CloneNotSupportedException {
        return new ItemBoxBatch((Id) getIdObject().clone(), location, boxDetails);
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        ItemBoxBatch other = (ItemBoxBatch) obj;
        return other.getId().equals(this.getId());
    }

    public ItemBoxDetails getBoxDetails() {
        return boxDetails;
    }

    public java.lang.String getId() {
        return id.getId();
    }

    public static final int getIdLength() {
        return Id.getIdLength();
    }

    public Id getIdObject() {
        return id;
    }

    public ItemLocation getLocation() {
        return location;
    }

    public TableDescriptor getTableDescriptor() {
        return new AbstractTableDescriptor() {

            private String[] COLUMNS = { "BatchId" };

            public Object getField(Item item, int column) {
                ItemBoxBatch batch = (ItemBoxBatch) item;
                if (batch != null) {
                    switch(column) {
                        case 0:
                            return batch.getIdObject().getId();
                    }
                }
                return null;
            }

            public Class getColumnClass(int column) {
                switch(column) {
                    case 0:
                        return String.class;
                }
                return null;
            }

            public String[] getColumnNames() {
                return COLUMNS;
            }
        };
    }

    public boolean isValid() {
        return true;
    }

    public static boolean isValidId(String id) {
        try {
            new Id(id);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public void setLocation(ItemLocation newValue) {
        if (newValue == null) {
            throw new NullPointerException();
        }
        if (!newValue.isValid()) {
            throw new IllegalArgumentException("Invalid newLocation: " + newValue);
        }
        ItemLocation oldValue = location;
        location = newValue;
        firePropertyChange("location", oldValue, newValue);
    }

    public String toString() {
        return id.getId();
    }
}

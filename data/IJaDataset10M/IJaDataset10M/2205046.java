package org.ozoneDB.core.storage.gammaStore;

import java.util.Comparator;

public final class FreeSpace {

    private static Comparator sizeComparator = new Comparator() {

        public int compare(Object o1, Object o2) {
            return compare((FreeSpace) o1, (FreeSpace) o2);
        }

        private int compare(FreeSpace o1, FreeSpace o2) {
            if (o1 == o2) {
                return 0;
            }
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            int result = o1.getSize() - o2.getSize();
            if (result == 0) {
                result = o1.getDataFileId() - o2.getDataFileId();
                if (result == 0) {
                    result = o1.getLocation() - o2.getLocation();
                }
            }
            return result;
        }
    };

    public static Comparator getSizeComparator() {
        return sizeComparator;
    }

    private int dataFileId;

    private int location;

    private int size;

    public FreeSpace(int dataFileId, int location, int size) {
        this.dataFileId = dataFileId;
        this.location = location;
        this.size = size;
    }

    public int getDataFileId() {
        return dataFileId;
    }

    public int getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public int hashCode() {
        return (dataFileId + 1) * location;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String toString() {
        return "FreeSpace(dataFileId: " + getDataFileId() + "; location: " + getLocation() + "; size: " + getSize() + ")";
    }
}

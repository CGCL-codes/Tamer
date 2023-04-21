package net.sf.rcpforms.experimenting.rcp_base.dnd;

public class DnDOperationTypeSet {

    private long bits = 0;

    private static final int max = DnDOperationType.values().length;

    public DnDOperationTypeSet(final DnDOperationType... dndTypes) {
        super();
        for (final DnDOperationType dndType : dndTypes) {
            bits |= 1 << dndType.ordinal();
        }
    }

    public void remove(final DnDOperationType type) {
        bits &= ~(1 << type.ordinal());
    }

    public boolean contains(final DnDOperationType type) {
        return (bits & (1 << type.ordinal())) != 0;
    }

    public DnDOperationType[] toArray() {
        final DnDOperationType[] array = new DnDOperationType[max];
        int index = 0;
        for (final DnDOperationType type : DnDOperationType.values()) {
            if (contains(type)) {
                array[index++] = type;
            }
        }
        final DnDOperationType[] result = new DnDOperationType[index];
        System.arraycopy(array, 0, result, 0, index);
        return result;
    }

    @Override
    public int hashCode() {
        return (int) bits;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof DnDOperationTypeSet)) {
            return false;
        }
        final DnDOperationTypeSet other = (DnDOperationTypeSet) obj;
        return other.bits == bits;
    }
}

package annone.local.linker;

class LocalInfo extends ReferenceInfo {

    private final long typeId;

    private final int ordinal;

    public LocalInfo(String referenceId, long typeId, int ordinal) {
        super(referenceId);
        this.typeId = typeId;
        this.ordinal = ordinal;
    }

    public long getTypeId() {
        return typeId;
    }

    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public String toString() {
        return typeId + "@" + ordinal;
    }
}

package ti.targetinfo.symtable.bfdw;

public final class bfd_link_discard {

    public static final bfd_link_discard discard_sec_merge = new bfd_link_discard("discard_sec_merge");

    public static final bfd_link_discard discard_none = new bfd_link_discard("discard_none");

    public static final bfd_link_discard discard_l = new bfd_link_discard("discard_l");

    public static final bfd_link_discard discard_all = new bfd_link_discard("discard_all");

    public final int swigValue() {
        return swigValue;
    }

    public String toString() {
        return swigName;
    }

    public static bfd_link_discard swigToEnum(int swigValue) {
        if (swigValue < swigValues.length && swigValues[swigValue].swigValue == swigValue) return swigValues[swigValue];
        for (int i = 0; i < swigValues.length; i++) if (swigValues[i].swigValue == swigValue) return swigValues[i];
        throw new IllegalArgumentException("No enum " + bfd_link_discard.class + " with value " + swigValue);
    }

    private bfd_link_discard(String swigName) {
        this.swigName = swigName;
        this.swigValue = swigNext++;
    }

    private bfd_link_discard(String swigName, int swigValue) {
        this.swigName = swigName;
        this.swigValue = swigValue;
        swigNext = swigValue + 1;
    }

    private static bfd_link_discard[] swigValues = { discard_sec_merge, discard_none, discard_l, discard_all };

    private static int swigNext = 0;

    private final int swigValue;

    private final String swigName;
}

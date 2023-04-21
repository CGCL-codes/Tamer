package net.sf.refactorit.ui;

import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.source.format.BinFormatter;

public class BinCITypeRefWrapper implements Comparable {

    BinTypeRef ref;

    public BinCITypeRefWrapper(BinTypeRef ref) {
        this.ref = ref;
    }

    public int compareTo(Object obj) {
        if (!(obj instanceof BinCITypeRefWrapper)) {
            return -1;
        }
        int result = BinFormatter.formatQualified(ref).compareTo(BinFormatter.formatQualified(((BinCITypeRefWrapper) obj).ref));
        return result;
    }

    public String toString() {
        return BinFormatter.formatQualified(ref);
    }

    public BinTypeRef getItem() {
        return ref;
    }
}

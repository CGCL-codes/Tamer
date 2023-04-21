package net.sf.refactorit.ui.audit.numericliterals;

import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinPackage;
import net.sf.refactorit.ui.treetable.BinTreeTableNode;
import java.util.Comparator;

public class ProjectTreeNodesComparator implements Comparator {

    public static final ProjectTreeNodesComparator instance = new ProjectTreeNodesComparator();

    private static final int PUBLIC = 0;

    private static final int PROTECTED = 10;

    private static final int PACKAGE_PRIVATE = 20;

    private static final int PRIVATE = 30;

    private static final int PACKAGE = 0;

    private static final int CLASS = 10;

    private static final int MEMBER = 20;

    private ProjectTreeNodesComparator() {
    }

    public int compare(Object o1, Object o2) {
        BinTreeTableNode node1 = (BinTreeTableNode) o1;
        BinTreeTableNode node2 = (BinTreeTableNode) o2;
        Object bin1 = node1.getBin();
        Object bin2 = node2.getBin();
        int res = 0;
        if (bin1 instanceof BinMember && bin2 instanceof BinMember) {
            res = getAccessValueFor((BinMember) bin1) - getAccessValueFor((BinMember) bin2);
        }
        if (res == 0) {
            res = getTypeValueFor(bin1) - getTypeValueFor(bin2);
        }
        if (res == 0) {
            res = node1.getDisplayName().compareTo(node2.getDisplayName());
        }
        return res;
    }

    private static int getAccessValueFor(BinMember member) {
        if (member.isPublic()) {
            return PUBLIC;
        }
        if (member.isPrivate()) {
            return PRIVATE;
        }
        if (member.isPackagePrivate()) {
            return PACKAGE_PRIVATE;
        }
        if (member.isProtected()) {
            return PROTECTED;
        }
        return 0;
    }

    private static int getTypeValueFor(Object bin) {
        if (bin instanceof BinCIType) {
            return CLASS;
        }
        if (bin instanceof BinMember) {
            return MEMBER;
        }
        if (bin instanceof BinPackage) {
            return PACKAGE;
        }
        return 0;
    }
}

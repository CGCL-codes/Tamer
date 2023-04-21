package net.sf.refactorit.refactorings;

import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinModifier;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.query.usage.InvocationData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Class for deteremining optimal field visiblity.
 *
 * @author  tanel
 */
public final class MemberVisibilityAnalyzer {

    final BinMember member;

    List innerClasses;

    /** Creates a new instance of FieldAccessAnalyzer */
    public MemberVisibilityAnalyzer(BinMember member) {
        this.member = member;
    }

    /**
   * Finds optimal visibility of the member after a refactoring.
   *
   * @param allUsages all usages of a field before refactoring
   * @param replacedUsages all usages of a field after the refactoring, e.g
   *   Encapsulate Field or replacing constructor with a factory method
   **/
    public final int getPosterioriFieldAccess(List allUsages, List replacedUsages) {
        List remaining = new ArrayList(allUsages);
        remaining.removeAll(replacedUsages);
        return getOptimalVisibility(remaining);
    }

    /**
   * Finds optimal visibility of a member given all member usages.
   *
   * @param usages a list of {@link InvocationData} objects
   * @return optimal member visibility (@list BinModifier} constant
   */
    public final int getOptimalVisibility(final List usages) {
        int result = BinModifier.PRIVATE;
        for (Iterator i = usages.iterator(); i.hasNext(); ) {
            InvocationData data = (InvocationData) i.next();
            BinTypeRef invokedIn = data.getWhereType();
            result = checkLocation(invokedIn, result);
        }
        return result;
    }

    /**
   * To check manually those places which are not yet in usages list, but will
   * appear only after the refactoring.
   * @param invokedIn location to check like if member is invoked there
   * @param visibility determined earlier visibility
   * @return new minimal visibility
   */
    public final int checkLocation(final BinTypeRef invokedIn, int visibility) {
        if (!invokedIn.equals(getMember().getOwner()) && !getInnerClasses().contains(invokedIn)) {
            if (Arrays.asList(invokedIn.getSupertypes()).contains(getMember().getOwner())) {
                if ((visibility == BinModifier.PRIVATE) || (visibility == BinModifier.PACKAGE_PRIVATE)) {
                    visibility = BinModifier.PROTECTED;
                }
            } else if (invokedIn.getPackage() == getMember().getOwner().getPackage()) {
                if (visibility == BinModifier.PRIVATE) {
                    visibility = BinModifier.PACKAGE_PRIVATE;
                }
            } else {
                visibility = BinModifier.PUBLIC;
            }
        }
        return visibility;
    }

    public final BinMember getMember() {
        return this.member;
    }

    private List getInnerClasses() {
        if (innerClasses == null) {
            innerClasses = new ArrayList();
            innerClasses.addAll(Arrays.asList(getMember().getOwner().getBinCIType().getDeclaredTypes()));
        }
        return innerClasses;
    }
}

package fi.vtt.noen.testgen.model.daikon.constraints;

import fi.vtt.noen.testgen.parser.DaikonParser;
import fi.vtt.noen.testgen.model.daikon.constraints.DaikonConstraint;
import fi.vtt.noen.testgen.model.daikon.constraints.ReferenceValue;
import java.util.Collection;
import java.util.HashSet;

/**
 * This invariant says that only the given object is contained in an array at any point of
 * program execution. For example, array Clients is ["myclient"] and this never changes at
 * any of the monitored points of program execution.
 *
 * @author Teemu Kanstr�n
 */
public class ArrayElementsConstraint extends DaikonConstraint {

    public ArrayElementsConstraint(String left, String value) {
        this.left = (ReferenceValue) DaikonParser.parseValueObject(left);
        this.right = DaikonParser.parseValueObject(value);
    }

    public Collection<String> arrayNames() {
        Collection<String> names = new HashSet<String>();
        names.add(left.getReferredVariable());
        return names;
    }

    public String toString() {
        return left + " elements == " + valueObjectToString(right);
    }

    public String asAssert(String returnVar) {
        String java = "for (Object o : " + returnVar + ") {" + ln;
        java += "  assertEquals(" + valueObjectToString(right) + ", o);" + ln;
        java += "}" + ln;
        return java;
    }

    protected String toJava() {
        String java = "    Object expected = " + valueObjectToString(right) + ";" + ln;
        java += "    for (Object o : " + left.getReferredVariable() + ") {" + ln;
        String check = createCondition("expected.equals(o)", 1);
        java += check;
        java += "    }" + ln;
        return java;
    }

    protected String guardName() {
        return left.getReferredVariable() + "AreDifferentFrom" + valueObjectToGuardObject(right);
    }
}

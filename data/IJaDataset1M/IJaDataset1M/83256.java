package org.jmlspecs.eclipse.refactor.action.testdata;

import org.jmlspecs.annotation.Invariant;
import org.jmlspecs.annotation.ModelField;
import org.jmlspecs.annotation.Pure;
import org.jmlspecs.jc.JC;

/**
 * Test Case class used as the source of PullUp refactorings.
 * The PullUpRefactoringTest refactors different combinations of these methods.
 * 
 * @author iain
 */
public class PullUpSource extends PullUpDestination {

    @Invariant
    protected boolean jcInvNumber() {
        return (0 <= number) && (number <= 20);
    }

    @ModelField
    public int sizeModel() {
        return JC.someValue();
    }

    @Invariant
    public boolean jcInvSize() {
        return (0 <= sizeModel()) && (sizeModel() < 10);
    }

    @Pure
    public int basicIntMethod() {
        return 1;
    }

    @Invariant
    public boolean jcBasicIntMethod() {
        return 0 < basicIntMethod();
    }
}

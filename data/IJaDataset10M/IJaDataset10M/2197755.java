package fitlibrary.global;

import fitlibrary.closure.LookupClosureStandard;
import fitlibrary.closure.LookupClosure;
import fitlibrary.closure.LookupMethodTarget;
import fitlibrary.closure.LookupMethodTargetStandard;
import fitlibrary.diff.StringDifferencing;
import fitlibrary.diff.StringDifferencingStandard;
import fitlibrary.exception.ExceptionHandling;
import fitlibrary.exception.ExceptionHandlingStandard;

public class PlugBoard {

    public static LookupMethodTarget lookupTarget = new LookupMethodTargetStandard();

    public static ExceptionHandling exceptionHandling = new ExceptionHandlingStandard();

    public static StringDifferencing stringDifferencing = new StringDifferencingStandard();

    public static LookupClosure lookupClosure = new LookupClosureStandard();
}

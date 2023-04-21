package net.sourceforge.refactor4pdt.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "net.sourceforge.refactor4pdt.core.messages";

    public static String PhpRefactoringDelegate_AddDangerousFunctionCall;

    public static String ExtractFunctionConditionChecker_InvalidSelection;

    public static String ExtractFunctionConditionChecker_OnlyOneReturnSupported;

    public static String ExtractFunctionDelegate_BeginTask;

    static {
        NLS.initializeMessages(BUNDLE_NAME, net.sourceforge.refactor4pdt.core.Messages.class);
    }
}

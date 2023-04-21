package com.aptana.ide.io;

import org.eclipse.osgi.util.NLS;

/**
 * @author Kevin Lindsey
 */
public final class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.aptana.ide.io.messages";

    private Messages() {
    }

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /**
	 * FileUtilities_Text_Undefined
	 */
    public static String FileUtilities_Text_Undefined;

    /**
	 * SourceWriter_Offset_Below_Zero
	 */
    public static String SourceWriter_Offset_Below_Zero;

    /**
	 * SourceWriter_Remove_Length_Below_Zero
	 */
    public static String SourceWriter_Remove_Length_Below_Zero;

    /**
	 * SourceWriter_Remove_Beyond_Length
	 */
    public static String SourceWriter_Remove_Beyond_Length;

    /**
	 * TabledInputStream_Input_Undefined
	 */
    public static String TabledInputStream_Input_Undefined;

    /**
	 * TabledInputStream_Incompatible_Format
	 */
    public static String TabledInputStream_Incompatible_Format;

    /**
	 * TabledOutputStream_Output_Undefined
	 */
    public static String TabledOutputStream_Output_Undefined;
}

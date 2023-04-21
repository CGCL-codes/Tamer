package de.lema.client.editor.configuration;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "de.lema.client.editor.configuration.messages";

    public static String ConfigurationEditor_Lbl_DefaultValue;

    public static String ConfigurationEditor_Lbl_Description;

    public static String ConfigurationEditor_Lbl_Key;

    public static String ConfigurationEditor_Lbl_Value;

    public static String ConfigurationEditor_PartName;

    public static String ConfigurationEditor_Text_Configuration;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}

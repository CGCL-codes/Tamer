package ch.fusun.baron.client.ui.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Messages
 */
public class Messages extends NLS {

    private static final String BUNDLE_NAME = "ch.fusun.baron.client.ui.messages.messages";

    /**	 */
    public static String AnnectLandTileAction_Description;

    /**	 */
    public static String CreateCityTileAction_Description;

    /**	 */
    public static String CreateFarmTileAction_Description;

    /**	 */
    public static String CreateUnitTileAction_CreateUnitTitle;

    /**	 */
    public static String CreateUnitTileAction_NumberOfUnitDefault;

    /**	 */
    public static String CreateUnitTileAction_NumberOfUnitsQuestion;

    /**	 */
    public static String CreateUnitTileAction_NumerOfUnitsTitle;

    /**	 */
    public static String CreateUnitTileAction_Warning;

    /**	 */
    public static String CreateUnitTileAction_Warning2;

    /**	 */
    public static String MoveUnitDownTileAction_Description;

    /**	 */
    public static String MoveUnitLeftTileAction_Description;

    /**	 */
    public static String MoveUnitRightTileAction_Description;

    /**	 */
    public static String MoveUnitUpTileAction_Description;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}

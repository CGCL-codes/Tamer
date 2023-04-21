package mumav.i18n;

/** Contains all strings used in the game
*/
public class GuiStrings {

    public final String REPORT_TAB;

    public final String MARKET_TAB;

    public final String REPORT_TAB_DESC;

    public final String MARKET_TAB_DESC;

    public GuiStrings(int lang) {
        if (lang == I18n.DE) {
            REPORT_TAB = "Bericht";
            MARKET_TAB = "Markt";
            REPORT_TAB_DESC = "Zeige Aufkl�rungsbericht";
            MARKET_TAB_DESC = "Zeige Marktangebote";
        } else {
            REPORT_TAB = "";
            MARKET_TAB = "";
            REPORT_TAB_DESC = "";
            MARKET_TAB_DESC = "";
        }
    }
}

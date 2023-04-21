package wsl.mdn.guiconfig;

import wsl.fw.datasource.*;
import wsl.fw.util.Log;

/**
 * DataObject to hold the settings used to customise the login and logout.
 */
public class LoginSettings extends DataObject {

    private static final String _ident = "$Date: 2003/02/10 20:45:43 $  $Revision: 1.2 $ " + "$Archive: /Mobile Data Now/Source/wsl/mdn/guiconfig/LoginSettings.java $ ";

    public static final String ENT_LOGINSETTINGS = "ENT_LOGINSETTINGS";

    public static final String FLD_ID = "FLD_ID";

    public static final String FLD_FLAGS = "FLD_FLAGS";

    public static final String FLD_SPLASHTITLE = "FLD_SPLASHTITLE";

    public static final String FLD_SPLASHTEXT = "FLD_SPLASHTEXT";

    public static final String FLD_LOGINTITLE = "FLD_LOGINTITLE";

    public static final String FLD_LOGINTEXT = "FLD_LOGINTEXT";

    public static final String FLD_USERNAMEPROMPT = "FLD_USERNAMEPROMPT";

    public static final String FLD_PASSWORDPROMPT = "FLD_PASSWORDPROMPT";

    public static final String FLD_LOGOUTTITLE = "FLD_LOGOUTTITLE";

    public static final String FLD_LOGOUTTEXT = "FLD_LOGOUTTEXT";

    public static final int FLAG_USE_SPLASHSCREEN = 1;

    public static final int FLAG_REQUIRE_PASSWORD = 2;

    public static final int FLAG_NUMERIC_PASSWORD = 4;

    /**
     * Default constructor.
     */
    public LoginSettings() {
    }

    /**
     * Static factory method to create the entity to be used by this dataobject
     * and any subclasses. This is called by the DataManager's factory when
     * creating a MENUACTION entity.
     * @return the created entity.
     */
    public static Entity createEntity() {
        Entity ent = new EntityImpl(ENT_LOGINSETTINGS, LoginSettings.class);
        ent.addKeyGeneratorData(new DefaultKeyGeneratorData(ENT_LOGINSETTINGS, FLD_ID));
        ent.addField(new FieldImpl(FLD_ID, Field.FT_INTEGER, Field.FF_SYSTEM_KEY | Field.FF_UNIQUE_KEY));
        ent.addField(new FieldImpl(FLD_FLAGS, Field.FT_INTEGER));
        ent.addField(new FieldImpl(FLD_SPLASHTITLE, Field.FT_STRING, Field.FF_NONE, 50));
        ent.addField(new FieldImpl(FLD_SPLASHTEXT, Field.FT_STRING, Field.FF_NONE, 255));
        ent.addField(new FieldImpl(FLD_LOGINTITLE, Field.FT_STRING, Field.FF_NONE, 50));
        ent.addField(new FieldImpl(FLD_LOGINTEXT, Field.FT_STRING, Field.FF_NONE, 255));
        ent.addField(new FieldImpl(FLD_USERNAMEPROMPT, Field.FT_STRING, Field.FF_NONE, 50));
        ent.addField(new FieldImpl(FLD_PASSWORDPROMPT, Field.FT_STRING, Field.FF_NONE, 50));
        ent.addField(new FieldImpl(FLD_LOGOUTTITLE, Field.FT_STRING, Field.FF_NONE, 50));
        ent.addField(new FieldImpl(FLD_LOGOUTTEXT, Field.FT_STRING, Field.FF_NONE, 255));
        return ent;
    }

    /**
     * Return the Entity name.
     * @return the entity name.
     */
    public String getEntityName() {
        return ENT_LOGINSETTINGS;
    }

    /**
     * Use a fixed name as there is only one and we want a specific name.
     */
    public String toString() {
        return wsl.mdn.admin.MdnAdminGuiManager.NODE_LOGINSETTINGS.getText();
    }

    /**
     * @return the ID
     */
    public Object getId() {
        return getObjectValue(FLD_ID);
    }

    /**
     * Set the ID.
     */
    public void setId(Object id) {
        setValue(FLD_ID, id);
    }

    /**
     * @return the flags.
     */
    public int getFlags() {
        return getIntValue(FLD_FLAGS);
    }

    /**
     * Set the flags.
     */
    public void setFlags(int flags) {
        setValue(FLD_FLAGS, flags);
    }

    /**
     * @param mask, the bit values to test for (usually only test one at a time).
     * @return true if the flags contains ALL the bits specified in the mask.
     */
    public boolean getFlags(int mask) {
        return (getIntValue(FLD_FLAGS) & mask) == mask;
    }

    /**
     * @return the Splashscreen Title.
     */
    public String getSplashTitle() {
        return getStringValue(FLD_SPLASHTITLE);
    }

    /**
     * Set the Splashscreen Title.
     */
    public void setSplashTitle(String title) {
        setValue(FLD_SPLASHTITLE, title);
    }

    /**
     * @return the Splashscreen Text.
     */
    public String getSplashText() {
        return getStringValue(FLD_SPLASHTEXT);
    }

    /**
     * Set the Splashscreen Text.
     */
    public void setSplashText(String text) {
        setValue(FLD_SPLASHTEXT, text);
    }

    /**
     * @return the Login Screen Title.
     */
    public String getLoginTitle() {
        return getStringValue(FLD_LOGINTITLE);
    }

    /**
     * Set the Login Screen Title.
     */
    public void setLoginTitle(String title) {
        setValue(FLD_LOGINTITLE, title);
    }

    /**
     * @return the Login Screen Text.
     */
    public String getLoginText() {
        return getStringValue(FLD_LOGINTEXT);
    }

    /**
     * Set the Login Screen Text.
     */
    public void setLoginText(String text) {
        setValue(FLD_LOGINTEXT, text);
    }

    /**
     * @return the Username prompt.
     */
    public String getUsernamePrompt() {
        return getStringValue(FLD_USERNAMEPROMPT);
    }

    /**
     * Set the Username prompt.
     */
    public void setUsernamePrompt(String prompt) {
        setValue(FLD_USERNAMEPROMPT, prompt);
    }

    /**
     * @return the password prompt.
     */
    public String getPasswordPrompt() {
        return getStringValue(FLD_PASSWORDPROMPT);
    }

    /**
     * Set the password prompt.
     */
    public void setPasswordPrompt(String prompt) {
        setValue(FLD_PASSWORDPROMPT, prompt);
    }

    /**
     * @return the logout title.
     */
    public String getLogoutTitle() {
        return getStringValue(FLD_LOGOUTTITLE);
    }

    /**
     * Set the logout title.
     */
    public void setLogoutTitle(String title) {
        setValue(FLD_LOGOUTTITLE, title);
    }

    /**
     * @return the logout text.
     */
    public String getLogoutText() {
        return getStringValue(FLD_LOGOUTTEXT);
    }

    /**
     * Set the logout text.
     */
    public void setLogoutText(String text) {
        setValue(FLD_LOGOUTTEXT, text);
    }

    /**
     * Get the LoginSettings from the DB or creating if required.
     * Note that this is logically a singleton but must refer to the db as other
     * processes may update it.
     */
    public static LoginSettings getLoginSettings() {
        LoginSettings loginSettings = new LoginSettings();
        DataSource ds = loginSettings.getDataSource();
        Query query = new Query(LoginSettings.ENT_LOGINSETTINGS);
        try {
            RecordSet rs = ds.select(query);
            if (rs.next()) loginSettings = (LoginSettings) rs.getCurrentObject(); else {
                loginSettings = new LoginSettings();
                loginSettings.setLoginText("Login");
                loginSettings.setLogoutText("Logout");
                loginSettings.setPasswordPrompt("Password");
                loginSettings.setSplashText("Welcome to Mobile Data Now");
                loginSettings.setFlags(LoginSettings.FLAG_USE_SPLASHSCREEN | LoginSettings.FLAG_REQUIRE_PASSWORD);
                loginSettings.setUsernamePrompt("User Name");
                loginSettings.save();
            }
        } catch (Exception e) {
            Log.error("LoginSettings.getLoginSettings: " + e.toString());
        }
        return loginSettings;
    }
}

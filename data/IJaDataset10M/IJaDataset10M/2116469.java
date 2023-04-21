package corny.FritzPhoneBook.utils;

import corny.FritzPhoneBook.data.contact.FritzContact.PhoneLabel;
import corny.Preferences.Preferences;
import corny.addressbook.data.MultiValue.MultiValueLabel;

public class PreferencesUtils {

    public static final String APPLICATION_NAME = "Fritz!Sync";

    public static final String AREA_CODE_PREFERENCE = "areaCode";

    public static final String COUNTRY_CODE_PREFERENCE = "countryCode";

    public static final String PREFERRED_PHONE_BOOK_PREFERENCE = "preferredPhoneBook";

    public static final String DEFAULT_EMAIL_LABEL_PREFERENCE = "defaultEmailLabel";

    private static final String PREFERRED_DEFAULT_CALL_PREFERENCE = "preferredDefaultCall";

    public static enum PhoneBookPreference {

        AUTOMATIC("Automatisch"), PREFER_NATIVE("Adressbuch"), PREFER_FRITZ("Fritz-Telefonbuch");

        private final String name;

        private PhoneBookPreference(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static PhoneBookPreference getPreferredPhoneBook() {
        return PhoneBookPreference.values()[Preferences.sharedInstance().getInt(PREFERRED_PHONE_BOOK_PREFERENCE)];
    }

    public static void setPreferredPhoneBook(PhoneBookPreference pref) {
        Preferences.sharedInstance().set(PREFERRED_PHONE_BOOK_PREFERENCE, pref.ordinal());
    }

    public static String getDefaultEmailLabel() {
        return Preferences.sharedInstance().getString(DEFAULT_EMAIL_LABEL_PREFERENCE, MultiValueLabel.HOME.getHumanReadableName());
    }

    public static PhoneLabel getPreferredDefaultCallLabel() {
        return PhoneLabel.values()[Preferences.sharedInstance().getInt(PREFERRED_DEFAULT_CALL_PREFERENCE) + 1];
    }

    public static String getCountryCode() {
        return Preferences.sharedInstance().getString(COUNTRY_CODE_PREFERENCE, "49");
    }

    public static String getAreaCode() {
        return Preferences.sharedInstance().getString(AREA_CODE_PREFERENCE, "1234");
    }
}

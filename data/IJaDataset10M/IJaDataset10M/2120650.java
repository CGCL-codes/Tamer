package edu.columbia.hypercontent.editors.vcard.model;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: Aug 22, 2003
 * Time: 3:59:00 PM
 * To change this template use Options | File Templates.
 */
public interface IVCardConstants {

    public final String BEGIN = "BEGIN";

    public final String END = "END";

    public final String NAME = "NAME";

    public final String PROFILE = "PROFILE";

    public final String SOURCE = "SOURCE";

    public final String FN = "FN";

    public final String N = "N";

    public final String NICKNAME = "NICKNAME";

    public final String PHOTO = "PHOTO";

    public final String BDAY = "BDAY";

    public final String ADR = "ADR";

    public final String LABEL = "LABEL";

    public final String TEL = "TEL";

    public final String EMAIL = "EMAIL";

    public final String MAILER = "MAILER";

    public final String TZ = "TZ";

    public final String GEO = "GEO";

    public final String TITLE = "TITLE";

    public final String ROLE = "ROLE";

    public final String LOGO = "LOGO";

    public final String AGENT = "AGENT";

    public final String ORG = "ORG";

    public final String CATEGORIES = "CATEGORIES";

    public final String NOTE = "NOTE";

    public final String PRODID = "PRODID";

    public final String REV = "REV";

    public final String SORT_STRING = "SORT-STRING";

    public final String SOUND = "SOUND";

    public final String UID = "UID";

    public final String URL = "URL";

    public final String VERSION = "VERSION";

    public final String CLASS = "CLASS";

    public final String KEY = "KEY";

    public final String TYPE = "TYPE";

    public final String ENCODING = "ENCODING";

    public final String VALUE = "VALUE";

    public final String LANGUAGE = "LANGUAGE";

    public final String ADR_DOMESTIC = "dom";

    public final String ADR_INTERNATIONAL = "intl";

    public final String ADR_POSTAL = "postal";

    public final String ADR_PARCEL = "parcel";

    public final String ADR_HOME = "home";

    public final String ADR_WORK = "work";

    public final String ADR_PREFERRED = "pref";

    public final String[] ADR_TYPES = new String[] { ADR_DOMESTIC, ADR_INTERNATIONAL, ADR_POSTAL, ADR_PARCEL, ADR_HOME, ADR_WORK, ADR_PREFERRED };

    public final String[] DEFAULT_ADR_TYPES = new String[] { ADR_INTERNATIONAL, ADR_POSTAL, ADR_PARCEL, ADR_WORK };

    public final String TEL_HOME = "home";

    public final String TEL_MESSAGE = "msg";

    public final String TEL_WORK = "work";

    public final String TEL_VOICE = "voice";

    public final String TEL_CELLULAR = "cell";

    public final String TEL_VIDEO = "video";

    public final String TEL_PAGER = "pager";

    public final String TEL_BBS = "bbs";

    public final String TEL_MODEM = "modem";

    public final String TEL_CAR = "car";

    public final String TEL_ISDN = "isdn";

    public final String TEL_PCS = "pcs";

    public final String TEL_PREFERRED = "pref";

    public final String[] TEL_TYPES = new String[] { TEL_HOME, TEL_MESSAGE, TEL_WORK, TEL_VOICE, TEL_CELLULAR, TEL_VIDEO, TEL_PAGER, TEL_BBS, TEL_MODEM, TEL_CAR, TEL_ISDN, TEL_PCS, TEL_PREFERRED };

    public final String EMAIL_INTERNET = "internet";

    public final String EMAIL_X400 = "x400";

    public final String EMAIL_PREFERRED = "pref";

    public final String BINARY_IMAGE = "image";

    public final String BINARY_AUDIO = "audio";

    public final String[] BINARY_TYPES = new String[] { BINARY_IMAGE, BINARY_AUDIO };

    public final String URI = "uri";

    public final String TEXT = "text";

    public final String VCARD = "VCARD";

    public final String DATE = "date";

    public final String DATE_TIME = "date-time";

    public final String B_ENCODING = "b";

    public final char[] ESCAPE_CHARS = new char[] { ',', ';', ':' };
}

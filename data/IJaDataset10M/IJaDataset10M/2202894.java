package saadadb.util;

import saadadb.collection.Category;
import saadadb.database.Database;

/**
 * This class is a simple dictionnary of regular expressions used in Saada
 * @author michel
 * * @version $Id: RegExp.java 293 2012-03-23 16:52:46Z laurent.mistahl $

 */
public class RegExp {

    public static final String CLASSNAME = "[_a-zA-Z][_a-zA-Z0-9]*";

    public static final String COLLNAME = "[_a-zA-Z][_a-zA-Z0-9]*";

    public static final String FILENAME = "\\w+(?:\\.\\w*)?";

    public static final String FILEPATH = "(?:[A-Za-z]:)?" + Database.getRegexpSepar() + "?(?:" + FILENAME + Database.getRegexpSepar() + ")*" + "\\w+(?:\\.\\w*)?";

    public static final String DBNAME = "[_a-zA-Z][_a-zA-Z0-9]*";

    public static final String EXTATTRIBUTE = "[a-zA-Z][_a-zA-Z0-9]*";

    public static final String PSEUDO_TABLE = "(ANY)|(" + COLLNAME + ")";

    public static final String FORBIDDEN_CLASSNAME = "(?i)((table)|(create)|(alter)|(index))";

    public static final String MAPPING = "(?i)((only)|(first)|(last))";

    public static final String CATEGORY = "(?i)" + Category.buildRegExp();

    public static final String ALLOWED_ERROR_UNITS = "(deg)|(arcmin)|(arcsec)|(mas)|(uas)";

    public static final String REPOSITORY = "(?i)((no)|(move))";

    public static final String FITS_FILE = "(?i)(.*(\\.(((fit|fits)(\\.gz)?)|(ftz|fgz))))$";

    public static final String VOTABLE_FILE = "(?i)(.*(\\.(vot|votable|xml)(\\.gz)?))$";

    public static final String IMAGE_FILE = "(?i)(.*(\\.(gif|jpeg|jpg|png|tiff|bmp)))$";

    public static final String FITS_KEYWORD = "([_\\-a-zA-Z0-9\\s]+)=\\s*";

    public static final String FITS_INT_VAL = "[+\\-]?[0-9]+";

    public static final String FITS_STR_VAL = "'[^']*'";

    public static final String FITS_BOOLEAN_VAL = "[TF]{1}";

    public static final String FITS_COMMENT = "/.*";

    public static final String FITS_FLOAT_NDNEXP = "(?:[0-9]+\\.[0-9]+[Ee][+-]?[0-9]*)";

    public static final String FITS_FLOAT_DNEXP = "(?:\\.[0-9]+[Ee][+-]?[0-9]*)";

    public static final String FITS_FLOAT_NDEXP = "(?:[0-9]+\\.[Ee][+-]?[0-9]*)";

    public static final String FITS_FLOAT_NEXP = "(?:[0-9]+[Ee][+-]?[0-9]+)";

    public static final String FITS_FLOAT_NDN = "(?:[0-9]+\\.[0-9]+)";

    public static final String FITS_FLOAT_DN = "(?:\\.[0-9]+)";

    public static final String FITS_FLOAT_ND = "(?:[0-9]+\\.)";

    public static final String FITS_FLOAT_VAL = "[+\\-]?(?:" + FITS_FLOAT_NDNEXP + "|" + FITS_FLOAT_DNEXP + "|" + FITS_FLOAT_NDEXP + "|" + FITS_FLOAT_NEXP + "|" + FITS_FLOAT_NDN + "|" + FITS_FLOAT_DN + "|" + FITS_FLOAT_ND + ")";

    public static final String NUMERIC = "[+\\-]?(?:" + FITS_FLOAT_NDNEXP + "|" + FITS_FLOAT_DNEXP + "|" + FITS_FLOAT_NDEXP + "|" + FITS_FLOAT_NEXP + "|" + FITS_FLOAT_NDN + "|" + FITS_FLOAT_DN + "|" + FITS_FLOAT_ND + "|" + "[0-9]+)";

    public static final String ONE_COORDINATE = "[+-]?(?:(?:\\.[0-9]+)|(?:[0-9]+\\.?[0-9]*))(?:[eE][+-]?[0-9]+)?";

    public static final String POSITION_COORDINATE = "^(" + ONE_COORDINATE + ")((?:[+-]|(?:[,:;\\s]+[+-]?))" + ONE_COORDINATE + ")$";

    public static final String RA_KW = "(_*ra)|(_*ra.?(2000)?)|(_*ra.?[^(dec)]?)|(_*ra.?obj)";

    public static final String DEC_KW = "(_*de)|(_*dec)|(_*dec.?(2000)?)|(_*de.?(2000)?)|(_*dec.?[^(ra)]?)|(_*dec.?obj)|(_*de.?obj)";

    public static final String SPEC_AXIS = "(?i)((channel)|(wavelength)|(freq)|(frequency)|(spectral_value))";

    public static final String URL = "(http|https):\\/\\/(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?";

    public static final String BIBCODE = "\\d{4}[\\w\\.]{10}\\d{4}\\w";

    public static final String UTYPE = "(?:\\w[\\w;:]*\\.)*(?:\\w[\\w;:]*)";

    public static final String UCD = "(?:(?:(?:\\w+\\.)*\\w+);)*(?:(?:\\w+\\.)*\\w+)";

    public static final String NOT_SET_VALUE = "(NULL)|(NaN)|(Infinity)|(" + Integer.MAX_VALUE + ")";

    /**
	 * Coordinate system
	 */
    public static final String ECL_SYSTEM = "(?i)(ecl_FK5|ecliptic)";

    public static final String FK4_SYSTEM = "(?i)(eq_FK4|FK4)";

    public static final String FK5_SYSTEM = "(?i)(eq_FK5|FK5)";

    public static final String GALACTIC = "(?i)(galactic)";

    public static final String ICRS = "(?i)(icrs)";

    /**
	 * Position kw
	 */
    public static final String FK5_RA_MAINUCD = "(?i)(POS_EQ_RA_MAIN|(pos\\.eq\\.ra;meta\\.main))";

    public static final String FK5_DEC_MAINUCD = "(?i)(POS_EQ_DEC_MAIN|(pos\\.eq\\.dec;meta\\.main))";

    public static final String FK5_RA_UCD = "(?i)(POS_EQ_RA|(pos\\.eq\\.ra))";

    public static final String FK5_DEC_UCD = "(?i)(POS_EQ_DEC|(pos\\.eq\\.dec))";

    public static final String FK5_RA_KW = "(?i)((_*ra)|(_*ra[^Bb]?(2000)?)|(_*ra.?[^(dec)]?)|(_*ra.?obj))";

    public static final String FK5_DEC_KW = "(?i)((_*de)|(_*dec)|(_*dec[^Bb]?(2000)?)|(_*de[^Bb]?(2000)?)|(_*dec.?[^(ra)]?)|(_*dec.?obj)|(_*de.?obj))";

    public static final String FK4_RA_MAINUCD = "(?i)(POS_EQ_RA_MAIN|(pos\\.eq\\.ra;meta\\.main))";

    public static final String FK4_DEC_MAINUCD = "(?i)(POS_EQ_DEC_MAIN|(pos\\.eq\\.dec;meta\\.main))";

    public static final String FK4_RA_UCD = "(?i)(POS_EQ_RA|(pos\\.eq\\.ra))";

    public static final String FK4_DEC_UCD = "(?i)(POS_EQ_DEC|(pos\\.eq\\.dec))";

    public static final String FK4_RA_KW = "(?i)(_*ra[^(dec)]*b1950)";

    public static final String FK4_DEC_KW = "(?i)(_*de[^(ra)]*b1950)";

    public static final String ICRS_RA_MAINUCD = "(?i)(POS_EQ_RA_MAIN|(pos\\.eq\\.ra;meta\\.main))";

    public static final String ICRS_DEC_MAINUCD = "(?i)(POS_EQ_DEC_MAIN|(pos\\.eq\\.dec;meta\\.main))";

    public static final String ICRS_RA_UCD = "(?i)(POS_EQ_RA|(pos\\.eq\\.ra))";

    public static final String ICRS_DEC_UCD = "(?i)(POS_EQ_DEC|(pos\\.eq\\.dec))";

    public static final String ICRS_RA_KW = "(?i)(_*ra[^(dec)*])";

    public static final String ICRS_DEC_KW = "(?i)(_*de[^(ra)]*)";

    public static final String ECLIPTIC_RA_MAINUCD = "(?i)(POS_EC_RA_MAIN|(pos\\.ecliptic\\.lon;meta\\.main))";

    public static final String ECLIPTIC_DEC_MAINUCD = "(?i)(POS_EC_DEC_MAIN|(pos\\.ecliptic\\.lat;meta\\.main))";

    public static final String ECLIPTIC_RA_UCD = "(?i)(POS_EC_RA|(pos\\.ecliptic\\.lon))";

    public static final String ECLIPTIC_DEC_UCD = "(?i)(POS_EC_DEC|(pos\\.ecliptic\\.lat))";

    public static final String ECLIPTIC_RA_KW = "(?i)(_elon\\.*)";

    public static final String ECLIPTIC_DEC_KW = "(?i)(_elat\\.*)";

    public static final String GALACTIC_RA_MAINUCD = "(?i)(POS_GAL_LON_MAIN|(pos\\.galactic\\.lat;meta\\.main))";

    public static final String GALACTIC_DEC_MAINUCD = "(?i)(POS_GAL_LAT|(pos\\.ecliptic\\.dec;meta\\.main))";

    public static final String GALACTIC_RA_UCD = "(?i)(POS_GAL_LON|(pos\\.galactic\\.lon))";

    public static final String GALACTIC_DEC_UCD = "(?i)(POS_GAL_LAT|(pos\\.galactic\\.lat))";

    public static final String GALACTIC_RA_KW = "(?i)(_glon)";

    public static final String GALACTIC_DEC_KW = "(?i)(_glat)";

    public static final String FIST_COOSYS_KW = "(?i)(COO.*SYS|RADECSYS|SYSTEM)";

    public static void main(String[] args) {
        String ra = "__raj2000";
        System.out.println(ra.matches(RegExp.RA_KW));
        String dec = "__decj2000";
        System.out.println(dec.matches(RegExp.DEC_KW));
        String fits = "SW_HD117555_057_A.1.ms.fits";
        System.out.println(fits.matches(RegExp.FITS_FILE));
        System.out.println("SpectralAxis.cov.bounds.extent#stc:double1Type".matches(RegExp.UTYPE));
        System.out.println("SpectralAxis.cov.bounds.extent;stc:double1Type".matches(RegExp.UTYPE));
        System.out.println("SpectralAxis.cov.bounds.extentstcdouble1Type".matches(RegExp.UTYPE));
        System.out.println("0.2".matches(RegExp.NUMERIC));
    }
}

package com.phloc.commons.charset;

import javax.annotation.concurrent.Immutable;
import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * These constants only exist to work around the common file encoding problem
 * with Cp1252/UTF-8. This file should only be stored as UTF-8!
 * 
 * @author philip
 */
@Immutable
public final class CSpecialChars {

    public static final char AUML_LC = 'ä';

    public static final String AUML_LC_STR = Character.toString(AUML_LC);

    public static final char AUML_UC = 'Ä';

    public static final String AUML_UC_STR = Character.toString(AUML_UC);

    public static final char OUML_LC = 'ö';

    public static final String OUML_LC_STR = Character.toString(OUML_LC);

    public static final char OUML_UC = 'Ö';

    public static final String OUML_UC_STR = Character.toString(OUML_UC);

    public static final char UUML_LC = 'ü';

    public static final String UUML_LC_STR = Character.toString(UUML_LC);

    public static final char UUML_UC = 'Ü';

    public static final String UUML_UC_STR = Character.toString(UUML_UC);

    public static final char SZLIG = 'ß';

    public static final String SZLIG_STR = Character.toString(SZLIG);

    public static final char EURO = '€';

    public static final String EURO_STR = Character.toString(EURO);

    public static final char COPYRIGHT = '©';

    public static final String COPYRIGHT_STR = Character.toString(COPYRIGHT);

    @PresentForCodeCoverage
    @SuppressWarnings("unused")
    private static final CSpecialChars s_aInstance = new CSpecialChars();

    private CSpecialChars() {
    }
}

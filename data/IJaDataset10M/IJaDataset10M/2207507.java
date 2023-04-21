package de.mnit.basis.daten.typ.zeit.format;

/**
 * @author Michael Nitsche
 */
public interface FORMAT_ZEITPUNKT {

    public static final Object[] GRUPPE_DS = new Object[] { FORMAT_DATUM.JAHR_4, "-", FORMAT_DATUM.MONAT_2, "-", FORMAT_DATUM.TAG_2, "_", FORMAT_ZEIT.STD_2, "-", FORMAT_ZEIT.MIN_2, "-", FORMAT_ZEIT.SEK_2 };

    public static final Object[] GRUPPE_EDV = new Object[] { FORMAT_DATUM.JAHR_4, "-", FORMAT_DATUM.MONAT_2, "-", FORMAT_DATUM.TAG_2, " ", FORMAT_ZEIT.STD_2, ":", FORMAT_ZEIT.MIN_2, ":", FORMAT_ZEIT.SEK_2 };

    public static final Object[] GRUPPE_STANDARD = new Object[] { FORMAT_DATUM.TAG_2, ".", FORMAT_DATUM.MONAT_2, ".", FORMAT_DATUM.JAHR_4, " ", FORMAT_ZEIT.STD_2, ":", FORMAT_ZEIT.MIN_2, ":", FORMAT_ZEIT.SEK_2 };
}

package fr.dauphine.bookstore.commun;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Outils de formatage pour les dates et les montants.
 */
public final class FormatUtil {

    /**
	 * Locale par defaut.
	 */
    private static final Locale LOCALE = Locale.FRANCE;

    /**
	 * Formateur de dates.
	 */
    private static DateFormat dateFormat;

    /**
	 * Formateur de montants.
	 */
    private static NumberFormat priceFormat;

    static {
        dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, LOCALE);
        priceFormat = NumberFormat.getCurrencyInstance(LOCALE);
        priceFormat.setCurrency(Currency.getInstance(LOCALE));
    }

    /**
	 * Constructeur par defaut.
	 */
    private FormatUtil() {
    }

    /**
	 * Formate une date.
	 * 
	 * @param date
	 *            Date
	 * @return Date formatee
	 */
    public static String formatDate(final Date date) {
        return dateFormat.format(date);
    }

    /**
	 * Parse une date.
	 * 
	 * @param text
	 *            Date textuelle
	 * @return Date
	 * @throws ParseException
	 *             Si la date textuelle est au mauvais format
	 */
    public static Date parseDate(final String text) throws ParseException {
        return dateFormat.parse(text);
    }

    /**
	 * Formate un montant.
	 * 
	 * @param price
	 *            Montant
	 * @return Montant formate
	 */
    public static String formatPrice(final BigDecimal price) {
        return priceFormat.format(price);
    }

    /**
	 * Parse un montant.
	 * 
	 * @param text
	 *            Montant textuel
	 * @return Montant
	 * @throws ParseException
	 *             Si le montant textuel est au mauvais format
	 */
    public static BigDecimal parsePrice(final String text) throws ParseException {
        return BigDecimal.valueOf(priceFormat.parse(text).doubleValue());
    }
}

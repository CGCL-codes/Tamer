package uk.ac.ed.ph.snuggletex.definitions;

/**
 * Defines all of the {@link AccentMap}s for the various types of accents we support.
 * 
 * <h2>Developer Note</h2>
 * 
 * Add more entries to here as required, subject to the resulting accented characters
 * having adequate font support across the targetted browsers.
 *
 * @author  David McKain
 * @version $Revision:179 $
 */
public interface AccentMaps {

    public static final AccentMap ACCENT = new AccentMap(new char[] { 'A', 'Á', 'E', 'É', 'I', 'Í', 'O', 'Ó', 'U', 'Ú', 'a', 'á', 'e', 'é', 'i', 'í', 'o', 'ó', 'u', 'ú', 'y', 'ý' }, "");

    public static final AccentMap GRAVE = new AccentMap(new char[] { 'A', 'À', 'E', 'É', 'I', 'Ì', 'O', 'Ò', 'U', 'Ù', 'a', 'à', 'e', 'è', 'i', 'ì', 'o', 'ò', 'u', 'ù' }, "");

    public static final AccentMap CIRCUMFLEX = new AccentMap(new char[] { 'A', 'Â', 'E', 'Ê', 'I', 'Î', 'O', 'Ô', 'U', 'Û', 'a', 'â', 'e', 'ê', 'i', 'î', 'o', 'ô', 'u', 'û' }, "");

    public static final AccentMap TILDE = new AccentMap(new char[] { 'A', 'Ã', 'O', 'Õ', 'a', 'ã', 'n', 'ñ', 'o', 'õ' }, "");

    public static final AccentMap UMLAUT = new AccentMap(new char[] { 'A', 'Ä', 'E', 'Ë', 'I', 'Ï', 'O', 'Ö', 'U', 'Ü', 'a', 'ä', 'e', 'ë', 'i', 'ï', 'o', 'ö', 'u', 'ü' }, "");
}

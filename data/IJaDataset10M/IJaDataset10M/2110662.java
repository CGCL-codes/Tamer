package net.seagis.resources;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Utility methods working on SQL statements.
 *
 * @version $Id: SQL.java 162 2007-11-06 15:05:54Z desruisseaux $
 * @author Martin Desruisseaux
 */
@Deprecated
public final class SQL {

    /**
     * Do not allow instantiation of this class.
     */
    private SQL() {
    }

    /**
     * Retourne la chaîne spécifiée après avoir préfixé chaque caractères génériques par le
     * caractère d'échappement. Cette méthode peut être utilisée pour effectuer une recherche
     * exacte dans une instruction {@code LIKE}. Dans le cas particulier ou l'argument {@code text}
     * est {@code null}, alors cette méthode retourne le caractère d'échappement {@code "%"}, qui
     * accepte toutes les chaînes de caractères.
     *
     * @param  text Le texte qui peut contenir les caractères génériques {@code '%'} et {@code '_'}.
     * @return Le texte avec un caractère d'échappement devant chaque caractères génériques.
     * @throws SQLException si une erreur est survenue lors de l'accès à la base de données.
     */
    public static String escapeSearch(final String text, final DatabaseMetaData metadata) throws SQLException {
        if (text == null) {
            return "%";
        }
        StringBuilder buffer = null;
        String escape = "\\";
        int lower = 0;
        final int length = text.length();
        for (int i = 0; i < length; i++) {
            final char c = text.charAt(i);
            if (c == '_' || c == '%') {
                if (buffer == null) {
                    buffer = new StringBuilder(length + 5);
                    if (metadata != null) {
                        escape = metadata.getSearchStringEscape();
                    }
                }
                buffer.append(text.substring(lower, i));
                buffer.append(escape);
                lower = i;
            }
        }
        if (buffer == null) {
            return text;
        }
        buffer.append(text.substring(lower));
        return buffer.toString();
    }

    /**
     * Extrait la partie {@code "SELECT ... FROM ..."} de la requête spécifiée. Cette méthode
     * retourne la chaîne {@code query} à partir du début jusqu'au dernier caractère précédant
     * la première clause {@code "WHERE"}. La clause {@code "WHERE"} et tout ce qui suit jusqu'à
     * la clause {@code "GROUP"} ou {@code "ORDER"} ne sera pas inclue.
     */
    public static String selectWithoutWhere(String query) {
        final int lower = indexOfWord(query, "WHERE", 0);
        if (lower >= 0) {
            final String old = query;
            query = query.substring(0, lower);
            int upper = indexOfWord(old, "GROUP", lower);
            int upper2 = indexOfWord(old, "ORDER", lower);
            if (upper < 0 || (upper2 >= 0 && upper2 < upper)) {
                upper = upper2;
            }
            if (upper >= 0) {
                query += old.substring(upper);
            }
        }
        return query;
    }

    /**
     * Recherche une sous-chaîne dans une chaîne en ignorant les différences entre majuscules et
     * minuscules. Les racourcis du genre <code>text.toUpperCase().indexOf("SEARCH FOR")</code>
     * ne fonctionnent pas car {@code toUpperCase()} et {@code toLowerCase()} peuvent changer le
     * nombre de caractères de la chaîne. De plus, cette méthode vérifie que le mot est délimité
     * par des espaces ou de la ponctuation.
     */
    private static int indexOfWord(final String text, final String searchFor, final int startAt) {
        final int searchLength = searchFor.length();
        final int length = text.length();
        for (int i = startAt; i < length; i++) {
            if (text.regionMatches(true, i, searchFor, 0, searchLength)) {
                if (i != 0 && Character.isUnicodeIdentifierPart(text.charAt(i - 1))) {
                    continue;
                }
                final int upper = i + length;
                if (upper < length && Character.isUnicodeIdentifierPart(text.charAt(upper))) {
                    continue;
                }
                return i;
            }
        }
        return -1;
    }

    /**
     * Change la colonne sur laquelle s'applique un argument après la clause {@code WHERE}.
     * Par exemple cette méthode peut remplacer:
     *
     * <blockquote><pre>
     * SELECT identifier, name FROM stations WHERE <em>identifier</em>=?
     * </pre></blockquote>
     *
     * par
     *
     * <blockquote><pre>
     * SELECT identifier, name FROM stations WHERE <em>name</em>=?
     * </pre></blockquote>
     *
     * Celle méthode procède comme suit:
     *
     * <ol>
     *   <li>Recherche dans la requête la première colonne après la clause {@code SELECT}.</li>
     *   <li>Recherche le colonne <var>target</var> après la clause {@code SELECT}, où <var>target</var>
     *       est un numéro de colonne compté à partir de 1.</li>
     *   <li>Recherche le premier nom dans la clause {@code WHERE}, et le remplace par le deuxième nom.</li>
     * </ol>
     *
     * @param  query  La requête SQL dont on veut changer la cible d'un argument.
     * @param  target Le numéro de colonne dont le nom doit remplacer celui de la première colonne
     *         dans les arguments.
     * @return La requête SQL avec l'argument modifié.
     * @throws SQLException si cette méthode n'a pas pu comprendre la requête SQL spécifiée.
     */
    @SuppressWarnings("fallthrough")
    public static String changeArgumentTarget(final String query, int target) throws SQLException {
        String oldColumn = null;
        String newColumn = null;
        int step = 0;
        int lower = 0;
        boolean scanword = false;
        final int length = query.length();
        for (int index = 0; index < length; index++) {
            final char c = query.charAt(index);
            if ((c != ',' && c != '=' && !Character.isSpaceChar(c)) == scanword) {
                continue;
            }
            scanword = !scanword;
            if (scanword) {
                lower = index;
                continue;
            }
            final String word = query.substring(lower, index);
            switch(step) {
                case 0:
                    {
                        if (!word.equalsIgnoreCase("SELECT")) {
                            continue;
                        }
                        break;
                    }
                case 1:
                    {
                        oldColumn = word;
                        break;
                    }
                case 2:
                    {
                        if (word.equalsIgnoreCase("AS")) {
                            step = 1;
                            continue;
                        }
                        step++;
                    }
                case 3:
                    {
                        if (word.equalsIgnoreCase("FROM")) {
                            throw new SQLException("Numéro de colonne introuvable.");
                        }
                        if (--target != 1) {
                            continue;
                        }
                        newColumn = word;
                        break;
                    }
                case 4:
                    {
                        if (!word.equalsIgnoreCase("WHERE")) {
                            continue;
                        }
                        break;
                    }
                case 5:
                    {
                        if (!word.equalsIgnoreCase(oldColumn)) {
                            continue;
                        }
                        final StringBuilder buffer = new StringBuilder(query.substring(0, lower));
                        buffer.append(newColumn);
                        buffer.append(query.substring(index));
                        return buffer.toString();
                    }
                default:
                    {
                        throw new AssertionError(step);
                    }
            }
            step++;
        }
        throw new SQLException("La première colonne après SELECT devrait apparaître dans la clause WHERE.");
    }
}

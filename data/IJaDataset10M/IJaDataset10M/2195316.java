package fr.wbr;

/**
 * Created by IntelliJ IDEA.
 * User: Christophe
 * Date: 27 f�vr. 2012
 * Time: 16:10:27
 * To change this template use File | Settings | File Templates.
 */
public class AuthorUtils {

    public static String cleanAuthor(String author) {
        if (author == null) return null;
        author = author.trim();
        author = author.replaceAll(" and ", " & ");
        author = author.replaceAll(" et ", " & ");
        author = author.replaceAll(" *,", ",");
        author = author.replace("\n", "");
        author = author.replace("[", "(");
        author = author.replace("]", ")");
        author = author.trim();
        return author;
    }

    /**
     * Transform an author like "Fabricius, 1793" in a wikipedia form like "{{autheur|[[Fabricius]]}}, [[1793]]"
     */
    static String decorateAuthorTaxoboxFrance(String author) {
        if (author.length() == 0) {
            return author;
        }
        boolean parenthesis = (author.startsWith("(") && author.endsWith(")"));
        if (parenthesis) {
            author = author.substring(1, author.length() - 1);
        }
        int pos = author.lastIndexOf(", ");
        String date;
        if (pos > 0) {
            date = ", [[" + author.substring(pos + 1).trim() + "]]";
            author = author.substring(0, pos);
        } else {
            date = ", {{date � pr�ciser}}";
        }
        pos = author.indexOf(" in ");
        if (pos > 0) {
            author = author.substring(0, pos);
        }
        author = author.replaceAll(", ", "]]}}, {{auteur|[[");
        author = author.replaceAll(" & ", "]]}} & {{auteur|[[");
        author = author.replaceAll(" ex ", "]]}} ex {{auteur|[[");
        author = "{{auteur|[[" + author + "]]}}";
        author = author.replaceAll("\\{\\{auteur\\|\\[\\[L\\.]]}}", "[[Carl von Linn�|L.]]");
        author = author.replaceAll("\\{\\{auteur\\|\\[\\[Linnaeus]]}}", "[[Carl von Linn�|Linnaeus]]");
        int pos2 = author.indexOf(". ");
        while (pos2 > 0) {
            if (author.startsWith(".]")) {
            } else if (author.startsWith(". ,")) {
            } else {
                author = author.substring(0, pos2 + 1) + author.substring(pos2 + 2);
                pos2 = -1;
            }
            pos2 = author.indexOf(". ", pos2 + 1);
        }
        StringBuilder sb = new StringBuilder();
        if (parenthesis) {
            sb.append("(");
        }
        sb.append(author);
        sb.append(date);
        if (parenthesis) {
            sb.append(")");
        }
        return sb.toString();
    }

    /**
     * Transform an author like "Fabricius, 1793" in a wikipedia form like "{{autheur|[[Fabricius]]}}, [[1793]]"
     */
    static String decorateAuthorTaxonavigationCommons(String author) {
        author = author.replaceAll("Linnaeus", "[[Carl von Linn�|Linnaeus]]");
        author = author.replaceAll("L\\.", "[[Carl von Linn�|L.]]");
        author = author.replaceAll("P\\. \\[\\[Carl von Linn�\\|L\\.]] Sclater", "[[:en:Philip Sclater|P. L. Sclater]]");
        author = author.replaceAll("Vieillot", "[[Louis Jean Pierre Vieillot|Vieillot]]");
        return author;
    }

    static String[] authors = { "Fabricius, 1793", "(Fabricius, 1793)", "(Linnaeus et Compagno, 1982)", "Veith, Kosuch, Ohler and Dubois, 2001", "(L. et Compagno, 1982)", "J. F. Gmelin ex titi", "Vieillot, 1952", "P. L. Sclater" };

    static String[] decoratedAuthorsFrance = { "{{auteur|[[Fabricius]]}}, [[1793]]", "({{auteur|[[Fabricius]]}}, [[1793]])", "([[Carl von Linn�|Linnaeus]] & {{auteur|[[Compagno]]}}, [[1982]])", "{{auteur|[[Veith]]}}, {{auteur|[[Kosuch]]}}, {{auteur|[[Ohler]]}} & {{auteur|[[Dubois]]}}, [[2001]]", "([[Carl von Linn�|L.]] & {{auteur|[[Compagno]]}}, [[1982]])", "{{auteur|[[J.F.Gmelin]]}} ex {{auteur|[[titi]]}}, {{date � pr�ciser}}", "{{auteur|[[Vieillot]]}}, [[1952]]", "{{auteur|[[P.L.Sclater]]}}, {{date � pr�ciser}}" };

    static String[] decoratedAuthorsCommons = { "Fabricius, 1793", "(Fabricius, 1793)", "([[Carl von Linn�|Linnaeus]] & Compagno, 1982)", "Veith, Kosuch, Ohler & Dubois, 2001", "([[Carl von Linn�|L.]] & Compagno, 1982)", "J. F. Gmelin ex titi", "[[Louis Jean Pierre Vieillot|Vieillot]], 1952", "[[:en:Philip Sclater|P. L. Sclater]]" };

    public static void autoTest() {
        for (int i = 0; i < AuthorUtils.authors.length; ++i) {
            String author = AuthorUtils.cleanAuthor(AuthorUtils.authors[i]);
            WikiUtils.assertEquals(AuthorUtils.decorateAuthorTaxoboxFrance(author), AuthorUtils.decoratedAuthorsFrance[i]);
            WikiUtils.assertEquals(AuthorUtils.decorateAuthorTaxonavigationCommons(author), AuthorUtils.decoratedAuthorsCommons[i]);
        }
        System.out.println("AuthorUtils JUnit OK");
    }
}

package fr.crim.m2im.a2008;

import org.apache.lucene.search.FilteredTermEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import java.io.IOException;
import java.util.regex.Pattern;

/**
Sous-classe de {@link org.apache.lucene.search.FilteredTermEnum}
filtrant l'énumération des termes
d'un champ  selon une expression régulière.
La liste peut demander jusqu'à 100ms pour être compilée
sur les 500 000 formes de Morphalou.
*/
public class RETermEnum extends FilteredTermEnum {

    private String field = "";

    private boolean endEnum = false;

    /** Le motif d'expression régulière qui sélectionne les termes dans la liste. */
    Pattern pattern;

    /**
   Le constructeur commence l'énumération de termes sur le champ,
   et initialise le {@link java.util.regex.Pattern} en expression régulière.
   
   @param reader  L'index lucene à parcourir. 
   @param term    Le terme utilisé pour passer le motif.
   
   @throws IOException 
   */
    public RETermEnum(IndexReader reader, Term term) throws IOException {
        super();
        field = term.field();
        pattern = Pattern.compile(term.text());
        setEnum(reader.terms(new Term(term.field(), "")));
    }

    /**
  La méthode qui filtre l'énumération de termes.
  */
    protected final boolean termCompare(Term term) {
        if (field != term.field()) {
            endEnum = true;
            return false;
        }
        return pattern.matcher(term.text()).find();
    }

    /** (non-Javadoc)
   * @see org.apache.lucene.search.FilteredTermEnum#difference()
   */
    public final float difference() {
        return 1.0f;
    }

    /** Est-ce la fin de l'énumération. 
   * @return Le drapeau levé par la méthod {@link #termCompare(Term)} */
    public final boolean endEnum() {
        return endEnum;
    }

    /** Nettoyer ce qui est possible derrière soi. 
   * @throws IOException Si l'index n'est plus disponible. */
    public void close() throws IOException {
        super.close();
        field = null;
    }
}

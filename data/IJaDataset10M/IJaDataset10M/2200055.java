package fr.crim.lexique.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import fr.crim.lexique.common.Candidate;
import fr.crim.lexique.sqlite.LexiqueOrgSQLiteReader;

/**
 * Un token filter Lucene basé sur Lexique3
 * @author Pierre DITTGEN
 */
public class LexiqueOrgReplacementFilter extends TokenFilter {

    /**
	 * Classe de lecture du lexique
	 */
    private LexiqueOrgSQLiteReader lexiqueReader;

    /** Manipulation Lucene. */
    private TermAttribute termAttr;

    /**
	 * Constructeur
	 * @param input Le flux d'entrée
	 * @param dbfile le fichier SQLite contenant l'extrait de Lexique3.txt
	 */
    public LexiqueOrgReplacementFilter(TokenStream input, File dbFile) {
        super(input);
        try {
            lexiqueReader = new LexiqueOrgSQLiteReader(dbFile);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de se connecter à la base " + dbFile);
        }
        termAttr = (TermAttribute) addAttribute(TermAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!input.incrementToken()) {
            return false;
        }
        List<Candidate> results = lexiqueReader.lookupForm(termAttr.term().toLowerCase());
        if (!results.isEmpty()) {
            termAttr.setTermBuffer(results.get(0).getLemma());
        }
        return true;
    }
}

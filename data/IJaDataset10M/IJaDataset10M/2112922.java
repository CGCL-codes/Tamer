package uk.ac.shef.wit.runes.rune.nlp;

import uk.ac.shef.wit.commons.UtilCollections;
import uk.ac.shef.wit.runes.Runes;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionRuneExecution;
import uk.ac.shef.wit.runes.rune.Rune;
import uk.ac.shef.wit.runes.runestone.Content;
import uk.ac.shef.wit.runes.runestone.Runestone;
import uk.ac.shef.wit.runes.runestone.Structure;
import uk.ac.shef.wit.runes.runestone.StructureAndContent;
import uk.ac.shef.wit.text.stemmer.PorterStemmer;
import uk.ac.shef.wit.text.stemmer.Stemmer;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Class implments Rune interface which stems text tokens For example Cats stemmed to cat
 *
 * @author <a href="mailto:j.iria@dcs.shef.ac.uk">Jos&eacute; Iria</a>
 * @author <a href="mailto:l.xia@dcs.shef.ac.uk">Lei Xia</a>
 * @version $Id: RuneTextStemmer.java 328 2008-04-15 17:45:42Z jiria $
 */
public class RuneTextStemmer implements Rune {

    private static final double VERSION = 0.6;

    private static final Logger log = Logger.getLogger(RuneTextStemmer.class.getName());

    static {
        Runes.registerRune(RuneTextStemmer.class, MessageFormat.format("[nlp] stemmer v{0}", VERSION));
    }

    /**
     * {@inheritDoc}
     * <br/><br/>
     * <strong>Required:</strong>
     * <pre>
     *  string
     * token_has_string
     * </pre>
     */
    public Set<String> analyseRequired(final Runestone stone, final Set<String> model) {
        return UtilCollections.add(new HashSet<String>(), "string", "token_has_string");
    }

    /**
     * {@inheritDoc}
     * <br/><br/>
     * <strong>Provided:</strong>
     * <pre>
     * 	stem
     *  token_has_stem
     * </pre>
     * <p/>
     */
    public Set<String> analyseProvided(final Runestone stone, final Set<String> model) {
        return UtilCollections.add(new HashSet<String>(), "stem", "token_has_stem");
    }

    public void carve(final Runestone stone) throws RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent, RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        final Stemmer stemmer = new PorterStemmer();
        final Content<String> string = stone.getContent("string");
        final StructureAndContent<String> stem = stone.getStructureAndContent("stem");
        final Structure tokenHasStem = stone.getStructure("token_has_stem");
        for (final int[] id : stone.getStructure("token_has_string")) tokenHasStem.inscribe(id[0], stem.encode(stemmer.stem(string.retrieve(id[1]))));
    }

    @Override
    public String toString() {
        return "text stemmer";
    }
}

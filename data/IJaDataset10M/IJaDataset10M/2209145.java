package uk.ac.gla.terrier.terms;

/** Romanian stemmer implmented by Snowball.
  * @author Craig Macdonald <craigm{a.}dcs.gla.ac.uk>
  * @version $Revision: 1.3 $
  */
public class RomanianSnowballStemmer extends SnowballStemmer {

    public RomanianSnowballStemmer(TermPipeline n) {
        super("Romanian", n);
    }
}

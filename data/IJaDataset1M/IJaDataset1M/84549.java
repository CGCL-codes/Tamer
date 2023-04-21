package uk.ac.gla.terrier.terms;

/** Italian stemmer implmented by Snowball.
  * @author Craig Macdonald <craigm{a.}dcs.gla.ac.uk>
  * @version $Revision: 1.3 $
  */
public class ItalianSnowballStemmer extends SnowballStemmer {

    public ItalianSnowballStemmer(TermPipeline n) {
        super("Italian", n);
    }
}

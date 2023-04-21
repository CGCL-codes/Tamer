package darwInvest.news.utility;

/**
 * The stemming normalizer applies Porter's stemming algorithm to the input string
 * 
 * @author Jae Yong Sung, Kevin Dolan
 */
public class Stemming implements Normalizer {

    private Stemmer stemmer;

    public Stemming() {
        stemmer = new Stemmer();
    }

    @Override
    public String normalize(String word) {
        stemmer.add(word.toCharArray(), word.length());
        stemmer.stem();
        return stemmer.toString();
    }
}

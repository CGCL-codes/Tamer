package darwInvest.news.utility;

/**
 * Convert all letters to lower case
 * @author Kevin Dolan
 */
public class LowerCase implements Normalizer {

    @Override
    public String normalize(String string) {
        return string.toLowerCase();
    }
}

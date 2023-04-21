package mudownmanager.backend;

public class FrenchLanguageUtils {

    public static String removeURLSpecialChars(String input) {
        return input.replaceAll("é", "e").replaceAll("è", "e").replaceAll("à", "a").replaceAll("ç", "c").replaceAll("ù", "u").replaceAll("è", "e").replaceAll("ê", "e").replaceAll("ë", "e").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D").replaceAll(" ", "%20").replaceAll("<", "%3C").replaceAll(">", "%3E").replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\\|", "%7C").replaceAll("\\^", "%5E").replaceAll("\\~", "%7E").replaceAll("\\$", "%24").replaceAll("\\+", "%2B").replaceAll("\\@", "%40");
    }
}

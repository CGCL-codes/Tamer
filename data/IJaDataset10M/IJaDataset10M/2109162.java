package Application;

/**
 * Az aramkor leiro fajl parse-olasa kozben fellepo esetleges hibak jelzeset
 * leiro exception
 * @author Stikmann
 */
public class ParseSyntaxErrorException extends Exception {

    private String msg;

    public ParseSyntaxErrorException() {
        msg = "Szintaktikai hiba.";
    }

    public ParseSyntaxErrorException(int line, String message) {
        msg = "Szintaktikai hiba a " + line + ". sorban: " + message;
    }

    public String toString() {
        return msg;
    }
}

package desperateDoctors;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.Serializable;

public class Parser implements Serializable {

    /**
     * Cree un nouveau parser.
     */
    public Parser() {
    }

    /**
     * Recupere la nouvelle commande entree par je joueur.
     * La commande est lue par le parser.
     */
    public Command getCommand(String inputLine) {
        String word1;
        String word2;
        StringTokenizer tokenizer = new StringTokenizer(inputLine);
        if (tokenizer.hasMoreTokens()) word1 = tokenizer.nextToken(); else word1 = null;
        if (tokenizer.hasMoreTokens()) word2 = tokenizer.nextToken(); else word2 = null;
        return new Command(word1, word2);
    }

    public String showCommands() {
        String s = "";
        return s;
    }
}

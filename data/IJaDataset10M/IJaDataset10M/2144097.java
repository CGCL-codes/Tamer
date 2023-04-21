package Exception;

/**
 * Cette classe sert � la validation de la syntaxe des script
 * xml. Elle permet d'avoir quelques d�tails sur la raison
 * du disfonctionnnement d'un script.
 * 
 * @author K�vin Bouchard
 * @since 15/07/2009
 */
@SuppressWarnings("serial")
public class InvalideScriptSyntaxException extends Exception {

    public String balise;

    public InvalideScriptSyntaxException() {
        balise = "**Inconnue**";
    }

    public InvalideScriptSyntaxException(String bb) {
        balise = bb;
    }
}

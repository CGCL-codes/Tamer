package br.ufpe.cin.imersion.pattern.structural.decorator;

/**
 * @author Marcello Alves de Sales Junior <BR>
 * email: <a href=mailto:masj2@cin.ufpe.br>masj2@cin.ufpe.br</a> <BR>
 * @created 14/07/2004 10:38:38
 */
public class MoneyStaredString extends StaredString {

    /**
     * @param originalString
     * @created 14/07/2004 10:38:43
     */
    public MoneyStaredString(String originalString) {
        super(originalString);
    }

    public String toString() {
        return "$$$ " + super.toString() + " $$$";
    }
}

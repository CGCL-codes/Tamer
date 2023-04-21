package fr.insa_rennes.pcreator.editiongraphique.model;

/**
 * 
 * @author
 */
public class ErreurNumero extends Erreur {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int numero;

    /**
     * Default constructor
     */
    public ErreurNumero() {
        super();
        this.numero = 0;
    }

    /**
     * Return numero
     * @return int
     */
    public int getNumero() {
        return this.numero;
    }

    /**
     * Set the value of numero.
     * @param mynumero 
     */
    public void setNumero(int mynumero) {
        this.numero = mynumero;
    }

    @Override
    public String toString() {
        String str = "";
        str += "numéro: " + numero + "\n";
        return str;
    }
}

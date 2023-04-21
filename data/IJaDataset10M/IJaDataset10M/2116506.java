package logica.gestoreProcessi;

/**
 * Classe per la gersione dell'eccezione ParteCodiceMancante, che si verifica 
 * quando la si effettua il controllo sulla dimensione delle parti codice di un 
 * processo e non è stata definita nessuna parte codice
 * 
 * @author Orlandin Marco
 * @version 1.0
 */
public class ParteCodiceMancante extends Exception {

    /**
	 * id errore
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * interfaccia al processo per cui è stata sollevata l'eccezione
	 */
    private final transient InterfacciaProcesso processo;

    /**
	 * Costruttore
	 * @param processoEsistente processo per cui è stata sollevata l'eccezione
	 */
    ParteCodiceMancante(Processo processoEsistente) {
        super();
        processo = processoEsistente;
    }

    /**
	 * Metodo che ritorna l'interfaccia al processo per cui è stata sollevata 
	 * l'eccezione
	 * @return processo per cui è stata sollevata l'eccezione
	 */
    public InterfacciaProcesso getProcesso() {
        return processo;
    }

    /**
	 * Ridefinizione del metodo toString()
	 */
    public String toString() {
        return "Il processo " + processo.getId() + "(" + processo.getNome() + ") " + "non ha parti codice";
    }
}

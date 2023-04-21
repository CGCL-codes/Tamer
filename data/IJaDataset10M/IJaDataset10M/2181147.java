package fsmsim.exception;

/**
 * <p>Title: FSMSim</p>
 *
 * <p>Description: Simulatore di macchine a stati finiti.</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: Bollati, Donati, Gabrielli, Peli</p>
 *
 * @author Bollati, Donati, Gabrielli, Peli
 * @version 3.0
 */
public class FSMException extends Exception {

    private String errorMessage;

    /**
     * Costruttore
     */
    public FSMException() {
        this("Errore generico di runtime.");
    }

    /**
     * Costruttore con stringa in ingresso
     * @param errorMessage
     */
    public FSMException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    /**
     * Ritorna la stringa di errore
     * @return
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }
}

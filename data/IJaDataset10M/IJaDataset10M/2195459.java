package alocador.exceptions;

/**
 * Exception que Representa Uma Finalidade Invalida
 */
public class FinalidadeInvalidaException extends SalaInvalidaException {

    private static final long serialVersionUID = 1L;

    /**
	 * Construtor da Classe
	 * 
	 * @param errorMsg
	 *            Recebe o Motivo da Finalidade ser Invalida
	 */
    public FinalidadeInvalidaException(String errorMsg) {
        super(errorMsg);
    }
}

package negocios.excecoes;

/**
 * Excecao para objetos nao encontrados no sistema e no banco de dados
 * 
 * @author Jonathan Brilhante
 * @author Jose Rafael
 * @author Nata Venancio
 * @author Renato Almeida
 * 
 * @version 1.0
 */
public class NaoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Constroi uma NaoEncontradoException
	 * 
	 * @param o
	 *            o objeto nao encontrado
	 */
    public NaoEncontradoException(Object o) {
        super(o.toString() + " nao foi encontrado.");
    }

    /**
	 * Constroi uma NaoEncontradoException
	 * 
	 * @param mensagem
	 *            a mensagem, caso precise ser personalizada
	 */
    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

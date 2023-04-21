package sql;

/**
 * SAW - Sistema Academica Web.
 * Classe de tratamento de exceções da classe AlunoBD. 
 *
 * @author Antonio Carlos Trajano de Oliveira, antonio.c.trajano@gmail.com
 * @author Icaro Vasconcelos, icaroswim@gmail.com
 * @version 1.0
 */
public class AlunoBDException extends Exception {

    /**
	 * Construtor da exceção informando problemas em relação ao relacionamento Aluno com o Banco de Dados.
	 * @param exceptionReason O motivo que levou ao lançamento da exceção.
	 */
    public AlunoBDException(String exceptionReason) {
        super(exceptionReason);
    }
}

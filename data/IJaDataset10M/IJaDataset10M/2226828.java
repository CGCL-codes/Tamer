package br.com.hs.nfe.certificado.exception;

/**
 * Classe de exceção de senha inválida do certificado.
 * @author Ranlive Hrysyk
 */
public class HSCertificadoSenhaException extends HSCertificadoException {

    /**
     * Cria uma instancia de <code>HSCertificadoSenhaException</code> expecificando uma mensagem detalhada.
     * @param msg Mensagem detalhada.
     */
    public HSCertificadoSenhaException(String msg) {
        super(msg);
    }
}

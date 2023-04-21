package br.com.srv.exception;

public class InvalidTelefoneException extends BaseBusinessException {

    private static final long serialVersionUID = 2166001095954063605L;

    public InvalidTelefoneException(String erros) {
        super("N�mero telefone inv�lido!");
        setKey("configurar.smsPanicoAlarme.error.telefone.invalido");
        setParm(erros);
    }
}

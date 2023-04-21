package br.ufal.bibliweb.exception;

public class ResourceNotFoundException extends ResourceException {

    /**
	 * Exce��o gerada quando um recurso do ambiente n�o for encontrado.
	 * EX: Status, Language
	 **/
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

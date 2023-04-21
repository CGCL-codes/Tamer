package br.com.insight.consultoria.erro.exception;

public class InsightNegocioException extends InsightException {

    private String argumentos[];

    public InsightNegocioException() {
    }

    public InsightNegocioException(String message) {
        super(message);
    }

    public InsightNegocioException(String message, String args[]) {
        super(message);
        this.argumentos = args;
    }

    private static final long serialVersionUID = 1L;

    public String[] getArgumentos() {
        return argumentos;
    }
}

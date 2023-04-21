package negocio.cliente;

public class ClienteInexistenteException extends Exception {

    private String codCliente;

    private static final String MSG_CLI_INEXISTENTE = "Cliente n�o cadastrado !!";

    public ClienteInexistenteException(String codCliente) {
        super(MSG_CLI_INEXISTENTE);
        this.codCliente = codCliente;
    }

    public String getCodCliente() {
        return codCliente;
    }
}

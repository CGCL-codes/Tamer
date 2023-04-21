package logger.sd.server.service;

import logger.sd.protocol.LRP;

/**
 * Implementa o servico de echo
 * 
 * @author Wendell
 */
public class EchoService implements Service {

    private String requisicaoDoCliente;

    /**
	 * Construtor
	 * 
	 * @param message
	 *            Socket do cliente
	 */
    public EchoService(String message) {
        this.requisicaoDoCliente = message;
    }

    /**
	 * Tratamento de mensagens via echo
	 */
    public String trataRequisicao() {
        return requisicaoDoCliente.toUpperCase();
    }

    @Override
    public String getRequest() {
        return requisicaoDoCliente;
    }

    @Override
    public String getCode() {
        return LRP.ECHO.getMessage();
    }
}

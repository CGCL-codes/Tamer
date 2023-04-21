package logger.sd.server.service;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.log4j.Level;
import logger.sd.protocol.LRP;
import logger.sd.server.LogWriter;
import logger.sd.server.ServerConfiguration;

/**
 * Servico de Logging do Servidor
 * 
 * @author wendell
 * 
 */
public class LoggingService implements Service {

    private String request;

    private String clientAddress;

    private Level level;

    private String message;

    private LogWriter logger;

    private ServerConfiguration config;

    /**
	 * Construtor
	 * 
	 * @param clientSocket
	 *            Socket do cliente
	 * @exception IOException
	 */
    public LoggingService(String request, String clientAddress, ServerConfiguration config) {
        this.request = request;
        this.clientAddress = clientAddress;
        this.config = config;
        this.logger = LogWriter.getInstance(LoggingService.class, this.config);
        logger.setLevel(Level.toLevel(config.getLevel()));
    }

    /**
	 * Construtor
	 * 
	 * @param clientSocket
	 *            Socket do cliente
	 * @exception IOException
	 */
    public LoggingService(String request, String clientAddress) {
        this.request = request;
        this.clientAddress = clientAddress;
        config = new ServerConfiguration();
        this.logger = LogWriter.getInstance(LoggingService.class, config);
        logger.setLevel(Level.toLevel(config.getLevel()));
    }

    /**
	 * Executa o servico de Logging
	 * 
	 * @exception IOException
	 */
    public String trataRequisicao() {
        String resposta = this.parse();
        if (resposta.equals(LRP.okMessage())) {
            logger.log(level, clientAddress + " - " + message + " - on port " + config.getPort());
        }
        return resposta;
    }

    /**
	 * 
	 * @return a resposta para a requisicao
	 */
    public String parse() {
        String comando = new String();
        String resposta = new String();
        StringTokenizer line = new StringTokenizer(request, "[\"]");
        if (line.hasMoreTokens()) {
            comando = line.nextToken().replace(" ", "");
        } else {
            resposta = LRP.error01();
        }
        if (LRP.isLogging(comando) && line.hasMoreTokens()) {
            this.level = Level.toLevel(comando);
            this.message = line.nextToken();
            resposta = LRP.okMessage();
        } else if (comando.equals(LRP.END.getMessage())) {
            resposta = comando;
        } else {
            System.err.println("Erro de Requisicao: " + request);
            System.err.println("Erro de Comando: " + comando);
            resposta = LRP.error01();
        }
        return resposta;
    }

    /**
	 * @return request
	 */
    @Override
    public String getRequest() {
        return request;
    }

    /**
	 * 
	 * @return O codigo do servico no LRP
	 */
    @Override
    public String getCode() {
        return LRP.LOG.getMessage();
    }
}

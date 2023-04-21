package negocio.xml;

import javax.xml.parsers.*;
import negocio.comunicacao.Mensagem;
import org.xml.sax.*;

/**
 * Classe responsavel em instanciar o parser. Esta classe realiza o parser de uma fonte de entrada
 * de dados.
 * @author Luiz Carlos Viana Melo, Gizeli Ribas de Moraes e Denise Lam
 *
 */
public class SAXInputParser {

    private MensagemHandler msgHandler;

    public SAXInputParser() {
        msgHandler = new MensagemHandler();
    }

    /**
	 * Realiza o parser de uma fonte de dados.
	 * @param input a fonte de dados de entrada
	 */
    public void parse(InputSource input) throws Exception {
        SAXParserFactory spFactory = SAXParserFactory.newInstance();
        SAXParser parser = spFactory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(msgHandler);
        try {
            reader.parse(input);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().equals("pare")) {
            } else throw e;
        }
    }

    public Mensagem getMensagem() {
        return msgHandler.getMensagem();
    }
}

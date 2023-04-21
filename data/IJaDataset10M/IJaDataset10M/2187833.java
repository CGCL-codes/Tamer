package util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 
 * <b>HelpDeskTRE</b><br><br>
 *  
 *  
 * Classe que representa uma Conexao baseada na tecnologia
 * XML. Os objetos desta classe podem ler e escrever objetos em XML.
 * 
 * 
 * @author Arthur Lucio Meneses Farias <br>
 * @author Andre Luiz Alves <br>
 * @author Danilo Coura Moreira <br>
 */
public class ConexaoXML {

    private XMLDecoder decoder;

    private XMLEncoder encoder;

    /**
	 * Cria um novo ConexaoXML
	 * 
	 */
    public ConexaoXML() {
    }

    /**
	 * Realiza a gravacao de um Object em um arquivo XML.
	 * 
	 * @param obj Objeto a ser gravado.
	 * @param nomeArquivo O nome do darquivo XML a ser lido.
	 */
    public void write(Object obj, String nomeArquivo) {
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nomeArquivo)));
            encoder.writeObject(obj);
            encoder.close();
        } catch (FileNotFoundException e) {
        }
    }

    /**
	 * Realiza a leitura de um Object gravado em XML.
	 * @param nomeArquivo Nome do arquivo XML contendo o arquivo a ser lido.
	 * @return retorna um Object se o arquivo XML existir e retorna <b>NULL </b>
	 *         caso o arquivo nao exista.
	 */
    public Object read(String nomeArquivo) {
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(nomeArquivo)));
            return decoder.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}

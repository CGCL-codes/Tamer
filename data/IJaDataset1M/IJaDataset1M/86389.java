package br.org.skenp.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 
 * Classe para manipular arquivos de properties
 * @author Davy Diegues Duran
 * 
 */
public class PropertiesUtil {

    /** o objeto Properties */
    private Properties prop;

    private String fileName;

    private InputStream inputStream;

    /**
	 * Construtor
	 * 
	 * @param filename
	 *            nome do aquivo properties
	 * 
	 */
    public PropertiesUtil(String filename) throws FileNotFoundException, IOException {
        inputStream = getClass().getClassLoader().getResourceAsStream(filename);
        fileName = filename;
        this.prop = new Properties();
        this.prop.load(inputStream);
    }

    /**
	 * @return Retorna o properties usado.
	 */
    public Properties getProp() {
        return prop;
    }

    /**
	 * seta uma propriedade no arquivo
	 * 
	 * @param key
	 *            nome da chave
	 * @param value
	 *            valor da chave
	 * @throws IOException 
	 */
    public void setFileProperty(String key, String value) throws IOException {
        this.prop.setProperty(key, value);
        URL url = getClass().getClassLoader().getResource(fileName);
        FileOutputStream fos = new FileOutputStream(url.getFile());
        prop.store(fos, "");
    }

    /**
	 * pega o valor de uma chave do arquivo
	 * 
	 * @param key
	 *            nome da chave
	 * @return valor da chave
	 */
    public String getFileKeyValue(String key) {
        return this.prop.getProperty(key);
    }

    /**
	 * metodo que fecha o Input Stream usado para ler o arquivo
	 * @throws IOException 
	 */
    public void close() throws IOException {
        inputStream.close();
    }
}

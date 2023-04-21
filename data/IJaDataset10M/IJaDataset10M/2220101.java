package com.dashboard.util.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe utilit&aacute;ria utilizada para leitura das propriedades de configura&ccedil;&atilde;o  dos
 * testes de unidade do arquivo <b>testes.properties</b>.
 * 
 * <p>
 * A motiva&ccedil;&atilde;o para o uso dessa classe &eacute; permitir a leitura das propriedades apenas uma
 * vez para todos os testes, e encapsular a manipula&ccedil;&atilde;o do <code>java.io.InputStream</code>
 * utilizado para ler o arquivo de propriedades.
 * </p>
 * 
 * <p>
 * Esta classe l&ecirc; o valor das propriedades de um arquivo entitulado <b>testes.properties</b> que cada
 * desenvolvedor deve configurar na raiz de seu projeto e <b>n&atilde;o incluir no controle de
 * vers&atilde;o</b>.
 * </p>
 * 
 * @author João Neves
 *
 */
public class PropertyTestsUtils {

    /**
	 * Propriedades de configura&ccedil;&atilde;o dos testes de unidade.
	 */
    private static Properties properties;

    /**
	 * Carrega as propriedades a partir do arquivo de propriedades <b>testes.properties</b>.
	 */
    private static void loadProperties() {
        try {
            InputStream in = new FileInputStream("tests.properties");
            properties = new Properties();
            properties.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("O arquivo de propriedades de testes nao foi encontrado.", e);
        } catch (IOException e) {
            throw new RuntimeException("Erro na leitura do arquivo de propriedades de testes.", e);
        }
    }

    /**
	 * Retorna o valor de uma propriedade configura&ccedil;&atilde;o a partir da sua chave.
	 * 
	 * @param chavePropriedade - A chave da propriedade.
	 * @return O valor da propriedade.
	 */
    public static String getProperty(String chavePropriedade) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(chavePropriedade);
    }
}

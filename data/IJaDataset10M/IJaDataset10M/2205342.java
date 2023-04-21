package org.freedom.infra.util.ini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Projeto: <a
 * href="http://sourceforge.net/projects/freedom-erp/">Freedom-infra</a> <br>
 * Este programa � licenciado de acordo com a LPG-PC <br>
 * modifica-lo dentro dos termos da Licen�a P�blica Geral GNU como publicada
 * pela Funda��o do Software Livre (FSF); <BR>
 * <br>
 * 
 * Gerenciador para fluxo em arquivo de parametros.<br>
 * 
 * Esta classe gerencia o fluxo de parametros em arquivo, lendo o arquivo e
 * organizando os parametros em sess�es, conforme descrito no pr�prio arquivo.<br>
 * A sess�o deve ser disposta da seguinte maneira: <br>
 * <br>
 * <blockquote> [nome da sess�o]<br>
 * parametro = valor do parametro<br>
 * ...<br>
 * [outra sess�o]<br>
 * parametro = valor do parametro<br>
 * ...<br>
 * </blockquote> <br>
 * Desta forma o gerenciador mantera um mapa das sess�es, e o acesso a estas se
 * da atrav�s dos metodos {@link #getSession(String)} e
 * {@link #setSession(String, Properties)}.<br>
 * A atualiza��o do conteudo do arquivo � efetuada pelo metodo
 * {@link #postProperties()}, reescreve as sess�es e seus parametros, tamb�m as
 * sess�es incluidas posteriormente a leitura do arquivo, atualizando o estado
 * dos paramentros. <br>
 * 
 * @see java.util.Map
 * @see java.util.Properties
 * 
 * @author Alex Rodrigues
 * @version 0.0.2 � 07/07/2008
 * 
 * @since 04/07/2008
 */
public class ManagerIni {

    /**
	 * Constante padr�o, para nome de parametro a ser resgatado com
	 * java.lang.System.getProperty(String, String), parametro este que indicara
	 * o arquivo de parametros para leitura.
	 */
    public static final String FILE_INIT_DEFAULT = "INITFILE";

    /**
	 * Nome padr�o para sess�o, caso o arquivo n�o possua uma sess�o definida.
	 */
    private static final String PROPERTIES_DEFAULT = "PROPERTIES_DEFAULT";

    /**
	 * Mapa das sess�es.
	 */
    private Map<String, Properties> sessions = new HashMap<String, Properties>();

    /**
	 * Arquivo de parametros.
	 */
    private File file;

    private Properties properties;

    /**
	 * @return Um novo gerenciador a partir do nome do parametro padr�o.
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
    public static ManagerIni createManagerIniParameter() throws IOException {
        return new ManagerIni(new File(System.getProperty(FILE_INIT_DEFAULT, "")));
    }

    /**
	 * @param initFileName
	 *            Nome do parametro
	 * @return Um novo gerenciador a partir do nome do parametro informado.
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
    public static ManagerIni createManagerIniParameter(String initFileName) throws IOException {
        return new ManagerIni(new File(System.getProperty(initFileName, "")));
    }

    /**
	 * @param file
	 *            Arquivo de parametros
	 * @return Um novo gerenciador a partir do arquivo informado.
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
    public static ManagerIni createManagerIniFile(File file) throws IOException {
        return new ManagerIni(file);
    }

    /**
	 * @param fileName
	 *            Nome do arquivo de parametros
	 * @return Um novo gerenciador a partir do arquivo criado com o nome
	 *         informado.
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
    public static ManagerIni createManagerIniFile(String fileName) throws IOException {
        return new ManagerIni(new File(fileName));
    }

    /**
	 * Contrutor que recebe arquivo de parametros, verifica se a refer�ncia est�
	 * nula ou se arquivo � inexistente, do contr�rio executa a leitura do
	 * arquivo e cria��o do mapa de sess�es.
	 * 
	 * @see #readFile()
	 * 
	 * @param initFile
	 *            Arquivo de parametros.
	 * 
	 * @throws IOException
	 *             Repassada por readFile()
	 * 
	 * @since 04/07/2008
	 */
    private ManagerIni(File initFile) throws IOException {
        if (initFile != null && initFile.exists()) {
            file = initFile;
            readFile();
        }
    }

    /**
	 * Executa a leitura do arquivo de parametros, a cria��o das sess�es e
	 * montagem do mapa.<br>
	 * <blockquote> Quanto a leitura, ela � feita linha a linha, no entanto s�o
	 * ignoradas as linhas identificadas como linhas de coment�rios.<br>
	 * A linha de coment�rio � identificada das seguintes formas:<br>
	 * # Com o caracter # no �ncio da linha. Ou <br>
	 * // Com dois caracters / no �nicio da linha. </blockquote>
	 * 
	 * @see java.io.FileReader
	 * @see java.io.BufferedReader
	 * 
	 * @throws IOException
	 *             Esta pode provir das classes FileReader ou BufferedReader,
	 *             utilizadas para a leitura do arquivo.
	 * 
	 * @since 04/07/2008
	 */
    private void readFile() throws IOException {
        if (file == null || !file.exists()) {
            return;
        }
        FileReader reader = new FileReader(file);
        BufferedReader buffered = new BufferedReader(reader);
        sessions = new HashMap<String, Properties>();
        properties = null;
        if (buffered != null) {
            String line = "";
            String name = null;
            String value = null;
            int ivl = -1;
            while ((line = buffered.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                } else if ('#' == line.charAt(0)) {
                    continue;
                } else if (line.length() > 1 && ('/' == line.charAt(0) && '/' == line.charAt(1))) {
                    continue;
                } else if (ckeckNewSession(line)) {
                    continue;
                }
                if (properties == null) {
                    properties = new Properties();
                    sessions.put(PROPERTIES_DEFAULT, properties);
                }
                ivl = line.indexOf('=');
                if (ivl > -1) {
                    name = line.substring(0, ivl);
                    value = line.substring(ivl + 1);
                    properties.put(name, value);
                }
            }
        }
    }

    /**
	 * Re-escreve o arquivo de parametros.<br>
	 * <blockquote> O arquivo ser� completamento re-escrito com base no estado
	 * atual das sess�es, incluindo sess�es e parametros adicion�is e eliminando
	 * os parametros ou sess�es retiradas de suas sess�es ou mapa
	 * respectivamente. e o estado antigo do arquivo ser� perdido. </blockquote>
	 * 
	 * @see java.io.PrintWriter
	 * 
	 * @throws IOException
	 * 
	 * @since 04/07/2008
	 */
    public void postProperties() throws IOException {
        PrintWriter printWriter = new PrintWriter(file);
        if (printWriter != null) {
            Properties p;
            Object[] pk;
            for (String nameSession : sessions.keySet()) {
                p = sessions.get(nameSession);
                if (p.size() > 0) {
                    printWriter.println("[" + nameSession + "]");
                    pk = p.keySet().toArray();
                    Arrays.sort(pk);
                    for (Object k : pk) {
                        printWriter.println(k + "=" + p.getProperty((String) k));
                    }
                    printWriter.println();
                }
            }
            if (sessions.size() > 0) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

    /**
	 * Verifica o texto da linha informada, para identifica��o de nome de
	 * sess�o. <blockquote> Esta identifica��o se da atrav�s dos caracters [ e
	 * ], a string encontrada entre estes dois caracteres ser� identificada como
	 * abertura de uma nova sess�o. </blockquote>
	 * 
	 * @param line
	 *            String contendo a linha a ser analisada.
	 * @return Verdadeiro para identifica��o de um nome de sess�o e Falso para a
	 *         n�o identifica��o.
	 * 
	 * @since 04/07/2008
	 */
    private boolean ckeckNewSession(String line) {
        int ik1 = line.indexOf('[');
        int ik2 = line.indexOf(']');
        if (ik1 > -1 && ik2 > -1 && ik2 > ik1 + 1) {
            properties = new Properties();
            sessions.put(line.substring(ik1 + 1, ik2), properties);
            return true;
        }
        return false;
    }

    public Properties getSession(String session) {
        return sessions.get(session);
    }

    public void setSession(String sessionName, Properties session) {
        sessions.put(sessionName, session);
    }

    /**
	 * Retorna o valor do parametro da sess�o, ambos informados por parametro.
	 * 
	 * @param session
	 *            Nome da sess�o de parametros
	 * @param key
	 *            Nome do parametro
	 * @return Valor do parametro
	 * 
	 * @since 04/07/2008
	 */
    public String getProperty(String session, String key) {
        String value = null;
        Properties p = sessions.get(session);
        if (p != null && key != null) {
            value = p.getProperty(key);
        }
        return value;
    }

    /**
	 * Define um novo valor para o parametro da sess�o. <blockquote> Caso a
	 * refer�ncia ao parametro seja igual a <code>null</code> ou se a sess�o n�o
	 * for encontrada no mapa, o valor n�o ser� atribuido ao parametro.
	 * </blockquote>
	 * 
	 * @see java.util.Map#get(Object)
	 * @see java.util.Properties#setProperty(String, String)
	 * 
	 * @param session
	 *            Nome da sess�o
	 * @param key
	 *            Nome do parametro
	 * @param value
	 *            Novo valor para o parametro
	 * 
	 * @since 04/07/2008
	 */
    public void setProperty(String session, String key, String value) {
        Properties p = sessions.get(session);
        if (p != null && key != null && value != null) {
            p.setProperty(key, value);
        }
    }
}

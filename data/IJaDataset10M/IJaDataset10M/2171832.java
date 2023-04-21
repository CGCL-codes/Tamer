package br.com.cefetrn.apoena.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Arquivo {

    public static final String ENTER = "\r\n";

    protected static final Log LOG = LogFactory.getLog(Arquivo.class);

    /**
	 * Método responável pela leitura de qualqer arquivo.
	 * Cada linha do arquivo corresponde a um item da lista
	 * retornada. 
	 * Caso tenha lido algum arquivo ou não
	 * houve poblema na leitura, retorna List senao null.
	 * 
	 * @param pathNomeArquivo
	 * @return status da leitura.
	 */
    public static List<String> ler(String pathNomeArquivo) {
        if (pathNomeArquivo != null) {
            try {
                return read(new File(pathNomeArquivo));
            } catch (FileNotFoundException e) {
                LOG.error(" RECEBER PROCESSAMENTO " + "String pathNomeArquivo: " + pathNomeArquivo, e);
            } catch (IOException e) {
                LOG.error(" RECEBER PROCESSAMENTO " + "String pathNomeArquivo: " + pathNomeArquivo, e);
            }
        }
        return null;
    }

    /**
	 * Método responável pela leitura de qualqer arquivo.
	 * Cada linha do arquivo corresponde a um item da lista
	 * retornada. 
	 * Caso tenha lido algum arquivo ou não
	 * houve poblema na leitura, retorna List senao null.
	 * 
	 * @param pathNomeArquivo
	 * @return status da leitura.
	 */
    public static List<String> ler(File file) {
        if (file != null) {
            try {
                return read(file);
            } catch (FileNotFoundException e) {
                LOG.error(" RECEBER PROCESSAMENTO ", e);
            } catch (IOException e) {
                LOG.error(" RECEBER PROCESSAMENTO ", e);
            }
        }
        return null;
    }

    private static List<String> read(File file) throws IOException {
        List<String> arquivo = new ArrayList<String>();
        BufferedReader leitor = new BufferedReader(new FileReader(file));
        String s;
        do {
            s = leitor.readLine();
            if (s != null) {
                arquivo.add(s);
            }
        } while (s != null);
        leitor.close();
        return arquivo;
    }

    /**
	 * Método responável pela marcação de qualqer arquivo.
	 * 
	 * @param pathNomeArquivo
	 * @param tagParaArquivo
	 * @return sucesso.
	 */
    public static boolean marcarComo(String pathNomeArquivo, String tag) {
        if (pathNomeArquivo != null & tag != null) {
            File arq = new File(pathNomeArquivo);
            File novo = new File(pathNomeArquivo + tag);
            arq.renameTo(novo);
            return true;
        }
        return false;
    }

    /**
	 * Método responsável pela renomeaçaão de qualqer arquivo.
	 * 
	 * @param pathDirArquivo
	 * @param nomeArquivo
	 * @param novoNomeArquivo
	 * @return sucesso.
	 */
    public static boolean renomearComo(String pathArquivo, String nome, String novoNome) {
        if (pathArquivo != null & nome != null & novoNome != null) {
            File arq = new File(pathArquivo + "/" + nome);
            File novo = new File(pathArquivo + "/" + novoNome);
            arq.renameTo(novo);
            return true;
        }
        return false;
    }

    /**
	 * Cria um arquivo a partir de uma única string com o layout da mesma.
	 * Ou seja, se a string tem quebra de linha o arquivo também terá.
	 * 
	 * @param path
	 * @param conteudo
	 */
    public static boolean criarArquivo(String path, String conteudo) {
        LOG.debug("String path: " + path);
        LOG.debug("String conteudo: " + conteudo);
        boolean created = false;
        try {
            File arq = null;
            arq = new File(path);
            BufferedWriter escritor = new BufferedWriter(new FileWriter(arq));
            escritor.write(conteudo);
            escritor.flush();
            escritor.close();
            arq.setWritable(true);
            arq.setReadable(true);
            arq.setExecutable(true);
            created = true;
        } catch (FileNotFoundException e) {
            LOG.error(" CRIACAO DE ARQUIVO ", e);
        } catch (IOException e) {
            LOG.error(" CRIACAO DE ARQUIVO ", e);
        }
        return created;
    }

    /**
	 * Cria um arquivo com várias linhas a partir de uma lista de strings.
	 * Para cada item da lista uma linha será criada no arquivo.
	 * 
	 * @param path
	 * @param conteudo
	 */
    public static void criarArquivo(String path, List<String> conteudo) {
        LOG.debug("String path: " + path);
        LOG.debug("String conteudo: " + conteudo);
        File arq = null;
        arq = new File(path);
        criarArquivo(arq, conteudo);
    }

    /**
	 * Cria um arquivo com várias linhas a partir de uma lista de strings.
	 * Para cada item da lista uma linha será criada no arquivo.
	 * 
	 * @param path
	 * @param conteudo
	 */
    public static void criarArquivo(File file, List<String> conteudo) {
        LOG.debug("String conteudo: " + conteudo);
        try {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(file));
            for (String c : conteudo) {
                escritor.write(c);
            }
            escritor.flush();
            escritor.close();
        } catch (FileNotFoundException e) {
            LOG.error(" CRIACAO DE ARQUIVO ", e);
        } catch (IOException e) {
            LOG.error(" CRIACAO DE ARQUIVO ", e);
        }
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }
}

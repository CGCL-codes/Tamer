package br.com.eteg.curso.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import br.com.eteg.curso.util.basedados.EntidadeBanco;

public class ArquivoUtil {

    /**
	 * metodo est�tico que cria um arquivo contendo dados para 
	 * extratos em HTML e TXT
	 * @param nome nome do arquivo
	 * @param dados os dados a serem escritos
	 * @throws IOException
	 */
    public static void criarArquivoDados(String nome, StringBuilder dados) throws IOException {
        FileOutputStream fos = new FileOutputStream(nome);
        fos.write(dados.toString().getBytes());
        fos.flush();
        fos.close();
    }

    /**
	 * metodo estatico que salva os dados de uma conta bancaria em um arquivo.
	 * @param cb a conta bancaria a se salva
	 * @throws IOException
	 */
    public static void salvarEntidadeEmArquivo(EntidadeBanco entidade, String nomeArquivo) throws IOException {
        FileOutputStream fos = new FileOutputStream(nomeArquivo);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(entidade);
        oos.flush();
        oos.close();
        fos.close();
    }

    /**
	 * metodo est�tico que l�e os dados de uma conta bancaria de um arquivo
	 * @return a conta banc�ria
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public static EntidadeBanco lerEntidadeDeArquivo(String nomeArquivo) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(nomeArquivo);
        ObjectInputStream ois = new ObjectInputStream(fis);
        EntidadeBanco eb = (EntidadeBanco) ois.readObject();
        ois.close();
        fis.close();
        return eb;
    }
}

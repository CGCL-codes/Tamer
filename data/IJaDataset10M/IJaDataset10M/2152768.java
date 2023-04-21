package atualizare.tools;

import java.util.*;
import java.io.*;

/** 
 * Este classe verifica formatos de arquivos, copia arquivos e removeo arquivos
 *
 * @author 	Edney Ramalho e Oliveira
 * @version	v1.0 2002 Julho 03
 */
public class FileTools {

    /**
	 * Remove um arquivo
	 *
	 * @param	O arquivo a ser removido
	 * @return	True se o arquivo foi removido<br>
	 *		False se o arquivo n�o foi removido
	 */
    public static boolean removeArquivo(String file) {
        try {
            File arquivo = new File(file);
            return arquivo.delete();
        } catch (Exception e) {
            System.out.println("FileTools.removeArquivo - " + e.getMessage());
        }
        return false;
    }

    /**
	 * Renomear um arquivo
	 *
	 * @param	O arquivo a ser renomeado
	 * @param	O novo nome do arquivo
	 * @return	True se o arquivo foi renomeado<br>
	 *		False se o arquivo n�o foi renomeado
	 */
    public static boolean renomearArquivo(String file, String novoNome) {
        try {
            File arquivo = new File(file);
            File destino = new File(novoNome);
            if (!arquivo.renameTo(destino)) {
                if (copiaArquivo(file, novoNome)) {
                    removeArquivo(file);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("FileTools.renomearArquivo - " + e.getMessage());
        }
        return false;
    }

    /**
	 * Faz uma c�pia do arquivo
	 *
	 * @param	O arquivo a ser copiado
	 * @param	O novo nome do arquivo
	 * @return	True se o arquivo foi copiado com sucesso<br>
	 *			False se o arquivo n�o foi renomeado
	 */
    public static boolean copiaArquivo(String file, String path) {
        try {
            FileInputStream in = new FileInputStream(file);
            FileOutputStream out = new FileOutputStream(path);
            int temp = 0;
            byte[] bt = new byte[1];
            int cont = 1;
            while ((temp = in.read(bt)) > 0) {
                out.write(bt);
                cont++;
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            System.out.println("Erro no copyFile:" + e.getMessage());
        }
        return false;
    }

    /**
	 * Verifica se um arquivo � do formato imagem (GIF, PNG ou JPEG/JPG)
	 *
	 * @param	O arquivo a ser verificar
	 * @throw	Lan�a exce��o se o arquivo n�o for uma imagem
	 */
    public static void checkImagem(String file) throws ImagemInvalidaException {
        try {
            InputStream in = new FileInputStream(file);
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setInput(in);
            imageInfo.setDetermineImageNumber(true);
            imageInfo.setCollectComments(true);
            if (!imageInfo.check()) {
                removeArquivo(file);
                String nomeArquivo = file.substring(file.lastIndexOf(File.separator) + 1);
                throw new ImagemInvalidaException("Formato do arquivo <b>" + nomeArquivo + "</b> enviado n�o � suportado (envie apenas GIF, JPEG/JPG ou PNG).");
            }
        } catch (IOException e) {
            System.out.println("Erro no checkImagem: " + e.getMessage());
            throw new ImagemInvalidaException("N�o foi poss�vel obter a foto. Envie novamente");
        }
    }

    /**
	 * Verifica se j� existe um arquivo com o mesmo nome e caso exista
	 * retorna o novo nome para que n�o ocorra substitui��o de arquivos
	 *
	 * @param	O nome do arquivo a ser testado
	 * @return	O nome do arquivo modificado ou n�o
	 */
    public static String renomearFotos(String caminho, String foto) {
        while (true) {
            File file = new File(caminho + foto);
            if (file.exists()) {
                foto = "_" + foto;
            } else {
                break;
            }
        }
        return foto;
    }
}

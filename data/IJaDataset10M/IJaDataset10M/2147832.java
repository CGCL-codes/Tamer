package jsimulator.src.br.ufrj.dcc.arquitetura.controller;

import java.io.File;
import jsimulator.src.br.ufrj.dcc.arquitetura.model.FileModel;

/***
 * Controller para se comunicar com o FileModel
 */
public abstract class FileController {

    /***
	 * Inst�ncia do FileModel
	 */
    private static final FileModel model = new FileModel();

    /***
	 * Executa o m�todo de abrir arquivo do model
	 */
    public static void abrirArquivo() {
        model.abrirArquivo();
    }

    /***
	 * Executa o m�todo de salvar o arquivo do model
	 * @param buffer String para gravar no arquivo texto
	 * @param dir �ltimo File aberto
	 */
    public static void salvarArquivo(String buffer, File dir) {
        model.salvarArquivo(buffer, dir);
    }

    /***
	 * Executa o m�todo de salvar como do model
	 * @param buffer String para gravar no arquivo texto
	 */
    public static void salvarComoArquivo(String buffer) {
        model.salvarComoArquivo(buffer);
    }
}

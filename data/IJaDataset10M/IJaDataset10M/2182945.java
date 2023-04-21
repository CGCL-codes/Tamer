package br.com.jteam.jfcm.model.services;

import java.io.File;
import br.com.jteam.jfcm.model.FileHistory;
import br.com.jteam.jfcm.model.services.exception.FileListContentServiceException;

/**
 * <p>
 * Respons&aacute;vel por manipular o conte&uacute;do de um arquivo .ZIP
 * 
 * @author Rafael Naufal <rafael.naufal@gmail.com>
 * @since 1.0
 * @version $Id$
 */
public interface FileListContentService {

    /**
	 * <p>
	 * Lista os arquivos que s&atilde;o conte&uacute;do de um arquivo .ZIP.
	 * 
	 * @param file -
	 *            arquivo .ZIP a ser aberto
	 * @return - uma inst�ncia da classe <code>ZipFile</code>, contendo uma
	 *         lista de <code>File</code>.
	 * @throws FileListContentServiceException -
	 *             Caso o arquivo seja nulo ou n&atilde;o seja um arquivo .ZIP
	 */
    br.com.jteam.jfcm.model.File openZipContent(File file) throws FileListContentServiceException;

    /**
	 * <p>
	 * Retorna o hist&oacute;rico de arquivos.
	 * 
	 * @return hist&oacute;rico de arquivos.
	 */
    FileHistory getHistory();
}

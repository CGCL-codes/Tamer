package br.com.jteam.jfcm.model.services.exception;

/**
 * <p>
 * Exce&ccedil;&atilde;o que representa que determinado arquivo n&atilde;o
 * &eacute; um arquivo .ZIP.
 * 
 * @link {br.ita.poo.fcv.model.FileType #ZIP}
 * 
 * @author Rafael Naufal <rafael.naufal@gmail.com>
 * @since 1.0
 * @version $Id$
 */
public class FileIsNotAZipException extends IncorrectFileExtensionException {

    /**
	 * <p>
	 * Construtor sem argumento.
	 * 
	 */
    public FileIsNotAZipException() {
        super();
    }

    /**
	 * <p>
	 * Construtor com mensagem a ser passada para a exce&ccedil;&atilde;o.
	 * 
	 * @param message -
	 *            mensagem da exce&ccedil;&atilde;o.
	 */
    public FileIsNotAZipException(String message) {
        super(message);
    }

    /**
	 * <p>
	 * Construtor com mensagem e causa da exce&ccedil;&atilde;o.
	 * 
	 * @param message -
	 *            mensagem da exce&ccedil;&atilde;o.
	 * @param cause -
	 *            causa da exce&ccedil;&atilde;o.
	 */
    public FileIsNotAZipException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * <p>
	 * Construtor com causa da exce&ccedil;&atilde;o.
	 * 
	 * @param cause -
	 *            causa da exce&ccedil;&atilde;o.
	 */
    public FileIsNotAZipException(Throwable cause) {
        super(cause);
    }
}

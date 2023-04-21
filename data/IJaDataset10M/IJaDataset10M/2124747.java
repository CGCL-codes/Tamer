package br.com.jteam.jfcm.model;

/**
 * <p>
 * Classe respons&aacute;vel por representar um arquivo
 * {@link br.com.minaurod.fcm.model.FileType#PNG} no modelo OO
 * 
 * @author Rafael Naufal <rafael.naufal@gmail.com>
 * @since 1.0
 * @version $Id$
 */
public class PngFile extends File {

    /**
	 * <p>
	 * Construtor da classe <code>PngFile</code>.
	 * 
	 * @param name -
	 *            Nome do arquivo.
	 * @param path -
	 *            Caminho do arquivo
	 * @throws Pode
	 *             lan&ccedil;ar IllegalArgumentException caso o nome ou o caminho do
	 *             arquivo seja nulo.
	 */
    public PngFile(String name, String path) {
        super(name, path);
    }
}

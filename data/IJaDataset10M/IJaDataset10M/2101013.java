package br.com.jteam.jfcm.gui;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import br.com.jteam.jfcm.model.FileType;

/**
 * <p>
 * Classe que representa um arquivo visualiz&aacute;vel ({@link br.com.jteam.jfcm.model.FileType})
 * na camada de apresenta&ccedil;&atilde;o.
 * 
 * @author Rafael Naufal <rafael.naufal@gmail.com>
 * @version $Id$
 * @param <T> -
 *            Tipo parametrizado em fun&ccedil;&atilde;o do conte&uacute;o, pode
 *            ser TEXTO ou IMAGEM.
 */
public class PresentationFile<T> implements Iterable<PresentationFile<?>> {

    /**
	 * <p>
	 * Nome do arquivo.
	 */
    private String name;

    /**
	 * <p>
	 * Caminho do arquivo.
	 */
    private String path;

    /**
	 * <p>
	 * Lista de arquivos-filho pertencentes a este arquivo (caso seja um arquivo
	 * {@link br.com.jteam.jfcm.model.FileType#ZIP})
	 */
    private List<PresentationFile<?>> presentationFiles;

    /**
	 * <p>
	 * Conte&uacute;do do arquivo, que pode ser TEXTO ou IMAGEM.
	 */
    private T content;

    /**
	 * <p>
	 * Tipo do arquivo a ser visualizado, que pode ser
	 * {@link br.com.jteam.jfcm.gui.RenderedFileType}
	 */
    private RenderedFileType renderedFileType;

    /**
	 * <p>
	 * Contrutor.
	 * 
	 * @param name -
	 *            nome do arquivo.
	 * @param path -
	 *            caminho do arquivo.
	 * @param fileType -
	 *            tipo do arquivo.
	 * @throws IllegalArgumentException -
	 *             caso o nome, caminho ou tipo do arquivo sejam nulos.
	 */
    public PresentationFile(String name, String path, FileType fileType) {
        if (name == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }
        if (path == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (fileType == null) {
            throw new IllegalArgumentException("File type cannot be null");
        }
        this.name = name;
        this.path = path;
        this.presentationFiles = new LinkedList<PresentationFile<?>>();
    }

    /**
	 * <p>
	 * Retorna o nome do arquivo a ser visualizado.
	 * 
	 * @return - nome do arquivo.
	 */
    public String getName() {
        return name;
    }

    /**
	 * <p>
	 * Retorna o caminho do arquivo a ser visualizado.
	 * 
	 * @return - caminho do arquivo.
	 */
    public String getPath() {
        return path;
    }

    /**
	 * <p>
	 * Retorna o conte&uacute;do do arquivo a ser visualizado.
	 * 
	 * @return - nome do arquivo.
	 */
    public T getContent() {
        return content;
    }

    /**
	 * <p>
	 * Define o conte&uacute;do do arquivo a ser visualizado.
	 * 
	 * @param content
	 *            conte&uacute;do do arquivo.
	 */
    public void setContent(T content) {
        this.content = content;
    }

    /**
	 * <p>
	 * Retorna o tipo do arquivo a ser renderizado, que pode ser
	 * {@link br.com.jteam.jfcm.gui.RenderedFileType#TEXT} ou
	 * {@link br.com.jteam.jfcm.gui.RenderedFileType#IMAGE}
	 * 
	 * @return - tipo do arquivo a ser visualizado.
	 */
    public RenderedFileType getRenderedFileType() {
        return renderedFileType;
    }

    /**
	 * <p>
	 * Define o tipo do arquivo a ser renderizado, que pode ser
	 * {@link br.com.jteam.jfcm.gui.RenderedFileType#TEXT} ou
	 * {@link br.com.jteam.jfcm.gui.RenderedFileType#IMAGE}
	 * 
	 * @param renderedFileType
	 *            Tipo do arquivo a ser visualizado.
	 */
    public void setRenderedFileType(RenderedFileType renderedFileType) {
        this.renderedFileType = renderedFileType;
    }

    /**
	 * <p>
	 * Adi&ccedil;&atilde;o de um arquivo na lista de arquivos de
	 * apresenta&ccedil;&atilde;o.
	 * 
	 * @param presentationFile
	 *            arquivo a ser adicionado na lista de arquivos. Caso este
	 *            arquivo seja nulo, nada &eacute; feito.
	 * @throws UnsupportedOperationException
	 *             caso a opera&ccedil;&atilde;o <code>add</code> n&atilde;o
	 *             seja suportada por este arquivo.
	 */
    @SuppressWarnings("unchecked")
    public void add(PresentationFile<?> file) {
        if (file == null) {
            return;
        }
        presentationFiles.add(file);
    }

    /**
	 * <p>
	 * Remo&ccedil;&atilde;o de um arquivo da lista de arquivos de
	 * apresenta&ccedil;&atilde;o.
	 * 
	 * @param presentationFile
	 *            Arquivo a ser removido da lista de arquivos. Caso este arquivo
	 *            seja nulo, nada &eacute; feito.
	 * @throws UnsupportedOperationException
	 *             caso a opera&ccedil;&atilde;o <code>remove</code>
	 *             n&atilde;o seja suportada por este arquivo.
	 */
    public void remove(PresentationFile<T> presentationFile) {
        if (presentationFile != null) {
            presentationFiles.remove(presentationFile);
        }
    }

    /**
	 * 
	 * <p>
	 * Obten&ccedil;&atilde;o de um arquivo da lista de arquivos de
	 * apresenta&ccedil;&atilde;o.
	 * 
	 * @param index
	 *            arquivo a ser obtido na lista de arquivos. Caso este
	 *            &iacute;ndice seja menor que zero ou maior que o tamanho
	 *            corrente da lista de arquivos, retorna <code>null</code>.
	 * @throws UnsupportedOperationException
	 *             caso a opera&ccedil;&atilde;o <code>get</code> n&atilde;o
	 *             seja suportada por este arquivo.
	 */
    public PresentationFile<T> get(int index) {
        if (index >= 0 && index < presentationFiles.size()) {
            presentationFiles.get(index);
        }
        return null;
    }

    /**
	 * <p>
	 * Produz um <code>iterator</code> da lista de arquivos corrente.
	 * 
	 * @return iterator da lista criado.
	 * 
	 * @throws UnsupportedOperationException
	 *             caso a opera&ccedil;&atilde;o <code>iterator</code>
	 *             n&atilde;o seja suportada por este arquivo.
	 */
    public Iterator<PresentationFile<?>> iterator() {
        List<PresentationFile<?>> unmodifiableFiles = Collections.unmodifiableList(presentationFiles);
        return (Iterator<PresentationFile<?>>) unmodifiableFiles.iterator();
    }

    /**
	 * Determina se um arquivo de apresenta&ccedil;�o &eacute; arquivo texto.
	 * @return <code>true</code> se for texto, <code>false</code> em caso contr&aacute;rio.
	 */
    public boolean isTextFile() {
        return renderedFileType == RenderedFileType.TEXT;
    }

    /**
	 * Determina se um arquivo de apresenta&ccedil;�o &eacute; uma imagem.
	 * @return <code>true</code> se for imagem, <code>false</code> em caso contr&aacute;rio.
	 */
    public boolean isImageFile() {
        return renderedFileType == RenderedFileType.IMAGE;
    }

    /**
	 * <p>
	 * Produz uma String que representa visualmente o arquivo.
	 */
    @Override
    public String toString() {
        StringBuilder fileDescription = new StringBuilder();
        fileDescription.append("File:");
        fileDescription.append(" ");
        fileDescription.append("[");
        fileDescription.append("Name:");
        fileDescription.append(" ");
        fileDescription.append(name);
        fileDescription.append(",");
        fileDescription.append(" ");
        fileDescription.append(path);
        fileDescription.append(" ");
        fileDescription.append("]");
        return fileDescription.toString();
    }
}

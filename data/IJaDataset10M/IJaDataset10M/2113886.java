package unbbayes.gui;

import javax.swing.filechooser.*;
import java.io.File;
import java.util.ResourceBundle;

/**
 *  Classe que extende <code>FileFilter</code>, respons�vel por filtrar
 *  os tipos de arquivo a mostrar.
 *
 *@author     Rommel N. Carvalho, Michael S. Onishi
 *@created    27 de Junho de 2001
 *@see        FileFilter
 */
public class SimpleFileFilter extends FileFilter {

    private String[] extensions;

    private String description;

    /** Load resource file from this package */
    private static ResourceBundle resource = ResourceBundle.getBundle("unbbayes.gui.resources.GuiResources");

    /**
     *  Constr�i um <code>FileFilter</code> com a extens�o desejada.
     *
     *@param  ext  extens�o (<code>String</code>) dos arquivos a mostrar
     *@see         FileFilter
     */
    public SimpleFileFilter(String ext) {
        this(new String[] { ext }, null);
    }

    /**
     *  Constr�i um <code>FileFilter</code> com as extens�es desejadas e
     *  descri��o dessas extens�es.
     *
     *@param  exts   um array de <code>String</code> (extens�es desejadas)
     *@param  descr  descri��o das extens�es (<code>String</code>)
     *
     *@see           String
     */
    public SimpleFileFilter(String[] exts, String descr) {
        extensions = new String[exts.length];
        for (int i = exts.length - 1; i >= 0; i--) {
            extensions[i] = exts[i].toLowerCase();
        }
        description = (descr == null ? exts[0] + resource.getString("filesText") : descr);
    }

    /**
     *  Retorna a descri��o geral.
     *
     *@return    descri��o (<code>String</code>) geral
     *@see       String
     */
    public String getDescription() {
        return description;
    }

    /**
     *  Verifica se o arquivo desejado possui a extens�o que a classe filtra.
     *
     *@param  f  arquivo (<code>File</code>) a verificar
     *@return    true se possuir a extens�o correta e false caso contr�rio
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String name = f.getName().toLowerCase();
        for (int i = extensions.length - 1; i >= 0; i--) {
            if (name.endsWith(extensions[i])) {
                return true;
            }
        }
        return false;
    }
}

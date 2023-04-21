package br.ufal.npd.fox.document.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import br.ufal.npd.fox.commons.StringUtil;
import br.ufal.npd.fox.commons.lang.FoxDocumentException;
import br.ufal.npd.fox.document.FoxDocumentIF;

/**
 * @author �dle M�rcio (edlemarcio@gmail.com) 31/07/2006
 * @version 1.0.0
 * 
 * <br><br>
 * 16/01/2008 BUGFIX - Alterado o m�todo getText() - Na convers�o de arquivo txt
 * para String o a linha atual estava ficando colada com a seguinte.
 * 
 */
public class TXTDocument implements FoxDocumentIF {

    /**
     * Caminho f�sico do arquivo <code>pathFile</code>
     * 
     * @uml.property name="file"
     */
    private InputStream file;

    /**
     * @param inputStream
     *                Objeto InputStream (arquivo que ser� convertido)
     */
    public TXTDocument(InputStream inputStream) {
        super();
        this.file = inputStream;
    }

    public String getText() throws FoxDocumentException {
        String bodyText = StringUtil.STRING_EMPTY;
        String line = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.file));
            while ((line = br.readLine()) != null) {
                bodyText += StringUtil.STRING_SPACING + line;
            }
            return bodyText;
        } catch (Exception e) {
            throw new FoxDocumentException(e.getMessage(), e.getCause());
        }
    }

    /**
     * @return InputStream
     * @uml.property name="file"
     */
    public InputStream getFile() {
        return this.file;
    }

    /**
     * @param file
     * @uml.property name="file"
     */
    public void setFile(InputStream file) {
        this.file = file;
    }
}

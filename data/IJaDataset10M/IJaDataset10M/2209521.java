package br.ufal.npd.fox.document.impl;

import java.io.InputStream;
import org.textmining.text.extraction.WordExtractor;
import br.ufal.npd.fox.commons.lang.FoxDocumentException;
import br.ufal.npd.fox.document.FoxDocumentIF;

/**
 * @author �dle M�rcio (edlemarcio@gmail.com) 31/07/2006
 * @version 1.0.0
 *
 */
public class TextMiningWordDocument implements FoxDocumentIF {

    private InputStream file;

    /**
	 * 
	 */
    public TextMiningWordDocument(InputStream is) {
        super();
        this.file = is;
    }

    public String getText() throws FoxDocumentException {
        String bodyText = null;
        try {
            bodyText = new WordExtractor().extractText(this.file);
            if (bodyText != null) {
                return bodyText;
            } else {
                throw new FoxDocumentException(FoxDocumentException.NULL_FILE_EXCEPTION);
            }
        } catch (Exception e) {
            throw new FoxDocumentException(e.getMessage(), e.getCause());
        }
    }
}

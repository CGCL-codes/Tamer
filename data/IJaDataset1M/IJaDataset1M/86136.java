package br.ufal.npd.fox.document.impl;

import java.io.InputStream;
import br.ufal.npd.fox.commons.StringUtil;
import br.ufal.npd.fox.commons.lang.FoxDocumentException;
import br.ufal.npd.fox.document.FoxDocumentIF;

public class NekoHTMLDocument implements FoxDocumentIF {

    /**
	 * @param is - Objeto InputStream.
	 */
    public NekoHTMLDocument(InputStream is) {
        super();
    }

    public String getText() throws FoxDocumentException {
        return StringUtil.STRING_EMPTY;
    }
}

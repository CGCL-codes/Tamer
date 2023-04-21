package net.sourceforge.dita4publishers.impl.dita;

import net.sourceforge.dita4publishers.api.dita.DitaApiException;
import net.sourceforge.dita4publishers.api.dita.DitaKeyDefinitionContext;
import net.sourceforge.dita4publishers.api.dita.KeyAccessOptions;
import org.w3c.dom.Document;

/**
 *
 */
public class KeyDefinitionContextImpl implements DitaKeyDefinitionContext {

    private String rootMapId;

    private boolean outOfDate = true;

    private KeyAccessOptions keyAccessOptions = new KeyAccessOptions();

    private Document rootMapDoc = null;

    /**
	 * @param rootMap
	 */
    public KeyDefinitionContextImpl(Document rootMap) throws DitaApiException {
        this.rootMapId = rootMap.getDocumentURI();
        this.rootMapDoc = rootMap;
    }

    public String getRootMapId() throws DitaApiException {
        return this.rootMapId;
    }

    public boolean isOutOfDate() throws DitaApiException {
        return this.outOfDate;
    }

    public void setOutOfDate(boolean outOfDate) throws DitaApiException {
        this.outOfDate = outOfDate;
    }

    public void setUpToDate() throws DitaApiException {
        this.outOfDate = false;
    }

    /**
	 * @throws DitaApiException 
	 * 
	 */
    public void setOutOfDate() throws DitaApiException {
        this.setOutOfDate(true);
    }

    public KeyAccessOptions getKeyAccessOptions() {
        return this.keyAccessOptions;
    }

    public void setKeyAccessOptions(KeyAccessOptions keyAccessOptions) {
        this.keyAccessOptions = keyAccessOptions;
    }

    @Override
    public Document getRootMapDoc() {
        return this.rootMapDoc;
    }
}

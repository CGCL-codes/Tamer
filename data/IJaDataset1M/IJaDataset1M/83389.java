package org.akrogen.tkui.core.internal.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.akrogen.tkui.core.ITkuiConfiguration;
import org.akrogen.tkui.core.dom.ITkuiDocument;
import org.akrogen.tkui.core.dom.ITkuiElementProvider;
import org.akrogen.tkui.core.exceptions.TkuiLoaderException;
import org.akrogen.tkui.core.helpers.dom.TkuiDocumentHelper;
import org.akrogen.tkui.core.internal.dom.TkuiDocumentImpl;
import org.akrogen.tkui.core.loader.ITkuiLoader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public abstract class AbstractTkuiLoaderImpl implements ITkuiLoader {

    private ITkuiConfiguration configuration;

    public void initialize(ITkuiConfiguration configuration) throws TkuiLoaderException {
        this.configuration = configuration;
        if (configuration == null) throw new TkuiLoaderException("Tk-UI configuration cannot be null.");
        ITkuiElementProvider[] providers = getElementProviders();
        if (providers == null || providers.length < 1) throw new TkuiLoaderException("Tk-UI element providers cannot be null or empty. getElementProviders method must return element providers.");
        for (int i = 0; i < providers.length; i++) {
            ITkuiElementProvider provider = providers[i];
            configuration.registerElementProvider(provider);
        }
    }

    public ITkuiConfiguration getConfiguration() {
        return configuration;
    }

    protected abstract ITkuiElementProvider[] getElementProviders();

    public ITkuiDocument newDocument(String guiBuilderId, Object documentContainer) {
        return newDocument(guiBuilderId, documentContainer, null);
    }

    public ITkuiDocument newDocument(String guiBuilderId, Object documentContainer, Object[] guiParameters) {
        ITkuiDocument document = getTkuiDocumentInstance();
        document.setConfiguration(configuration);
        document.setGuiBuilderId(guiBuilderId);
        document.setDocumentContainer(documentContainer);
        document.setURIResolver(configuration.getURIResolver());
        document.setGuiParameters(guiParameters);
        return document;
    }

    public ITkuiDocument newDocument() {
        ITkuiDocument document = getTkuiDocumentInstance();
        document.setConfiguration(configuration);
        document.setURIResolver(configuration.getURIResolver());
        return document;
    }

    public void load(InputStream sourceStream, ITkuiDocument document) throws TkuiLoaderException, IOException, SAXException {
        loadDocument(sourceStream, document);
    }

    public void load(Reader sourceReader, ITkuiDocument document) throws TkuiLoaderException, IOException, SAXException {
        loadDocument(sourceReader, document);
    }

    protected void loadDocument(Object source, ITkuiDocument document) throws TkuiLoaderException, IOException, SAXException {
        Element sourceElement = (source instanceof InputStream ? loadXMLSource((InputStream) source) : loadXMLSource((Reader) source));
        loadGuiDocument(sourceElement, document);
    }

    public Element loadXMLSource(InputStream sourceStream) throws TkuiLoaderException {
        return configuration.loadXMLSource(sourceStream);
    }

    public Element loadXMLSource(Reader sourceReader) throws TkuiLoaderException {
        return configuration.loadXMLSource(sourceReader);
    }

    public void loadGuiDocument(Element sourceElement, ITkuiDocument document) throws TkuiLoaderException {
        loadGuiDocument(sourceElement, document, true);
    }

    public void loadGuiDocument(Element sourceElement, ITkuiDocument document, boolean searchViewRoot) throws TkuiLoaderException {
        if (searchViewRoot) {
            sourceElement = configuration.getViewRoot(sourceElement, this);
        }
        onBeforeDOMDocumentImported(sourceElement, document);
        sourceElement = TkuiDocumentHelper.importElement(sourceElement, document);
        if (sourceElement == null) throw new TkuiLoaderException("Error while loading XML Document. XML View cannot be null!");
        onBeforeDOMDocumentTotallyParsed(document);
        if (document instanceof TkuiDocumentImpl) {
            TkuiDocumentImpl tkuiDocument = ((TkuiDocumentImpl) document);
            tkuiDocument.onDOMDocumentTotallyParsed();
        }
        onAfterDOMDocumentTotallyParsed(document);
    }

    protected abstract ITkuiDocument getTkuiDocumentInstance();

    protected void onBeforeDOMDocumentImported(Element sourceElement, ITkuiDocument xulDocument) {
    }

    protected void onBeforeDOMDocumentTotallyParsed(ITkuiDocument xulDocument) {
    }

    protected void onAfterDOMDocumentTotallyParsed(ITkuiDocument xulDocument) {
    }

    public String getGuiBuilderId(Element sourceElement, ITkuiDocument document) {
        return null;
    }

    public boolean isViewRootNode(Node node) {
        return true;
    }
}

package gate.teamware.richui.annotationdiffgui;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.event.AnnotationEvent;
import gate.event.AnnotationListener;
import gate.event.AnnotationSetEvent;
import gate.event.AnnotationSetListener;
import gate.event.DocumentEvent;
import gate.event.DocumentListener;
import gate.teamware.richui.annotationdiffgui.gui.MainFrame;
import gate.teamware.richui.common.RichUIException;
import gate.teamware.richui.common.RichUIUtils;
import gleam.docservice.proxy.DocServiceProxy;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DocserviceConnection extends Connection implements Constants, AnnotationListener, AnnotationSetListener, DocumentListener {

    private static final boolean DEBUG = false;

    private String docserviceUrlString;

    private String docId;

    private DocServiceProxy dsProxy;

    private String annotationSetName = "";

    private Document document;

    private boolean documentModified = false;

    private Set<String> loadedAnnotationSets;

    private DocserviceConnection() {
    }

    /**
   * Constructor
   * @param docserviceUrlString
   * @param docId
   * @param annotationSetName
   *          annotation set name to load. If == "" (empty string) - all
   *          annotation sets will be loaded. NULL corresponds to default
   *          annotation set.
   * @throws RichUIException
   */
    public DocserviceConnection(String docserviceUrlString, final String docId) throws RichUIException {
        this.docserviceUrlString = docserviceUrlString;
        this.docId = docId;
        try {
            URI uri = new URI(this.docserviceUrlString);
            this.dsProxy = RichUIUtils.getDocServiceProxy(uri);
        } catch (URISyntaxException e) {
            throw new RichUIException("An error occured while connecting to Document Service at:\n" + this.docserviceUrlString + "\n\n" + e.getMessage(), e);
        }
    }

    public String getDocserviceUrlString() {
        return docserviceUrlString;
    }

    public String getDocId() {
        return docId;
    }

    public Document getDocument() {
        return document;
    }

    public String getAnnotationSetName() {
        return annotationSetName;
    }

    public boolean isDocumentAnnotationsModified() {
        return documentModified;
    }

    public String getConnectionStatus() {
        String serviceLocation = docserviceUrlString;
        try {
            URL url = new URL(serviceLocation);
            serviceLocation = url.getHost() + url.getPath();
        } catch (MalformedURLException e) {
        }
        return "Connected in DIRECT mode: " + docId;
    }

    public Document loadDocument() throws RichUIException {
        if (this.document == null) {
            try {
                this.document = dsProxy.getDocumentContentOnly(docId);
                loadedAnnotationSets = new HashSet<String>();
                if (annotationSetName == null) {
                    loadedAnnotationSets.add(null);
                } else if (annotationSetName.length() == 0) {
                    loadedAnnotationSets.addAll(Arrays.asList(this.dsProxy.getAnnotationSetNames(this.document)));
                } else {
                    loadedAnnotationSets.add(annotationSetName);
                }
                for (String name : loadedAnnotationSets) {
                    this.dsProxy.getAnnotationSet(this.document, name, name, false);
                    AnnotationSet theSet = null;
                    if (name == null || "".equals(name)) {
                        theSet = this.document.getAnnotations();
                    } else {
                        theSet = this.document.getAnnotations(name);
                    }
                    theSet.addAnnotationSetListener(this);
                    for (Annotation a : theSet) {
                        a.addAnnotationListener(this);
                    }
                }
            } catch (Exception e) {
                throw new RichUIException(e.getMessage(), e);
            }
        }
        return this.document;
    }

    public void saveDocument() throws RichUIException {
        if (this.document != null) {
            try {
                for (String asName : loadedAnnotationSets) {
                    this.dsProxy.saveAnnotationSet(this.document, asName, asName, true);
                }
                this.documentModified = false;
                MainFrame.getInstance().updateBottomStatus();
            } catch (Exception e) {
                throw new RichUIException(e.getMessage(), e);
            }
        }
    }

    public void cleanup() throws RichUIException {
        if (this.document != null) {
            try {
                this.dsProxy.release(this.document);
                this.document.removeDocumentListener(this);
                Factory.deleteResource(this.document);
            } catch (Exception e) {
                throw new RichUIException(e.getMessage(), e);
            }
        }
    }

    public void annotationSetAdded(DocumentEvent e) {
        this.document.getAnnotations(e.getAnnotationSetName()).addAnnotationSetListener(this);
        this.documentModified = true;
        MainFrame.getInstance().updateBottomStatus();
    }

    public void annotationSetRemoved(DocumentEvent e) {
        this.documentModified = true;
        MainFrame.getInstance().updateBottomStatus();
    }

    public void contentEdited(DocumentEvent e) {
        this.documentModified = true;
        MainFrame.getInstance().updateBottomStatus();
    }

    public void annotationUpdated(AnnotationEvent e) {
        this.documentModified = true;
        MainFrame.getInstance().updateBottomStatus();
    }

    public void annotationAdded(AnnotationSetEvent e) {
        e.getAnnotation().addAnnotationListener(this);
        this.documentModified = true;
        MainFrame.getInstance().updateBottomStatus();
    }

    public void annotationRemoved(AnnotationSetEvent e) {
        e.getAnnotation().removeAnnotationListener(this);
        this.documentModified = true;
        MainFrame.getInstance().updateBottomStatus();
    }
}

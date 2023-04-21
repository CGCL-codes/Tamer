package org.progeeks.mnotes;

import java.io.IOException;
import org.progeeks.form.*;
import org.progeeks.form.style.*;
import org.progeeks.meta.LongStringType;
import org.progeeks.util.ProgressReporter;
import org.progeeks.util.log.Log;
import org.progeeks.view.BindableViewContext;

/**
 *  Holds a document and related editing logic.
 *
 *  @version   $Revision: 4249 $
 *  @author    Paul Speed
 */
public class DocumentEditorContext extends BindableViewContext {

    static Log log = Log.getLog();

    public static final String PROP_DOCUMENT = "document";

    public static final String PROP_CONTENT = "content";

    public static final String PROP_CHANGED = "changed";

    static {
        DefaultForm form = new DefaultForm();
        form.addField("content", new LongStringType()).addStyles(FillStyle.BOTH, LabelStyle.NONE);
        Main.viewEnv.registerForm(DocumentEditorContext.class, form);
    }

    public DocumentEditorContext() {
        this(null);
    }

    public DocumentEditorContext(Document doc) {
        super(Main.EXP);
        if (doc != null) setDocument(doc);
    }

    public void setDocument(Document doc) {
        if (log.isTraceEnabled()) log.trace("setDocument(" + doc + ")");
        if (setObjectProperty(PROP_DOCUMENT, doc)) {
            if (getParentContext() == null) setContent(null); else loadContent();
        }
    }

    public Document getDocument() {
        return getObjectProperty(PROP_DOCUMENT);
    }

    /**
     *  Sets the document without clearing or changing the
     *  current content.
     */
    public void renameDocument(Document doc) {
        if (log.isTraceEnabled()) log.trace("setDocument(" + doc + ")");
        setObjectProperty(PROP_DOCUMENT, doc);
        setChanged(true);
    }

    protected void attach() {
        super.attach();
        if (getContent() == null && getDocument() != null) loadContent();
    }

    protected void loadContent() {
        log.trace("loadContent()");
        DocumentWindowContext root = findParent(DocumentWindowContext.class);
        Document doc = getDocument();
        if (root == null || doc == null) {
            setContent(null);
            return;
        }
        ProgressReporter pr = root.getRequestHandler().requestProgressReporter("Loading File", doc.getName(), -1, -1);
        try {
            String content = doc.loadContent(pr);
            setContent(content);
            setChanged(false);
        } catch (IOException e) {
            throw new RuntimeException("Error loading document:" + doc.getLocation(), e);
        }
    }

    /**
     *  Saves the current contents to the current document location.
     */
    public void save() {
        DocumentWindowContext root = findParent(DocumentWindowContext.class);
        Document doc = getDocument();
        if (root == null || doc == null) return;
        ProgressReporter pr = root.getRequestHandler().requestProgressReporter("Saving File", doc.getName(), -1, -1);
        try {
            doc.saveContent(getContent(), pr);
            setChanged(false);
        } catch (IOException e) {
            throw new RuntimeException("Error loading document:" + doc.getLocation(), e);
        }
    }

    public void setContent(String content) {
        setStringProperty(PROP_CONTENT, content);
        setChanged(true);
    }

    public String getContent() {
        return getStringProperty(PROP_CONTENT, null);
    }

    protected void setChanged(boolean b) {
        setBooleanProperty(PROP_CHANGED, b);
    }

    public boolean isChanged() {
        return getBooleanProperty(PROP_CHANGED, false);
    }

    public String toString() {
        return "DocumentEditorContext[" + getDocument() + "]";
    }
}

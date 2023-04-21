package uk.co.ordnancesurvey.confluence;

import java.io.Serializable;
import org.semanticweb.owl.model.OWLOntology;
import uk.co.ordnancesurvey.confluence.model.ConfluenceModelManager;
import uk.co.ordnancesurvey.confluence.ui.IConfluenceWorkspace;

/**
 * Base class for all objects that are defined in Confluence and don't inherit
 * from another class. It provides convenience methods to retrieve common
 * classes like {@link ConfluenceEditorKit}.
 * 
 * @author rdenaux
 * 
 */
public class ObjectInConfluence implements Serializable {

    private static final long serialVersionUID = -3057956279807060800L;

    private IConfluenceEditorKit editorKit;

    public ObjectInConfluence(IConfluenceEditorKit aEditorKit) {
        editorKit = aEditorKit;
    }

    /**
	 * Returns the confluence editorKit
	 * 
	 * @return
	 */
    protected final IConfluenceEditorKit getEditorKit() {
        return editorKit;
    }

    /**
	 * Returns the workspace for this {@link ObjectInConfluence}.
	 * 
	 * @return
	 */
    protected final IConfluenceWorkspace getWorkspace() {
        return getEditorKit().getConfluenceWorkspace();
    }

    /**
	 * Returns the model manager
	 * 
	 * @return
	 */
    protected final ConfluenceModelManager getModelManager() {
        return getEditorKit().getConfluenceModelManager();
    }

    /**
	 * Returns the active ontology. Note that this might be null.
	 * 
	 * @return
	 */
    protected final OWLOntology getActiveOntology() {
        return getModelManager().getActiveOntology();
    }
}

package uk.co.ordnancesurvey.confluence.ui.view.instance;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.framelist.OWLFrameList;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import uk.co.ordnancesurvey.confluence.ui.frame.ConfluenceEntityAnnotationFrame;
import uk.co.ordnancesurvey.confluence.vocab.VocabularyAdapter;

/**
 * Provides a viewcomponent with an {@link OWLFrameList2} showing an annotation
 * frame. Subclasses can instantiate this by overriding the
 * {@link #getConfluenceVocabulary()} method to indicate which annotation URIs
 * should be shown.
 * 
 * @author scsrde
 * 
 */
public abstract class BaseRooInstanceAnnotationViewComponent extends AbstractConfluenceInstanceViewComponent {

    private static final long serialVersionUID = 5175005365412378340L;

    private OWLFrameList<OWLAnnotationSubject> list;

    @Override
    public void disposeView() {
        if (list != null) {
            list.dispose();
        }
    }

    /**
	 * Returns the confluence vocabulary to be shown by this concept annotation
	 * view component.
	 * 
	 * @return
	 */
    protected abstract VocabularyAdapter getConfluenceVocabulary();

    @Override
    protected void initialiseConfluenceIndividualsView() {
        setName("RooInstanceAnnotationViewComponent for " + getConfluenceVocabulary().getUIName());
        final OWLFrame<OWLAnnotationSubject> frame = new ConfluenceEntityAnnotationFrame(getRooEditorKit(), getConfluenceVocabulary());
        list = new OWLFrameList<OWLAnnotationSubject>(getOWLEditorKit(), frame);
        setLayout(new BorderLayout());
        add(new JScrollPane(list));
    }

    @Override
    public OWLNamedIndividual updateView(OWLNamedIndividual aIndividual) {
        if (list != null) {
            IRI iri = null;
            if (aIndividual != null) iri = aIndividual.getIRI();
            list.setRootObject(iri);
        }
        return aIndividual;
    }
}

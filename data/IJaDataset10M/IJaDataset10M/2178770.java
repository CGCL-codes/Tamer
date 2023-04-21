package unbbayes.io.mebn.protege;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import unbbayes.io.mebn.owlapi.OWLAPIStorageImplementorDecorator;
import unbbayes.util.IBridgeImplementor;

/**
 * This is the default implementor or {@link IProtegeStorageImplementorDecorator}
 * They carry Protege-specific objects
 *@author Shou Matsumoto
 *
 */
public class ProtegeStorageImplementorDecorator extends OWLAPIStorageImplementorDecorator implements IProtegeStorageImplementorDecorator {

    private OWLEditorKit owlEditorKit;

    /**
	 * The default constructor is not private in order to allow inheritance.
	 * @deprecated use {@link #getInstance(OWLModelManager)} instead.
	 */
    protected ProtegeStorageImplementorDecorator() {
        super();
    }

    /**
	 * Construction method using fields
	 * @param owlEditorKit
	 * @return an instance of ProtegeStorageImplementorDecorator
	 */
    public static IBridgeImplementor newInstance(OWLEditorKit owlEditorKit) {
        ProtegeStorageImplementorDecorator ret = new ProtegeStorageImplementorDecorator();
        ret.setOWLEditorKit(owlEditorKit);
        return ret;
    }

    public OWLEditorKit getOWLEditorKit() {
        return owlEditorKit;
    }

    public void setOWLEditorKit(OWLEditorKit owlEditorKit) {
        this.owlEditorKit = owlEditorKit;
    }

    public OWLOntology getAdaptee() {
        if (this.getOWLEditorKit() == null || this.getOWLEditorKit().getModelManager() == null) {
            return null;
        }
        return this.getOWLEditorKit().getModelManager().getActiveOntology();
    }

    public void setAdaptee(OWLOntology adaptee) {
        if (this.getOWLEditorKit() == null || this.getOWLEditorKit().getModelManager() == null) {
            return;
        }
        this.getOWLEditorKit().getModelManager().setActiveOntology(adaptee);
    }

    public OWLReasoner getOWLReasoner() {
        try {
            super.setOWLReasoner(this.getOWLEditorKit().getOWLModelManager().getReasoner());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return this.getOWLEditorKit().getOWLModelManager().getReasoner();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 
	 * This method only tries to look for a similar reasoner in the protege's list of installed listeners.
	 * The passed argument will only be used to extract the reasoner ID using {@link OWLReasoner#getReasonerName()}
	 * @see unbbayes.io.mebn.owlapi.OWLAPIStorageImplementorDecorator#setOWLReasoner(org.semanticweb.owlapi.reasoner.OWLReasoner)
	 * @deprecated use {@link org.protege.editor.owl.model.inference.OWLReasonerManager#setCurrentReasonerFactoryId(String)}
	 * and {@link org.protege.editor.owl.model.inference.OWLReasonerManager#classifyAsynchronously(java.util.Set)} instead.
	 * @see OWLReasoner#getReasonerName()
	 */
    public void setOWLReasoner(OWLReasoner owlReasoner) {
        OWLReasonerManager owlReasonerManager = this.getOWLEditorKit().getOWLModelManager().getOWLReasonerManager();
        owlReasonerManager.setCurrentReasonerFactoryId(owlReasoner.getReasonerName());
        try {
            owlReasonerManager.classifyAsynchronously(owlReasonerManager.getReasonerPreferences().getPrecomputedInferences());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setOWLReasoner(owlReasoner);
    }
}

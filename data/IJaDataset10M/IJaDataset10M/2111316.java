package net.datao.datamodel.impl.jena;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;
import net.datao.jung.ontologiesItems.Ont;
import net.datao.repository.OntRepository;
import net.datao.datamodel.OClass;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class OModelPelletImpl extends OModelJenaImpl {

    protected boolean inference = true;

    public OModelPelletImpl(OntRepository ont) {
        super(ont);
    }

    public void doInference() {
        ontModel.prepare();
        ((PelletInfGraph) ontModel.getGraph()).getKB().classify();
    }

    protected void initialize() {
        Model lowLevelModel;
        lowLevelModel = ModelFactory.createOntologyModel();
        Resource configuration = ModelFactory.createDefaultModel().createResource();
        lowLevelModel.createProperty("http://internalStuff/is-A");
        GenericRuleReasoner r = (GenericRuleReasoner) GenericRuleReasonerFactory.theInstance().create(configuration);
        ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, lowLevelModel);
        ((PelletInfGraph) ontModel.getGraph()).setLazyConsistency(true);
        ((PelletInfGraph) ontModel.getGraph()).setDerivationLogging(true);
        OntDocumentManager.getInstance().setProcessImports(false);
        addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (uriToOClass != null) {
                    uriToOClass.clear();
                    uriToOClass = null;
                }
                if (setOfRootOClasses != null) {
                    setOfRootOClasses.clear();
                    setOfRootOClasses = null;
                }
            }
        });
    }

    protected boolean acceptClassOrNot(OntClass c) {
        return true;
    }
}

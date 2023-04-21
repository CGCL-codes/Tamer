package org.coode.cloud.view;

import org.coode.cloud.model.AbstractClassCloudModel;
import org.coode.cloud.model.OWLCloudModel;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 26, 2006<br><br>
 * <p/>
 */
public class ClassesBySiblingCount extends AbstractClassCloudView {

    private static final long serialVersionUID = 4293731689667274587L;

    protected OWLCloudModel createModel() {
        return new ClassesBySiblingCount.ClassesBySiblingCountModel(getOWLModelManager());
    }

    class ClassesBySiblingCountModel extends AbstractClassCloudModel {

        protected ClassesBySiblingCountModel(OWLModelManager mngr) {
            super(mngr);
        }

        protected int getValueForEntity(OWLClass entity) throws OWLException {
            Set<OWLClass> siblings = new HashSet<OWLClass>();
            OWLObjectHierarchyProvider<OWLClass> hierarchyProvider = getOWLModelManager().getOWLHierarchyManager().getOWLClassHierarchyProvider();
            for (OWLClass parent : hierarchyProvider.getParents(entity)) {
                siblings.addAll(hierarchyProvider.getChildren(parent));
            }
            return siblings.size();
        }

        public void activeOntologiesChanged(Set<OWLOntology> activeOntologies) throws OWLException {
        }
    }
}

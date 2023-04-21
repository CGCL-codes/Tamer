package br.ufal.ic.forbile.infra;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.query.QueryResults;
import java.util.Collection;

/**
 * KnowledgeOWL.java
 *
 * <p>A full description of this interface.
 *
 * @see SomeRelatedInterface.
 *
 * @author <a href="mailto:ig.ibert@gmail.com">ig</a>.
 * @since 0.1
 * @version 0.1
 *
 */
public interface KnowledgeOWL extends Knowledge {

    Collection getErrors();

    OWLModel getOwlModel();

    Project getProject();

    Object getOntologyFactory();

    QueryResults executeQuery(final String query) throws Exception;
}

package de.fzi.harmonia.basematcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import de.fzi.harmonia.basematcher.helper.AnnotationPropertyHelper;
import de.fzi.kadmos.api.Alignment;

/**
 * A base matcher that computes a distance between the <code>rdfs:comment</code>s of two entities.
 * The comment texts are compared using Vector Space Similarity following (Salton, G., Wong, A.,
 * Yang, C.S.: A Vector Space Model for Automatic Indexing. Communications of the ACM 18(11) (1975))
 * 
 * @author bock
 *
 */
public class EntityCommentDistanceMatcher extends TextDistanceMatcher {

    private Log logger = LogFactory.getLog(EntityCommentDistanceMatcher.class);

    private OWLAnnotationProperty commentProperty;

    @Override
    public void init(Alignment alignment) {
        super.init(alignment);
        commentProperty = dataFactory.getRDFSComment();
    }

    @Override
    protected <T extends OWLEntity> double computeDistance(T entity1, T entity2) throws InfeasibleBaseMatcherException {
        double result = 1.d;
        String comment1 = AnnotationPropertyHelper.getAnnotationLiteralValue(commentProperty, entity1, alignment.getOntology1());
        String comment2 = AnnotationPropertyHelper.getAnnotationLiteralValue(commentProperty, entity2, alignment.getOntology2());
        if (comment1 == null || comment2 == null) throw new InfeasibleBaseMatcherException("At least one entity did not contain an rdfs:comment.");
        result = computeVectorSpaceDistance(comment1, comment2);
        return result;
    }
}

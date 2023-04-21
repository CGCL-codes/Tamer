package org.omwg.mediation.language.export;

import org.omwg.mediation.language.objectmodel.api.Annotation;
import org.omwg.mediation.language.objectmodel.api.Expression;
import org.omwg.mediation.language.objectmodel.api.Id;
import org.omwg.mediation.language.objectmodel.api.MappingDoc;
import org.omwg.mediation.language.objectmodel.api.MappingRule;
import org.omwg.mediation.language.objectmodel.impl.IRI;

/**
 * <p>
 * This is the Interface which will be used by the exporter to export the
 * mapping document to the specified syntax (e.g. wsml or owl)
 * </p>
 * 
 * @author ricp�t
 * 
 */
public interface SyntaxFormat {

    /**
	 * <p>
	 * Exports the whole document. In order to do this use the oder methods.
	 * </p>
	 * 
	 * @param document
	 *            the MappingDocumnet which should be exported
	 * @return the transformed document
	 */
    public String export(final MappingDoc document);

    /**
	 * <p>
	 * Transforms a IRI to the given Syntax
	 * </p>
	 * 
	 * @param expression
	 *            the IRI
	 * @return the transformed id
	 */
    public String export(final IRI id);

    /**
	 * <p>
	 * Transforms a Annotation to the given Syntax
	 * </p>
	 * 
	 * @param expression
	 *            the Annotation
	 * @return the transformed annotation
	 */
    public String export(final Annotation annotation);

    /**
	 * <p>
	 * Transforms a MappingRule to the given Syntax
	 * </p>
	 * 
	 * @param expression
	 *            the MappingRule
	 * @return the transformed mappingRule
	 */
    public String export(final MappingRule mappingRule);

    /**
	 * <p>
	 * Transforms a Expression to the given Syntax
	 * </p>
	 * 
	 * @param expression
	 *            the Expression
	 * @return the transformed expression
	 */
    public String export(final Expression expression);

    /**
	 * <p>
	 * Returns the last transformed document without running the whole
	 * transformation again (at least one run of the export(MappingDocument)
	 * must be done).
	 * </p>
	 * 
	 * @return the transformed document or a empty String
	 */
    public String getComposedString();

    /**
	 * Exports a Id to its stringrepresentation.
	 * @param id which should be exported
	 * @return the String
	 */
    public String export(final Id id);
}

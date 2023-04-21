package net.sf.etl.parsers.internal.term_parser.flattened;

import net.sf.etl.parsers.internal.term_parser.grammar.DocumentationSyntax;

/**
 * This is view of the documentation element.
 * 
 * @author const
 */
public class DocumentationView extends DefinitionView {

    /**
	 * A constructor from view
	 * 
	 * @param context
	 *            including context
	 * @param definition
	 *            a view to copy
	 */
    public DocumentationView(ContextView context, DefinitionView definition) {
        super(context, definition);
    }

    /**
	 * A constructor from definition
	 * 
	 * @param context
	 *            an defining context
	 * @param definition
	 *            a definition
	 */
    public DocumentationView(ContextView context, DocumentationSyntax definition) {
        super(context, definition);
    }
}

package no.jish.luclipse.ui.editors;

import no.jish.luclipse.core.SearchDocument;

/**
 *
 * Implementors of this interface can determine a SearchDocuments behaviour in a column
 *
 * @author John Inge S. Hervik
 *
 */
public interface DocumentColumnStrategy {

    String getText(SearchDocument row);

    int compare(SearchDocument row1, SearchDocument row2);
}

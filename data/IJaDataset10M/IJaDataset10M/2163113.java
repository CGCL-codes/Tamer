package ontorama.ui;

import ontorama.ontotools.query.Query;

/**
 * @author nataliya
 */
public interface HistoryElement {

    public String getMenuDisplayName();

    public String getToolTipText();

    public Query getQuery();

    public void displayElement();
}

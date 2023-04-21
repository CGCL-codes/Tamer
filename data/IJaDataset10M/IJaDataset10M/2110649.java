package uk.ac.ebi.intact.dataexchange.cvutils.model;

/**
 * Configuration for ontology terms from the PSI-MI ontology
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>06/02/12</pre>
 */
public class MiOntologyConfig implements ExternalOntologyConfig {

    private static final String MI_ALIAS_IDENTIFIER = "PSI-MI-alternate";

    private static final String MI_SHORTLABEL_IDENTIFIER = "PSI-MI-short";

    private static final String MI_NAMESPACE = "PSI-MI";

    private static final String MI_ALIAS_DEF = "Alternate label curated by PSI-MI";

    private static final String MI_SHORTLABEL_DEF = "Unique short label curated by PSI-MI";

    @Override
    public String getDefaultNamespace() {
        return MI_NAMESPACE;
    }

    @Override
    public String getShortLabelSynonymCategory() {
        return MI_SHORTLABEL_IDENTIFIER;
    }

    @Override
    public String getAliasSynonymCategory() {
        return MI_ALIAS_IDENTIFIER;
    }

    @Override
    public String getAliasSynonymDefinition() {
        return MI_ALIAS_DEF;
    }

    @Override
    public String getShortLabelSynonymDefinition() {
        return MI_SHORTLABEL_DEF;
    }
}

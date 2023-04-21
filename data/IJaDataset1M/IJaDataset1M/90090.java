package uk.ac.ebi.intact.dataexchange.cvutils.model;

/**
 * Ontology configuration for terms from the PSI-MOD ontology
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>06/02/12</pre>
 */
public class ModOntologyConfig implements ExternalOntologyConfig {

    private static final String MOD_ALIAS_IDENTIFIER = "PSI-MOD-alternate";

    private static final String MOD_SHORTLABEL_IDENTIFIER = "PSI-MOD-label";

    private static final String MOD_NAMESPACE = "PSI-MOD";

    private static final String MOD_ALIAS_DEF = "Short label curated by PSI-MOD";

    private static final String MOD_SHORTLABEL_DEF = "Alternate name curated by PSI-MOD";

    @Override
    public String getDefaultNamespace() {
        return MOD_NAMESPACE;
    }

    @Override
    public String getShortLabelSynonymCategory() {
        return MOD_SHORTLABEL_IDENTIFIER;
    }

    @Override
    public String getAliasSynonymCategory() {
        return MOD_ALIAS_IDENTIFIER;
    }

    @Override
    public String getAliasSynonymDefinition() {
        return MOD_ALIAS_DEF;
    }

    @Override
    public String getShortLabelSynonymDefinition() {
        return MOD_SHORTLABEL_DEF;
    }
}

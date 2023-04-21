package uk.ac.ebi.intact.util.protein.utils;

import uk.ac.ebi.intact.model.Alias;
import uk.ac.ebi.intact.model.InteractorAlias;
import uk.ac.ebi.intact.model.Protein;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Contains a summary of the aliases update
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>29/06/11</pre>
 */
public class AliasUpdateReport {

    private String protein;

    private Collection<Alias> addedAliases = new ArrayList<Alias>();

    private Collection<Alias> removedAliases = new ArrayList<Alias>();

    public AliasUpdateReport(Protein protein, Collection<InteractorAlias> addedAliases, Collection<InteractorAlias> removedAliases) {
        this.protein = protein != null ? protein.getAc() : null;
        this.addedAliases.addAll(addedAliases);
        this.removedAliases.addAll(removedAliases);
    }

    public AliasUpdateReport(Protein protein) {
        this.protein = protein != null ? protein.getAc() : null;
    }

    public String getProtein() {
        return protein;
    }

    public Collection<Alias> getAddedAliases() {
        return addedAliases;
    }

    public Collection<Alias> getRemovedAliases() {
        return removedAliases;
    }

    public static String aliasesToString(Collection<Alias> aliases) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Alias alias : aliases) {
            if (i < aliases.size()) {
                sb.append(", ");
            }
            String qual = (alias.getCvAliasType() != null) ? "(" + alias.getCvAliasType().getShortLabel() + ")" : "";
            sb.append(qual + ":" + (alias.getName() != null ? alias.getName() : ""));
            i++;
        }
        return sb.toString();
    }
}

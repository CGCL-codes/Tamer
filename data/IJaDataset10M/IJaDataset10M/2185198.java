package gpsxml.xml;

import java.util.HashMap;

/**
 *
 * @author PLAYER, Keith Ralph
 */
public class SpeciesAliasMap {

    private HashMap<String, String> aliasMap = new HashMap<String, String>();

    /** Creates a new instance of SpeciesAliasMap */
    public SpeciesAliasMap() {
        aliasMap.put("Mm", "Mus musculus");
        aliasMap.put("Hs", "Homo sapiens");
        aliasMap.put("Ce", "Caenorhabditis elegans");
        aliasMap.put("At", "Arabidopsis Thaliana");
    }

    public String getAlias(String species) {
        return aliasMap.get(species);
    }
}

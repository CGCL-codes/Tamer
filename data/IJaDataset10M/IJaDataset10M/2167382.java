package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.qualities;

import java.util.Map;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttrQualityWrapper;

/**
 * Gets the qualities of the read attributes
 * @author CLAISSE 
 */
public interface Qualities {

    /**
     * @return A Map which keys are the read attributes names, and which values are the read attributes values
     */
    public Map<String, AttrQualityWrapper> getQualities();

    /**
     * @param attributeName A read attribute's name
     * @return The specified read attribute's quality
     */
    public AttrQualityWrapper getQuality(String attributeName);
}

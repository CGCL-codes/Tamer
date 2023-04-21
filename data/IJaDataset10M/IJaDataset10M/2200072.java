package swg.crafting.schematics;

import java.io.Serializable;
import org.w3c.dom.Element;
import swg.crafting.resources.SWGResourceWeights;
import swg.tools.SWGXmlUtils;

/**
 * This class embraces one experimental line of an experimental group in a
 * schematic.
 * <P>
 * The XML fragment that is parsed is just one element named "experiment", where
 * one or several elements can be contained in the element "experiment_main" to
 * form a group.
 * 
 * <PRE>    &lt;experiment_main description=""&gt;
        &lt;experiment description="" cd="" cr="" dr="" er="" fl="" hr="" ma="" 
        oq="" pe="" sr="" ut=""/&gt;
        &lt;!-- only stats greater than zero --&gt;
    &lt;/experiment_main&gt;</PRE>
 * 
 * @author <a href="mailto:simongronlund@gmail.com">Simon Gronlund</a> aka
 *         Europe-Chimaera.Zimoon
 * @see swg.crafting.schematics.SWGSchematicExperimentGroup
 */
public class SWGSchematicExperiment implements Serializable {

    /**
     * Serialization version info; do not meddle with this or break the
     * deserialization
     */
    private static final long serialVersionUID = 4293836125745828179L;

    /**
     * The description of this experimental line
     */
    private String description;

    /**
     * The experimental weights of this line
     */
    private SWGResourceWeights weights;

    /**
     * Creates a new experimental line from the specified XML fragment
     * 
     * @param element
     *            the XML fragment to parse
     */
    public SWGSchematicExperiment(Element element) {
        description = element.getAttribute("description");
        weights = new SWGResourceWeights();
        weights.setCD(SWGXmlUtils.getIntegerFromAttribute(element, "cd"));
        weights.setCR(SWGXmlUtils.getIntegerFromAttribute(element, "cr"));
        weights.setDR(SWGXmlUtils.getIntegerFromAttribute(element, "dr"));
        weights.setER(SWGXmlUtils.getIntegerFromAttribute(element, "er"));
        weights.setFL(SWGXmlUtils.getIntegerFromAttribute(element, "fl"));
        weights.setHR(SWGXmlUtils.getIntegerFromAttribute(element, "hr"));
        weights.setMA(SWGXmlUtils.getIntegerFromAttribute(element, "ma"));
        weights.setOQ(SWGXmlUtils.getIntegerFromAttribute(element, "oq"));
        weights.setPE(SWGXmlUtils.getIntegerFromAttribute(element, "pe"));
        weights.setSR(SWGXmlUtils.getIntegerFromAttribute(element, "sr"));
        weights.setUT(SWGXmlUtils.getIntegerFromAttribute(element, "ut"));
        if (!weights.isValid()) {
            throw new IllegalArgumentException("\nSWGSchematicExperiment: desc=" + description + ", invalid experimental weights:" + weights.valuesAsString());
        }
    }

    /**
     * Returns the description of this experimental line
     * 
     * @return the description of this experimental line, as read at the
     *         schematic
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the experimental weights of this line
     * 
     * @return the experimental weights of this line
     */
    public final SWGResourceWeights getWeights() {
        return new SWGResourceWeights(weights.getValues());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Experimental line: [\"");
        sb.append(description);
        sb.append("\", weights: ").append(weights.valuesAsShortString());
        sb.append("]");
        return sb.toString();
    }
}

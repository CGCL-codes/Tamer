package sts.framework.generate;

import javax.xml.transform.Source;
import java.util.Properties;

/**
 * Instances of this class are registered with the GenerationManager to
 * @author ken
 */
public interface Generator {

    /** Returns a short human-readable name of this generator. I.e., "Collection sheet". */
    public String getName();

    /** Returns name upon which the stylesheet and output filenames are based. I.e., "collection-sheet". */
    public String getBaseFilename();

    /** Returns the source which is transformed into the output. 
     *  @param additionalProperties additional properties to set in the XML marshaller
     *  @see kellinwood.meshi.xml.Marshaler
     */
    public Source getXMLSource(Properties additionalProperties);
}

package uk.ac.cam.caret.imscp.api;

import java.net.URI;
import java.net.URISyntaxException;
import uk.ac.cam.caret.minibix.general.*;
import uk.ac.cam.caret.minibix.general.svo.api.*;

/** The resources in this manifest. Note that thing in the resources tag, alien md
 * and xml:base, are within the Manifest object, as these are likely little used nooks
 * of the standard which would require an otherwise superfluous resources object. 
 * 
 * @author CARET
 *
 */
public interface Resource extends BuildsSvo, Reproducable {

    /** Get the XMLBase on this resource tag, if any, or null otherwise.
	 * 
	 * @return the xmlbase tag, if any, or null otherwise.
	 */
    public String getXmlBase();

    /** Get the href attribute for the resource tag, if any or null otherwise.
	 * 
	 * @return The value of the href attribute
	 */
    public String getHref();

    /** Get the type attribute for this resource.
	 * 
	 * @return the type attribute of this resource.
	 */
    public String getType();

    /** Get the identifier for this resource.
	 * 
	 * @return the identifier for this resource.
	 */
    public String getIdentifier();

    /** Retrieve the metadata associated with this resource.
	 * 
	 * @return the metadata associated with this resource.
	 */
    public Metadata getMetadata();

    /** Get all the (hrefs of the) files associated with this resource.
	 * 
	 * @return the files associated with this resource.
	 */
    public ManifestFile[] getFiles();

    /** Get references for all the dependencies of this resource.
	 * 
	 * @return
	 */
    public ManifestDependency[] getDependencies();

    /** Get any alien xml elements within this resource. Each top-level element gets
	 * an extra.
	 * 
	 * @return the top-level elements, in order in the document. 
	 */
    public Object[] getExtras();

    public void setIdentifier(String in);

    public void setType(String in);

    public void setXmlBase(String in);

    public void setHref(String in);

    public ManifestFile addFile(String href);

    public ManifestDependency addDependency(String href);

    public void addMetadata();

    public URI getBaseURI() throws URISyntaxException;

    public Resource reproduce();
}

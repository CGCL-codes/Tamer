package fr.cnes.sitools.service.admin;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import fr.cnes.sitools.common.SitoolsResource;

/**
 * Discover sitools services 
 * 
 * @author jp.boignard
 */
public class BundleCollectionResource extends SitoolsResource {

    private volatile AdminServiceApplication app = null;

    /**
   * Init the resource
   */
    public void doInit() {
        super.doInit();
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        app = (AdminServiceApplication) this.getApplication();
    }

    @Override
    public void sitoolsDescribe() {
        setName("ServiceTrackerResource");
        setDescription("Status of different bundles registered on the platform");
    }

    @Get
    public Representation getBundles(Variant variant) {
        String result = "";
        BundleContext bc = (BundleContext) getApplication().getContext().getAttributes().get("BUNDLE_CONTEXT");
        Bundle[] references = bc.getBundles();
        for (int i = 0; references != null && i < references.length; i++) {
            result += (references[i].toString() + "\n");
        }
        return new StringRepresentation(result, MediaType.TEXT_PLAIN);
    }
}

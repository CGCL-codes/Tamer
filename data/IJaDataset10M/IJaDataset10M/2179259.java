package fr.cnes.sitools.dataset.sva.task;

import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.xstream.XstreamRepresentation;
import org.restlet.representation.ObjectRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import com.thoughtworks.xstream.XStream;
import fr.cnes.sitools.common.SitoolsResource;
import fr.cnes.sitools.common.XStreamFactory;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.dataset.DataSetApplication;
import fr.cnes.sitools.dataset.sva.task.business.SvaTask;
import fr.cnes.sitools.dataset.sva.task.model.SvaTaskModel;

/**
 * Parent resource for managing SvaTask resources
 * 
 * @author m.gond (AKKA Technologies)
 * 
 * @version
 * 
 */
public abstract class AbstractSvaTaskAdminResource extends SitoolsResource {

    /** parent application */
    protected DataSetApplication application = null;

    /** SVA identifier parameter */
    protected String svaModelId = null;

    /** SVA identifier parameter */
    protected String svaTaskId = null;

    /**
   * Default constructor
   */
    public AbstractSvaTaskAdminResource() {
        super();
    }

    @Override
    public void doInit() {
        super.doInit();
        this.svaModelId = (String) this.getRequest().getAttributes().get("svaId");
        this.svaTaskId = (String) this.getRequest().getAttributes().get("svaTaskId");
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        application = (DataSetApplication) getApplication();
    }

    /**
   * Gets representation according to the specified Variant if present. If
   * variant is null (when content negotiation = false) sets the variant to the
   * first client accepted mediaType.
   * 
   * @param response
   *          : The response to get the representation from
   * @param variant
   *          : The variant needed
   * @return Representation
   */
    public final Representation getRepresentation(Response response, Variant variant) {
        MediaType defaultMediaType = getVariant(variant);
        return getRepresentation(response, defaultMediaType);
    }

    /**
   * Gets the mediaType of the request
   * 
   * @param variant
   *          the variant of the request
   * @return the MediaType of the request
   */
    public final MediaType getVariant(Variant variant) {
        MediaType defaultMediaType = null;
        if (variant == null) {
            if (this.getRequest().getClientInfo().getAcceptedMediaTypes().size() > 0) {
                MediaType first = this.getRequest().getClientInfo().getAcceptedMediaTypes().get(0).getMetadata();
                if (first.isConcrete() && (first.isCompatible(MediaType.APPLICATION_JAVA_OBJECT))) {
                    defaultMediaType = first;
                }
            }
            if ((defaultMediaType == null) && (getVariants() != null) && (!getVariants().isEmpty())) {
                Variant preferredVariant = getClientInfo().getPreferredVariant(getVariants(), getMetadataService());
                defaultMediaType = preferredVariant.getMediaType();
            }
        } else {
            defaultMediaType = variant.getMediaType();
        }
        return defaultMediaType;
    }

    /**
   * Gets representation according to the specified MediaType.
   * 
   * @param response
   *          : The response to get the representation from
   * @param media
   *          : The MediaType asked
   * @return The Representation of the response with the selected mediaType
   */
    public final Representation getRepresentation(Response response, MediaType media) {
        getLogger().info(media.toString());
        if (media.isCompatible(MediaType.APPLICATION_JAVA_OBJECT)) {
            return new ObjectRepresentation<Response>(response);
        }
        XStream xstream = XStreamFactory.getInstance().getXStream(media);
        xstream.alias("response", Response.class);
        xstream.alias("SvaTaskModel", SvaTaskModel.class);
        xstream.omitField(Response.class, "itemName");
        xstream.omitField(Response.class, "itemClass");
        xstream.omitField(SvaTaskModel.class, "requestParams");
        if (response.getItemClass() != null) {
            xstream.alias("item", Object.class, response.getItemClass());
        }
        if (response.getItemName() != null) {
            xstream.aliasField(response.getItemName(), Response.class, "item");
        }
        XstreamRepresentation<Response> rep = new XstreamRepresentation<Response>(media, response);
        rep.setXstream(xstream);
        return rep;
    }

    /**
   * Get a representation containing a Response with a SvaTaskDTO object
   * 
   * @param svaTask
   *          the svaTask
   * @param variant
   *          the variant needed
   * @return a representation containing a Response with a SvaTaskDTO object
   */
    public final Representation returnStatus(SvaTask svaTask, Variant variant) {
        SvaTaskModel svaTaskModel = svaTask.getSvaTaskModel();
        Response response = new Response(true, svaTaskModel, SvaTaskModel.class, "SvaTaskModel");
        return getRepresentation(response, variant);
    }

    /**
   * Get the SVA object from the representation sent
   * 
   * @param representation
   *          the representation sent (POST or PUT)
   * @return the corresponding SVA model
   */
    public final SvaTaskModel getSvaTaskModelFromRepresentation(Representation representation) {
        SvaTaskModel svaOut = null;
        if (MediaType.APPLICATION_XML.isCompatible(representation.getMediaType())) {
            XstreamRepresentation<SvaTaskModel> repXML = new XstreamRepresentation<SvaTaskModel>(representation);
            XStream xstream = XStreamFactory.getInstance().getXStreamReader(MediaType.APPLICATION_XML);
            xstream.autodetectAnnotations(false);
            xstream.alias("SvaTaskModel", SvaTaskModel.class);
            repXML.setXstream(xstream);
            svaOut = repXML.getObject();
        } else if (MediaType.APPLICATION_JSON.isCompatible(representation.getMediaType())) {
            svaOut = new JacksonRepresentation<SvaTaskModel>(representation, SvaTaskModel.class).getObject();
        }
        return svaOut;
    }
}

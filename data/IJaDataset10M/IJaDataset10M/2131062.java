package fr.cnes.sitools.form.project;

import java.util.List;
import java.util.logging.Level;
import org.restlet.data.Status;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import fr.cnes.sitools.common.SitoolsSettings;
import fr.cnes.sitools.common.application.ContextAttributes;
import fr.cnes.sitools.common.exception.SitoolsException;
import fr.cnes.sitools.common.model.ResourceCollectionFilter;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.form.project.model.FormProject;
import fr.cnes.sitools.plugins.resources.dto.ResourceModelDTO;
import fr.cnes.sitools.plugins.resources.model.ResourceModel;
import fr.cnes.sitools.server.Consts;
import fr.cnes.sitools.util.RIAPUtils;

/**
 * Class Resource for managing FormProject FormProject (GET, POST)
 * 
 * @author jp.boignard (AKKA Technologies)
 * 
 */
public class FormProjectCollectionResource extends AbstractFormProjectResource {

    @Override
    public void sitoolsDescribe() {
        setName("FormProjectCollectionResource");
        setDescription("Resource for managing FormProject collection");
        setNegotiated(false);
    }

    /**
   * Update / Validate existing FormProject
   * 
   * @param representation
   *          FormProject representation
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Post
    public Representation newFormProject(Representation representation, Variant variant) {
        if (representation == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "PROJECT_REPRESENTATION_REQUIRED");
        }
        try {
            FormProject formProjectInput = getObject(representation, variant);
            if (formProjectInput.getParent() == null) {
                formProjectInput.setParent(getProjectId());
            }
            formProjectInput = attachServices(formProjectInput);
            FormProject formProjectOutput = getStore().create(formProjectInput);
            Response response = new Response(true, formProjectOutput, FormProject.class, "formProject");
            Representation rep = getRepresentation(response, variant);
            return rep;
        } catch (ResourceException e) {
            getLogger().log(Level.INFO, null, e);
            throw e;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
        }
    }

    /**
   * Attach the search services to the current project application
   * 
   * @param formProject
   *          the FormProject
   * @return the FormProject with the id of resources assigned
   * @throws SitoolsException
   *           if the resource wasn't created properly
   * @throws InstantiationException
   *           if there is an error while instantiating className
   * @throws IllegalAccessException
   *           if the class or its nullary constructor is not accessible
   * @throws ClassNotFoundException
   *           if className cannot be found
   */
    private FormProject attachServices(FormProject formProject) throws SitoolsException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String resourceModelClassNamePropertiesSearch = "fr.cnes.sitools.form.project.services.ServicePropertiesSearchResourceModel";
        String resourceModelClassNameDatasetSearch = "fr.cnes.sitools.form.project.services.ServiceDatasetSearchResourceModel";
        String idPropertiesSearch = attachResource(resourceModelClassNamePropertiesSearch, formProject.getUrlServicePropertiesSearch(), formProject.getCollection().getId(), formProject.getDictionary().getId());
        formProject.setIdServicePropertiesSearch(idPropertiesSearch);
        String idDatasetSearch = attachResource(resourceModelClassNameDatasetSearch, formProject.getUrlServiceDatasetSearch(), formProject.getCollection().getId(), formProject.getDictionary().getId());
        formProject.setIdServiceDatasetSearch(idDatasetSearch);
        return formProject;
    }

    /**
   * Attach a resource with the given parameters to the current project
   * 
   * @param className
   *          the class of the resource to attach
   * @param attachment
   *          the urlAttachment
   * @param collectionId
   *          the id of the collection
   * @param dictionary
   *          the id of the dictionary
   * @return The id of the created Resource
   * @throws SitoolsException
   *           if the resource wasn't created properly
   * @throws InstantiationException
   *           if there is an error while instantiating className
   * @throws IllegalAccessException
   *           if the class or its nullary constructor is not accessible
   * @throws ClassNotFoundException
   *           if className cannot be found
   */
    private String attachResource(String className, String attachment, String collectionId, String dictionary) throws SitoolsException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        @SuppressWarnings("unchecked") Class<ResourceModel> resourceModelClass = (Class<ResourceModel>) Class.forName(className);
        ResourceModel resourceModel = resourceModelClass.newInstance();
        resourceModel.getParameterByName("url").setValue(attachment);
        resourceModel.getParameterByName(collectionParamName).setValue(collectionId);
        resourceModel.getParameterByName(dictionaryParamName).setValue(dictionary);
        SitoolsSettings settings = (SitoolsSettings) getContext().getAttributes().get(ContextAttributes.SETTINGS);
        String url = settings.getString(Consts.APP_APPLICATIONS_URL) + "/" + getProjectId() + settings.getString(Consts.APP_RESOURCES_URL);
        ResourceModelDTO dto = ResourceModelDTO.resourceModelToDTO(resourceModel);
        ResourceModelDTO resourceModelOut = RIAPUtils.persistObject(dto, url, getContext());
        if (resourceModelOut == null) {
            throw new SitoolsException("Cannot create resource model");
        }
        return resourceModelOut.getId();
    }

    @Override
    public final void describePost(MethodInfo info) {
        info.setDocumentation("Method to create a new form components sending its representation.");
        this.addStandardPostOrPutRequestInfo(info);
        this.addStandardResponseInfo(info);
        this.addStandardInternalServerErrorInfo(info);
    }

    /**
   * get all FormProject
   * 
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Get
    public Representation retrieveFormProject(Variant variant) {
        try {
            Response response;
            if (getFormProjectId() != null) {
                FormProject formProject = getStore().retrieve(getFormProjectId());
                if ((getProjectId() != null) && (!getProjectId().equals(formProject.getParent()))) {
                    response = new Response(false, "FORM_DONT_BELONG_TO_PROJECT");
                } else {
                    response = new Response(true, formProject, FormProject.class, "formProject");
                }
            } else {
                ResourceCollectionFilter filter = new ResourceCollectionFilter(this.getRequest());
                if (getProjectId() != null) {
                    filter.setParent(getProjectId());
                }
                List<FormProject> formProject = getStore().getList(filter);
                int total = formProject.size();
                formProject = getStore().getPage(filter, formProject);
                response = new Response(true, formProject, FormProject.class, "formProject");
                response.setTotal(total);
            }
            Representation rep = getRepresentation(response, variant);
            return rep;
        } catch (ResourceException e) {
            getLogger().log(Level.INFO, null, e);
            throw e;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
        }
    }

    @Override
    public final void describeGet(MethodInfo info) {
        info.setDocumentation("Method to retrieve the list of form components available on the server.");
        this.addStandardGetRequestInfo(info);
        this.addStandardResponseInfo(info);
        this.addStandardInternalServerErrorInfo(info);
    }
}

package fr.cnes.sitools.project.modules;

import java.util.List;
import java.util.logging.Level;
import org.restlet.data.Status;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.ext.wadl.ParameterInfo;
import org.restlet.ext.wadl.ParameterStyle;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import fr.cnes.sitools.common.model.ResourceCollectionFilter;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.project.modules.model.ProjectModule;

/**
 * Class Resource for managing single ProjectModule (GET UPDATE DELETE)
 * 
 * @author jp.boignard (AKKA Technologies)
 * 
 */
public class ProjectModuleResource extends AbstractProjectModuleResource {

    @Override
    public void sitoolsDescribe() {
        setName("ProjectModuleResource");
        setDescription("Resource for managing an identified project modules");
        setNegotiated(false);
    }

    /**
   * get all projectModules
   * 
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Get
    public Representation retrieveProjectModule(Variant variant) {
        if (getProjectModuleId() != null) {
            ProjectModule projectModule = getStore().retrieve(getProjectModuleId());
            Response response = new Response(true, projectModule, ProjectModule.class, "projectModule");
            return getRepresentation(response, variant);
        } else {
            ResourceCollectionFilter filter = new ResourceCollectionFilter(this.getRequest());
            List<ProjectModule> projectModules = getStore().getList(filter);
            int total = projectModules.size();
            projectModules = getStore().getPage(filter, projectModules);
            Response response = new Response(true, projectModules, ProjectModule.class, "projectModules");
            response.setTotal(total);
            return getRepresentation(response, variant);
        }
    }

    @Override
    public final void describeGet(MethodInfo info) {
        info.setDocumentation("Method to retrieve a single ProjectModule by ID");
        this.addStandardGetRequestInfo(info);
        ParameterInfo param = new ParameterInfo("projectModuleId", true, "class", ParameterStyle.TEMPLATE, "Module identifier");
        info.getRequest().getParameters().add(param);
        this.addStandardObjectResponseInfo(info);
        addStandardResourceCollectionFilterInfo(info);
    }

    /**
   * Update / Validate existing projectModule
   * 
   * @param representation
   *          ProjectModule representation
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Put
    public Representation updateProjectModule(Representation representation, Variant variant) {
        ProjectModule projectModuleOutput = null;
        try {
            ProjectModule projectModuleInput = null;
            if (representation != null) {
                projectModuleInput = getObject(representation, variant);
                projectModuleOutput = getStore().update(projectModuleInput);
            }
            Response response = new Response(true, projectModuleOutput, ProjectModule.class, "projectModule");
            return getRepresentation(response, variant);
        } catch (ResourceException e) {
            getLogger().log(Level.INFO, null, e);
            throw e;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
        }
    }

    @Override
    public final void describePut(MethodInfo info) {
        info.setDocumentation("Method to modify a single module sending its new representation");
        this.addStandardPostOrPutRequestInfo(info);
        ParameterInfo param = new ParameterInfo("projectModuleId", true, "class", ParameterStyle.TEMPLATE, "Module identifier");
        info.getRequest().getParameters().add(param);
        this.addStandardObjectResponseInfo(info);
        this.addStandardInternalServerErrorInfo(info);
    }

    /**
   * Delete projectModule
   * 
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Delete
    public Representation deleteProjectModule(Variant variant) {
        try {
            getStore().delete(getProjectModuleId());
            Response response = new Response(true, "projectModule.delete.success");
            return getRepresentation(response, variant);
        } catch (ResourceException e) {
            getLogger().log(Level.INFO, null, e);
            throw e;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
        }
    }

    @Override
    public final void describeDelete(MethodInfo info) {
        info.setDocumentation("Method to delete a single module by ID");
        this.addStandardGetRequestInfo(info);
        ParameterInfo param = new ParameterInfo("projectModuleId", true, "class", ParameterStyle.TEMPLATE, "Module identifier");
        info.getRequest().getParameters().add(param);
        this.addStandardSimpleResponseInfo(info);
        this.addStandardInternalServerErrorInfo(info);
    }
}

package fr.cnes.sitools.plugins.filters;

import java.util.ArrayList;
import java.util.List;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.ext.wadl.ParameterInfo;
import org.restlet.ext.wadl.ParameterStyle;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.engine.SitoolsEngine;
import fr.cnes.sitools.plugins.filters.dto.FilterModelDTO;
import fr.cnes.sitools.plugins.filters.model.FilterModel;

/**
 * Resource handling the list of available dynamic filters
 * 
 * @author m.marseille (AKKA Technologies)
 */
public final class FilterClassPluginCollectionResource extends AbstractFilterPluginResource {

    @Override
    public void sitoolsDescribe() {
        setName("FilterClassPluginCollectionResource");
        setDescription("Resource handling the list of available customizable filters");
        setNegotiated(false);
    }

    /**
   * GET request
   * 
   * @param variant
   *          a RESTlet representation {@code Variant}
   * @return a RESTlet representation
   */
    @Get
    public Representation getFilters(Variant variant) {
        Response response;
        SitoolsEngine sitoolsEng = SitoolsEngine.getInstance();
        List<FilterModel> listResources = new ArrayList<FilterModel>(sitoolsEng.getFilterPlugins());
        List<FilterModelDTO> listFilterDTO = new ArrayList<FilterModelDTO>();
        for (FilterModel filterModel : listResources) {
            listFilterDTO.add(getFilterModelDTO(filterModel));
        }
        response = new Response(true, listFilterDTO, FilterModelDTO.class, "filters");
        return getRepresentation(response, variant);
    }

    /**
   * GET method description
   * 
   * @param info
   *          WADL method information
   */
    public void describeGet(MethodInfo info) {
        info.setDocumentation("Method to retrieve the list of available filters in Sitools2.");
        this.addStandardGetRequestInfo(info);
        ParameterInfo pic = new ParameterInfo("pluginId", true, "xs:string", ParameterStyle.TEMPLATE, "Identifier of the filter");
        info.getRequest().getParameters().add(pic);
        this.addStandardResponseInfo(info);
    }
}

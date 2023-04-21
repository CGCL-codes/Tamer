package fr.cnes.sitools.datasetViews;

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
import fr.cnes.sitools.datasetViews.model.DatasetView;

/**
 * Class Resource for managing single DatasetView (GET UPDATE DELETE)
 * 
 * @author jp.boignard (AKKA Technologies)
 * 
 */
public class DatasetViewResource extends AbstractDatasetViewResource {

    @Override
    public void sitoolsDescribe() {
        setName("DatasetViewResource");
        setDescription("Resource for managing an identified datasetView");
        setNegotiated(false);
    }

    /**
   * get all datasetViews
   * 
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Get
    public Representation retrieveDatasetView(Variant variant) {
        if (getDatasetViewId() != null) {
            DatasetView datasetView = getStore().retrieve(getDatasetViewId());
            Response response = new Response(true, datasetView, DatasetView.class, "datasetView");
            return getRepresentation(response, variant);
        } else {
            ResourceCollectionFilter filter = new ResourceCollectionFilter(this.getRequest());
            List<DatasetView> datasetViews = getStore().getList(filter);
            int total = datasetViews.size();
            datasetViews = getStore().getPage(filter, datasetViews);
            Response response = new Response(true, datasetViews, DatasetView.class, "datasetViews");
            response.setTotal(total);
            return getRepresentation(response, variant);
        }
    }

    @Override
    public final void describeGet(MethodInfo info) {
        info.setDocumentation("Method to retrieve a single DatasetView by ID");
        this.addStandardGetRequestInfo(info);
        ParameterInfo param = new ParameterInfo("datasetViewId", true, "class", ParameterStyle.TEMPLATE, "Form component identifier");
        info.getRequest().getParameters().add(param);
        this.addStandardObjectResponseInfo(info);
    }

    /**
   * Update / Validate existing datasetView
   * 
   * @param representation
   *          DatasetView representation
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Put
    public Representation updateDatasetView(Representation representation, Variant variant) {
        DatasetView datasetViewOutput = null;
        try {
            DatasetView datasetViewInput = null;
            if (representation != null) {
                datasetViewInput = getObject(representation, variant);
                datasetViewOutput = getStore().update(datasetViewInput);
            }
            Response response = new Response(true, datasetViewOutput, DatasetView.class, "datasetView");
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
        info.setDocumentation("Method to modify a single dataset view sending its new representation");
        this.addStandardPostOrPutRequestInfo(info);
        ParameterInfo param = new ParameterInfo("datasetViewId", true, "class", ParameterStyle.TEMPLATE, "Form component identifier");
        info.getRequest().getParameters().add(param);
        this.addStandardObjectResponseInfo(info);
        this.addStandardInternalServerErrorInfo(info);
    }

    /**
   * Delete datasetView
   * 
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Delete
    public Representation deleteDatasetView(Variant variant) {
        try {
            getStore().delete(getDatasetViewId());
            Response response = new Response(true, "datasetView.delete.success");
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
        info.setDocumentation("Method to delete a single dataset view by ID");
        this.addStandardGetRequestInfo(info);
        ParameterInfo param = new ParameterInfo("datasetViewId", true, "class", ParameterStyle.TEMPLATE, "Form component identifier");
        info.getRequest().getParameters().add(param);
        this.addStandardSimpleResponseInfo(info);
        this.addStandardInternalServerErrorInfo(info);
    }
}

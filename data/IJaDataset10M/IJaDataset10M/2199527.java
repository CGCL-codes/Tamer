package fr.cnes.sitools.datasource.jdbc;

import java.util.List;
import java.util.logging.Level;
import org.restlet.data.Status;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.ext.wadl.ParameterInfo;
import org.restlet.ext.wadl.ParameterStyle;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import fr.cnes.sitools.common.model.ResourceCollectionFilter;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.datasource.jdbc.model.JDBCDataSource;

/**
 * Class for JDBC data source collection management
 * 
 * @author jp.boignard (AKKA Technologies)
 * 
 */
public final class JDBCDataSourceCollectionResource extends AbstractDataSourceResource {

    @Override
    public void sitoolsDescribe() {
        setName("JDBCDataSourceCollectionResource");
        setDescription("Resource for managing jdbc datasource collection");
        this.setNegotiated(false);
    }

    /**
   * Create a new DataSource
   * 
   * @param representation
   *          input
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Post
    public Representation newDataSource(Representation representation, Variant variant) {
        if (representation == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "DATASOURCE_REPRESENTATION_REQUIRED");
        }
        try {
            JDBCDataSource datasourceInput = getObject(representation);
            datasourceInput.setStatus("NEW");
            if ((datasourceInput.getSitoolsAttachementForUsers() == null) || datasourceInput.getSitoolsAttachementForUsers().equals("")) {
                datasourceInput.setSitoolsAttachementForUsers("/sitools/users/resources/datasources/" + datasourceInput.getName());
            }
            JDBCDataSource datasourceOutput = getStore().create(datasourceInput);
            Response response = new Response(true, datasourceOutput, JDBCDataSource.class, "jdbcdatasource");
            return getRepresentation(response, variant);
        } catch (ResourceException e) {
            getLogger().log(Level.INFO, null, e);
            throw e;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
        }
    }

    /**
   * Describe the POST method
   * 
   * @param info
   *          WADL method information
   */
    public void describePost(MethodInfo info) {
        info.setDocumentation("Method to create a new JDBC datasource.");
        this.addStandardPostOrPutRequestInfo(info);
        this.addStandardObjectResponseInfo(info);
        this.addStandardInternalServerErrorInfo(info);
    }

    /**
   * get all DataSets
   * 
   * @param variant
   *          client preferred media type
   * @return Representation
   */
    @Get
    public Representation retrieveDataSource(Variant variant) {
        try {
            if (getDatasourceId() != null) {
                JDBCDataSource dataset = getStore().retrieve(getDatasourceId());
                Response response = new Response(true, dataset, JDBCDataSource.class, "jdbcdatasource");
                return getRepresentation(response, variant);
            } else {
                ResourceCollectionFilter filter = new ResourceCollectionFilter(this.getRequest());
                List<JDBCDataSource> datasources = getStore().getList(filter);
                int total = datasources.size();
                datasources = getStore().getPage(filter, datasources);
                Response response = new Response(true, datasources, JDBCDataSource.class, "jdbcdatasource");
                response.setTotal(total);
                return getRepresentation(response, variant);
            }
        } catch (ResourceException e) {
            getLogger().log(Level.INFO, null, e);
            throw e;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
        }
    }

    /**
   * Describe the GET method
   * 
   * @param info
   *          WADL method information
   */
    public void describeGet(MethodInfo info) {
        info.setDocumentation("Method to list all JDBC datasources.");
        this.addStandardGetRequestInfo(info);
        ParameterInfo param = new ParameterInfo("datasourceId", false, "xs:string", ParameterStyle.TEMPLATE, "datasourceId to retrieve a single datasource definition.");
        info.getRequest().getParameters().add(param);
        this.addStandardResponseInfo(info);
        this.addStandardInternalServerErrorInfo(info);
        this.addStandardResourceCollectionFilterInfo(info);
    }
}

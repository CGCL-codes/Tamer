package fr.cnes.sitools.resources.tasks.html;

import org.restlet.ext.wadl.MethodInfo;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import fr.cnes.sitools.common.resource.SitoolsParameterizedResource;
import fr.cnes.sitools.dataset.DataSetApplication;
import fr.cnes.sitools.dataset.jdbc.DataSetExplorerUtil;
import fr.cnes.sitools.tasks.TaskUtils;

/**
 * Facade for HTML resource
 * 
 * 
 * @author m.gond
 */
public class HtmlResourceFacade extends SitoolsParameterizedResource {

    @Override
    public void sitoolsDescribe() {
        setName("HtmlResourceFacade");
        setDescription("Export dataset records with HTML format");
    }

    @Override
    protected void describeGet(MethodInfo info) {
        this.addInfo(info);
        info.setIdentifier("retrieve_records and format it as HTML");
        info.setDocumentation("Method to retrieve records of a dataset and format it as HTML");
        addStandardGetRequestInfo(info);
        DataSetExplorerUtil.addDatasetExplorerGetRequestInfo(info);
        DataSetApplication application = (DataSetApplication) getApplication();
        DataSetExplorerUtil.addDatasetExplorerGetFilterInfo(info, application.getFilterChained());
        addStandardResponseInfo(info);
        addStandardInternalServerErrorInfo(info);
    }

    /**
   * Get HTML
   * 
   * @return Representation
   */
    @Get
    public Representation getHtml() {
        return TaskUtils.execute(this, null);
    }
}

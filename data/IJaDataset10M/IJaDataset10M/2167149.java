package com.proclos.ns.etl_server;

/**
 *  ETLServerCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class ETLServerCallbackHandler {

    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public ETLServerCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public ETLServerCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for getProjectInfo method
     * override this method for handling normal response from getProjectInfo operation
     */
    public void receiveResultgetProjectInfo(com.proclos.ns.etl_server.ETLServerStub.GetProjectInfoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getProjectInfo operation
     */
    public void receiveErrorgetProjectInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getTables method
     * override this method for handling normal response from getTables operation
     */
    public void receiveResultgetTables(com.proclos.ns.etl_server.ETLServerStub.GetTablesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getTables operation
     */
    public void receiveErrorgetTables(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for executeComponent method
     * override this method for handling normal response from executeComponent operation
     */
    public void receiveResultexecuteComponent(com.proclos.ns.etl_server.ETLServerStub.ExecuteComponentResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from executeComponent operation
     */
    public void receiveErrorexecuteComponent(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getComponentSchema method
     * override this method for handling normal response from getComponentSchema operation
     */
    public void receiveResultgetComponentSchema(com.proclos.ns.etl_server.ETLServerStub.GetComponentSchemaResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getComponentSchema operation
     */
    public void receiveErrorgetComponentSchema(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for removeComponent method
     * override this method for handling normal response from removeComponent operation
     */
    public void receiveResultremoveComponent(com.proclos.ns.etl_server.ETLServerStub.RemoveComponentResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from removeComponent operation
     */
    public void receiveErrorremoveComponent(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for removeAliasMap method
     * override this method for handling normal response from removeAliasMap operation
     */
    public void receiveResultremoveAliasMap(com.proclos.ns.etl_server.ETLServerStub.RemoveAliasMapResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from removeAliasMap operation
     */
    public void receiveErrorremoveAliasMap(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addSource method
     * override this method for handling normal response from addSource operation
     */
    public void receiveResultaddSource(com.proclos.ns.etl_server.ETLServerStub.AddSourceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addSource operation
     */
    public void receiveErroraddSource(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getCubes method
     * override this method for handling normal response from getCubes operation
     */
    public void receiveResultgetCubes(com.proclos.ns.etl_server.ETLServerStub.GetCubesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getCubes operation
     */
    public void receiveErrorgetCubes(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for removeProject method
     * override this method for handling normal response from removeProject operation
     */
    public void receiveResultremoveProject(com.proclos.ns.etl_server.ETLServerStub.RemoveProjectResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from removeProject operation
     */
    public void receiveErrorremoveProject(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for executeJobs method
     * override this method for handling normal response from executeJobs operation
     */
    public void receiveResultexecuteJobs(com.proclos.ns.etl_server.ETLServerStub.ExecuteJobsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from executeJobs operation
     */
    public void receiveErrorexecuteJobs(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for executeExporters method
     * override this method for handling normal response from executeExporters operation
     */
    public void receiveResultexecuteExporters(com.proclos.ns.etl_server.ETLServerStub.ExecuteExportersResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from executeExporters operation
     */
    public void receiveErrorexecuteExporters(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getDimensions method
     * override this method for handling normal response from getDimensions operation
     */
    public void receiveResultgetDimensions(com.proclos.ns.etl_server.ETLServerStub.GetDimensionsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getDimensions operation
     */
    public void receiveErrorgetDimensions(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getProject method
     * override this method for handling normal response from getProject operation
     */
    public void receiveResultgetProject(com.proclos.ns.etl_server.ETLServerStub.GetProjectResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getProject operation
     */
    public void receiveErrorgetProject(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for removeSource method
     * override this method for handling normal response from removeSource operation
     */
    public void receiveResultremoveSource(com.proclos.ns.etl_server.ETLServerStub.RemoveSourceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from removeSource operation
     */
    public void receiveErrorremoveSource(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getComponentOutline method
     * override this method for handling normal response from getComponentOutline operation
     */
    public void receiveResultgetComponentOutline(com.proclos.ns.etl_server.ETLServerStub.GetComponentOutlineResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getComponentOutline operation
     */
    public void receiveErrorgetComponentOutline(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addJob method
     * override this method for handling normal response from addJob operation
     */
    public void receiveResultaddJob(com.proclos.ns.etl_server.ETLServerStub.AddJobResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addJob operation
     */
    public void receiveErroraddJob(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for removeJob method
     * override this method for handling normal response from removeJob operation
     */
    public void receiveResultremoveJob(com.proclos.ns.etl_server.ETLServerStub.RemoveJobResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from removeJob operation
     */
    public void receiveErrorremoveJob(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getComponentName method
     * override this method for handling normal response from getComponentName operation
     */
    public void receiveResultgetComponentName(com.proclos.ns.etl_server.ETLServerStub.GetComponentNameResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getComponentName operation
     */
    public void receiveErrorgetComponentName(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for executeSource method
     * override this method for handling normal response from executeSource operation
     */
    public void receiveResultexecuteSource(com.proclos.ns.etl_server.ETLServerStub.ExecuteSourceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from executeSource operation
     */
    public void receiveErrorexecuteSource(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addComponent method
     * override this method for handling normal response from addComponent operation
     */
    public void receiveResultaddComponent(com.proclos.ns.etl_server.ETLServerStub.AddComponentResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addComponent operation
     */
    public void receiveErroraddComponent(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for drillThrough method
     * override this method for handling normal response from drillThrough operation
     */
    public void receiveResultdrillThrough(com.proclos.ns.etl_server.ETLServerStub.DrillThroughResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from drillThrough operation
     */
    public void receiveErrordrillThrough(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addPipeline method
     * override this method for handling normal response from addPipeline operation
     */
    public void receiveResultaddPipeline(com.proclos.ns.etl_server.ETLServerStub.AddPipelineResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addPipeline operation
     */
    public void receiveErroraddPipeline(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addAliasMap method
     * override this method for handling normal response from addAliasMap operation
     */
    public void receiveResultaddAliasMap(com.proclos.ns.etl_server.ETLServerStub.AddAliasMapResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addAliasMap operation
     */
    public void receiveErroraddAliasMap(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addConnection method
     * override this method for handling normal response from addConnection operation
     */
    public void receiveResultaddConnection(com.proclos.ns.etl_server.ETLServerStub.AddConnectionResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addConnection operation
     */
    public void receiveErroraddConnection(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getCatalogs method
     * override this method for handling normal response from getCatalogs operation
     */
    public void receiveResultgetCatalogs(com.proclos.ns.etl_server.ETLServerStub.GetCatalogsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getCatalogs operation
     */
    public void receiveErrorgetCatalogs(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getSchemas method
     * override this method for handling normal response from getSchemas operation
     */
    public void receiveResultgetSchemas(com.proclos.ns.etl_server.ETLServerStub.GetSchemasResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getSchemas operation
     */
    public void receiveErrorgetSchemas(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addExporter method
     * override this method for handling normal response from addExporter operation
     */
    public void receiveResultaddExporter(com.proclos.ns.etl_server.ETLServerStub.AddExporterResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addExporter operation
     */
    public void receiveErroraddExporter(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getData method
     * override this method for handling normal response from getData operation
     */
    public void receiveResultgetData(com.proclos.ns.etl_server.ETLServerStub.GetDataResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getData operation
     */
    public void receiveErrorgetData(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for removePipeline method
     * override this method for handling normal response from removePipeline operation
     */
    public void receiveResultremovePipeline(com.proclos.ns.etl_server.ETLServerStub.RemovePipelineResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from removePipeline operation
     */
    public void receiveErrorremovePipeline(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getRunningJobs method
     * override this method for handling normal response from getRunningJobs operation
     */
    public void receiveResultgetRunningJobs(com.proclos.ns.etl_server.ETLServerStub.GetRunningJobsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getRunningJobs operation
     */
    public void receiveErrorgetRunningJobs(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addProject method
     * override this method for handling normal response from addProject operation
     */
    public void receiveResultaddProject(com.proclos.ns.etl_server.ETLServerStub.AddProjectResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addProject operation
     */
    public void receiveErroraddProject(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getDatabases method
     * override this method for handling normal response from getDatabases operation
     */
    public void receiveResultgetDatabases(com.proclos.ns.etl_server.ETLServerStub.GetDatabasesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getDatabases operation
     */
    public void receiveErrorgetDatabases(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getComponentConfig method
     * override this method for handling normal response from getComponentConfig operation
     */
    public void receiveResultgetComponentConfig(com.proclos.ns.etl_server.ETLServerStub.GetComponentConfigResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getComponentConfig operation
     */
    public void receiveErrorgetComponentConfig(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for removeExporter method
     * override this method for handling normal response from removeExporter operation
     */
    public void receiveResultremoveExporter(com.proclos.ns.etl_server.ETLServerStub.RemoveExporterResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from removeExporter operation
     */
    public void receiveErrorremoveExporter(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for removeConnection method
     * override this method for handling normal response from removeConnection operation
     */
    public void receiveResultremoveConnection(com.proclos.ns.etl_server.ETLServerStub.RemoveConnectionResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from removeConnection operation
     */
    public void receiveErrorremoveConnection(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getProjects method
     * override this method for handling normal response from getProjects operation
     */
    public void receiveResultgetProjects(com.proclos.ns.etl_server.ETLServerStub.GetProjectsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getProjects operation
     */
    public void receiveErrorgetProjects(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getColumns method
     * override this method for handling normal response from getColumns operation
     */
    public void receiveResultgetColumns(com.proclos.ns.etl_server.ETLServerStub.GetColumnsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getColumns operation
     */
    public void receiveErrorgetColumns(java.lang.Exception e) {
    }
}

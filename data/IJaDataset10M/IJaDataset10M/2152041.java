package com.tensegrity.webetlclient.modules.core.client.model;

public abstract class TypeCastVisitor implements IModelNodeVisitor, IModelConstants {

    public void visit(IModelNode node) {
        final int typeId = node.getTypeID();
        switch(typeId) {
            case ROOT_TYPE:
                {
                    visitRoot((Root) node);
                    break;
                }
            case ETL_SERVER_TYPE:
                {
                    visitETLServer((ETLServer) node);
                    break;
                }
            case ETL_PROJECT_TYPE:
                {
                    visitETLProject((ETLProject) node);
                    break;
                }
            case EXPORTS_TYPE:
                {
                    ExportsNode exportsNode = (ExportsNode) node;
                    visitSectionNode(exportsNode);
                    visitExportsNode(exportsNode);
                    break;
                }
            case CONTEXS_TYPE:
                {
                    ContextsNode contextsNode = (ContextsNode) node;
                    visitSectionNode(contextsNode);
                    visitContextsNode(contextsNode);
                    break;
                }
            case PIPELINES_TYPE:
                {
                    PipelinesNode pipelinesNode = (PipelinesNode) node;
                    visitSectionNode(pipelinesNode);
                    visitPipelinesNode(pipelinesNode);
                    break;
                }
            case CONNECTIONS_TYPE:
                {
                    ConnectionsNode connectionsNode = (ConnectionsNode) node;
                    visitSectionNode(connectionsNode);
                    visitConnectionsNode(connectionsNode);
                    break;
                }
            case JOBS_TYPE:
                {
                    JobsNode jobsNode = (JobsNode) node;
                    visitSectionNode(jobsNode);
                    visitJobsNode(jobsNode);
                    break;
                }
            case EXPORT_TYPE:
                {
                    visitExportNode((ExportNode) node);
                    break;
                }
            case EXPORT_REF_TYPE:
                {
                    visitRefNode((RefNode) node);
                    visitExportRefNode((ExportRefNode) node);
                    break;
                }
            case CONTEXT_TYPE:
                {
                    visitContextNode((ContextNode) node);
                    break;
                }
            case PARAMETER_TYPE:
                {
                    visitParameterNode((ParameterNode) node);
                    break;
                }
            case JOB_TYPE:
                {
                    visitJobNode((JobNode) node);
                    break;
                }
            case JOB_REF_TYPE:
                {
                    visitRefNode((RefNode) node);
                    visitJobRefNode((JobRefNode) node);
                    break;
                }
            case SOURCES_TYPE:
                {
                    SourcesNode sourcesNode = (SourcesNode) node;
                    visitSectionNode(sourcesNode);
                    visitSourcesNode(sourcesNode);
                    break;
                }
            case CONNECTION_REF_TYPE:
                {
                    visitRefNode((RefNode) node);
                    visitConnectionRefNode((ConnectionRefNode) node);
                    break;
                }
            case SOURCE_REF_TYPE:
                {
                    visitRefNode((RefNode) node);
                    visitSourceNode((SourceRefNode) node);
                    break;
                }
            case CONTEXT_REF_TYPE:
                {
                    visitRefNode((RefNode) node);
                    visitContextRefNode((ContextRefNode) node);
                    break;
                }
            default:
                {
                    throw new RuntimeException("Node of unknown type: " + node);
                }
        }
    }

    public abstract void visitSectionNode(SectionNode node);

    public abstract void visitContextRefNode(ContextRefNode node);

    public abstract void visitRefNode(RefNode node);

    public abstract void visitSourceNode(SourceRefNode node);

    public abstract void visitConnectionRefNode(ConnectionRefNode node);

    public abstract void visitSourcesNode(SourcesNode node);

    public abstract void visitJobRefNode(JobRefNode node);

    public abstract void visitJobNode(JobNode node);

    public abstract void visitParameterNode(ParameterNode node);

    public abstract void visitContextNode(ContextNode node);

    public abstract void visitExportRefNode(ExportRefNode node);

    public abstract void visitExportNode(ExportNode node);

    public abstract void visitJobsNode(JobsNode node);

    public abstract void visitConnectionsNode(ConnectionsNode node);

    public abstract void visitPipelinesNode(PipelinesNode node);

    public abstract void visitContextsNode(ContextsNode node);

    public abstract void visitExportsNode(ExportsNode node);

    public abstract void visitETLProject(ETLProject project);

    public abstract void visitRoot(Root root);

    public abstract void visitETLServer(ETLServer server);
}

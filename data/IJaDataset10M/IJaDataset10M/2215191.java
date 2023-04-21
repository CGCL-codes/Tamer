package progranet.model.service.tags;

import progranet.ganesa.metamodel.Facet;
import progranet.ganesa.metamodel.View;
import progranet.model.service.ModelService;
import progranet.model.service.ReportService;

public class ExecutionContext {

    private Facet facet;

    private View view;

    private ReportService reportService;

    private ModelService modelService;

    private progranet.omg.ocl.expression.ExecutionContext executionContext;

    private CompilationContext compilationContext;

    private boolean verbatim = false;

    public ExecutionContext(Facet facet, View view, ReportService reportService, ModelService modelService, progranet.omg.ocl.expression.ExecutionContext executionContext, CompilationContext compilationContext) {
        this.facet = facet;
        this.view = view;
        this.reportService = reportService;
        this.modelService = modelService;
        this.executionContext = executionContext;
        this.compilationContext = compilationContext;
    }

    public ExecutionContext(ExecutionContext outer, progranet.omg.ocl.expression.ExecutionContext executionContext, CompilationContext compilationContext) {
        this(outer.getFacet(), outer.getView(), outer.getReportService(), outer.getModelService(), executionContext, compilationContext);
    }

    public Facet getFacet() {
        return this.facet;
    }

    public View getView() {
        return this.view;
    }

    public ReportService getReportService() {
        return this.reportService;
    }

    public ModelService getModelService() {
        return this.modelService;
    }

    public progranet.omg.ocl.expression.ExecutionContext getExecutionContext() {
        return this.executionContext;
    }

    public CompilationContext getCompilationContext() {
        return this.compilationContext;
    }

    public void setVerbatim(boolean verbatim) {
        this.verbatim = verbatim;
    }

    public boolean isVerbatim() {
        return verbatim;
    }
}

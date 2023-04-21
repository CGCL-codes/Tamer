package game.report.subreports;

import configuration.report.CfgReports;
import fakegame.flow.Blackboard;
import fakegame.flow.operations.CrossValidationStrategy;
import game.report.ISubreport;
import game.report.SRRenderer;
import game.report.SubreportContainer;
import game.report.srobjects.ISRObjectRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: drchaj1
 * Date: Jan 10, 2010
 * Time: 5:42:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingTestingSR implements ISubreport {

    private SubreportContainer subreportContainer;

    private final ModelSR modelReport;

    private final EvaluationSR trainEvaluation;

    private final EvaluationSR testEvaluation;

    private final Blackboard blackboard;

    private final CfgReports reportCfg;

    private String headerString;

    public TrainingTestingSR(Blackboard blackboard, ModelSR modelReport, EvaluationSR trainEvaluation, EvaluationSR testEvaluation) {
        this.blackboard = blackboard;
        this.modelReport = modelReport;
        this.trainEvaluation = trainEvaluation;
        this.testEvaluation = testEvaluation;
        this.reportCfg = blackboard.getConfigurations().getGeneralReportConfig();
        create();
    }

    private void create() {
        headerString = blackboard.getString(CrossValidationStrategy.CV_INFO);
        subreportContainer = new SubreportContainer();
        subreportContainer.addSRObject(modelReport);
        subreportContainer.addSRObject(trainEvaluation);
        subreportContainer.addSRObject(testEvaluation);
    }

    @Override
    public SubreportContainer getContainer() {
        return subreportContainer;
    }

    @Override
    public String getName() {
        return "Training/Testing Sets Strategy: " + headerString;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public ISRObjectRenderer getRenderer(SRRenderer renderer) {
        return renderer.createSubreportRenderer(this);
    }
}

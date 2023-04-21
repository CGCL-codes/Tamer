package scap.check.assessment;

import java.util.Collection;
import scap.check.content.ResultContent;
import scap.check.content.ResultContext;

public interface AssessmentResult {

    public enum Status {

        SUCCESSFUL, ERROR
    }

    AssessmentFile getAssessmentFile();

    Status getStatus();

    AssessmentResultItem getAssessmentResultItem(String name);

    Collection<? extends AssessmentResultItem> getAssessmentResultItems();

    String getStatusMessage();

    ResultContext getResultContext();

    Collection<ResultContent> getSupportingResultContent();
}

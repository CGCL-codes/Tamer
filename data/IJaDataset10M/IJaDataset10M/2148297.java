package au.gov.nla.aons.mvc.actions.repository;

import java.util.Iterator;
import java.util.List;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import au.gov.nla.aons.constants.TestResultStatus;
import au.gov.nla.aons.repository.RepositoryManager;
import au.gov.nla.aons.repository.domain.Repository;
import au.gov.nla.aons.repository.domain.RepositoryTestResult;

public class TestRepositoryAction implements Action {

    private String repositoryName;

    private RepositoryManager repositoryManager;

    public Event execute(RequestContext context) throws Exception {
        Repository repository = (Repository) context.getFlowScope().get(repositoryName);
        List<RepositoryTestResult> testResults = repositoryManager.testRepository(repository);
        boolean success = true;
        Iterator<RepositoryTestResult> testResultIter = testResults.iterator();
        while (testResultIter.hasNext()) {
            RepositoryTestResult result = (RepositoryTestResult) testResultIter.next();
            if (!result.getStatus().equals(TestResultStatus.SUCCESS)) {
                success = false;
                break;
            }
        }
        context.getFlowScope().put("testResults", testResults);
        if (success) {
            context.getFlowScope().put("testsPassed", Boolean.TRUE);
            return new Event(this, "testsPassed");
        } else {
            context.getFlowScope().put("testsPassed", Boolean.FALSE);
            return new Event(this, "testsFailed");
        }
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}

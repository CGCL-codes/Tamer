package ch.skyguide.tools.requirement.hmi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.skyguide.tools.requirement.data.AbstractRequirement;
import ch.skyguide.tools.requirement.data.AbstractTestResult;
import ch.skyguide.tools.requirement.data.AutomatedTestResult;
import ch.skyguide.tools.requirement.data.IRequirementVisitor;
import ch.skyguide.tools.requirement.data.ITestResultVisitor;
import ch.skyguide.tools.requirement.data.ManualTestResult;
import ch.skyguide.tools.requirement.data.ManualTestStep;
import ch.skyguide.tools.requirement.data.Requirement;
import ch.skyguide.tools.requirement.data.RequirementDomain;
import ch.skyguide.tools.requirement.data.RequirementProject;

public class RequirementTestDependencies {

    @SuppressWarnings("serial")
    private static class RequirementInUseException extends RuntimeException {

        private final ManualTestResult testResult;

        public RequirementInUseException(ManualTestResult _testResult) {
            super();
            testResult = _testResult;
        }

        public ManualTestResult getTestResult() {
            return testResult;
        }
    }

    private static class TestVisitor implements ITestResultVisitor {

        private final Requirement requirement;

        public TestVisitor(final Requirement _requirement) {
            super();
            requirement = _requirement;
        }

        public void visit(AutomatedTestResult _automatedTestResult) {
        }

        public void visit(ManualTestResult _manualTestResult) {
            for (ManualTestStep step : _manualTestResult) {
                if (step.getRequirement() == requirement) {
                    throw new RequirementInUseException(_manualTestResult);
                }
            }
        }
    }

    private static class RequirementVisitor implements IRequirementVisitor, Iterable<ManualTestResult> {

        private final Requirement referenceRequirement;

        private final Set<ManualTestResult> referencingTests = new HashSet<ManualTestResult>();

        public RequirementVisitor(Requirement _referenceRequirement) {
            super();
            referenceRequirement = _referenceRequirement;
        }

        public void visit(RequirementProject _requirementProject) {
            visit((RequirementDomain) _requirementProject);
        }

        public void visit(RequirementDomain _requirementDomain) {
            for (AbstractRequirement req : _requirementDomain) {
                req.accept(this);
            }
        }

        public void visit(Requirement requirement) {
            final TestVisitor testVisitor = new TestVisitor(referenceRequirement);
            try {
                requirement.accept(testVisitor);
            } catch (RequirementInUseException e) {
                referencingTests.add(e.getTestResult());
            }
        }

        public Iterator<ManualTestResult> iterator() {
            return referencingTests.iterator();
        }

        public boolean hasReferences() {
            return !referencingTests.isEmpty();
        }

        protected Requirement getReferenceRequirement() {
            return referenceRequirement;
        }
    }

    private final RequirementVisitor visitor;

    public RequirementTestDependencies(Requirement _referenceRequirement) {
        super();
        visitor = new RequirementVisitor(_referenceRequirement);
        _referenceRequirement.getProject().accept(visitor);
    }

    public boolean hasReferences() {
        return visitor.hasReferences();
    }

    public List<ManualTestStep> cleanupReferences() {
        final List<ManualTestStep> cleanedSteps = new ArrayList<ManualTestStep>();
        for (AbstractTestResult test : visitor) {
            cleanupReferences(cleanedSteps, test);
        }
        return cleanedSteps;
    }

    private void cleanupReferences(final List<ManualTestStep> cleanedSteps, final AbstractTestResult test) {
        for (ManualTestStep step : test) {
            if (step.getRequirement() == visitor.getReferenceRequirement()) {
                step.setRequirement(null);
                cleanedSteps.add(step);
            }
        }
    }

    public static void cleanReferences(final List<ManualTestStep> cleanedSteps) {
        for (ManualTestStep step : cleanedSteps) {
            step.setRequirement(null);
        }
    }

    public static void restoreReferences(final Requirement requirement, final List<ManualTestStep> cleanedSteps) {
        for (ManualTestStep step : cleanedSteps) {
            step.setRequirement(requirement);
        }
    }

    public String getDependenciesString() {
        final StringBuilder buffer = new StringBuilder();
        boolean first = true;
        for (AbstractTestResult test : visitor) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(test.getName());
        }
        return buffer.toString();
    }
}

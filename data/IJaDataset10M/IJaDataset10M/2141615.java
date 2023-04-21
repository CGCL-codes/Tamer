package org.jtestconnect.codechecker.requirements.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jtestconnect.codechecker.impl.Context;
import org.jtestconnect.codechecker.impl.MatchedAnnotation;
import org.jtestconnect.codechecker.requirements.IRequirementMatcher;
import org.jtestconnect.codechecker.requirements.TestRequirement;
import org.jtestconnect.common.Artifact;
import org.jtestconnect.common.PackageAndClass;
import org.jtestconnect.scanner.AccessModifier;
import org.jtestconnect.scanner.IJavaSourceFileScannerListener;
import org.jtestconnect.scanner.impl.JavaASTWalkerScanner;

public class RequirementMatcher implements IRequirementMatcher, IJavaSourceFileScannerListener {

    private List<TestRequirement> testRequirements;

    private Map<TestRequirement, Boolean> matches = new HashMap<TestRequirement, Boolean>();

    private String currentPackage;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Map<TestRequirement, Boolean> matchRequirements(final Context context, final File targetFile, final List<TestRequirement> fileTestRequirements) {
        if (context == null) {
            throw new IllegalArgumentException("Param was null: context");
        }
        if (targetFile == null) {
            throw new IllegalArgumentException("Param was null: file");
        }
        if (fileTestRequirements == null) {
            throw new IllegalArgumentException("Param was null: context");
        }
        resetMatchMap(fileTestRequirements);
        this.testRequirements = fileTestRequirements;
        final JavaASTWalkerScanner scanner = new JavaASTWalkerScanner(targetFile);
        scanner.registerListener(this);
        scanner.performScan();
        return this.matches;
    }

    /**
	 * Resets all of matches for a new processing run.
	 * @param testRequirements The test requirements to initialise the 
	 * matched map with.
	 */
    private void resetMatchMap(final List<TestRequirement> testRequirements) {
        this.matches = new HashMap<TestRequirement, Boolean>();
        for (final TestRequirement testRequirement : testRequirements) {
            this.matches.put(testRequirement, Boolean.FALSE);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void matchedClass(final String className, final List<MatchedAnnotation> annotations, final int lineNum, final int colNum) {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void matchedMethod(final String className, final String methodName, final AccessModifier accessModifier, final List<MatchedAnnotation> annotations, final int lineNum, final int colNum) {
        for (final TestRequirement testRequirement : this.testRequirements) {
            final Artifact artifact = testRequirement.getExpectedArtifact();
            final PackageAndClass thisPackageAndClass = new PackageAndClass(this.currentPackage, className);
            if (thisPackageAndClass.equals(artifact.getPackageAndClass()) && methodName.equals(artifact.getName())) {
                this.matches.put(testRequirement, Boolean.TRUE);
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void matchedPackage(final String packageName) {
        this.currentPackage = packageName;
    }
}

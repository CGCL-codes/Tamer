package org.moreunit.mock;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.moreunit.mock.dependencies.Dependencies;
import org.moreunit.mock.log.Logger;
import org.moreunit.mock.model.MockingTemplate;
import org.moreunit.mock.preferences.Preferences;
import org.moreunit.mock.templates.MockingTemplateException;
import org.moreunit.mock.templates.MockingTemplateStore;
import org.moreunit.mock.templates.TemplateProcessor;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DependencyMocker {

    private final Preferences preferences;

    private final MockingTemplateStore mockingTemplateStore;

    private final TemplateProcessor templateProcessor;

    private final Logger logger;

    @Inject
    public DependencyMocker(Preferences preferences, MockingTemplateStore mockingTemplateStore, TemplateProcessor templateApplicator, Logger logger) {
        this.preferences = preferences;
        this.mockingTemplateStore = mockingTemplateStore;
        this.templateProcessor = templateApplicator;
        this.logger = logger;
    }

    public void mockDependencies(Dependencies dependencies, IType classUnderTest, IType testCase) {
        if (dependencies.isEmpty()) {
            return;
        }
        MockingTemplate template = getTemplate(classUnderTest.getJavaProject());
        if (template == null) {
            return;
        }
        try {
            templateProcessor.applyTemplate(template, dependencies, classUnderTest, testCase);
        } catch (MockingTemplateException e) {
            logger.error("Could not apply " + template + " to " + testCase.getElementName(), e);
        }
    }

    private MockingTemplate getTemplate(IJavaProject project) {
        final String templateId = preferences.getMockingTemplate(project);
        MockingTemplate template = mockingTemplateStore.get(templateId);
        if (template == null) {
            logger.error("Template not found: " + templateId);
        }
        if (logger.debugEnabled()) {
            logger.debug("MockDependenciesAction: retrieved template: " + template);
        }
        return template;
    }
}

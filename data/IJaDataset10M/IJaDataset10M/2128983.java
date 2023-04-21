package org.nakedobjects.ide.ui.wizards.proj.clnt;

import org.nakedobjects.ide.ui.wizards.proj.common.AbstractProjectTemplateSection;
import org.nakedobjects.ide.ui.wizards.proj.common.AbstractProjectTemplateWizard;
import org.nakedobjects.ide.ui.wizards.proj.common.ProjectWizardsMessages;
import org.nakedobjects.ide.ui.wizards.proj.common.pages.GenerateExampleCodeProvider;

@SuppressWarnings("restriction")
public class ClientProjectTemplateWizard extends AbstractProjectTemplateWizard {

    public ClientProjectTemplateWizard(GenerateExampleCodeProvider provider) {
        super(provider);
    }

    public AbstractProjectTemplateSection createTemplateSection() {
        return new ClientProjectTemplateSection(this);
    }

    @Override
    protected String getProjectWindowTitle() {
        return ProjectWizardsMessages.NewClientProject_windowTitle;
    }
}

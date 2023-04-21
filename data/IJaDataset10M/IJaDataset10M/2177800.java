package com.halware.nakedide.eclipse.core.wiz.proj.fixt;

import org.apache.log4j.Logger;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.ui.IFieldData;
import com.halware.nakedide.eclipse.core.wiz.proj.common.AbstractProjectCreationOperation;
import com.halware.nakedide.eclipse.core.wiz.proj.common.pages.GenerateExampleCodeProvider;

@SuppressWarnings("restriction")
public class FixtureProjectCreationOperation extends AbstractProjectCreationOperation {

    private static final Logger LOGGER = Logger.getLogger(FixtureProjectCreationOperation.class);

    protected Logger getLOGGER() {
        return LOGGER;
    }

    public FixtureProjectCreationOperation(IFieldData data, IProjectProvider provider, GenerateExampleCodeProvider generateExampleCodeProvider) {
        super(data, provider, new FixtureProjectTemplateWizard(generateExampleCodeProvider));
    }
}

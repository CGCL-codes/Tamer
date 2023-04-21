package org.jactr.eclipse.ui.wizards.templates;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.ui.templates.BooleanOption;
import org.eclipse.pde.ui.templates.PluginReference;
import org.eclipse.pde.ui.templates.TemplateOption;

/**
 * basic template that contributes a jACT-R Module
 */
public class ModuleContributorTemplateSection extends BaseACTRContributorTemplateSection {

    public static final String KEY_CONTRIBUTE_MODULE = "contributeModule";

    public static final String KEY_MODULE_CLASS = "moduleClass";

    public static final String KEY_MODULE_NAME = "moduleName";

    public static final String KEY_PM_MODULE = "isPMModule";

    public static final String KEY_CONTRIBUTE_AST = "contributeAST";

    public static final String DEFAULT_MODULE_PACKAGE = "edu.yourUniversity.jactr.modules";

    public static final String DEFAULT_MODULE_CLASS = "YourModule";

    TemplateOption _contributeModule;

    TemplateOption _modulePackageName;

    TemplateOption _moduleClassName;

    TemplateOption _moduleName;

    TemplateOption _pmModule;

    TemplateOption _contributeAST;

    public ModuleContributorTemplateSection() {
        setPageCount(1);
        _contributeModule = addOption(KEY_CONTRIBUTE_MODULE, "Create a module", false, 0);
        _moduleName = addOption(KEY_MODULE_NAME, "Module's name", "", 0);
        _moduleClassName = addOption(KEY_MODULE_CLASS, "Module class name", DEFAULT_MODULE_CLASS, 0);
        _modulePackageName = addOption(KEY_PACKAGE_NAME, "Module package", DEFAULT_MODULE_PACKAGE, 0);
        _pmModule = addOption(KEY_PM_MODULE, "Is an embodied module", false, 0);
        _contributeAST = addOption(KEY_CONTRIBUTE_AST, "Will this module provide chunk-types, chunks, productions, or buffers", false, 0);
        _moduleName.setRequired(false);
        _modulePackageName.setRequired(true);
        _moduleClassName.setRequired(true);
        _moduleName.setEnabled(false);
        _modulePackageName.setEnabled(false);
        _moduleClassName.setEnabled(false);
        _pmModule.setEnabled(false);
        _contributeAST.setEnabled(false);
    }

    @Override
    public void addPages(Wizard wizard) {
        WizardPage page = createPage(0, null);
        page.setTitle("Module contribution");
        page.setDescription("Does this project contribute a new functional Module to ACT-R? If so, provide the package and class name of the module and it will be generated");
        wizard.addPage(page);
        markPagesAdded();
    }

    @Override
    protected void updateModel(IProgressMonitor monitor) throws CoreException {
        if (getBooleanOption(KEY_CONTRIBUTE_MODULE)) {
            IPluginBase plugin = model.getPluginBase();
            IPluginExtension extension = createExtension("org.jactr.modules", true);
            IPluginModelFactory factory = model.getPluginFactory();
            IPluginElement moduleElement = factory.createElement(extension);
            moduleElement.setName("module");
            moduleElement.setAttribute("name", getStringOption(KEY_MODULE_NAME));
            moduleElement.setAttribute("class", getStringOption(KEY_PACKAGE_NAME) + "." + getStringOption(KEY_MODULE_CLASS));
            moduleElement.setAttribute("requiresCommonReality", "" + getBooleanOption(KEY_PM_MODULE));
            moduleElement.setAttribute("defaultEnabled", "false");
            IPluginElement descElement = factory.createElement(moduleElement);
            descElement.setName("description");
            descElement.setText("This is the description of your new module");
            moduleElement.add(descElement);
            extension.add(moduleElement);
            if (!extension.isInTheModel()) plugin.add(extension);
            if (getBooleanOption(KEY_CONTRIBUTE_AST)) {
                extension = createExtension("org.jactr.io.astparticipants", true);
                factory = model.getPluginFactory();
                IPluginElement participant = factory.createElement(extension);
                participant.setName("astparticipant");
                participant.setAttribute("contributingClass", getStringOption(KEY_PACKAGE_NAME) + "." + getStringOption(KEY_MODULE_CLASS));
                participant.setAttribute("class", getStringOption(KEY_PACKAGE_NAME) + ".ModuleASTParticipant");
                participant.setAttribute("content", getStringOption(KEY_PACKAGE_NAME).replace(".", "/") + "/module-content.jactr");
                extension.add(participant);
                if (!extension.isInTheModel()) plugin.add(extension);
            }
            exportPackages(Collections.singleton(getStringOption(KEY_PACKAGE_NAME)));
        }
    }

    @Override
    public String getLabel() {
        return "Contributes jACT-R Module";
    }

    public String getUsedExtensionPoint() {
        return "org.jactr.modules";
    }

    @Override
    public String getSectionId() {
        if (getBooleanOption(KEY_CONTRIBUTE_MODULE)) {
            if (getBooleanOption(KEY_PM_MODULE)) return "pm_module";
            return "module";
        }
        return null;
    }

    @Override
    protected boolean isOkToCreateFile(File file) {
        if (file.getName().endsWith(".jactr") && !getBooleanOption(KEY_CONTRIBUTE_AST)) return false;
        return true;
    }

    @Override
    public void validateOptions(TemplateOption changed) {
        if (changed == _contributeModule) {
            boolean selected = ((BooleanOption) _contributeModule).isSelected();
            setOptionEnabled(KEY_MODULE_NAME, selected);
            setOptionEnabled(KEY_PACKAGE_NAME, selected);
            setOptionEnabled(KEY_MODULE_CLASS, selected);
            setOptionEnabled(KEY_PM_MODULE, selected);
            setOptionEnabled(KEY_CONTRIBUTE_AST, selected);
        }
        boolean error = false;
        for (TemplateOption option : getOptions(0)) if (option.isRequired() && option.isEnabled() && option.isEmpty()) {
            flagMissingRequiredOption(option);
            error = true;
        }
        if (!error) resetPageState();
    }

    @Override
    protected Collection<IPluginReference> getDefaultPluginReferences() {
        Collection<IPluginReference> references = super.getDefaultPluginReferences();
        if (getBooleanOption(KEY_PM_MODULE)) references.add(new PluginReference("org.commonreality", null, 0));
        return references;
    }

    @Override
    public String getReplacementString(String fileName, String key) {
        if ("packageLocation".equals(key)) return getStringOption(KEY_PACKAGE_NAME).replace(".", "/");
        return super.getReplacementString(fileName, key);
    }
}

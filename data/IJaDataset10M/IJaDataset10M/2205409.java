package net.sf.lunareclipse.internal.ui.wizards;

import net.sf.lunareclipse.core.LuaNature;
import net.sf.lunareclipse.internal.ui.LuaImages;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.wizards.NewSourceModulePage;
import org.eclipse.dltk.ui.wizards.NewSourceModuleWizard;

public class LuaFileCreationWizard extends NewSourceModuleWizard {

    public LuaFileCreationWizard() {
        setDefaultPageImageDescriptor(LuaImages.DESC_WIZBAN_FILE_CREATION);
        setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
        setWindowTitle(LuaWizardMessages.FileCreationWizard_title);
    }

    protected NewSourceModulePage createNewSourceModulePage() {
        return new NewSourceModulePage() {

            protected String getRequiredNature() {
                return LuaNature.NATURE_ID;
            }

            protected String getPageDescription() {
                return "This wizard creates a new Lua file.";
            }

            protected String getPageTitle() {
                return "Create new Lua file";
            }
        };
    }
}

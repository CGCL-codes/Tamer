package net.sf.lunareclipse.internal.debug.ui.interpreters;

import net.sf.lunareclipse.core.LuaNature;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterContainerWizardPage;

public class LuaInterpreterContainerWizardPage extends AbstractInterpreterContainerWizardPage {

    @Override
    public String getScriptNature() {
        return LuaNature.NATURE_ID;
    }
}

package com.dfruits.ui.wizards;

import java.util.ArrayList;
import java.util.List;
import net.java.custos.annots.Post;
import net.java.custos.annots.Pre;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;
import com.dfruits.dto.LazyLoader;
import com.dfruits.ui.DUIPlugin;

public class WizardsManager {

    private static WizardsManager plugin;

    public static class XUIWizardDef implements Cloneable {

        public String id;

        public String description;

        public String context;

        public boolean clearContextOnOpen;

        public boolean autoBind;

        public IFinishPerformer onFinishExec;

        public boolean confirmFinish;

        public List<WizardPageDef> pages = new ArrayList<WizardPageDef>();
    }

    public static class WizardPageDef {

        public String formID;

        public String description;

        public String selectionID;

        public IPageValidator validator;

        public String validatorSrc;

        public String message;
    }

    private List<XUIWizardDef> xuiWizardDefs = new ArrayList<XUIWizardDef>();

    public void start(BundleContext context) throws Exception {
        plugin = this;
        loadXUIWizards();
    }

    public List<XUIWizardDef> getXuiWizardDefs() {
        return xuiWizardDefs;
    }

    public XUIWizardDef findWizard(String wizardID) {
        XUIWizardDef ret = null;
        for (XUIWizardDef def : xuiWizardDefs) {
            if (def.id.equals(wizardID)) {
                ret = def;
                break;
            }
        }
        return ret;
    }

    public boolean showWizard(IWizard wizard) {
        WizardDialog dialog = new WizardDialog(new Shell(PlatformUI.getWorkbench().getDisplay().getActiveShell()), wizard) {
        };
        return dialog.open() == dialog.OK;
    }

    public void showWizard(String wizardFormID, Object... params) {
        XUIWizard wizard = new XUIWizard(wizardFormID);
        wizard.setExecParams(params);
        showWizard(wizard);
    }

    public static WizardsManager getPlugin() {
        return plugin;
    }

    private void loadXUIWizards() {
        List<IConfigurationElement> elements = DUIPlugin.getConfigurationElements("xuiWizards", "xuiWizard");
        for (IConfigurationElement element : elements) {
            loadWizardDefinition(element);
        }
    }

    @Pre(exec = "id = element.getAttribute( ''id'' );" + "$log.info(''registering wizard: '' + id )")
    @Post(cond = "this.findWizard( id ) != null", onFail = "$log.error(''wizard not found: '' + id)")
    private void loadWizardDefinition(IConfigurationElement element) {
        XUIWizardDef def = new XUIWizardDef();
        def.id = element.getAttribute("id");
        def.context = element.getAttribute("context");
        String clearCtx = element.getAttribute("clear-context");
        def.clearContextOnOpen = clearCtx == null ? true : Boolean.parseBoolean(clearCtx);
        def.description = element.getAttribute("description");
        def.autoBind = Boolean.parseBoolean(element.getAttribute("auto-bind"));
        def.confirmFinish = Boolean.parseBoolean(element.getAttribute("confirm-finish"));
        def.onFinishExec = LazyLoader.newInstance(element.getAttribute("onFinishExec"), element.getContributor().getName(), IFinishPerformer.class);
        for (IConfigurationElement child : element.getChildren("page")) {
            WizardPageDef pDef = new WizardPageDef();
            pDef.formID = child.getAttribute("formID");
            pDef.description = child.getAttribute("description");
            pDef.selectionID = child.getAttribute("selection");
            pDef.validatorSrc = child.getAttribute("validator");
            pDef.message = child.getAttribute("message");
            try {
                pDef.validator = (IPageValidator) element.createExecutableExtension("validator");
            } catch (Exception e) {
            }
            def.pages.add(pDef);
        }
        xuiWizardDefs.add(def);
    }
}

package pluginWorkbench.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import pluginWorkbench.Application;
import applicationWorkbench.Editor;
import applicationWorkbench.uielements.ConfigurationDialog;
import cards.CardConstants;
import cards.model.ProjectModel;

public class ConfigureAction extends WorkbenchPartAction implements PropertyChangeListener {

    private ProjectModel projectModel = null;

    IWorkbenchPart arg0;

    Editor editor;

    public ConfigureAction(IWorkbenchPart arg0) {
        super(arg0);
        if (arg0 instanceof Editor) {
            this.arg0 = arg0;
            this.editor = (Editor) arg0;
            projectModel = editor.getModel();
        }
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, CardConstants.ConfigureIcon));
    }

    public void run() {
        this.editor = (Editor) arg0;
        ConfigurationDialog cnfig = new ConfigurationDialog(null, editor.getModel());
        cnfig.open();
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    public void propertyChange(PropertyChangeEvent arg0) {
    }
}

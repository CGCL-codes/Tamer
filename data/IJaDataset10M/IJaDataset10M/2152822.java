package applicationWorkbench.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWorkbenchPart;
import applicationWorkbench.Editor;
import cards.commands.ArrangeCommand;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;

public class ArrangeAction extends WorkbenchPartAction implements PropertyChangeListener {

    private ProjectModel projectModel = null;

    Editor arg0;

    Editor editor;

    public ArrangeAction(IWorkbenchPart arg0) {
        super(arg0);
        if (arg0 instanceof Editor) {
            this.arg0 = (Editor) arg0;
            this.editor = (Editor) arg0;
            projectModel = editor.getModel();
        }
    }

    private Command createCommand() {
        this.editor = arg0;
        ArrangeCommand cmd = new ArrangeCommand(editor.getModel());
        return cmd;
    }

    @Override
    protected boolean calculateEnabled() {
        List<IterationCardModel> iterationCards = projectModel.getIterations();
        if (iterationCards.size() > 0 || projectModel.getStoryCardModelList().size() > 0) return true; else return false;
    }

    @Override
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        (projectModel).addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        update();
    }

    @Override
    public void run() {
        this.execute(this.createCommand());
    }

    @Override
    public void update() {
        super.update();
    }
}

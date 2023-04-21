package applicationWorkbench.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWorkbenchPart;
import applicationWorkbench.Editor;
import cards.commands.ArrangeWithStoryCardsCommand;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;

public class ArrangeWithStoryCardsAction extends WorkbenchPartAction implements PropertyChangeListener {

    private ProjectModel projectModel = null;

    public ArrangeWithStoryCardsAction(IWorkbenchPart arg0) {
        super(arg0);
        if (arg0 instanceof Editor) {
            Editor editor = (Editor) arg0;
            projectModel = editor.getModel();
        }
    }

    private Command createCommand() {
        ArrangeWithStoryCardsCommand cmd = new ArrangeWithStoryCardsCommand(projectModel);
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

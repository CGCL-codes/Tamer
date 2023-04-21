package rallydemogef.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWorkbenchPart;
import persister.datachangeimplement.PersisterDataChangeImplementer;
import cards.commands.CollapseStoryCardCommand;
import cards.commands.ExpandStoryCardCommand;
import cards.model.IndexCardModel;

public class CollapseStoryCardAction extends WorkbenchPartAction implements PropertyChangeListener {

    public CollapseStoryCardAction(IWorkbenchPart arg0) {
        super(arg0);
    }

    @Override
    protected boolean calculateEnabled() {
        ArrayList<IndexCardModel> indexCards = new ArrayList<IndexCardModel>();
        indexCards.addAll(PersisterDataChangeImplementer.getDataChangeListener().getTableModel().getCardsExceptRemoteMice());
        for (IndexCardModel indexCard : indexCards) {
            if (indexCard.getAllChildren() == null) {
            } else {
                if (indexCard.getAllChildren().size() > 0) return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        this.execute(this.createCommand());
    }

    private Command createCommand() {
        CollapseStoryCardCommand cmd = new CollapseStoryCardCommand();
        cmd.initialize();
        return cmd;
    }

    @Override
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        (PersisterDataChangeImplementer.getDataChangeListener().getTableModel()).addPropertyChangeListener(this);
    }

    @Override
    public void update() {
        super.update();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        update();
    }
}

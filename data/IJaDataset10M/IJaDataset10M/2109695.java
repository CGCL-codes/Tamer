package ch.intertec.storybook.view.lists;

import ch.intertec.storybook.model.PCSDispatcher;
import ch.intertec.storybook.model.PCSDispatcher.Property;
import ch.intertec.storybook.view.model.dbtable.PartDbTable;

public class PartListFrame extends AbstractListFrame {

    private static final long serialVersionUID = 4173551345912200532L;

    @Override
    void init() {
        table = new PartDbTable();
        setTitle("msg.dlg.mng.parts.title");
    }

    @Override
    int getPreferredWidth() {
        return 800;
    }

    @Override
    protected void addListeners() {
        PCSDispatcher.getInstance().addPropertyChangeListener(Property.PART, this);
    }

    @Override
    protected void removeListeners() {
        PCSDispatcher.getInstance().removeAllPropertyChangeListener(this);
    }
}

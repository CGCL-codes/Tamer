package toxTree.ui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import toxTree.data.ActionList;
import toxTree.data.DataModule;

public abstract class DataModulePanel extends JPanel implements Observer {

    protected DataModule dataModule;

    /**
	 * 
	 */
    private static final long serialVersionUID = -7992562763847145598L;

    public DataModulePanel(DataModule dataModule) {
        super();
        setDataModule(dataModule);
        addWidgets(dataModule.getActions());
    }

    public DataModulePanel(DataModule dataModule, boolean arg0) {
        super(arg0);
        setDataModule(dataModule);
        addWidgets(dataModule.getActions());
    }

    public DataModulePanel(DataModule dataModule, LayoutManager arg0) {
        super(arg0);
        setDataModule(dataModule);
        addWidgets(dataModule.getActions());
    }

    public DataModulePanel(DataModule dataModule, LayoutManager arg0, boolean arg1) {
        super(arg0, arg1);
        setDataModule(dataModule);
        addWidgets(dataModule.getActions());
    }

    public void setDataModule(DataModule dataModule) {
        this.dataModule = dataModule;
        if (dataModule instanceof Observable) ((Observable) dataModule).addObserver(this);
    }

    public Dimension getPeferredSize() {
        return new Dimension(256, 64 * 4 + 20);
    }

    protected abstract void addWidgets(ActionList actions);
}

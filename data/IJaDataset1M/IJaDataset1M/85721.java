package blueprint4j.gui;

import java.io.IOException;
import javax.swing.JComponent;
import blueprint4j.utils.BindException;
import blueprint4j.utils.BindFieldInterface;
import blueprint4j.utils.Bindable;

public interface DataPanelInterface {

    void setBinderBindable(Binder p_binder, Bindable p_bindable) throws IOException, BindException;

    String getName();

    void build(Binder binder, Bindable bindable) throws IOException, BindException;

    void activated();

    void deactivated();

    JComponent getComponent();

    boolean doesPanelContain(BindFieldInterface field) throws BindException;
}

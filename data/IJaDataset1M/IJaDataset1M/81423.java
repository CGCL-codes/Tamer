package net.sf.fc.gui.v;

import java.beans.PropertyChangeEvent;
import javax.swing.JDialog;

@SuppressWarnings("serial")
public abstract class AbstractViewDialog extends JDialog implements View {

    @Override
    public abstract void modelPropertyChange(PropertyChangeEvent evt);
}

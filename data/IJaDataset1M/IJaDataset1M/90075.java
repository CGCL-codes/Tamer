package perl.variables;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import org.gjt.sp.jedit.jEdit;

@SuppressWarnings("serial")
public class Variables extends JPanel {

    private static final class SplitterLocationChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName() == JSplitPane.DIVIDER_LOCATION_PROPERTY) {
                jEdit.setProperty("gdbplugin.variables.splitter", evt.getNewValue().toString());
            }
        }
    }

    Watches watchesPanel = null;

    JSplitPane pane = null;

    public Variables() {
        setupUI(null);
    }

    public Variables(Watches watches) {
        setupUI(watches);
    }

    private void setupUI(Watches watches2) {
        setLayout(new GridLayout(0, 1));
        JPanel locals = new JPanel();
        locals.setLayout(new BoxLayout(locals, 1));
        TitledBorder border = new TitledBorder(jEdit.getProperty("debugger-show-locals.title"));
        locals.setBorder(border);
        locals.add(new LocalVariables());
        JPanel watches = new JPanel();
        watches.setLayout(new BoxLayout(watches, 1));
        border = new TitledBorder(jEdit.getProperty("debugger-watches.title"));
        watches.setBorder(border);
        watchesPanel = (watches2 == null) ? new Watches() : watches2;
        watches.add(watchesPanel);
        pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, locals, watches);
        add(pane);
        pane.setDividerLocation(jEdit.getIntegerProperty("gdbplugin.variables.splitter", 350));
        pane.addPropertyChangeListener(new SplitterLocationChangeListener());
    }

    public Watches getWatches() {
        return watchesPanel;
    }

    @Override
    protected void finalize() throws Throwable {
        pane = null;
        super.finalize();
    }
}

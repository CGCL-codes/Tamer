package cross.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import cross.Logging;
import cross.event.EventSource;
import cross.event.IEvent;
import cross.event.IEventSource;
import cross.event.IListener;
import cross.ui.LoadConfigurationDialog;
import cross.ui.events.LoadConfigurationEvent;
import cross.ui.filefilters.PropertiesFileFilter;

public class LoadConfiguration extends AbstractAction implements IEventSource<Configuration> {

    private final EventSource<Configuration> es = new EventSource<Configuration>();

    /**
	 * 
	 */
    private static final long serialVersionUID = 5395067677304337055L;

    private final Logger log = Logging.getLogger(this);

    private final File cwd = null;

    private final FileFilter pf = new PropertiesFileFilter();

    public LoadConfiguration() {
        super();
    }

    public LoadConfiguration(final String name) {
        super(name);
    }

    public LoadConfiguration(final String name, final Icon icon) {
        super(name, icon);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Configuration cfg = LoadConfigurationDialog.getInstance().loadConfiguration(new PropertiesFileFilter());
        fireEvent(new LoadConfigurationEvent(cfg, this));
    }

    @Override
    public void addListener(final IListener<IEvent<Configuration>> l) {
        this.es.addListener(l);
    }

    @Override
    public void fireEvent(final IEvent<Configuration> e) {
        this.es.fireEvent(e);
    }

    @Override
    public void removeListener(final IListener<IEvent<Configuration>> l) {
        this.es.removeListener(l);
    }
}

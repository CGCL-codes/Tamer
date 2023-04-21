package net.sf.jmp3renamer.plugins.Web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import net.sf.jmp3renamer.AbstractTranslatablePlugin;
import net.sf.jmp3renamer.DataPlugin;
import net.sf.jmp3renamer.Translatable;
import net.sf.jmp3renamer.plugins.Web.gui.WebPanel;
import net.sf.jmp3renamer.plugins.Web.provider.WebClientProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Web extends AbstractTranslatablePlugin implements DataPlugin, BundleActivator {

    private static final String VERSION = "0.1";

    private static final String AUTHOR = "Henrik Niehaus, hampelratte@users.sf.net";

    private static final String NAME = "Web Client";

    private static final String DESCRIPTION = "The " + NAME + " connects to several webpages to get all needed " + "data. You just have to enter the artist the album.";

    private static transient Logger logger = LoggerFactory.getLogger(Web.class);

    private WebPanel gui;

    private ProviderTracker providerTracker;

    public String getVersion() {
        return Web.VERSION;
    }

    public String getAuthor() {
        return Web.AUTHOR;
    }

    public String getDescription() {
        return Web.DESCRIPTION;
    }

    public String getName() {
        return NAME;
    }

    public JComponent getGUI() {
        return gui;
    }

    public String toString() {
        return this.getName();
    }

    public Icon getIcon() {
        Icon icon = null;
        InputStream in = this.getClass().getResourceAsStream("/icons/icon32.png");
        int length = -1;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((length = in.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            icon = new ImageIcon(bos.toByteArray());
        } catch (IOException e) {
            logger.warn("Couldn't load icon", e);
        }
        return icon;
    }

    public void coreStarted() {
    }

    public void start(BundleContext ctx) throws Exception {
        loadLanguage();
        providerTracker = new ProviderTracker(ctx, WebClientProvider.class.getName(), null);
        providerTracker.open();
        gui = new WebPanel(providerTracker);
        providerTracker.setPanel(gui);
        registerServices(ctx);
    }

    private void registerServices(BundleContext ctx) {
        ctx.registerService(DataPlugin.class.getName(), this, null);
        ctx.registerService(Translatable.class.getName(), this, null);
    }

    public void stop(BundleContext ctx) throws Exception {
        providerTracker.close();
    }
}

package net.sourceforge.filebot;

import static java.awt.GraphicsEnvironment.*;
import static javax.swing.JFrame.*;
import static net.sourceforge.filebot.Settings.*;
import static net.sourceforge.tuned.ui.TunedUtilities.*;
import java.awt.Desktop;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilderFactory;
import org.kohsuke.args4j.CmdLineException;
import org.w3c.dom.NodeList;
import net.miginfocom.swing.MigLayout;
import net.sf.ehcache.CacheManager;
import net.sourceforge.filebot.cli.ArgumentBean;
import net.sourceforge.filebot.cli.ArgumentProcessor;
import net.sourceforge.filebot.cli.CmdlineOperations;
import net.sourceforge.filebot.format.ExpressionFormat;
import net.sourceforge.filebot.ui.MainFrame;
import net.sourceforge.filebot.ui.SinglePanelFrame;
import net.sourceforge.filebot.ui.sfv.SfvPanelBuilder;
import net.sourceforge.filebot.ui.transfer.FileTransferable;
import net.sourceforge.filebot.web.CachedResource;
import net.sourceforge.tuned.ByteBufferInputStream;
import net.sourceforge.tuned.PreferencesMap.PreferencesEntry;

public class Main {

    /**
	 * @param args
	 */
    public static void main(String... arguments) throws Exception {
        initializeCache();
        initializeSecurityManager();
        try {
            final ArgumentProcessor cli = new ArgumentProcessor();
            final ArgumentBean args = cli.parse(arguments);
            if (args.printHelp() || args.printVersion() || (!args.runCLI() && isHeadless())) {
                System.out.format("%s / %s%n%n", getApplicationIdentifier(), getJavaRuntimeIdentifier());
                if (args.printHelp() || (!args.printVersion() && isHeadless())) {
                    cli.printHelp(args);
                }
                System.exit(0);
            }
            if (args.clearUserData()) {
                System.out.println("Reset preferences and clear cache");
                Settings.forPackage(Main.class).clear();
                CacheManager.getInstance().clearAll();
            }
            Analytics.setEnabled(!args.disableAnalytics);
            if (args.runCLI()) {
                if (args.script != null && !isHeadless()) {
                    try {
                        Class<?> nimbusLook = Class.forName("javax.swing.plaf.nimbus.NimbusLookAndFeel", false, Thread.currentThread().getContextClassLoader());
                        System.setProperty("swing.crossplatformlaf", nimbusLook.getName());
                    } catch (Throwable e) {
                    }
                }
                int status = cli.process(args, new CmdlineOperations());
                System.exit(status);
            }
            try {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception e) {
                            Logger.getLogger(Main.class.getName()).log(Level.WARNING, e.getMessage(), e);
                        }
                        startUserInterface(args);
                    }
                });
                MediaTypes.getDefault();
                if (!"skip".equals(System.getProperty("application.update"))) {
                    checkUpdate();
                }
            } catch (Exception e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private static void startUserInterface(ArgumentBean args) {
        JFrame frame;
        if (args.openSFV()) {
            FileTransferable files = new FileTransferable(args.getFiles(false));
            frame = new SinglePanelFrame(new SfvPanelBuilder()).publish(files);
        } else {
            frame = new MainFrame();
        }
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            restoreWindowBounds(frame, Settings.forPackage(MainFrame.class));
        } catch (Exception e) {
        }
        frame.setVisible(true);
    }

    /**
	 * Show update notifications if updates are available
	 */
    private static void checkUpdate() throws Exception {
        final PreferencesEntry<String> updateIgnoreRevision = Settings.forPackage(Main.class).entry("update.revision.ignore");
        final Properties updateProperties = new CachedResource<Properties>(getApplicationProperty("update.url"), Properties.class, 24 * 60 * 60 * 1000) {

            @Override
            public Properties process(ByteBuffer data) {
                try {
                    Properties properties = new Properties();
                    NodeList fields = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteBufferInputStream(data)).getFirstChild().getChildNodes();
                    for (int i = 0; i < fields.getLength(); i++) {
                        properties.setProperty(fields.item(i).getNodeName(), fields.item(i).getTextContent().trim());
                    }
                    return properties;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.get();
        int latestRev = Integer.parseInt(updateProperties.getProperty("revision"));
        int latestIgnoreRev = Math.max(getApplicationRevisionNumber(), updateIgnoreRevision.getValue() == null ? 0 : Integer.parseInt(updateIgnoreRevision.getValue()));
        if (latestRev > latestIgnoreRev) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final JDialog dialog = new JDialog(JFrame.getFrames()[0], updateProperties.getProperty("title"), ModalityType.APPLICATION_MODAL);
                    final JPanel pane = new JPanel(new MigLayout("fill, nogrid, insets dialog"));
                    dialog.setContentPane(pane);
                    pane.add(new JLabel(ResourceManager.getIcon("window.icon.big")), "aligny top");
                    pane.add(new JLabel(updateProperties.getProperty("message")), "gap 10, wrap paragraph:push");
                    pane.add(new JButton(new AbstractAction("Download", ResourceManager.getIcon("dialog.continue")) {

                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            try {
                                Desktop.getDesktop().browse(URI.create(updateProperties.getProperty("download")));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } finally {
                                dialog.setVisible(false);
                            }
                        }
                    }), "tag ok");
                    pane.add(new JButton(new AbstractAction("Details", ResourceManager.getIcon("action.report")) {

                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            try {
                                Desktop.getDesktop().browse(URI.create(updateProperties.getProperty("discussion")));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }), "tag help2");
                    pane.add(new JButton(new AbstractAction("Ignore", ResourceManager.getIcon("dialog.cancel")) {

                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            updateIgnoreRevision.setValue(updateProperties.getProperty("revision"));
                            dialog.setVisible(false);
                        }
                    }), "tag cancel");
                    dialog.pack();
                    dialog.setLocation(getOffsetLocation(dialog.getOwner()));
                    dialog.setVisible(true);
                }
            });
        }
    }

    private static void restoreWindowBounds(final JFrame window, final Settings settings) {
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                if (!isMaximized(window)) {
                    settings.put("window.x", String.valueOf(window.getX()));
                    settings.put("window.y", String.valueOf(window.getY()));
                    settings.put("window.width", String.valueOf(window.getWidth()));
                    settings.put("window.height", String.valueOf(window.getHeight()));
                }
            }
        });
        int x = Integer.parseInt(settings.get("window.x"));
        int y = Integer.parseInt(settings.get("window.y"));
        int width = Integer.parseInt(settings.get("window.width"));
        int height = Integer.parseInt(settings.get("window.height"));
        window.setBounds(x, y, width, height);
    }

    /**
	 * Shutdown ehcache properly, so that disk-persistent stores can actually be saved to disk
	 */
    private static void initializeCache() {
        System.setProperty("ehcache.disk.store.dir", new File(getApplicationFolder(), "cache").getAbsolutePath());
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                CacheManager.getInstance().shutdown();
            }
        });
    }

    /**
	 * Initialize default SecurityManager and grant all permissions via security policy.
	 * Initialization is required in order to run {@link ExpressionFormat} in a secure sandbox.
	 */
    private static void initializeSecurityManager() {
        try {
            Policy.setPolicy(new Policy() {

                @Override
                public boolean implies(ProtectionDomain domain, Permission permission) {
                    return true;
                }

                @Override
                public PermissionCollection getPermissions(CodeSource codesource) {
                    return new Permissions();
                }
            });
            System.setSecurityManager(new SecurityManager());
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.WARNING, e.toString(), e);
        }
    }
}

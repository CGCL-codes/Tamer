package info.reflectionsofmind.connexion.platform.gui.host;

import info.reflectionsofmind.connexion.platform.core.server.IRemoteClient;
import info.reflectionsofmind.connexion.platform.core.server.IServer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ClientsPanel extends JPanel implements IServer.IListener {

    private final HostGameFrame window;

    private final List<ClientPanel> panels = new ArrayList<ClientPanel>();

    public ClientsPanel(final HostGameFrame window) {
        this.window = window;
        this.window.getServer().addListener(this);
        setBorder(BorderFactory.createTitledBorder("Clients"));
        setLayout(new MigLayout("ins 0", "[]", "[]"));
        getLayout().layoutContainer(this);
    }

    @Override
    public void onClientConnected(final IRemoteClient client) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final ClientPanel panel = new ClientPanel(ClientsPanel.this, client);
                ClientsPanel.this.panels.add(panel);
                ClientsPanel.this.add(panel, "grow, span");
            }
        });
    }

    @Override
    public void onClientMessage(final IRemoteClient client, final String message) {
    }

    @Override
    public void onClientDisconnected(final IRemoteClient client) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final ClientPanel clientPanel = Iterables.find(ClientsPanel.this.panels, new Predicate<ClientPanel>() {

                    @Override
                    public boolean apply(final ClientPanel panel) {
                        return panel.getClient() == client;
                    }
                });
                ClientsPanel.this.panels.remove(clientPanel);
                remove(clientPanel);
            }
        });
    }

    public HostGameFrame getHostGameDialog() {
        return this.window;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        for (final ClientPanel panel : this.panels) {
            panel.setEnabled(enabled);
        }
    }
}

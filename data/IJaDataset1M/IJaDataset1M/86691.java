package demo.messaging.client.ui;

import com.google.gwt.user.client.ui.DockPanel;
import demo.messaging.client.core.ClientManager;
import demo.messaging.client.core.MessageEvent;
import demo.util_ui.client.ui.SimpleDialog;

public abstract class AbstractPluginUI extends SimpleDialog {

    protected ChatPanel chatPanel = new ChatPanel();

    public AbstractPluginUI(final MessageEvent event, final ClientManager clientManager) {
        super("Quit");
        setText("You are chatting with " + ClientManager.getClientManager().getCurrentConnectedUser());
        getContentPanel().add(this.chatPanel, DockPanel.CENTER);
        clientManager.getEventListeners().add(this.chatPanel);
    }

    public ChatPanel getChatPanel() {
        return this.chatPanel;
    }

    @Override
    protected void onCloseButtonClicked() {
        ClientManager.getClientManager().endConnection();
    }

    public void deactivate(final ClientManager clientManager) {
        this.removeFromParent();
        clientManager.getEventListeners().remove(this.chatPanel);
    }

    public void setChatPanel(final ChatPanel chatPanel) {
        this.chatPanel = chatPanel;
    }
}

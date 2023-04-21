package webirc.client.gui.contactpanel;

import webirc.client.Channel;
import webirc.client.WebIRC;
import webirc.client.gui.ImageButton;
import webirc.client.gui.sectionpanel.Section;

/**
 * @author Ayzen
 * @version 1.0 17.08.2006 0:30:32
 */
public class ChannelsSection extends Section {

    private ImageButton enterBtn;

    public ChannelsSection() {
        super();
        setTitle(WebIRC.mainMessages.channels());
    }

    public void removeChannel(Channel channel) {
        for (int i = 0; i < contentPanel.getWidgetCount(); i++) if (((ChannelLine) contentPanel.getWidget(i)).getChannel().equals(channel)) {
            contentPanel.remove(i);
            break;
        }
    }
}

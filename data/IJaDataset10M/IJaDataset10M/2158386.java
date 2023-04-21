package net.sourceforge.x360mediaserve.plugins.wicketUI.impl.pages;

import java.util.Map.Entry;
import net.sourceforge.x360mediaserve.api.database.items.media.AudioItem;
import net.sourceforge.x360mediaserve.api.formats.playback.images.ImagePlaybackInformation;
import net.sourceforge.x360mediaserve.plugins.wicketUI.impl.pages.components.ExternalImage;
import net.sourceforge.x360mediaserve.plugins.wicketUI.impl.pages.models.ItemModel;
import net.sourceforge.x360mediaserve.plugins.wicketUI.osgi.X360MSServices;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModalAudioPage extends WebPage {

    static Logger logger = LoggerFactory.getLogger(ModalAudioPage.class);

    String itemId;

    ItemModel<AudioItem> itemModel;

    public ModalAudioPage(String itemId) {
        super();
        this.itemId = itemId;
        itemModel = new ItemModel<AudioItem>(itemId);
        addElements();
    }

    private void addElements() {
        AudioItem item = itemModel.getObject();
        String address = "";
        if (item.getAlbumArtFormat() != null && item.getAlbumArtFormat().length() > 0) {
            for (Entry<String, String[]> entry : getRequest().getParameterMap().entrySet()) {
                logger.debug("Parameter:{} {}", entry.getKey(), entry.getValue());
            }
            ImagePlaybackInformation info = X360MSServices.getFormatHandler().getThumbnailPlaybackInformationForItem(item, null);
            String serverName = ((ServletWebRequest) RequestCycle.get().getRequest()).getHttpServletRequest().getServerName();
            int serverPort = ((ServletWebRequest) RequestCycle.get().getRequest()).getHttpServletRequest().getServerPort();
            address = X360MSServices.getContentServer().getThumbnailURLForId(itemId, info, serverName, serverPort);
        }
        add(new ExternalImage("albumArt", address));
        add(new Label("name", item.getName()));
        add(new Label("artist", item.getArtistName()));
        add(new Label("album", item.getAlbumName()));
    }
}

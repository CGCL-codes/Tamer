package net.jmarias.uqueue.resources;

import net.jmarias.uqueue.contact.MediaType;
import net.jmarias.storage.ElementNotFound;
import net.jmarias.properties.*;

/**
 *
 * @author jose
 */
public class ConcreteAnnouncement extends ConcreteResource implements Announcement, PropertiesSubject {

    private String URL = "";

    private MediaType mediaType = MediaType.VOICE;

    public static Announcement getInstance(Integer resId, String name, String desc, Integer subDomainId) {
        return new ConcreteAnnouncement(resId, name, desc, subDomainId);
    }

    private ConcreteAnnouncement(Integer resId, String name, String desc, Integer subDomainId) {
        super(resId, name, desc, subDomainId, ResourceType.ANNOUNCEMENT);
    }

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public void setProperty(String property, String value) {
        super.setProperty(property, value);
        if (property.compareTo(java.util.ResourceBundle.getBundle("net/jmarias/uqueue/resources/Bundle").getString("URL")) == 0) {
            URL = value;
        } else if (property.compareTo(java.util.ResourceBundle.getBundle("net/jmarias/uqueue/resources/Bundle").getString("Media")) == 0) {
            for (MediaType media : MediaType.VALUES) {
                if (value.compareTo(media.toString()) == 0) {
                    mediaType = media;
                    return;
                }
            }
            ElementNotFound ex = new ElementNotFound(java.util.ResourceBundle.getBundle("net/jmarias/uqueue/resources/Bundle").getString("Media_is_not_supported"));
            throw ex;
        }
    }
}

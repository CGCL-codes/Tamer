package dk.i2m.converge.core.content;

import dk.i2m.converge.core.content.catalogue.MediaItem;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Attachment of a {@link MediaItem} to a {@link NewsItem}.
 *
 * @author Allan Lykke Christensen
 */
@Entity
@Table(name = "news_item_media_attachment")
@NamedQueries({ @NamedQuery(name = NewsItemMediaAttachment.FIND_BY_MEDIA_ITEM, query = "SELECT m FROM NewsItemMediaAttachment m WHERE m.mediaItem = :mediaItem") })
public class NewsItemMediaAttachment implements Serializable {

    /** Query used to determine if a media item is used and how many times. */
    public static final String FIND_BY_MEDIA_ITEM = "NewsItemMediaAttachment.findByMediaItem";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "news_item_id")
    private NewsItem newsItem;

    @ManyToOne
    @JoinColumn(name = "media_item_id")
    private MediaItem mediaItem;

    @Column(name = "caption")
    @Lob
    private String caption = "";

    @Column(name = "display_order")
    private int displayOrder;

    /**
     * Creates a new instance of {@link NewsItemMediaAttachment}.
     */
    public NewsItemMediaAttachment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public MediaItem getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
    }

    public NewsItem getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(NewsItem newsItem) {
        this.newsItem = newsItem;
    }

    /**
     * Gets the order of display for the attachment. The lowest number is
     * displayed first.
     * 
     * @return Order of display for the attachment in relation to other
     *         attachments
     */
    public int getDisplayOrder() {
        return displayOrder;
    }

    /**
     * Sets the order of display for the attachment. The lowest number is
     * displayed first.
     * 
     * @param displayOrder
     *          Order of display for the attachment in relation to other
     *          attachments
     */
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NewsItemMediaAttachment other = (NewsItemMediaAttachment) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}

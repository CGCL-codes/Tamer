package org.atlantal.api.cms.display;

import org.atlantal.utils.wrapper.ObjectWrapper;

/**
 * Attention ! Cet objet est conserv� en cache ne pas pointer directement
 * vers des objets volatiles ou susceptibles d'�tre modifi�.
 * @author f.masurel
 */
public class DisplayContentCacheId {

    private ObjectWrapper displayW;

    private ContentDisplayId contentDisplayId;

    private DisplayMedia media;

    /**
     * On ne stocke pas directement l'objet Display mais un ObjectWrapper
     * dans l'object DisplayContentCacheId car il peut �tre modifi� durant
     * la dur�e de vie de l'objet en cache.
     * @param contentDisplayId contentDisplayId
     * @param displayW display
     * @param media media
     */
    public DisplayContentCacheId(ObjectWrapper displayW, ContentDisplayId contentDisplayId, DisplayMedia media) {
        this.displayW = displayW;
        this.contentDisplayId = contentDisplayId;
        this.media = media;
    }

    /**
     * @return Returns the contentDisplayId.
     */
    public ContentDisplayId getContentDisplayId() {
        return contentDisplayId;
    }

    /**
     * @param contentDisplayId The contentDisplayId to set.
     */
    public void setContentDisplayId(ContentDisplayId contentDisplayId) {
        this.contentDisplayId = contentDisplayId;
    }

    /**
     * @return Returns the display.
     */
    public ObjectWrapper getDisplay() {
        return displayW;
    }

    /**
     * @param displayW The display to set.
     */
    public void setDisplay(ObjectWrapper displayW) {
        this.displayW = displayW;
    }

    /**
     * @return Returns the display.
     */
    public Display getDisplayObject() {
        return (Display) displayW.getWrappedObject();
    }

    /**
     * @return Returns the media.
     */
    public DisplayMedia getMedia() {
        return media;
    }

    /**
     * @param media The media to set.
     */
    public void setMedia(DisplayMedia media) {
        this.media = media;
    }

    /**
     * @return cacheable
     */
    public boolean isCacheable() {
        Display display = (Display) displayW.getWrappedObject();
        return display.isContentDisplayCacheable(contentDisplayId);
    }

    /**
     * @return cache id
     */
    public String getCacheId() {
        Display display = (Display) displayW.getWrappedObject();
        return display.getContentDisplayCacheId(contentDisplayId);
    }
}

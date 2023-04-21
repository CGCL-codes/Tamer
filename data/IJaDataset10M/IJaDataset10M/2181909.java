package com.googlecode.janrain4j.api.engage.request;

import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONWriter;

/**
 * Flash media item to be posted to the user's activity stream.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Activity
 * @see <a href="http://developers.facebook.com/docs/guides/attachments">Media object format and rules</a>
 */
public class FlashMediaItem implements MediaItem {

    public final String TYPE = "flash";

    private String swfsrc = null;

    private String imgsrc = null;

    private Integer width = null;

    private Integer height = null;

    private Integer expandedWidth = null;

    private Integer expandedHeight = null;

    /**
     * Create a new <code>FlashMediaItem</code>.
     * 
     * @param swfsrc The swfsrc.
     * @param imgsrc The imgsrc.
     */
    public FlashMediaItem(String swfsrc, String imgsrc) {
        this.swfsrc = swfsrc;
        this.imgsrc = imgsrc;
    }

    public void writeJSON(JSONWriter writer) throws JSONException {
        writer.object();
        writer.key("type").value(TYPE);
        writer.key("swfsrc").value(swfsrc);
        writer.key("imgsrc").value(imgsrc);
        if (width != null && width > 0) {
            writer.key("width").value(width);
        }
        if (height != null && height > 0) {
            writer.key("height").value(height);
        }
        if (expandedWidth != null && expandedWidth > 0) {
            writer.key("expanded_width").value(expandedWidth);
        }
        if (expandedHeight != null && expandedHeight > 0) {
            writer.key("expanded_height").value(expandedHeight);
        }
        writer.endObject();
    }

    /**
     * Returns the swfsrc of the flash attachment.
     */
    public String getSwfsrc() {
        return swfsrc;
    }

    /**
     * Sets the swfsrc of the flash attachment.
     * 
     * @param swfsrc The swfsrc.
     */
    public void setSwfsrc(String swfsrc) {
        this.swfsrc = swfsrc;
    }

    /**
     * Returns the imgsrc of the flash attachment.
     */
    public String getImgsrc() {
        return imgsrc;
    }

    /**
     * Sets the imgsrc of the flash attachment.
     * 
     * @param imgsrc The imgsrc.
     */
    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    /**
     * Retuns the width of the flash attachment.
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the width of the flash attachment.
     * 
     * @param width The width (in pixels).
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Returns the height of the flash attachment.
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets the height of the flash attachment.
     * 
     * @param height The height (in pixels).
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Returns the width to resize to when the user clicks on the flash object.
     */
    public Integer getExpandedWidth() {
        return expandedWidth;
    }

    /**
     * Sets the width to resize to when the user clicks on the flash object.
     * 
     * @param expandedWidth The expanded width (in pixels).
     */
    public void setExpandedWidth(Integer expandedWidth) {
        this.expandedWidth = expandedWidth;
    }

    /**
     * Returns the height to resize to when the user clicks on the flash object.
     */
    public Integer getExpandedHeight() {
        return expandedHeight;
    }

    /**
     * Sets the height to resize to when the user clicks on the flash object.
     * 
     * @param expandedHeight The expanded height (in pixels).
     */
    public void setExpandedHeight(Integer expandedHeight) {
        this.expandedHeight = expandedHeight;
    }
}

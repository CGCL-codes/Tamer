package org.fb4j.video.impl;

import net.sf.json.JSONObject;
import org.fb4j.impl.AbstractJsonFacebookObject;
import org.fb4j.video.Video;

/**
 * @author Mino Togna
 * 
 */
public class VideoImpl extends AbstractJsonFacebookObject implements Video {

    private long id;

    private String title, description, url;

    @Override
    protected void processJsonObject(JSONObject jsonObject) {
        id = jsonObject.optLong("vid");
        title = jsonObject.optString("title");
        description = jsonObject.optString("description");
        url = jsonObject.optString("link");
    }

    public String getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(id=").append(id).append(", title=").append(title).append(", description=").append(description).append(", url=").append(url).append(")");
        return buffer.toString();
    }
}

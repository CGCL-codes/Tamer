package bma.bricks.otter.model.feature;

import java.io.Serializable;
import bma.bricks.log.ToStringUtil;

public class PublishResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean done;

    private String content;

    private String url;

    public boolean isDone() {
        return this.done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ToStringUtil.fieldReflect(this);
    }

    public static PublishResult done(String url, String content) {
        PublishResult r = new PublishResult();
        r.setDone(true);
        r.setUrl(url);
        r.setContent(content);
        return r;
    }
}

package jblip.base.resources;

import java.util.Date;
import jblip.resources.Shortlink;

class BaseShortlink implements Shortlink {

    private static final long serialVersionUID = 1L;

    private final int id;

    private final int hitcount;

    private final Date created_at;

    private final String shortcode;

    private final String url;

    BaseShortlink(final int id_, final int hitcount_, final Date created_, final String short_, final String url_) {
        if (id_ <= 0) {
            throw new IllegalArgumentException("ID should be positive");
        }
        if (hitcount_ < 0) {
            throw new IllegalArgumentException("Hit count should be non-negative");
        }
        if (created_ == null) {
            throw new IllegalArgumentException("Null creation date");
        }
        if (short_ == null) {
            throw new IllegalArgumentException("Null shortcode");
        }
        if (url_ == null) {
            throw new IllegalArgumentException("Null URL");
        }
        this.id = id_;
        this.hitcount = hitcount_;
        this.created_at = created_;
        this.shortcode = short_;
        this.url = url_;
    }

    @Override
    public Date getCreatedAt() {
        return created_at;
    }

    @Override
    public Integer getHitCount() {
        return hitcount;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public String getOriginalURL() {
        return url;
    }

    @Override
    public String getShortcode() {
        return shortcode;
    }
}

package com.appspot.gossipscity.server;

import java.util.HashSet;
import java.util.Set;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.news.dto.ChannelDTO;

;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Channels {

    public Channels() {
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String name;

    @Persistent(defaultFetchGroup = "true")
    private String rssLink;

    @Persistent
    private int ratingCount;

    public Channels(ChannelDTO postsDTO) {
        this();
        this.setBasicInfo(postsDTO.getName(), postsDTO.getRssLink(), postsDTO.getRatingCount());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Long getId() {
        return id;
    }

    public void setBasicInfo(String name, String rssLink, int ratingCount) {
        this.name = name;
        this.rssLink = rssLink;
        this.ratingCount = ratingCount;
    }
}

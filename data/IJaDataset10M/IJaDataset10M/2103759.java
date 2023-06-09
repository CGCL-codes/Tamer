package org.blojsom.plugin.syndication.module;

import com.sun.syndication.feed.module.ModuleImpl;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of module for parsing blojsom-specific information using ROME
 *
 * @author David Czarnecki
 * @since blojsom 3.0
 * @version $Id: BlojsomImplementation.java,v 1.4 2006/08/18 17:24:12 czarneckid Exp $
 */
public class BlojsomImplementation extends ModuleImpl implements Blojsom {

    private String _author;

    private String _technoratiTags;

    private String _postSlug;

    private boolean _allowsComments;

    private boolean _allowsTrackbacks;

    private boolean _allowsPingbacks;

    private List _comments;

    private List _trackbacks;

    private List _pingbacks;

    private List _metadata;

    public BlojsomImplementation() {
        super(Blojsom.class, Blojsom.BLOJSOM_URI);
    }

    public Class getInterface() {
        return Blojsom.class;
    }

    public void copyFrom(Object object) {
        Blojsom blojsom = (Blojsom) object;
        setAuthor(blojsom.getAuthor());
        setTechnoratiTags(blojsom.getTechnoratiTags());
        setPostSlug(blojsom.getPostSlug());
        setAllowsComments(blojsom.getAllowsComments());
        setAllowsTrackbacks(blojsom.getAllowsTrackbacks());
        setAllowsPingbacks(blojsom.getAllowsPingbacks());
        List comments = blojsom.getComments();
        List copiedComments = new ArrayList();
        for (int i = 0; i < comments.size(); i++) {
            SimpleComment comment = (SimpleComment) comments.get(i);
            SimpleComment copiedComment = new SimpleComment();
            copiedComment.setAuthor(comment.getAuthor());
            copiedComment.setAuthorEmail(comment.getAuthorEmail());
            copiedComment.setAuthorURL(comment.getAuthorURL());
            copiedComment.setComment(comment.getComment());
            copiedComment.setIp(comment.getIp());
            copiedComment.setCommentDate(comment.getCommentDate());
            copiedComment.setStatus(comment.getStatus());
            copiedComment.setMetadata(comment.getMetadata());
            copiedComments.add(copiedComment);
        }
        setComments(copiedComments);
        List trackbacks = blojsom.getTrackbacks();
        List copiedTrackbacks = new ArrayList();
        for (int i = 0; i < trackbacks.size(); i++) {
            SimpleTrackback trackback = (SimpleTrackback) trackbacks.get(i);
            SimpleTrackback copiedTrackback = new SimpleTrackback();
            copiedTrackback.setBlogName(trackback.getBlogName());
            copiedTrackback.setExcerpt(trackback.getExcerpt());
            copiedTrackback.setIp(trackback.getIp());
            copiedTrackback.setStatus(trackback.getStatus());
            copiedTrackback.setTitle(trackback.getTitle());
            copiedTrackback.setTrackbackDate(trackback.getTrackbackDate());
            copiedTrackback.setUrl(trackback.getUrl());
            copiedTrackback.setMetadata(trackback.getMetadata());
            copiedTrackbacks.add(copiedTrackback);
        }
        setTrackbacks(copiedTrackbacks);
        List pingbacks = blojsom.getPingbacks();
        List copiedPingbacks = new ArrayList();
        for (int i = 0; i < pingbacks.size(); i++) {
            SimplePingback pingback = (SimplePingback) pingbacks.get(i);
            SimplePingback copiedPingback = new SimplePingback();
            copiedPingback.setBlogName(pingback.getBlogName());
            copiedPingback.setExcerpt(pingback.getExcerpt());
            copiedPingback.setIp(pingback.getIp());
            copiedPingback.setStatus(pingback.getStatus());
            copiedPingback.setTitle(pingback.getTitle());
            copiedPingback.setPingbackDate(pingback.getPingbackDate());
            copiedPingback.setUrl(pingback.getUrl());
            copiedPingback.setSourceURI(pingback.getSourceURI());
            copiedPingback.setTargetURI(pingback.getTargetURI());
            copiedPingback.setMetadata(pingback.getMetadata());
            copiedPingbacks.add(copiedPingback);
        }
        setPingbacks(copiedPingbacks);
        List metadata = blojsom.getMetadata();
        List copiedMetadata = new ArrayList();
        for (int i = 0; i < metadata.size(); i++) {
            Metadata metadataItem = (Metadata) metadata.get(i);
            Metadata copiedMetadataItem = new Metadata();
            copiedMetadataItem.setKey(metadataItem.getKey());
            copiedMetadataItem.setValue(metadataItem.getValue());
            copiedMetadata.add(copiedMetadataItem);
        }
        setMetadata(copiedMetadata);
    }

    public String getAuthor() {
        return _author;
    }

    public void setAuthor(String author) {
        _author = author;
    }

    public String getTechnoratiTags() {
        return _technoratiTags;
    }

    public void setTechnoratiTags(String technoratiTags) {
        _technoratiTags = technoratiTags;
    }

    public String getPostSlug() {
        return _postSlug;
    }

    public void setPostSlug(String postSlug) {
        _postSlug = postSlug;
    }

    public boolean getAllowsComments() {
        return _allowsComments;
    }

    public void setAllowsComments(boolean allowsComments) {
        _allowsComments = allowsComments;
    }

    public boolean getAllowsTrackbacks() {
        return _allowsTrackbacks;
    }

    public void setAllowsTrackbacks(boolean allowsTrackbacks) {
        _allowsTrackbacks = allowsTrackbacks;
    }

    public boolean getAllowsPingbacks() {
        return _allowsPingbacks;
    }

    public void setAllowsPingbacks(boolean allowsPingbacks) {
        _allowsPingbacks = allowsPingbacks;
    }

    public List getComments() {
        return _comments;
    }

    public void setComments(List comments) {
        _comments = comments;
    }

    public List getTrackbacks() {
        return _trackbacks;
    }

    public void setTrackbacks(List trackbacks) {
        _trackbacks = trackbacks;
    }

    public List getPingbacks() {
        return _pingbacks;
    }

    public void setPingbacks(List pingbacks) {
        _pingbacks = pingbacks;
    }

    public List getMetadata() {
        return _metadata;
    }

    public void setMetadata(List metadata) {
        _metadata = metadata;
    }

    public String getUri() {
        return Blojsom.BLOJSOM_URI;
    }
}

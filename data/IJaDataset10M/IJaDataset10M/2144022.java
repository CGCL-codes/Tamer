package org.feeddreamwork.feed;

import java.util.*;
import org.feeddreamwork.*;
import org.w3c.dom.*;

public class Atom10FeedBuilder extends FeedBuilder {

    public static final String TARGET_TYPE = "Atom 1.0";

    public Atom10FeedBuilder(Feed source, String feedId) {
        super(source, feedId);
    }

    @Override
    public String addUpdatedTime(String source, Date updatedTime) {
        Utils.throwIfNullOrEmpty(source);
        Utils.throwIfNull(updatedTime);
        return source.replace(FeedConstant.UPDATED_TIME_PLACEHOLDER, DateUtils.formatDateAsISO8601(updatedTime));
    }

    @Override
    public String getMIMEType() {
        return "application/atom+xml";
    }

    @Override
    protected Element buildRoot() {
        Element feed = this.feedDocument.createElementNS(FeedConstant.ATOM_10_NAMESPACE, "feed");
        if (!Utils.isNullOrEmpty(this.source.getLanguage())) feed.setAttribute("xml:lang", this.source.getLanguage());
        this.feedDocument.appendChild(feed);
        return feed;
    }

    @Override
    protected void buildMetadata(Element root) {
        XMLUtils.appendSimpleElement(root, "title", this.source.getTitle());
        if (!Utils.isNullOrEmpty(this.source.getDescription())) XMLUtils.appendSimpleElement(root, "subtitle", this.source.getDescription());
        if (!Utils.isNullOrEmpty(this.source.getHubLink())) appendLinkElement(root, "hub", this.source.getHubLink());
        appendLinkElement(root, "self", this.getFeedAddress());
        appendLinkElement(root, "alternate", this.source.getLink());
        if (!Utils.isNullOrEmpty(this.source.getImage())) XMLUtils.appendSimpleElement(root, "icon", this.source.getImage());
        XMLUtils.appendSimpleElement(root, "updated", FeedConstant.UPDATED_TIME_PLACEHOLDER);
        Element generator = XMLUtils.appendSimpleElement(root, "generator", this.getGeneratorInfo());
        generator.setAttribute("version", ApplicationProperty.VERSION);
        XMLUtils.appendSimpleElement(root, "id", this.getFeedAddress());
    }

    @Override
    protected Element buildEntry(Element root, Entry entry) {
        Element entryNode = this.feedDocument.createElement("entry");
        root.appendChild(entryNode);
        XMLUtils.appendSimpleElement(entryNode, "title", entry.getTitle());
        appendLinkElement(entryNode, "alternate", entry.getLink());
        Element contentNode = XMLUtils.appendSimpleElement(entryNode, "content", entry.getContent());
        contentNode.setAttribute("type", "html");
        Element authorNode = XMLUtils.appendSimpleElement(entryNode, "author", null);
        XMLUtils.appendSimpleElement(authorNode, "name", entry.getAuthor());
        XMLUtils.appendSimpleElement(entryNode, "updated", DateUtils.formatDateAsISO8601(entry.getUpdatedTime()));
        XMLUtils.appendSimpleElement(entryNode, "id", entry.getLink());
        return entryNode;
    }

    private static Element appendLinkElement(Element node, String rel, String href) {
        Element result = XMLUtils.appendSimpleElement(node, "link", null);
        result.setAttribute("rel", rel);
        result.setAttribute("href", href);
        return result;
    }
}

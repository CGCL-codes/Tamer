package com.escape.synder.handlers.rss;

import java.util.HashMap;
import java.util.List;
import org.xml.sax.Attributes;
import com.escape.synder.ParseContext;
import com.escape.synder.SynderContentHandler;
import com.escape.synder.handlers.ForeignMarkupElement;
import com.escape.synder.handlers.HandlerFactories;
import com.escape.synder.handlers.HandlerFactory;
import com.escape.synder.handlers.RssAny;
import com.escape.synder.handlers.SubHandler;
import com.escape.synder.handlers.HandlerImpl;
import com.escape.synder.setters.RFC822DateSetter;
import com.escape.synder.setters.Setter;
import com.escape.synder.setters.SyndForeignMarkupListSetter;
import com.escape.synder.setters.SyndLinkListSetter;
import com.sun.syndication.feed.synd.SyndImage;
import com.sun.syndication.feed.synd.impl.SyndFeedImpl;

public class Channel extends SubHandler<SyndFeedImpl> {

    static final HashMap<String, HandlerFactory<SyndFeedImpl>> props;

    static {
        props = new HashMap<String, HandlerFactory<SyndFeedImpl>>();
        try {
            props.put("title", new HandlerFactories.Feed_SimpleElement(new Setter(SyndFeedImpl.class, "Title")));
            props.put("description", new HandlerFactories.Feed_SimpleElement(new Setter(SyndFeedImpl.class, "Description")));
            props.put("pubDate", new HandlerFactories.Feed_SimpleElement(new RFC822DateSetter(SyndFeedImpl.class, "PublishedDate")));
            props.put("lastBuildDate", new HandlerFactories.Feed_SimpleElement(new RFC822DateSetter(SyndFeedImpl.class, "UpdatedDate")));
            props.put("link", new HandlerFactories.Feed_SimpleLinkElement(new SyndLinkListSetter(SyndFeedImpl.class, "Links")));
            props.put("image", new Feed_ImageElement(new Setter(SyndFeedImpl.class, "Image", SyndImage.class)));
        } catch (Exception e) {
        }
    }

    static final class Feed_ImageElement extends HandlerFactory<SyndFeedImpl> {

        public Feed_ImageElement(Setter sx) {
            super(sx);
        }

        @Override
        public SubHandler<?> create(ParseContext pc, Attributes ax, SyndFeedImpl root) {
            return new Image<SyndFeedImpl>(pc, root, sx);
        }
    }

    final boolean fm;

    public Channel(ParseContext ctx, SyndFeedImpl root, List<HandlerImpl> nsx) {
        super(ctx, root, nsx);
        fm = ctx.getOption(ParseContext.Features.FOREIGN_MARKUP_FEED) == null;
    }

    @Override
    public SubHandler<?> query(ParseContext pc, String[] path, SubHandler<?> tos, String uri, String localName, String name, Attributes attributes) {
        final String nx = SynderContentHandler.formatTag(RssAny.RSS10_NSURI.equals(uri) ? "" : uri, localName);
        if (props.containsKey(nx)) {
            return props.get(nx).create(pc, attributes, root);
        } else {
            final SubHandler<?> qx = super.query(pc, path, this, uri, localName, name, attributes);
            if (fm) return qx;
            if (qx != null) return qx;
            if (tos instanceof ForeignMarkupElement<?>) return tos;
            try {
                final ForeignMarkupElement<SyndFeedImpl> fme = new ForeignMarkupElement<SyndFeedImpl>(pc, root, new SyndForeignMarkupListSetter(SyndFeedImpl.class, "ForeignMarkup"));
                return fme;
            } catch (Exception e) {
                return qx;
            }
        }
    }
}

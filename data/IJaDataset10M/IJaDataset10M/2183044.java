package com.gcapmedia.dab.epg.binary.encode;

import java.util.Locale;
import com.gcapmedia.dab.epg.Link;
import com.gcapmedia.dab.epg.binary.Attribute;
import com.gcapmedia.dab.epg.binary.AttributeTag;
import com.gcapmedia.dab.epg.binary.Element;
import com.gcapmedia.dab.epg.binary.ElementTag;
import com.gcapmedia.dab.epg.binary.types.LanguageType;
import com.gcapmedia.dab.epg.binary.types.StringType;
import com.gcapmedia.dab.epg.binary.types.TimepointType;

/**
 * 
 */
public class LinkConstructor implements Constructor<Link> {

    /**
	 * @see com.gcapmedia.dab.epg.binary.encode.Constructor#construct(java.lang.Object)
	 */
    public Element construct(Link link) {
        Element element = new Element(ElementTag.link);
        element.addAttribute(new Attribute(AttributeTag.linkUrl, new StringType(link.getUrl().toString()), element));
        if (link.getDescription() != null) {
            element.addAttribute(new Attribute(AttributeTag.linkDescription, new StringType(link.getDescription()), element));
        }
        if (link.getMimeType() != null) {
            element.addAttribute(new Attribute(AttributeTag.linkMimeValue, new StringType(link.getMimeType()), element));
        }
        if (!link.getLocale().equals(Locale.getDefault())) {
            element.addAttribute(new Attribute(AttributeTag.linkLang, new LanguageType(link.getLocale()), element));
        }
        if (link.getExpiryTime() != null) {
            element.addAttribute(new Attribute(AttributeTag.linkExpiryTime, new TimepointType(link.getExpiryTime()), element));
        }
        return element;
    }
}

package info.bliki.wiki.model;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.model.WikiModel;
import java.util.Locale;
import java.util.Map;

/**
 * PDF Wiki model implementation
 * 
 * @deprecated use an extended APIWikiModel instead; see PDFCreatorTest in the
 *             test sources
 */
public class PDFWikiModel extends WikiModel {

    static {
        TagNode.addAllowedAttribute("style");
    }

    public PDFWikiModel(String imageBaseURL, String linkBaseURL) {
        this(Locale.ENGLISH, imageBaseURL, linkBaseURL);
    }

    /**
	 * 
	 * @param imageBaseURL
	 * @param linkBaseURL
	 */
    public PDFWikiModel(Locale locale, String imageBaseURL, String linkBaseURL) {
        super(Configuration.DEFAULT_CONFIGURATION, locale, imageBaseURL, linkBaseURL);
    }

    /**
	 * Add templates: &quot;Test&quot;, &quot;Templ1&quot;, &quot;Templ2&quot;,
	 * &quot;Include Page&quot;
	 * 
	 */
    @Override
    public String getRawWikiContent(String namespace, String articleName, Map<String, String> map) {
        String result = super.getRawWikiContent(namespace, articleName, map);
        if (result != null) {
            return result;
        }
        String name = Encoder.encodeTitleUrl(articleName);
        if (namespace.equals("Template")) {
        } else {
            if (name.equals("Include_Page")) {
            }
        }
        return null;
    }

    public void appendInternalLink(String topic, String hashSection, String topicDescription, String cssClass) {
        ContentToken text = new ContentToken(topicDescription);
        append(text);
    }

    public void parseInternalImageLink(String imageNamespace, String rawImageLink) {
        if (fExternalImageBaseURL != null) {
            String imageHref = fExternalWikiBaseURL;
            String imageSrc = fExternalImageBaseURL;
            ImageFormat imageFormat = ImageFormat.getImageFormat(rawImageLink, imageNamespace);
            String imageName = imageFormat.getFilename();
            imageName = Encoder.encodeUrl(imageName);
            if (replaceColon()) {
                imageHref = imageHref.replace("${title}", imageNamespace + '/' + imageName);
                imageSrc = imageSrc.replace("${image}", imageName);
            } else {
                imageHref = imageHref.replace("${title}", imageNamespace + ':' + imageName);
                imageSrc = imageSrc.replace("${image}", imageName);
            }
            appendInternalImageLink(imageHref, imageSrc, imageFormat);
        }
    }
}
